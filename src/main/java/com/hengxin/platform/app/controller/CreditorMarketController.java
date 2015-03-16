package com.hengxin.platform.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.app.dto.downstream.CreditorMarketPurchasedownDto;
import com.hengxin.platform.app.dto.downstream.CreditorMarketdownDto;
import com.hengxin.platform.app.dto.upstream.CreditorMarketPurchaseIdupDto;
import com.hengxin.platform.app.dto.upstream.CreditorMarketPurchaseupDto;
import com.hengxin.platform.app.dto.upstream.CreditorMarketupDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.market.controller.FinancingMarketController;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.downstream.FinancingMarketItemDetailDto;
import com.hengxin.platform.market.dto.upstream.FinancingMarketPurchaseDto;
import com.hengxin.platform.market.dto.upstream.FinancingMarketSearchDto;
import com.hengxin.platform.market.service.FinancingMarketService;
import com.hengxin.platform.market.service.FinancingResourceService;
import com.hengxin.platform.market.service.PackageSubscirbeService;
import com.hengxin.platform.risk.service.InvestorPurchaseLimitationService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * 
 * 债权行情及申购.
 * 
 * @author xiaofeng
 * 
 */
@Controller
public class CreditorMarketController {
    
	@Autowired
	private FinancingResourceService resourceService;

	@Autowired
	private FinancingMarketService financingMarketService;
	
	@Autowired
	private transient PackageSubscirbeService subcribeService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private LocalValidatorFactoryBean validator;

	@Autowired
    private transient InvestorPurchaseLimitationService limitationService;

	private FinancingMarketController financingMarketController;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CreditorMarketController.class);
	
	@PostConstruct
	public void init() {
		financingMarketController = new FinancingMarketController();

		financingMarketController.setFinancingMarketService(financingMarketService);
		financingMarketController.setResourceService(resourceService);
		financingMarketController.setSubcribeService(subcribeService);
		financingMarketController.setSecurityContext(securityContext);
		financingMarketController.setValidator(validator);
		financingMarketController.setInvestorPurchaseLimitationService(limitationService);
	}

	//债权行情
	@RequestMapping(value = "/creditorMarket", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
	public ResultDto doCreditorMarket(@OnValid @RequestBody CreditorMarketupDto creditmarket) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doCreditorMarket() start: ");
		}

		boolean isPlatformUser = securityContext.isPlatformUser();
		FinancingMarketSearchDto request = new FinancingMarketSearchDto();
		List<CreditorMarketdownDto> creditorMarketdown = new ArrayList<CreditorMarketdownDto>();

		if ("topurchase".equals(creditmarket.getState())) {// 申购中
			request.setTopurchase(true);
		} else if ("prerelease".equals(creditmarket.getState())) {// 待申购
			request.setPrerelease(true);
		} else if ("hasexpired".equals(creditmarket.getState())) {// 已过期
			request.setHasexpired(true);
		} else if ("all".equals(creditmarket.getState())) { // 全部
			request.setTopurchase(true);
			request.setPrerelease(true);
			request.setHasexpired(true);
		}

		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
		aiSortCol.add(0);
		asSortDir.add("desc");
		amDataProp.add("subscribeStartTimeStr");

		request.setSortedColumns(aiSortCol);
		request.setSortDirections(asSortDir);
		request.setDataProp(amDataProp);

		int start = Integer.parseInt(creditmarket.getDisplayStart());
		DataTablesResponseDto<MarketItemDto> marketItemDto = new DataTablesResponseDto<MarketItemDto>();

		if (start > 0) {
			request.setDisplayLength(1);
			request.setDisplayStart(start - 1);
			marketItemDto = financingMarketService.getFinancingMarketItems(request, securityContext.getCurrentUserId(), isPlatformUser);
			if (marketItemDto.getData() != null)
				for (MarketItemDto marketItem : marketItemDto.getData()) {
					if (!marketItem.getId().equals(creditmarket.getId())) {
						return ResultDtoFactory.toNack("dataupdated");
					}
				}
		}

		request.setDisplayLength(20);
		request.setDisplayStart(start);

		marketItemDto = financingMarketService.getFinancingMarketItems(request,	securityContext.getCurrentUserId(), isPlatformUser);

		if (marketItemDto.getData() != null) {
			for (MarketItemDto marketItem : marketItemDto.getData()) {
				CreditorMarketdownDto creditoritem = new CreditorMarketdownDto();

				creditoritem.setProductId(marketItem.getId());
				creditoritem.setProductName(marketItem.getPackageName());
				if ("S".equals(marketItem.getStatus().getCode())) {
					creditoritem.setState("topurchase");
				} else if ("WS".equals(marketItem.getStatus().getCode())) {
					creditoritem.setState("prerelease");
				} else if ("WN".equals(marketItem.getStatus().getCode())) {
					creditoritem.setState("hasexpired");
				}
				creditoritem.setRiskLevel(marketItem.getProduct()
						.getRiskLevel().getCode());
				creditoritem.setRiskSafeguard(marketItem.getProduct()
						.getWarrantyType().getText());
				creditoritem.setAnnualProfit(marketItem.getProduct()
						.getRatePercentage());
				creditoritem.setLimitTime(marketItem.getProduct()
						.getTermLength()
						+ (marketItem.getProduct().getTermType() == null ? null
								: marketItem.getProduct().getTermType()
										.getText()));
				creditoritem
						.setStartTime(marketItem.getSubscribeStartTimeStr());
				creditoritem.setFinancedAmount(marketItem.getPackageQuotaStr());
				creditoritem.setAipFlag(marketItem.getAipFlag());
				creditoritem.setAccumulateAmount(marketItem.getSupplyAmount()
						.toString());

				creditorMarketdown.add(creditoritem);
			}
		}

		return ResultDtoFactory.toAck("获取成功", creditorMarketdown);
	}

	//立即申购详情
	@RequestMapping(value = "/purchaseid", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
	public ResultDto getPurchaseInfo(
			@OnValid @RequestBody CreditorMarketPurchaseIdupDto creditorMarketPID) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getPurchaseInfo() start: ");
		}

		FinancingMarketItemDetailDto financingMarketItemDetailDto = new FinancingMarketItemDetailDto();
		CreditorMarketPurchasedownDto creditorMarketPD = new CreditorMarketPurchasedownDto();

		financingMarketItemDetailDto = financingMarketController.getItemBaseInfo(creditorMarketPID
				.getId());
		if (null == financingMarketItemDetailDto.getReason()) {
			creditorMarketPD.setMaxSubscribeAmount(financingMarketItemDetailDto
					.getMaxSubscribeAmountStr());
			creditorMarketPD.setMinSubscribeAmount(financingMarketItemDetailDto
					.getMinSubscribeAmountStr());
			creditorMarketPD
					.setTotalSubscribeAmount(financingMarketItemDetailDto
							.getTotalSubscribeAmountStr());

			Double rate = null;
			double term = financingMarketItemDetailDto.getProduct()
					.getTermLength();
			double monthly = financingMarketItemDetailDto.getProduct()
					.getRate().doubleValue() / 12;
			if (financingMarketItemDetailDto.getProduct().getPayMethod() != null
					&& "04".equals(financingMarketItemDetailDto.getProduct().getPayMethod().getCode())) {
				double sum = Math.pow((1 + monthly), term);
				rate = monthly * sum / (sum - 1) * term - 1;
			} else {
				rate = financingMarketItemDetailDto.getProduct().getRate()
						.doubleValue()
						/ 360
						* financingMarketItemDetailDto.getProduct()
								.getTermToDays();
			}

			creditorMarketPD.setRate(rate);

			return ResultDtoFactory.toAck("获取成功", creditorMarketPD);
		}

		return ResultDtoFactory
				.toNack(financingMarketItemDetailDto.getReason());
	}
	
	//申购
	@RequestMapping(value = "/purchase", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_PURCHASE)
	public ResultDto unrealpurchase(
			@OnValid @RequestBody CreditorMarketPurchaseupDto request) {
		
		FinancingMarketPurchaseDto requestDto = new FinancingMarketPurchaseDto();
		
		requestDto.setId(request.getId());
		requestDto.setAmount(request.getAmount());
		
		return financingMarketController.purchase(requestDto);
	}

	
	//债权转让行情，refer to UserController类的融资包详情接口
}