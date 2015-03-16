package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.entity.ActionHistoryPo;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.MarketProductDto;
import com.hengxin.platform.market.service.FinancingResourceService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.repository.PackageSubscribesRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;

/**
 * 融资包到时发布等Job的Service
 * 
 * @author runchen
 * 
 */

@Service
public class PackageJobService extends BaseService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PackageJobService.class);

	@Autowired
	private ProductPackageRepository pkgRepository;

	@Autowired
	private FinancingResourceService resourceService;

	@Autowired
	private CancelPkgService cancelPkgService;

	@Autowired
	private PackageSubscribesRepository subscribeRepository;

	@Autowired
	private ActionHistoryService actionHistoryService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProdPkgMsgRemindService prodPkgMsgRemindService;

	@Autowired
	private FinancingPackageService financingPackageService;

	/**
	 * 
	 * 发布融资包
	 * 
	 */
	public void releasePackages() {
		long start = System.currentTimeMillis();
		List<ProductPackage> pkgList = pkgRepository
				.findByStatusAndSupplyStartTimeLessThan(
						EPackageStatus.WAIT_SUBSCRIBE, new Date());
		if (CollectionUtils.isNotEmpty(pkgList)) {
			for (ProductPackage pkg : pkgList) {
				try {
					changePackageFromWaitSubsToSubs(pkg);
					financingPackageService.sendStatusChangeMessage(pkg
							.getProduct().getApplUserId(), pkg);
				} catch (Exception e) {
					LOGGER.info(pkg.getId() + "发布失败.", e);
				}
			}
		}
		LOGGER.info("releasePackages() processed {} packages in {}ms", pkgList.size(), System.currentTimeMillis() - start);
	}

	@Transactional
	private void changePackageFromWaitSubsToSubs(ProductPackage pkg) {
		// 更新状态
		pkg.setStatus(EPackageStatus.SUBSCRIBE);
		pkg.setLastTime(new Date());
		pkgRepository.save(pkg);
		// 日志
		actionHistoryService.save(EntityType.PRODUCTPACKAGE, pkg.getId(),
				ActionType.SUBSCRIBE,
				ActionResult.PRODUCT_PACKAGE_CHANGE_TO_SUBSCRIBE);

		// 修改resource pool中数据
		MarketItemDto dto = ConverterService.convert(pkg, MarketItemDto.class);
		dto.setAipFlag(pkg.getAipFlag() && pkg.getAipEndTime() != null
				&& pkg.getAipEndTime().compareTo(new Date()) >= 0);
		dto.setProduct(ConverterService.convert(pkg.getProduct(),
				MarketProductDto.class));
		resourceService.addItem(dto, true);
	}

	/**
	 * 
	 * 预发布融资包（预发布—>代申购）
	 * 
	 */
	public void preannouncePackages() {
		long start = System.currentTimeMillis();
		List<ProductPackage> pkgList = pkgRepository
				.findByStatusAndPrePublicTimeLessThan(
						EPackageStatus.PRE_PUBLISH, new Date());
		if (CollectionUtils.isNotEmpty(pkgList)) {
			for (ProductPackage pkg : pkgList) {
				// 更新状态
				try {
					pkg.setStatus(EPackageStatus.WAIT_SUBSCRIBE);
					financingPackageService.sendStatusChangeMessage(
							productRepository.findByProductId(
									pkg.getProductId()).getApplUserId(), pkg);
					pkg.setLastTime(new Date());
					changePackageFromPrepublishToWaitSubs(pkg);
				} catch (Exception e) {
					LOGGER.info(pkg.getId() + "预发布—>代申购失败.", e);
				}
			}
		}
		LOGGER.info("preannouncePackages() processed {} packages in {}ms", pkgList.size(), System.currentTimeMillis() - start);
	}

	@Transactional
	private void changePackageFromPrepublishToWaitSubs(ProductPackage pkg) {
		pkgRepository.save(pkg);
		// 日志
		actionHistoryService.save(EntityType.PRODUCTPACKAGE, pkg.getId(),
				ActionType.WAITSUBS,
				ActionResult.PRODUCT_PACKAGE_WAIT_SUBSCRIBE);
	}

	/**
	 * 
	 * 融资包到期（申购中—>待签约）
	 * 
	 */
	public void endPackages() {
		long start = System.currentTimeMillis();
		List<ProductPackage> pkgList = pkgRepository
				.findByStatusAndSupplyEndTimeLessThan(EPackageStatus.SUBSCRIBE,
						new Date());
		if (CollectionUtils.isNotEmpty(pkgList)) {
			for (ProductPackage pkg : pkgList) {
				try {
					changePackageFromSubsToWaitSign(pkg);
				} catch (Exception e) {
					LOGGER.info(pkg.getId() + "申购中—>待签约失败.", e);
				}
			}
		}
		LOGGER.info("endPackages() processed {} packages in {}ms", pkgList.size(), System.currentTimeMillis() - start);
	}

	@Transactional
	private void changePackageFromSubsToWaitSign(ProductPackage pkg) {
		endPackage(pkg, EPackageStatus.WAIT_SIGN);
		// 日志
		actionHistoryService.save(EntityType.PRODUCTPACKAGE, pkg.getId(),
				ActionType.SUBSCRIBE, ActionResult.PRODUCT_PACKAGE_WAIT_SIGN);
		// 发送提醒
		Product product = productRepository.findOne(pkg.getProductId());
		prodPkgMsgRemindService
				.pkgChangesToWaitSign(pkg.getId(), product.getApplUserId(),
						ProdPkgMsgRemindService.OPSUBSREACHEND);
	}

	/**
	 * 
	 * 逾期未签约
	 * 
	 * @throws ParseException
	 * 
	 */
	public void expiredSignedPackages() throws ParseException {
		List<ProductPackage> pkgList = pkgRepository
				.findByStatus(EPackageStatus.WAIT_SIGN);
		if (CollectionUtils.isNotEmpty(pkgList)) {
			for (ProductPackage pkg : pkgList) {
				Date lastSubsTime = pkg.getLastSubsTime();
				if (lastSubsTime != null) {
					Date lastSignDate = DateUtils.add(lastSubsTime,
							Calendar.DAY_OF_MONTH, CommonBusinessUtil
									.getFinancingPackageSigningTerm()
									.intValue() - 1);
					if (lastSignDate.compareTo(DateUtils
							.getStartDate(new Date())) < 0) {
						processExpiredSignPackage(pkg.getId(), "system");
					}
				}
			}
		}
	}

	@Transactional
	private void processExpiredSignPackage(String packageId, String opId) {
		try {
			cancelPkgService.cancelProductPackageInSign(packageId, opId);

			ActionHistoryPo actionHistory = new ActionHistoryPo();
			actionHistory.setAction(ActionType.PKGEXPIRED);
			actionHistory.setEntityType(EntityType.PRODUCTPACKAGE);
			actionHistory.setEntityId(packageId);
			actionHistory.setResult(ActionResult.PRODUCT_PACKAGE_SIGN_EXPIRED
					.getText());
			actionHistory.setOperateTime(addDateTime(new Date()));

			actionHistoryService.save(actionHistory);
		} catch (Exception e) {
			LOGGER.debug(packageId + "逾期未签约失败.", e);
		}
	}

	/**
	 * 
	 * Description: 时间增加一秒
	 * 
	 * @param date
	 * @return
	 */
	private Date addDateTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, 1);
		return cal.getTime();
	}

	/**
	 * 
	 * 单个融资包到期或者终止（申购中—>待签约）
	 * 
	 */
	@Transactional
	public void endPackage(ProductPackage pkg, EPackageStatus packageStatus) {
		// 同步申购金额和人数
		List<Object[]> result = subscribeRepository
				.getAmountAndCountByPkgId(pkg.getId());
		if (CollectionUtils.isNotEmpty(result)) {
			Object[] row = result.get(0);
			pkg.setSupplyAmount((BigDecimal) row[0]);
			pkg.setSupplyUserCount(((BigDecimal) row[1]).intValue());
			pkg.setLastSubsTime((Date) row[2]);// 更新最后申购时间
		}

		// 更新状态
		if (packageStatus != null) {
			pkg.setStatus(packageStatus);
		}
		pkgRepository.save(pkg);
		// 从内存中移除
		resourceService.removeItem(pkg.getId());
	}

}
