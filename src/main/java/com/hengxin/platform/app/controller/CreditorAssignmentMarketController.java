/*
 * Project Name: kmfex-platform
 * File Name: CreditorAssignmentMarketController.java
 * Class Name: CreditorAssignmentMarketController
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hengxin.platform.app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.app.dto.downstream.CreditorAssignmentMarketdownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDetailDownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDetailDownDto.ProductAssetDownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDetailDownDto.ProductDebtDownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDetailDownDto.ProductMortgageResidentialDetailsDownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDetailDownDto.ProductMortgageVehicleDetailsDownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDetailDownDto.ProductPledgeDetailsDownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDetailDownDto.ProductWarrantEnterpriseDownDto;
import com.hengxin.platform.app.dto.downstream.TransferMarketDetailDownDto.ProductWarrantPersonDownDto;
import com.hengxin.platform.app.dto.upstream.CreditorAssignmentMarketPurchaseUpDto;
import com.hengxin.platform.app.dto.upstream.PagingDto;
import com.hengxin.platform.app.dto.upstream.TransferMarketUpDto;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.common.util.velocity.NumberTool;
import com.hengxin.platform.market.controller.TransferMarketController;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.downstream.TransferMarketItemDetailDto;
import com.hengxin.platform.market.dto.upstream.TransferMarketPurchaseDto;
import com.hengxin.platform.market.dto.upstream.TransferMarketSearchDto;
import com.hengxin.platform.market.enums.ETransferType;
import com.hengxin.platform.market.service.TransferMarketService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.product.dto.ProductAssetDto;
import com.hengxin.platform.product.dto.ProductDebtDto;
import com.hengxin.platform.product.dto.ProductMortgageResidentialDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageVehicleDetailsDto;
import com.hengxin.platform.product.dto.ProductPledgeDetailsDto;
import com.hengxin.platform.product.dto.ProductWarrantEnterpriseDto;
import com.hengxin.platform.product.dto.ProductWarrantPersonDto;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * description: 债权转让市场控制器类.
 * 
 */
@Controller
public class CreditorAssignmentMarketController {

	@Autowired
	private SecurityContext securityContext;

    @Autowired
    private MemberService memberService;
	
	@Autowired
	private transient TransferMarketService transferMarketService;
	
	@Autowired
	private transient PaymentScheduleRepository paymentScheduleRepository;

	// inject web controller
	private TransferMarketController transferMarketController;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CreditorAssignmentMarketController.class);

	@PostConstruct
	public void init() {
		LOGGER.debug("init transferMarketController");
		transferMarketController = new TransferMarketController();
		transferMarketController.setSecurityContext(securityContext);
		transferMarketController.setTransferMarketService(transferMarketService);
		transferMarketController.setMemberService(memberService);
		transferMarketController.setPaymentScheduleRepository(paymentScheduleRepository);
	}
	
	@RequestMapping(value = "/creditorAssignmentMarket")
	@ResponseBody
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW)
	public ResultDto doCreditorAssignmentMarket(PagingDto pagingDto) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doCreditorAssignmentMarket() start: ");
		}

		boolean isPlatformUser = securityContext.isPlatformUser();
		TransferMarketSearchDto request = new TransferMarketSearchDto();
		List<CreditorAssignmentMarketdownDto> creditorAMD = new ArrayList<CreditorAssignmentMarketdownDto>();

		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
		aiSortCol.add(0);
		asSortDir.add("asc");
		amDataProp.add("maturityDateStr");

		request.setSortedColumns(aiSortCol);
		request.setSortDirections(asSortDir);
		request.setDataProp(amDataProp);
		request.setDisplayLength(10);
		request.setDisplayStart(0);

		DataTablesResponseDto<MarketItemDto> marketItemDto = transferMarketService
				.getTransferMarketItems(request,
						securityContext.getCurrentUserId(), isPlatformUser);

		if (marketItemDto.getData() != null) {
			for (MarketItemDto marketItem : marketItemDto.getData()) {
				CreditorAssignmentMarketdownDto creditoritem = new CreditorAssignmentMarketdownDto();

				creditoritem.setProductId(marketItem.getId());
				creditoritem.setProductName(marketItem.getPackageName());
				creditoritem.setRiskLevel(marketItem.getProduct()
						.getRiskLevel().getText());
				creditoritem.setRiskSafeguard(marketItem.getProduct()
						.getWarrantyType().getText());
				creditoritem.setAnnualProfit(marketItem.getProduct().getRate()
						.toString());
				creditoritem.setQuotedPrice(marketItem.getPrice().toString());
				creditoritem.setRemainingPrincipalAndInterest(marketItem
						.getRemainingAmount().toString());
				creditoritem.setDeadline(marketItem.getMaturityDate()
						.toString());
				creditoritem.setRepaymentManner(marketItem.getProduct()
						.getPayMethod().getText());

				creditorAMD.add(creditoritem);
			}
		}

		return ResultDtoFactory.toAck("获取成功", creditorAMD);
	}
	
	
	/***************/
	/* 二期新增接口 */
	/***************/
	
	/**
	 * 债权转让列表，post，获取列表条目
	 * 
	 * refer to market/transfer/search
	 * 
	 * @return
	 */
	@RequestMapping(value = "/market/transfers", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW)
	public ResultDto creditorAssignmentMarket(@RequestBody TransferMarketUpDto transferMarketUpDto) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("creditorAssignmentMarket() invoked");
		}
		TransferMarketSearchDto searchDto = initTransferMarketSearchDto(transferMarketUpDto);
		boolean isPlatformUser = securityContext.isPlatformUser();
        DataTablesResponseDto<MarketItemDto> marketItemDtoTable = transferMarketService.getTransferMarketItems(searchDto,
                securityContext.getCurrentUserId(), isPlatformUser);
        List<TransferMarketDownDto> transferMarketDownDtoList = convert2TransferMarketDownDtoList(marketItemDtoTable);
		return ResultDtoFactory.toAck("获取成功", transferMarketDownDtoList);
	}
	
	private List<TransferMarketDownDto> convert2TransferMarketDownDtoList(
			DataTablesResponseDto<MarketItemDto> marketItemDtoTable) {
		NumberTool tool = new NumberTool();
		List<TransferMarketDownDto> transferMarketDownDtoList = new ArrayList<TransferMarketDownDto>();
		TransferMarketDownDto transferMarketDownDto = null;
		for (MarketItemDto marketItemDto : marketItemDtoTable.getData()) {
			transferMarketDownDto = new TransferMarketDownDto();
			transferMarketDownDto.setTransferId(marketItemDto.getTransferId());
			transferMarketDownDto.setPackageId(marketItemDto.getId());
			transferMarketDownDto.setPakcageName(marketItemDto.getPackageName());
			transferMarketDownDto.setRiskLevel(marketItemDto.getProduct().getProductLevel());
			transferMarketDownDto.setWarrantyType(marketItemDto.getProduct().getWarrantyType());
			transferMarketDownDto.setAnnualProfit(FormatRate.formatRate(marketItemDto.getProduct().getRate().multiply(new BigDecimal(100))) + "%");
			transferMarketDownDto.setQuotedPrice(tool.formatMoney(marketItemDto.getPrice()));
			transferMarketDownDto.setPayMethod(marketItemDto.getProduct().getPayMethod());
			transferMarketDownDto.setMaturityDate(marketItemDto.getMaturityDate().toString());
			transferMarketDownDto.setRemTermLength(marketItemDto.getRemTermLength().toString());
			transferMarketDownDto.setRemainingAmount(tool.formatMoney(marketItemDto.getRemainingAmount()));
			transferMarketDownDto.setResidualPrincipalAmt(tool.formatMoney(marketItemDto.getResidualPrincipalAmt()));
			transferMarketDownDto.setResidualInterestAmt(tool.formatMoney(marketItemDto.getResidualInterestAmt()));
			transferMarketDownDtoList.add(transferMarketDownDto);
			TransferMarketItemDetailDto transferMarketItemDetailDto = 
					transferMarketController.detail(marketItemDto.getTransferId());
			transferMarketDownDto.setExpectedReturnStr(transferMarketItemDetailDto.getExpectedReturnStr());
			transferMarketDownDto.setExpectedReturnRate(transferMarketItemDetailDto.getExpectedReturnRate());
			transferMarketDownDto.setTransferFeeStr(transferMarketItemDetailDto.getTransferFeeStr());
			transferMarketDownDto.setReason(transferMarketItemDetailDto.getReason());
			transferMarketDownDto.setAipFlag(marketItemDto.getAipFlag());
			transferMarketDownDto.setMine(marketItemDto.getMine());
			transferMarketDownDto.setOverdue(marketItemDto.isOverdue());
		}
		return transferMarketDownDtoList;
	}

	private TransferMarketSearchDto initTransferMarketSearchDto(
			TransferMarketUpDto transferMarketUpDto) {
		TransferMarketSearchDto searchDto = new TransferMarketSearchDto();
		// 模拟前端参数-字段排序列
//		List<Integer> aiSortCol = new ArrayList<Integer>();
//		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
//		searchDto.setSortedColumns(aiSortCol);
//		searchDto.setSortDirections(asSortDir);
		searchDto.setDataProp(amDataProp);
//		aiSortCol.add(0);
//		asSortDir.add("asc");
		amDataProp.add("id");
		
		searchDto.setPackageId(StringUtils.EMPTY);
		searchDto.setTransferType(ETransferType.OTHER);
		searchDto.setDisplayLength(10); // default 10 item per page
		searchDto.setDisplayStart(Integer.parseInt(transferMarketUpDto
				.getDisplayStart()));
		searchDto.setCreateTime(new Date(System.currentTimeMillis())); // default current time
		if (!StringUtils.isEmpty(transferMarketUpDto.getPackageId())) {
			searchDto.setPackageId(transferMarketUpDto.getPackageId());
		}
		if (transferMarketUpDto.getDisplayLength() != 0) {
			searchDto.setDisplayLength(transferMarketUpDto.getDisplayLength());
		}
		if (!StringUtils.isEmpty(transferMarketUpDto.getTransferType()) &&
				!"OTHER".equals(transferMarketUpDto.getTransferType())) {
			searchDto.setTransferType(EnumHelper.translate(ETransferType.class,
					transferMarketUpDto.getTransferType()));
		} // "MINE", "OTHER", "ALL"
		if (transferMarketUpDto.getCreateTime() != 0) {
			searchDto.setCreateTime(new Date(transferMarketUpDto.getCreateTime()));
		}
		return searchDto;
	}
	
	/**
	 * 债权转让详情，get
	 * 
	 * refer to  market/transfer/detail/{id}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/market/transfer/detail/{transferId}", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW)
	public ResultDto creditorAssignmentMarketDetail(@PathVariable(value = "transferId") String id) {
		Map<String, Object> map = transferMarketController.processMarketTransferDetail(id);
		TransferMarketDetailDownDto transferMarketDetailDownDto = convert2TransferMarketDetailDownDto(map);
		return ResultDtoFactory.toAck("获取成功", transferMarketDetailDownDto);
	}

	private TransferMarketDetailDownDto convert2TransferMarketDetailDownDto(
			Map<String, Object> map) {
		NumberTool tool = new NumberTool();
		TransferMarketItemDetailDto itemDto = (TransferMarketItemDetailDto) map.get("itemDto");
		/* 融资包基本信息 */
		TransferMarketDetailDownDto transferMarketDetailDownDto = new TransferMarketDetailDownDto();
		transferMarketDetailDownDto.setPackageId(itemDto.getPkg().getId()); // 融资包编号
		transferMarketDetailDownDto.setPackageName(itemDto.getPkg().getPackageName()); // 融资包名称
		transferMarketDetailDownDto.setTerm(itemDto.getProduct().getTermLength() + 
				itemDto.getProduct().getTermType().getText()); // 融资期限，如：13天，1个月，6个月
		transferMarketDetailDownDto.setAnnualProfit(itemDto.getProduct().getRatePercent()[0] + 
				"." + itemDto.getProduct().getRatePercent()[1] + "%"); // 年利率
		transferMarketDetailDownDto.setPackageQuota(tool.formatMoney(itemDto.getPkg().getPackageQuota())); // 融资额（元）
		transferMarketDetailDownDto.setRemainingAmount(itemDto.getRemainingAmountStr()); // 剩余本息
		transferMarketDetailDownDto.setDeadline(itemDto.getMaturityDateStr());; // 最后还款日
		transferMarketDetailDownDto.setQuotedPrice(itemDto.getPriceStr()); // 报价（元）
		transferMarketDetailDownDto.setRiskSafeguard(itemDto.getProduct().getWarrantyType().getText());; // 风险保障：本息担保、本金担保、无担保、资产监
		/* 基本详情 */
		transferMarketDetailDownDto.setFinancier(itemDto.getProduct().getUser().getMaskChineseName());; // 融资人
		transferMarketDetailDownDto.setRepaymentWay(itemDto.getProduct().getPayMethod().getText()); // 还款方式
		try {
			transferMarketDetailDownDto.setIndustry(itemDto.getProduct().getFinancierIndustryType().getText()); // 行业
		} catch (Exception e) {
			transferMarketDetailDownDto.setIndustry(null); // 行业
		}
		transferMarketDetailDownDto.setFinancePurpose(itemDto.getProduct().getLoanPurpose()); // 借款用途说明
		transferMarketDetailDownDto.setGuaranteeInstitution(itemDto.getProduct().getGuaranteeInstitution()); // 担保机构
		transferMarketDetailDownDto.setWrtrCreditDesc(itemDto.getProduct().getWrtrCreditDesc());
		transferMarketDetailDownDto.setGuaranteeLicenseNoImg(itemDto.getProduct().getGuaranteeLicenseNoImg());;
		transferMarketDetailDownDto.setGuaranteeLicenseNoImgName(itemDto.getProduct().getGuaranteeLicenseNoImgUrl());
		/* 风险概况 */
		transferMarketDetailDownDto.setProductLevel(itemDto.getProduct().getProductLevel()); // 融资项目级别
		transferMarketDetailDownDto.setFinancierLevel(itemDto.getProduct().getFinancierLevel()); // 融资会员信用积分
		transferMarketDetailDownDto.setWarrantorLevel(itemDto.getProduct().getWarrantorLevel()); // 担保机构信用积分
		/* 抵押担保 */
		/* 反担保详情-抵押产品 */
		TransferMarketDetailDownDto temp = new TransferMarketDetailDownDto();
		List<ProductMortgageResidentialDetailsDownDto> buildingList = 
				new ArrayList<ProductMortgageResidentialDetailsDownDto>();
		transferMarketDetailDownDto.setBuildingList(buildingList); // 房产抵押
		ProductMortgageResidentialDetailsDownDto pmrddDto = null;
		for (ProductMortgageResidentialDetailsDto tempDto : itemDto.getProduct().getProductMortgageResidentialDetailsDtoList()) {
			pmrddDto = temp.new ProductMortgageResidentialDetailsDownDto();
			pmrddDto.setArea(tempDto.getArea() != null ? tempDto.getArea().toString() : "--");
			pmrddDto.setEvaluatePrice(tool.formatMoney(tempDto.getEvaluatePrice()));
			pmrddDto.setMarketPrice(tool.formatMoney(tempDto.getMarketPrice()));
			pmrddDto.setPurchasePrice(tool.formatMoney(tempDto.getPurchasePrice()));
			buildingList.add(pmrddDto);
		}
		List<ProductMortgageVehicleDetailsDownDto> carList = new ArrayList<ProductMortgageVehicleDetailsDownDto>();
		transferMarketDetailDownDto.setCarList(carList); // 车辆抵押
		ProductMortgageVehicleDetailsDownDto pmvddDto = null;
		for (ProductMortgageVehicleDetailsDto tempDto : itemDto.getProduct().getProductMortgageVehicleDetailsDtoList()) {
			pmvddDto = temp.new ProductMortgageVehicleDetailsDownDto();
			pmvddDto.setBrand(tempDto.getBrand());
			pmvddDto.setType(tempDto.getType());
			carList.add(pmvddDto);
		}
		/* 反担保详情-质押产品 */
		List<ProductPledgeDetailsDownDto> chattelMortgageList = new ArrayList<ProductPledgeDetailsDownDto>();
		transferMarketDetailDownDto.setChattelMortgageList(chattelMortgageList); // 动产质押
		ProductPledgeDetailsDownDto ppddDto = null;
		for (ProductPledgeDetailsDto tempDto : itemDto.getProduct().getProductPledgeDetailsDtoList()) {
			ppddDto = temp.new ProductPledgeDetailsDownDto();
			ppddDto.setCount(tempDto.getCount() != null ? tempDto.getCount().toString() : null);
			ppddDto.setLocation(tempDto.getLocation());
			ppddDto.setName(tempDto.getName());
			ppddDto.setPledgeClass(tempDto.getPledgeClass());
			ppddDto.setPrice(tool.formatMoney(tempDto.getPrice()));
			ppddDto.setType(tempDto.getType());
			chattelMortgageList.add(ppddDto);
		}
		/* 反担保详情-保证担保 */
		List<ProductWarrantPersonDownDto> warrantPersonList = new ArrayList<ProductWarrantPersonDownDto>();
		transferMarketDetailDownDto.setWarrantPersonList(warrantPersonList); // 自然人列表
		ProductWarrantPersonDownDto pwpdDto = null;
		for (ProductWarrantPersonDto tempDto : itemDto.getProduct().getProductWarrantPersonDtoList()) {
			pwpdDto = temp.new ProductWarrantPersonDownDto();
			pwpdDto.setWarrantPersonStr(tempDto.getName());
			warrantPersonList.add(pwpdDto);
		}
		List<ProductWarrantEnterpriseDownDto> warrantEnterpriseList = new ArrayList<ProductWarrantEnterpriseDownDto>();
		transferMarketDetailDownDto.setWarrantEnterpriseList(warrantEnterpriseList); // 法人列表
		ProductWarrantEnterpriseDownDto pwedDto = null;
		for (ProductWarrantEnterpriseDto tempDto : itemDto.getProduct().getProductWarrantEnterpriseDtoList()) {
			pwedDto = temp.new ProductWarrantEnterpriseDownDto();
			pwedDto.setWarrantEnterpriseStr(tempDto.getEnterpriseName());
			warrantEnterpriseList.add(pwedDto);
		}
		/* 资产及负债 */
		List<ProductAssetDownDto> personalAssetList = 
				new ArrayList<ProductAssetDownDto>();
		transferMarketDetailDownDto.setPersonalAssetList(personalAssetList); // 个人资产情况列表
		ProductAssetDownDto padDto = null;
		for (ProductAssetDto tempDto : itemDto.getProduct().getProductAssetDtoList()) {
			padDto = temp.new ProductAssetDownDto();
			padDto.setAssertAmount(tempDto.getAssertAmount());
			padDto.setNotes(tempDto.getNotes());
			padDto.setType(tempDto.getDtype().getText());
			personalAssetList.add(padDto);
		}
		List<ProductDebtDownDto> personalLiabilityList = new ArrayList<ProductDebtDownDto>();
		transferMarketDetailDownDto.setPersonalLiabilityList(personalLiabilityList); // 个人负债情况列表
		ProductDebtDownDto pddDto = null;
		for (ProductDebtDto tempDto : itemDto.getProduct().getProductDebtDtoList()) {
			pddDto = temp.new ProductDebtDownDto();
			pddDto.setDebtAmount(tool.formatMoney(tempDto.getDebtAmount()));
			pddDto.setMonthlyPayment(tool.formatMoney(tempDto.getMonthlyPayment()));
			pddDto.setNotes(tempDto.getNotes());
			pddDto.setType(tempDto.getDtype().getText());
			personalLiabilityList.add(pddDto);
		}
    	
		return transferMarketDetailDownDto;
	}

	
	/**
	 * 债权转让购买，post
	 * 
	 * refer to market/transfer/purchase/{id}
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "market/transfer/purchase/{lotId}", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW)
    public ResultDto detail(@PathVariable(value = "lotId") String id) {
        TransferMarketItemDetailDto resp = transferMarketService.getTransferItemDetail(id,
                securityContext.getCurrentUserId());
        if (!securityContext.isInvestor()) {
            resp.setTransfered(false);
            resp.setReason(MessageUtil.getMessage("market.error.subscribe.permissions.nothave"));
        }
        if (!CommonBusinessUtil.isMarketOpen()) {
            resp.setTransfered(false);
            resp.setReason(MessageUtil.getMessage("market.error.market.close"));
        }
        return ResultDtoFactory.toAck("获取成功", resp.getReason());
    }
	
	/**
	 * 债权转让购买，post
	 * 
	 * refer to market/transfer/purchase
	 * 
	 * @return
	 */
	@RequestMapping(value = "market/transfer/purchase", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_ASSIGNMENT_PURCHASE)
	public ResultDto creditorAssignmentMarketPhurse(@RequestBody CreditorAssignmentMarketPurchaseUpDto 
			creditorAssignmentMarketPurchaseUpDto) {
		 LOGGER.debug("purchase() invoked");
        // 判断是否开市
        if (!CommonBusinessUtil.isMarketOpen()) {
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.market.close"));
        }
        // 验证投资会员
        if (!securityContext.isInvestor()) {
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.subscribe.permissions.nothave"));
        }
		try {
			TransferMarketPurchaseDto item = transferMarketService.purchase(
					securityContext.getCurrentUserId(),
					creditorAssignmentMarketPurchaseUpDto.getId());
			creditorAssignmentMarketPurchaseUpDto = ConverterService.convert(
					item, CreditorAssignmentMarketPurchaseUpDto.class);
		} catch (BizServiceException ex) {
			return ResultDtoFactory.toNack(ex.getError());
		}
		return ResultDtoFactory.toAck(MessageUtil.getMessage(
				"market.transfer.deal.success", NumberUtil.formatMoney(creditorAssignmentMarketPurchaseUpDto
					.getAmount()), NumberUtil.formatMoney(creditorAssignmentMarketPurchaseUpDto
					.getFee())));
	}
}
