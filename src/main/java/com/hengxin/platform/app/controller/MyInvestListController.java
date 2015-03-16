/*
 * Project Name: kmfex-platform
 * File Name: MyInvestListController.java
 * Class Name: MyInvestListController
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.app.dto.downstream.MyInvestListV2DownDto;
import com.hengxin.platform.app.dto.downstream.MyInvestListdownDto;
import com.hengxin.platform.app.dto.downstream.MyInvestOverviewDownDto;
import com.hengxin.platform.app.dto.downstream.ProductPackageInvestorDetailsDownDto;
import com.hengxin.platform.app.dto.downstream.ProductPackageInvestorDetailsDownDto.ProductAssetDownDto;
import com.hengxin.platform.app.dto.downstream.ProductPackageInvestorDetailsDownDto.ProductDebtDownDto;
import com.hengxin.platform.app.dto.downstream.ProductPackageInvestorDetailsDownDto.ProductMortgageResidentialDetailsDownDto;
import com.hengxin.platform.app.dto.downstream.ProductPackageInvestorDetailsDownDto.ProductMortgageVehicleDetailsDownDto;
import com.hengxin.platform.app.dto.downstream.ProductPackageInvestorDetailsDownDto.ProductPledgeDetailsDownDto;
import com.hengxin.platform.app.dto.downstream.ProductPackageInvestorDetailsDownDto.ProductWarrantEnterpriseDownDto;
import com.hengxin.platform.app.dto.downstream.ProductPackageInvestorDetailsDownDto.ProductWarrantPersonDownDto;
import com.hengxin.platform.app.dto.upstream.InvestorPackagesUpDto;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.velocity.NumberTool;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.market.service.FinancingMarketService;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.product.controller.FinancePackageInvestorController;
import com.hengxin.platform.product.controller.FinancingPackageListController;
import com.hengxin.platform.product.converter.InvestorPackageConverter;
import com.hengxin.platform.product.dto.FinancePackageInvestorDto;
import com.hengxin.platform.product.dto.FinancingPackageSearchDto;
import com.hengxin.platform.product.dto.ProductAssetDto;
import com.hengxin.platform.product.dto.ProductDebtDto;
import com.hengxin.platform.product.dto.ProductMortgageResidentialDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageVehicleDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageInvestorDetailsDto;
import com.hengxin.platform.product.dto.ProductPledgeDetailsDto;
import com.hengxin.platform.product.dto.ProductWarrantEnterpriseDto;
import com.hengxin.platform.product.dto.ProductWarrantPersonDto;
import com.hengxin.platform.product.dto.TransferPriceDto;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.service.CreditorTransferMaintainServie;
import com.hengxin.platform.product.service.CreditorTransferService;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.PaymentScheduleService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * 我的债权.
 *
 */
@Controller
public class MyInvestListController extends BaseController {
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private FinancingPackageService financingPackageService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CreditorTransferMaintainServie creditorTransferMaintainServie;

	@Autowired
	private MemberService memberService;

	@Autowired
	private FinancingMarketService financingMarketService;
	
    @Autowired
    private CreditorTransferService creditorTransferService;

    @Autowired
    private PkgTradeJnlRepository pkgTradeJnlRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PaymentScheduleService paymentScheduleService;
    
	// inject web controller
	private FinancePackageInvestorController financePackageInvestorController;

	// inject web controller
	private FinancingPackageListController financingPackageListController;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MyInvestListController.class);

	@PostConstruct
	public void init() {
		LOGGER.debug("init financePackageInvestorController");
		financePackageInvestorController = new FinancePackageInvestorController();
		financePackageInvestorController.setProductService(productService);
		financePackageInvestorController
				.setFinancingPackageService(financingPackageService);
		financePackageInvestorController.setSecurityContext(securityContext);
		financePackageInvestorController
				.setCreditorTransferMaintainServie(creditorTransferMaintainServie);
		financePackageInvestorController.setMemberService(memberService);
		financePackageInvestorController
				.setFinancingMarketService(financingMarketService);
		financePackageInvestorController.setPkgTradeJnlRepository(pkgTradeJnlRepository);
		financePackageInvestorController.setUserRepository(userRepository);
		financePackageInvestorController.setPaymentScheduleService(paymentScheduleService);
		LOGGER.debug("init financingPackageListController");
		financingPackageListController = new FinancingPackageListController();
		financingPackageListController.setProductService(productService);
		financingPackageListController
				.setFinancingPackageService(financingPackageService);
		financingPackageListController.setSecurityContext(securityContext);
	}

    @Transactional(readOnly = true)
	@RequestMapping(value = "/user/myInvestList")
	@ResponseBody
	@RequiresPermissions(value = Permissions.MY_ACCOUNT_MY_CREDITOR_VIEW)
	public ResultDto doMyInvestList() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doMyInvestList() start: ");
		}

		FinancingPackageSearchDto request = new FinancingPackageSearchDto();
		List<MyInvestListdownDto> myILDDList = new ArrayList<MyInvestListdownDto>();

		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();

		aiSortCol.add(0);
		asSortDir.add("desc");
		amDataProp.add("signContractTime");

		request.setSortedColumns(aiSortCol);
		request.setSortDirections(asSortDir);
		request.setDataProp(amDataProp);
		request.setDisplayLength(10);
		request.setDisplayStart(0);

		request.setKeyword("");
		request.setStartDate("");
		request.setEndDate("");

		Page<PositionLotPo> packageListForInverstorList = financingPackageService
				.getFinancingPackageListByUserId(
						securityContext.getCurrentUserId(), request);

		if (packageListForInverstorList != null) {
			for (PositionLotPo investorPackage : packageListForInverstorList) {
				MyInvestListdownDto myILDD = new MyInvestListdownDto();
				FinancePackageInvestorDto financePackageInvestorDto = ConverterService
						.convert(investorPackage,
								FinancePackageInvestorDto.class,
								InvestorPackageConverter.class);

				myILDD.setProductId(financePackageInvestorDto.getPkgId());
				myILDD.setProductName(financePackageInvestorDto
						.getPackageName());
				myILDD.setAnnualProfit(financePackageInvestorDto.getRate()
						.toString());
				myILDD.setLimitTime(financePackageInvestorDto.getTerm());
				myILDD.setPurchaseTime(financePackageInvestorDto.getSubsDate());
				myILDD.setPurchaseAmount(financePackageInvestorDto.getAmount()
						.toString());

//				myILDD.setProductId(investorPackage.getPosition()
//						.getProductPackage().getId());
//				myILDD.setProductName(investorPackage.getPosition()
//						.getProductPackage().getPackageName());
//				myILDD.setAnnualProfit(investorPackage.getPosition()
//						.getFinancingPackageView().getRate().toString());
//				myILDD.setLimitTime(investorPackage.getPosition()
//						.getFinancingPackageView().getDuration());
//				myILDD.setPurchaseTime(investorPackage.getSubsDt().toString());
//				myILDD.setPurchaseAmount(investorPackage.getLotBuyPrice()
//						.toString());

				myILDDList.add(myILDD);
			}
		}

		return ResultDtoFactory.toAck("获取成功", myILDDList);
	}

    /***************/
	/* 二期新增接口 */
	/***************/
    
    
	/**
	 * 债权总览
	 * 
	 * refer to /mycreditors
	 * 
	 * @author yicai
	 */
	@RequestMapping(value = "/investor/overview", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = Permissions.MY_ACCOUNT_MY_CREDITOR_VIEW)
	public ResultDto doInvestOverview() {
		Map<String, BigDecimal> map = financingPackageListController
				.processSummaryCreditor();
		MyInvestOverviewDownDto myInvestOverviewDownDto = convertMyInvestOverviewDownDto(map);
		return ResultDtoFactory.toAck("获取成功", myInvestOverviewDownDto);
	}

	private MyInvestOverviewDownDto convertMyInvestOverviewDownDto(
			Map<String, BigDecimal> map) {
		BigDecimal totalNextPayAmt = map.get("totalNextPayAmt"); // 下期预期总收益
		BigDecimal totalPrincipal = map.get("totalPrincipal"); // 申购本金总额
		BigDecimal totalRestAmt = map.get("totalRestAmt"); // 剩余本息总额
		NumberTool tool = new NumberTool();
		MyInvestOverviewDownDto myInvestOverviewDownDto = new MyInvestOverviewDownDto();
		myInvestOverviewDownDto.setTotalPrincipal(tool
				.formatMoney(totalPrincipal));
		myInvestOverviewDownDto.setTotalNextPayAmt(tool
				.formatMoney(totalNextPayAmt));
		myInvestOverviewDownDto.setTotalRestAmt(tool.formatMoney(totalRestAmt));
		return myInvestOverviewDownDto;
	}

	/**
	 * 我的债权列表, post, 获取返回所有债权列表及过滤条件搜索
	 * 
	 * refer to search/getpackagelist.
	 * 
	 * @author yicai
	 */
	@Transactional(readOnly = true)
	@RequestMapping(value = "/investor/packages", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_VIEW })
	public ResultDto myInvestPackageList(@OnValid @RequestBody InvestorPackagesUpDto investorPackagesUpDto) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("myInvestPackageList() start: ");
		}
		FinancingPackageSearchDto searchDto = initFinancingPackageSearchDto(investorPackagesUpDto);
		Page<PositionLotPo> packageListForInvestorList = financingPackageService
				.getFinancingPackageListByUserId(securityContext.getCurrentUserId(), searchDto);
		List<MyInvestListV2DownDto> myILDDListDownDto = 
				processPackageListForInvestorList(packageListForInvestorList);
		return ResultDtoFactory.toAck("获取成功", myILDDListDownDto);
	}

	/**
	 * init FinancingPackageSearchDto from InvestorPackagesUpDto
	 * 
	 * @param investorPackagesUpDto
	 * @return
	 */
	private FinancingPackageSearchDto initFinancingPackageSearchDto(
			InvestorPackagesUpDto investorPackagesUpDto) {
		FinancingPackageSearchDto searchDto = new FinancingPackageSearchDto();
		// 模拟前端参数-字段排序列
		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
		searchDto.setSortedColumns(aiSortCol);
		searchDto.setSortDirections(asSortDir);
		searchDto.setDataProp(amDataProp);
		aiSortCol.add(0);
		asSortDir.add("desc");
		amDataProp.add("signContractTime");
		searchDto.setStartDate(StringUtils.EMPTY);
		searchDto.setEndDate(StringUtils.EMPTY);
		searchDto.setKeyword(StringUtils.EMPTY);
		searchDto.setPayStatus(EPayStatus.NULL);
		searchDto.setDisplayLength(10); // default 10 item per page
		searchDto.setDisplayStart(Integer.parseInt(investorPackagesUpDto
				.getDisplayStart()));
		searchDto.setCreateTime(new Date(System.currentTimeMillis())); // default current time
		if (!StringUtils.isEmpty(investorPackagesUpDto.getStartDate())) {
			searchDto.setStartDate(investorPackagesUpDto.getStartDate());
		}
		if (!StringUtils.isEmpty(investorPackagesUpDto.getEndDate())) {
			searchDto.setEndDate(investorPackagesUpDto.getEndDate());
		}
		if (investorPackagesUpDto.getDisplayLength() != 0) {
			searchDto.setDisplayLength(investorPackagesUpDto.getDisplayLength());
		}
		if (!StringUtils.isEmpty(investorPackagesUpDto.getKeyword())) {
			searchDto.setKeyword(investorPackagesUpDto.getKeyword());
		}
		if (!StringUtils.isEmpty(investorPackagesUpDto.getPayStatus()) &&
				!"NULL".equals(investorPackagesUpDto.getPayStatus())) {
			searchDto.setPayStatus(EnumHelper.translate(EPayStatus.class,
					investorPackagesUpDto.getPayStatus()));
		}
		if (investorPackagesUpDto.getCreateTime() != 0) {
			searchDto.setCreateTime(new Date(investorPackagesUpDto.getCreateTime()));
		}
		return searchDto;
	}
	
	private List<MyInvestListV2DownDto> processPackageListForInvestorList(
			Iterable<PositionLotPo> packageListForInverstorList) {
		List<MyInvestListV2DownDto> myILDDListDownDto = new ArrayList<MyInvestListV2DownDto>();
		Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
		Date processDate = DateUtils.getDate("2014-05-01 00:00:00",
				YYYY_MM_DD_HH_MM_SS);
		for (PositionLotPo investorPackage : packageListForInverstorList) {
			// invoke web controller process the core business.
			FinancePackageInvestorDto financePackageInvestorDto = financePackageInvestorController
					.processPositionLotPo(currentWorkDate, processDate,
							investorPackage);
			myILDDListDownDto
					.add(convertFinancePackageInvestorDto2MyInvestListdownDto(financePackageInvestorDto));
		} // end of for
		return myILDDListDownDto;
	}

	private MyInvestListV2DownDto convertFinancePackageInvestorDto2MyInvestListdownDto(
			FinancePackageInvestorDto financePackageInvestorDto) {
		NumberTool tool = new NumberTool();
		MyInvestListV2DownDto myInvestListdownDto = new MyInvestListV2DownDto();
		myInvestListdownDto.setAmount(tool.formatMoney(financePackageInvestorDto.getAmount()));
		myInvestListdownDto.setNextPayAmount(tool.formatMoney(
				financePackageInvestorDto.getNextPayAmount()));
		myInvestListdownDto.setPackageName(financePackageInvestorDto
				.getPackageName());
		myInvestListdownDto.setPkgId(financePackageInvestorDto.getPkgId());
		myInvestListdownDto.setLotId(financePackageInvestorDto.getLotId());
		myInvestListdownDto.setRate(financePackageInvestorDto.getRate().toString());
		myInvestListdownDto.setSubsDate(financePackageInvestorDto.getSubsDate());
		myInvestListdownDto.setSignContractTime(financePackageInvestorDto.getSignContractTime());
		myInvestListdownDto.setTerm(financePackageInvestorDto.getTerm());
		myInvestListdownDto.setAipFlag(financePackageInvestorDto.getAipFlag());
		myInvestListdownDto.setCanCreditorRightsTransfer(
				financePackageInvestorDto.isCanCreditorRightsTransfer());
		myInvestListdownDto.setCanTransferCancel(
				financePackageInvestorDto.isCanTransferCancel());
		myInvestListdownDto.setSysRate(financePackageInvestorDto.getSysRate());
		myInvestListdownDto.setLastAmount(
				tool.formatMoney(financePackageInvestorDto.getLastAmount()));
		myInvestListdownDto.setNextPayDay(financePackageInvestorDto
				.getNextPayDay());
		myInvestListdownDto.setResidualPrincipalAmt(tool
				.formatMoney(financePackageInvestorDto
						.getResidualPrincipalAmt()));
		myInvestListdownDto.setResidualInterestAmt(tool
						.formatMoney(financePackageInvestorDto
								.getResidualInterestAmt()));
		myInvestListdownDto.setLastPayDay(financePackageInvestorDto
				.getLastPayDay());
		myInvestListdownDto.setCostPrice(tool
				.formatMoney(financePackageInvestorDto.getCostPrice()));
		myInvestListdownDto.setTransferMinPrice(tool
				.formatMoney(financePackageInvestorDto.getTransferMinPrice()));
		/* 剩余本息合计为最高上限 */
		myInvestListdownDto.setTransferMaxPrice(tool
				.formatMoney(financePackageInvestorDto.getTransferMaxPrice()));
		return myInvestListdownDto;
	}

	/**
	 * 我的债权详情，也就是债权融资包详情
	 * 
	 * 请求地址示例：...investor/details/AC1400005201?lotId=14052000007402&_=
	 * 1410500260401 
	 * refer to /details/{pkgId}.
	 */
    @Transactional(readOnly = true)
	@RequestMapping(value = "/investor/details/{pkgId}", method = RequestMethod.GET)
    @ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_VIEW })
	public ResultDto getMyInvestPackageDetail(
			@PathVariable(value = "pkgId") String pkgId,
			@RequestParam(required = false) String lotId) {
		ProductPackageInvestorDetailsDto dto = financePackageInvestorController
				.processFinancierPackageDetails(pkgId, lotId);
		PositionLotPo positionLotPo = financingPackageService.getPositionLot(lotId);
		ProductPackageInvestorDetailsDownDto downDto = convertProductPackageInvestorDetailsDto(positionLotPo, dto);
		return ResultDtoFactory.toAck("获取成功", downDto);
	}

	/**
	 * 设置融资包基本信息
	 * 
	 * @param investorPackage
	 * @return
	 */
	private ProductPackageInvestorDetailsDownDto convertProductPackageInvestorDetailsDto(
			PositionLotPo investorPackage, ProductPackageInvestorDetailsDto dto) {
		ProductPackageInvestorDetailsDownDto downDto = 
				new ProductPackageInvestorDetailsDownDto();
		Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
        Date processDate = DateUtils.getDate("2014-05-01 00:00:00", YYYY_MM_DD_HH_MM_SS);
		NumberTool tool = new NumberTool();
		FinancePackageInvestorDto financePackageInvestorDto = 
				financePackageInvestorController.processPositionLotPo(currentWorkDate, processDate, investorPackage);
		/* 头部详情 */
		downDto.setLastAmount(tool.formatMoney(financePackageInvestorDto.getLastAmount())); // 剩余本息合计 
		downDto.setNextPayAmount(tool.formatMoney(financePackageInvestorDto.getNextPayAmount())); // 下期收益日
		downDto.setNextPayDay(financePackageInvestorDto.getNextPayDay()); // 下期预期收益额（元）
		downDto.setPackageName(financePackageInvestorDto.getPackageName()); // 融资包名称
		downDto.setLimitTime(financePackageInvestorDto.getTerm()); // 融资期限
		downDto.setAnnualProfit(financePackageInvestorDto.getRate() != null ? 
				financePackageInvestorDto.getRate().toString() : "--"); // 年利率 
		/* 基本详情 */
		downDto.setFinancedAmount(tool.formatMoney(dto.getPackageDetailsDto().getPackageQuota())); // 融资额（元）
		downDto.setRiskSafeguard(dto.getPackageDetailsDto().getWarrantyType().getText());
		downDto.setFinancier(dto.getPackageDetailsDto().getFinancierName()); // 融资人
		downDto.setIndustry(dto.getProductDetailsDto().getDfinancierIndustryType() != null ? 
				dto.getProductDetailsDto().getDfinancierIndustryType().getText() : "--"); // 行业
		downDto.setGuaranteeInstitution(StringUtils.isEmpty(dto.getGuaranteeLicenseNoImgUrl()) ? 
				 "--" : dto.getProductDetailsDto().getGuaranteeInstitution()); // 担保机构
		downDto.setProductGrageClass(dto.getProductDetailsDto().getProductGrageClass()); // 产品评分
		downDto.setFinancierGrageClass(dto.getProductDetailsDto().getFinancierGrageClass()); // 融资人评分
		downDto.setRepaymentWay(dto.getPackageDetailsDto().getPayMethod().getText()); // 还款方式
		downDto.setFinancePurpose(dto.getPackageDetailsDto().getLoanPurpose()); // 借款用途
		downDto.setGuaranteeLicenseNoImg(dto.getGuaranteeLicenseNoImg());
		downDto.setGuaranteeLicenseNoImgUrl(dto.getGuaranteeLicenseNoImgUrl()); //担保机构营业执照
		downDto.setWarrantGrageClass(dto.getProductDetailsDto().getWarrantGrageClass()); // 担保人（担保机构）评分
		downDto.setProductLevel(dto.getProductDetailsDto().getProductLevel());
		downDto.setFinancierLevel(dto.getProductDetailsDto().getFinancierLevel());
		downDto.setWarrantorLevel(dto.getProductDetailsDto().getWarrantorLevel());

		/* 反担保详情-抵押产品 */
		List<ProductMortgageResidentialDetailsDownDto> buildingList = 
				new ArrayList<ProductMortgageResidentialDetailsDownDto>();
		downDto.setBuildingList(buildingList); // 房产抵押
		ProductMortgageResidentialDetailsDownDto pmrddDto = null;
		for (ProductMortgageResidentialDetailsDto tempDto : dto.getProductDetailsDto().getProductMortgageResidentialDetailsDtoList()) {
			pmrddDto = downDto.new ProductMortgageResidentialDetailsDownDto();
			pmrddDto.setArea(tempDto.getArea() != null ? tempDto.getArea().toString() : "--");
			pmrddDto.setEvaluatePrice(tool.formatMoney(tempDto.getEvaluatePrice()));
			pmrddDto.setMarketPrice(tool.formatMoney(tempDto.getMarketPrice()));
			pmrddDto.setPurchasePrice(tool.formatMoney(tempDto.getPurchasePrice()));
			buildingList.add(pmrddDto);
		}
		List<ProductMortgageVehicleDetailsDownDto> carList = new ArrayList<ProductMortgageVehicleDetailsDownDto>();
		downDto.setCarList(carList); // 车辆抵押
		ProductMortgageVehicleDetailsDownDto pmvddDto = null;
		for (ProductMortgageVehicleDetailsDto tempDto : dto.getProductDetailsDto().getProductMortgageVehicleDetailsDtoList()) {
			pmvddDto = downDto.new ProductMortgageVehicleDetailsDownDto();
			pmvddDto.setBrand(tempDto.getBrand());
			pmvddDto.setType(tempDto.getType());
			carList.add(pmvddDto);
		}
		/* 反担保详情-质押产品 */
		List<ProductPledgeDetailsDownDto> chattelMortgageList = new ArrayList<ProductPledgeDetailsDownDto>();
		downDto.setChattelMortgageList(chattelMortgageList); // 动产质押
		ProductPledgeDetailsDownDto ppddDto = null;
		for (ProductPledgeDetailsDto tempDto : dto.getProductDetailsDto().getProductPledgeDetailsDtoList()) {
			ppddDto = downDto.new ProductPledgeDetailsDownDto();
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
		downDto.setWarrantPersonList(warrantPersonList); // 自然人列表
		ProductWarrantPersonDownDto pwpdDto = null;
		for (ProductWarrantPersonDto tempDto : dto.getProductDetailsDto().getProductWarrantPersonDtoList()) {
			pwpdDto = downDto.new ProductWarrantPersonDownDto();
			pwpdDto.setWarrantPersonStr(tempDto.getName());
			warrantPersonList.add(pwpdDto);
		}
		List<ProductWarrantEnterpriseDownDto> warrantEnterpriseList = new ArrayList<ProductWarrantEnterpriseDownDto>();
		downDto.setWarrantEnterpriseList(warrantEnterpriseList); // 法人列表
		ProductWarrantEnterpriseDownDto pwedDto = null;
		for (ProductWarrantEnterpriseDto tempDto : dto.getProductDetailsDto().getProductWarrantEnterpriseDtoList()) {
			pwedDto = downDto.new ProductWarrantEnterpriseDownDto();
			pwedDto.setWarrantEnterpriseStr(tempDto.getEnterpriseName());
			warrantEnterpriseList.add(pwedDto);
		}
		/* 资产及负债 */
		List<ProductAssetDownDto> personalAssetList = 
				new ArrayList<ProductAssetDownDto>();
		downDto.setPersonalAssetList(personalAssetList); // 个人资产情况列表
		ProductAssetDownDto padDto = null;
		for (ProductAssetDto tempDto : dto.getProductDetailsDto().getProductAssetDtoList()) {
			padDto = downDto.new ProductAssetDownDto();
			padDto.setAssertAmount(tempDto.getAssertAmount());
			padDto.setNotes(tempDto.getNotes());
			padDto.setType(tempDto.getDtype().getText());
			personalAssetList.add(padDto);
		}
		List<ProductDebtDownDto> personalLiabilityList = new ArrayList<ProductDebtDownDto>();
		downDto.setPersonalLiabilityList(personalLiabilityList); // 个人负债情况列表
		ProductDebtDownDto pddDto = null;
		for (ProductDebtDto tempDto : dto.getProductDetailsDto().getProductDebtDtoList()) {
			pddDto = downDto.new ProductDebtDownDto();
			pddDto.setDebtAmount(tool.formatMoney(tempDto.getDebtAmount()));
			pddDto.setMonthlyPayment(tool.formatMoney(tempDto.getMonthlyPayment()));
			pddDto.setNotes(tempDto.getNotes());
			pddDto.setType(tempDto.getDtype().getText());
			personalLiabilityList.add(pddDto);
		}
		return downDto;
	}

	/**
	 * 提交债权转让报价
	 * 
	 * refer to transfer/save/{lotId}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/investor/transfer/save", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_CREDITOR_ASSIGNMENT })
	public ResultDto saveTransfer(@OnValid @RequestBody TransferPriceDto transferPriceDto) {
		if (!CommonBusinessUtil.isMarketOpen()) {
    		return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.market.close.transfer"), null);
    	}
		this.creditorTransferService.saveCreditorTransfer(transferPriceDto);
        return ResultDtoFactory.toAck(MessageUtil.getMessage("mycreditor.transfer.price.success"), null);
	}

	/**
	 * 取消债权转让
	 * 
	 * refer to /transfer/cancel/{lotId}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/investor/transfer/cancel/{lotId}", method = RequestMethod.DELETE)
	@ResponseBody
    @RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_CREDITOR_ASSIGNMENT_WITHDRAW })
	public ResultDto cancelTransfer(@PathVariable(value = "lotId") String lotId) {
		creditorTransferService.cancelCreditorTransfer(lotId);
        return ResultDtoFactory.toAck(MessageUtil.getMessage("mycreditor.transfer.cancel.success"), null);
	}
}
