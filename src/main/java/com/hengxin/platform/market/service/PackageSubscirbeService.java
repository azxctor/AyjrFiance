/*
 * Project Name: kmfex-platform
 * File Name: PackageSubscirbeService.java
 * Class Name: PackageSubscirbeService
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.market.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.InvestorSubscribeService;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.downstream.FinancingMarketItemDetailDto;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.repository.InvestorInfoRepository;
import com.hengxin.platform.product.domain.PackageSubscribes;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.repository.PackageSubscribesRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.ProdPkgMsgRemindService;
import com.hengxin.platform.risk.dto.PurchaseRiskResultDto;
import com.hengxin.platform.risk.service.InvestorPurchaseLimitationService;

/**
 * Class Name: PackageSubscirbeService
 * 
 * @author runchen
 * 
 */
@Service
public class PackageSubscirbeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageSubscirbeService.class);
    
    @Autowired
    private transient PackageSubscribesRepository subscribeRepository;

    @Autowired
    private transient FundAcctBalService fundAccoutService;

    @Autowired
    private transient InvestorSubscribeService investorSubscribeSerivice;

    @Autowired
    private transient ProductPackageRepository packageRepository;

    @Autowired
    private transient InvestorInfoRepository investorRepository;

    @Autowired
    private transient FinancingResourceService financingResourceService;

    @Autowired
    private transient ProductRepository productRepository;

    @Autowired
    private transient ProdPkgMsgRemindService prodPkgMsgRemindService;
    
    @Autowired
    private transient InvestorPurchaseLimitationService limitationService;
    
    /**
     * 每个包最大申购用户数量
     */
    private BigDecimal getMaxUserPerPkg(){
    	return SubscribeUtils.getPerPkgMaxSubsUserCount();
    }
    
    /**
     * 申购单元金额
     */
    private BigDecimal getUnitFaceValue(){
    	return SubscribeUtils.getUnitFaceValue();
    }

    /**
     * 
     * Description: 申购融资包
     * 
     * @param userId
     * @param item
     * @param subscribeAmount
     * @throws BizServiceException
     */
    @Transactional
    public void purchase(String userId, MarketItemDto item, long consume) throws BizServiceException {
        BigDecimal subscribeAmount = new BigDecimal(consume);
        // 判断个人余额是否足够
        BigDecimal userCurrentAcctAvlBal = fundAccoutService.getUserCurrentAcctAvlBal(userId, true);
        if (userCurrentAcctAvlBal.compareTo(subscribeAmount) < 0) {
            throw new BizServiceException(EErrorCode.BALANCE_LACK);
        }

        Date now = new Date();
        // 判断包的状态和申购时间
        if (item != null && item.getStatus() == EPackageStatus.SUBSCRIBE
                && item.getSupplyStartTime().compareTo(now) < 0 && item.getSupplyEndTime().compareTo(now) > 0) {
            // 判断定投条件
            if (item.getAipFlag() && item.getAipEndTime().compareTo(now) > 0) {
                List<ProductPackage> pkgs = packageRepository.findByPkgIdAndAIPGroupUser(item.getId(), userId);
                if (CollectionUtils.isEmpty(pkgs)) {
                    throw new BizServiceException(EErrorCode.SUB_PACKAGE_NOT_FOUND);
                }
            }

            BigDecimal supplyAmount = item.getSupplyAmount().add(subscribeAmount);
            // 判断申购金额与剩余金额
            if (supplyAmount.compareTo(item.getPackageQuota()) > 0) {
                throw new BizServiceException(EErrorCode.SUB_PACKAGE_FULL);
            }
        } else if (item == null) {
            throw new BizServiceException(EErrorCode.SUB_PACKAGE_NOT_FOUND);
        } else if (item.getStatus() == EPackageStatus.WAIT_SIGN) {
            throw new BizServiceException(EErrorCode.SUB_PACKAGE_FULL);
        } else if (item.getSupplyStartTime().compareTo(now) < 0) {
            throw new BizServiceException(EErrorCode.SUB_NOT_START);
        } else if (item.getSupplyEndTime().compareTo(now) > 0) {
            throw new BizServiceException(EErrorCode.SUB_HAS_PASSED);
        }

        // 冻结个人账户资金
        ReserveReq subscribeRequest = new ReserveReq();
        subscribeRequest.setUserId(userId);
        subscribeRequest.setBizId(item.getId());
        subscribeRequest.setCurrOpId(userId);
        subscribeRequest.setAddXwb(true);
        subscribeRequest.setTrxAmt(subscribeAmount);
        subscribeRequest.setUseType(EFundUseType.SUBSCRIBE);
        subscribeRequest.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        subscribeRequest.setTrxMemo("申购融资包" + item.getId() + "，保留申购金额 " + subscribeAmount + "元 ");
        String reserveJnlNo = investorSubscribeSerivice.subsPkgPrePayAmt(subscribeRequest);

        // 保存融资包申购信息
        PackageSubscribes subscribe = new PackageSubscribes();
        subscribe.setPkgId(item.getId());
        subscribe.setUserId(userId);
        subscribe.setUnitAmt(getUnitFaceValue());
        subscribe.setUnit(subscribeAmount.divide(subscribe.getUnitAmt()).intValue());
        subscribe.setReserveJnlNo(reserveJnlNo);
        subscribe.setCreateOpid(userId);
        subscribe.setCreateTs(now);
        subscribe.setLastMntOpid(userId);
        subscribe.setLastMntTs(now);
        subscribe.setSubscribeDate(CommonBusinessUtil.getCurrentWorkDate());
        subscribeRepository.save(subscribe);
    }

    @Transactional(readOnly = true)
    public Map<String, Object[]> getSubscribeSumDetail() {
        Map<String, Object[]> map = new HashMap<String, Object[]>();
        List<Object[]> resultList = subscribeRepository.getAmountAndCount();
        for (Object[] result : resultList) {
            if (result != null && result.length == 4) {
                map.put((String) result[0], result);
            }
        }
        return map;
    }

    /**
     * 
     * Description: 异步更新融资包信息
     * 
     * @param objects
     */
    @Transactional
    public void updatePackageDetail(Object[] result) {
        if (result != null && result.length == 4) {
            String pkgId = (String) result[0];
            BigDecimal amount = (BigDecimal) result[1];
            ProductPackage pkg = packageRepository.findOne(pkgId);
            if (pkg != null && pkg.getStatus() == EPackageStatus.SUBSCRIBE
                    && pkg.getPackageQuota().compareTo(amount) >= 0) {
                // 如果已申购金额与计算出金额不相等，更新数据
                if (pkg.getSupplyAmount().compareTo(amount) != 0) {
                    pkg.setSupplyAmount(amount);
                    pkg.setSupplyUserCount(((BigDecimal) result[2]).intValue());
                    pkg.setLastTime(new Date());
                    // 如果满额，修改状态和满额时间
                    if (pkg.getPackageQuota().compareTo(amount) == 0) {
                        pkg.setStatus(EPackageStatus.WAIT_SIGN);
                        pkg.setLastSubsTime((Date) result[3]);
                        // 发送提醒给融资人
                        // 产品中的申请人编号applUserId，就是融资包中的融资人编号fncrId，减少数据库查询
                        String fncrId = pkg.getFinancierId();
                        if(StringUtils.isBlank(fncrId)){
                        	Product product = productRepository.findOne(pkg.getProductId());
                        	fncrId = product.getApplUserId();
                        }
                        prodPkgMsgRemindService.pkgChangesToWaitSign(pkg.getId(), fncrId,
                                ProdPkgMsgRemindService.OPSUBSISFULL);
                    }
                    packageRepository.save(pkg);

                    if (pkg.getStatus() == EPackageStatus.WAIT_SIGN) {
                        financingResourceService.removeItem(pkg.getId());
                    }
                }
            } else if (pkg == null) {
                LOGGER.debug("未找到融资包，ID：" + pkgId);
            } else if (pkg.getStatus() != EPackageStatus.SUBSCRIBE) {
                LOGGER.debug("融资包状态不对，ID：{}，status：{}", pkgId, pkg.getStatus().getText());
            } else if (pkg.getPackageQuota().compareTo(amount) < 0) {
                LOGGER.debug("申购金额大于融资包金额，ID：" + pkgId);
            }
        }
    }

    @Transactional(readOnly = true)
    public FinancingMarketItemDetailDto getLatestSubcribeAmount(String pkgId, String userId) {
        FinancingMarketItemDetailDto dto = new FinancingMarketItemDetailDto();
        BigDecimal minAmount = getUnitFaceValue();
        BigDecimal maxAmount = getUnitFaceValue();
        ProductPackage pkg = packageRepository.findOne(pkgId);
        if (pkg != null) {
        	if (CommonBusinessUtil.isRiskActive()) {
        		dto.setActiveRisk(true);
        		PurchaseRiskResultDto limitation = limitationService.getSubscribeLimitation(userId, pkgId);
        		/** 可用余额. **/
            	dto.setBalance(limitation.getBalance());
        		if (!limitation.isQualified()) {
        			dto.setSubscribed(false);
					dto.setReason(limitation.getMessage());
				} else {
	                // 最小值为max(faceValue, 总额/200)
	            	/** 替换原申购下限. **/
	            	minAmount = limitation.getLowerLimit();
	                // 通过可购买比例计算最大购买额
	            	/** 替换原申购上限. **/
	            	maxAmount = limitation.getUpperLimit();
	                dto.setLevelSubscribeAmount(maxAmount);
	                /** 替换已经购买金额. **/
	                BigDecimal subsAmount = limitation.getSubscribeAmount();
	                dto.setTotalSubscribeAmount(subsAmount);
	                /** 上限中已经减去当前包购买金额. **/
	                // 可用余额小于最大可申购额，以可用余额为准
	                maxAmount = maxAmount.min(dto.getBalance());
	                
	                if (maxAmount.compareTo(minAmount) < 0) {
	                    // 最大可申购余额小于最小申购额，不可申购
	                    dto.setSubscribed(false);
	                    dto.setReason(MessageUtil.getMessage("market.error.subscribe.amount.lack",
	                            NumberUtil.formatMoney(maxAmount), NumberUtil.formatMoney(subsAmount)));
	                    maxAmount = minAmount;
	                }
	                // 如果计算得出的值不是faceValue的整数倍，往下取faceValue的倍数
	                maxAmount = SubscribeUtils.floorUnitFaceValue(maxAmount);
	                /** add accumulative amount for same risk level projects. **/
	                dto.setAccumulativeAmount(limitation.getAccumulativeAmount());
	                dto.setRate(limitation.getRate());
				}
        	} else if (CommonBusinessUtil.isRiskActiveTemp()) {
        		dto.setActiveRisk(true);
        		PurchaseRiskResultDto limitation = limitationService.getSubscribeLimitationTemp(userId, pkgId);
        		/** 可用余额. **/
            	dto.setBalance(limitation.getBalance());
            	/** 申购下限. **/
            	minAmount = limitation.getLowerLimit();
            	/** 申购上限. **/
            	maxAmount = limitation.getUpperLimit();
        		if (!limitation.isQualified()) {
					dto.setSubscribed(false);
					dto.setInputable(false);
					dto.setReason(limitation.getMessage());
				} else {
	            	/** 最大可申购金额. **/
	                dto.setLevelSubscribeAmount(maxAmount);
	                /** 已购买金额. **/
	                dto.setTotalSubscribeAmount(limitation.getSubscribeAmount());
	                /** 同一风险级别融资项目累计持有金额. **/
	                dto.setAccumulativeAmount(limitation.getAccumulativeAmount());
	                /** 显示IV累计金额. **/
	                if (ERiskLevel.HIGH_RISK == limitation.getProjectLevel()) {
						dto.setDisplayLevel4(true);
					}
				}
        	} else {
        		dto.setBalance(fundAccoutService.getUserCurrentAcctAvlBal(userId, true));
        		// 最小值为max(faceValue, 总额/200)
                BigDecimal unitAmount = pkg.getPackageQuota().divide(getMaxUserPerPkg(), 0, RoundingMode.UP);
                if (unitAmount.compareTo(minAmount) > 0) {
                    // 整除200份并且向上进位到faceValue的倍数，结果肯定>=faceValue直接用来做最小购买额
                    minAmount = SubscribeUtils.roundUnitFaceValue(unitAmount);
                }
                InvestorInfo investorInfo = investorRepository.findOne(userId);
                if (investorInfo != null && StringUtils.isNotBlank(investorInfo.getInvestorLevelOriginal())) {
                    // 通过可购买比例计算最大购买额
                    maxAmount = CommonBusinessUtil.getMaxSubscribeRate(investorInfo.getInvestorLevelOriginal()).multiply(
                            pkg.getPackageQuota());
                    dto.setLevelSubscribeAmount(maxAmount);

                    // 计算已经购买金额
                    List<PackageSubscribes> subs = subscribeRepository.getSubsByPkgIdAndUserId(pkgId, userId);
                    BigDecimal subsAmount = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(subs)) {
                        for (PackageSubscribes sub : subs) {
                            subsAmount = subsAmount.add(sub.getUnitAmt().multiply(new BigDecimal(sub.getUnit())));
                        }
                        // 已申购过一次，累计申购开始最小申购额为faceValue
                        minAmount = getUnitFaceValue();
                    }
                    dto.setTotalSubscribeAmount(subsAmount);
                    maxAmount = maxAmount.subtract(subsAmount);
                    // 可用余额小于最大可申购额，以可用余额为准
                    maxAmount = maxAmount.min(dto.getBalance());
                    if (maxAmount.compareTo(minAmount) < 0) {
                        // 最大可申购余额小于最小申购额，不可申购
                        dto.setSubscribed(false);
                        dto.setInputable(false);
                        dto.setReason(MessageUtil.getMessage("market.error.subscribe.amount.lack",
                                NumberUtil.formatMoney(maxAmount), NumberUtil.formatMoney(subsAmount)));
                        maxAmount = minAmount;
                    }
                    // 如果计算得出的值不是faceValue的整数倍，往下取faceValue的倍数
                    maxAmount = SubscribeUtils.floorUnitFaceValue(maxAmount);
                }
        	}
        }
        dto.setMinSubscribeAmount(minAmount);
        dto.setMaxSubscribeAmount(maxAmount.compareTo(minAmount) < 0 ? minAmount : maxAmount);
        LOGGER.info("N MIN {}, N MAX {}", new Object[]{dto.getMinSubscribeAmount(), dto.getMaxSubscribeAmount()});
        return dto;
    }

}
