/*
 * Project Name: kmfex-platform
 * File Name: FinancingManagementController.java
 * Class Name: FinancingManagementController
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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.app.dto.downstream.FinancingManagePackagesDownDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductsDownDto;
import com.hengxin.platform.app.dto.downstream.MyFinancierPackagesDetailV3DownDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductAssetDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductAttachmentDetailsDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductDebtDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductMortgageResidentialDetailsDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductMortgageVehicleDetailsDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductPackageDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductPledgeDetailsDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductWarrantEnterpriseDto;
import com.hengxin.platform.app.dto.downstream.FinancingManageProductDetailsDownDto.InnerProductWarrantPersonDto;
import com.hengxin.platform.app.dto.upstream.FinancierManageProductsUpDto;
import com.hengxin.platform.app.dto.upstream.FinancierPackagesUpDto;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.velocity.NumberTool;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.product.controller.FinanceListController;
import com.hengxin.platform.product.controller.FinanceProductController;
import com.hengxin.platform.product.controller.FinancingPackageController;
import com.hengxin.platform.product.controller.FinancingPackageListController;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductContractTemplate;
import com.hengxin.platform.product.dto.FinancingPackageSearchDto;
import com.hengxin.platform.product.dto.ProductAssetDto;
import com.hengxin.platform.product.dto.ProductAttachmentDetailsDto;
import com.hengxin.platform.product.dto.ProductDebtDto;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductListDto;
import com.hengxin.platform.product.dto.ProductMortgageResidentialDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageVehicleDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.dto.ProductPledgeDetailsDto;
import com.hengxin.platform.product.dto.ProductQueryConditionDto;
import com.hengxin.platform.product.dto.ProductWarrantEnterpriseDto;
import com.hengxin.platform.product.dto.ProductWarrantPersonDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.EProductActionType;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.service.FeeGroupService;
import com.hengxin.platform.product.service.FeeService;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.PaymentScheduleService;
import com.hengxin.platform.product.service.ProductContractTemplateService;
import com.hengxin.platform.product.service.ProductFreezeService;
import com.hengxin.platform.product.service.ProductListService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * 融资管理（服务中心、担保机构）
 * 
 * @author yicai
 *
 */
@Controller
public class FinancingManagementController extends BaseController {
	private static String defaultString = "--";

	@Autowired
	private SecurityContext securityContext;
	
	@Autowired
	private FinancingPackageService financingPackageService;
	
	@Autowired
    private PaymentScheduleRepository paymentScheduleRepository;
	
	@Autowired
	private PaymentScheduleService paymentScheduleService;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
    private AcctService acctService;
	
	@Autowired
    private ProductService productService;
	
	@Autowired
    private FeeGroupService feeGroupService;

    @Autowired
    private FeeService feeService;
	
	@Autowired
    private ProductContractTemplateService productContractTemplateService;
	
	@Autowired
    private ProductFreezeService productFreezeService;
	
    @Autowired
    private MemberService memberService;
	
	// inject web controller
	private FinancingPackageListController financingPackageListController; 
	
	// inject web controller
	private FinanceListController financeListController;
	
	// inject web controller
	private FinanceProductController financeProductController;
	
	// inject web controller 
	private FinancingPackageController financingPackageController;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FinancingManagementController.class);
	
	@PostConstruct
	public void init() {
		LOGGER.debug("init financingPackageListController");
		financingPackageListController = new FinancingPackageListController();
		financingPackageListController.setPaymentScheduleRepository(paymentScheduleRepository);
		financingPackageListController.setPaymentScheduleService(paymentScheduleService);
		financingPackageListController.setSecurityContext(securityContext);
		LOGGER.debug("init financeListController");
		financeListController = new FinanceListController();
		financeListController.setSecurityContext(securityContext);
		financeListController.setProductListService(productListService);
		financeListController.setAcctService(acctService);
		LOGGER.debug("init financeProductController");
		financeProductController = new FinanceProductController();
		financeProductController.setSecurityContext(securityContext);
		financeProductController.setProductFreezeService(productFreezeService);
		financeProductController.setFeeGroupService(feeGroupService);
		financeProductController.setFeeService(feeService);
		financeProductController.setProductService(productService);
		financeProductController.setProductPackageService(financingPackageService);
		LOGGER.debug("init financingPackageController");
		financingPackageController = new FinancingPackageController();
		financingPackageController.setFinancingPackageService(financingPackageService);
		financingPackageController.setSecurityContext(securityContext);
		financingPackageController.setMemberService(memberService);
		financingPackageController.setProductService(productService);
	}
	
	/**
     * 融资管理-融资包管理, post, 获取返回所有融资包列表通过过滤条件
     * 
     * refer to financingpackage/financier/getpackagelist
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "/financing/manage/packages", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FINANCIER_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_MEMBER_VIEW }, logical = Logical.OR)
    public ResultDto financingManagePackageList(@OnValid @RequestBody FinancierPackagesUpDto financierPackagesUpDto) {
    	if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("financingManagePackageList() start: ");
		}
    	FinancingPackageSearchDto searchDto = initFinancingPackageSearchDto(financierPackagesUpDto);
    	boolean isFinancier = securityContext.isFinancier();
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForFinancier(
                searchDto, securityContext.getCurrentUserId(), isFinancier);
        List<ProductPackageDto> packageDtoList = 
        		financingPackageListController.processPackageListForFinancierList(packageListForFinancierList);
        List<FinancingManagePackagesDownDto> myFinancierPackagesListDownDtoList = 
        		convert2MyFinancierPackagesListDownDto(packageListForFinancierList, packageDtoList);
        return ResultDtoFactory.toAck("获取成功", myFinancierPackagesListDownDtoList);
    }
    
    /**
     * convert to MyFinancierPackagesListDownDto
     * 
     * @param packageListForFinancierList
     * @param packageDtoList
     * @return
     */
    private List<FinancingManagePackagesDownDto> convert2MyFinancierPackagesListDownDto(
			Page<FinancingPackageView> packageListForFinancierList,
			List<ProductPackageDto> packageDtoList) {
    	NumberTool tool = new NumberTool();
    	List<FinancingManagePackagesDownDto> financingManagePackagesDownDtoList = 
    			new ArrayList<FinancingManagePackagesDownDto>();
    	FinancingManagePackagesDownDto financingManagePackagesDownDto = null;
    	FinancingPackageView tempPackagePo = null;
    	for (ProductPackageDto currentProductPackageDto : packageDtoList) {
    		financingManagePackagesDownDto = new FinancingManagePackagesDownDto();
    		financingManagePackagesDownDtoList.add(financingManagePackagesDownDto);
    		tempPackagePo = financingPackageService.getPackageDetails(currentProductPackageDto.getId());
    		
    		financingManagePackagesDownDto.setId(currentProductPackageDto.getId());
    		financingManagePackagesDownDto.setPackageName(currentProductPackageDto.getPackageName());
    		financingManagePackagesDownDto.setPackageQuota(tool.formatMoney(currentProductPackageDto.getPackageQuota()));
    		financingManagePackagesDownDto.setRate(FormatRate.formatRate(tempPackagePo.getRate().multiply(new BigDecimal(100))) + "%");
    		financingManagePackagesDownDto.setSignContractTime(currentProductPackageDto.getSignContractTime());
    		financingManagePackagesDownDto.setDuration(currentProductPackageDto.getDuration());
    		financingManagePackagesDownDto.setSupplyAmount(tool.formatMoney(currentProductPackageDto.getSupplyAmount()));
    		financingManagePackagesDownDto.setStatus(currentProductPackageDto.getStatus().getText());
    		financingManagePackagesDownDto.setSubsPercent(currentProductPackageDto.getSubsPercent());
    		financingManagePackagesDownDto.setFinancierName(currentProductPackageDto.getFinancierName());
    		financingManagePackagesDownDto.setCanCancelPackage(currentProductPackageDto.isCanCancelPackage());
    		financingManagePackagesDownDto.setCanPrepayment(currentProductPackageDto.isCanPrepayment());
    		financingManagePackagesDownDto.setCanPrintDebtInfo(currentProductPackageDto.isCanPrintDebtInfo());
    		financingManagePackagesDownDto.setCanSignContract(currentProductPackageDto.isCanSignContract());
    		financingManagePackagesDownDto.setCanStopPackage(currentProductPackageDto.isCanStopPackage());
    		financingManagePackagesDownDto.setCanViewRepayTable(currentProductPackageDto.isCanViewRepayTable());
    	}
		return financingManagePackagesDownDtoList;
	}
    
    /**
	 * init FinancingPackageSearchDto from InvestorPackagesUpDto
	 * 
	 * @param investorPackagesUpDto
	 * @return
	 */
	private FinancingPackageSearchDto initFinancingPackageSearchDto(
			FinancierPackagesUpDto financierPackagesUpDto) {
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
		amDataProp.add("id");
		searchDto.setKeyword(StringUtils.EMPTY);
		searchDto.setPackageStatus(EPackageStatus.NULL);
		searchDto.setDisplayLength(10); // default 10 item per page
		searchDto.setDisplayStart(Integer.parseInt(financierPackagesUpDto
				.getDisplayStart()));
		searchDto.setCreateTime(new Date(System.currentTimeMillis())); // default current time
		if (financierPackagesUpDto.getDisplayLength() != 0) {
			searchDto.setDisplayLength(financierPackagesUpDto.getDisplayLength());
		}
		if (!StringUtils.isEmpty(financierPackagesUpDto.getKeyword())) {
			searchDto.setKeyword(financierPackagesUpDto.getKeyword());
		}
		if (!StringUtils.isEmpty(financierPackagesUpDto.getPackageStatus()) &&
				!"NULL".equals(financierPackagesUpDto.getPackageStatus())) {
			searchDto.setPackageStatus(EnumHelper.translate(EPackageStatus.class,
					financierPackagesUpDto.getPackageStatus()));
		}
		if (financierPackagesUpDto.getCreateTime() != 0) {
			searchDto.setCreateTime(new Date(financierPackagesUpDto.getCreateTime()));
		}
		return searchDto;
	}
	
	/**
	 * 融资管理-融资包管理-融资包详情, get, 获取融资包详情
	 * 
	 * refer to financingpackage/details/{packageId}/{mode}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/financing/manage/package/detail/{packageId}", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FIN_PROD_DETAIL_VIEW,
			Permissions.PRODUCT_FINANCING_PACKAGE_PLATFORM_DETAIL_VIEW }, logical = Logical.OR)
	public ResultDto myFinancierPackageDetail(@PathVariable(value = "packageId") String packageId) {
		String mode = "view";
		FinancingPackageView packagePo = financingPackageService.getPackageDetails(packageId);
		ProductPackageDetailsDto detailsDto = financingPackageController
				.processGetFinancierPackageDetail(packageId, mode, packagePo);
		MyFinancierPackagesDetailV3DownDto myFinancierPackagesDetailV3DownDto = convert2MyFinancierPackagesDetailV3DownDto(
				detailsDto, packagePo);
		return ResultDtoFactory.toAck("获取成功", myFinancierPackagesDetailV3DownDto);
	}

	private MyFinancierPackagesDetailV3DownDto convert2MyFinancierPackagesDetailV3DownDto(ProductPackageDetailsDto detailsDto, 
			FinancingPackageView packagePo) {
		NumberTool tool = new NumberTool();
		MyFinancierPackagesDetailV3DownDto myFinancierPackagesDetailV3DownDto = new MyFinancierPackagesDetailV3DownDto();
		myFinancierPackagesDetailV3DownDto.setId(detailsDto.getId());
		myFinancierPackagesDetailV3DownDto.setPackageName(detailsDto.getPackageName());
		myFinancierPackagesDetailV3DownDto.setProductName(detailsDto.getProductName());
		myFinancierPackagesDetailV3DownDto.setAccountNo(detailsDto.getAccountNo());
		myFinancierPackagesDetailV3DownDto.setPackageQuota(tool.formatMoney(detailsDto.getPackageQuota()));
		myFinancierPackagesDetailV3DownDto.setFinancierName(detailsDto.getFinancierName());
		String rateString = "0.0%";
		BigDecimal rate = FormatRate.formatRate(detailsDto.getRate());
		if (detailsDto.getRate() != null) {
			rateString = rate.toString() + "%";
		}
		myFinancierPackagesDetailV3DownDto.setRate(rateString);
		myFinancierPackagesDetailV3DownDto.setGuaranteeLicenseNoImg(detailsDto.getGuaranteeLicenseNoImg());
		myFinancierPackagesDetailV3DownDto.setGuaranteeLicenseNoImgUrl(detailsDto.getGuaranteeLicenseNoImgUrl());
		myFinancierPackagesDetailV3DownDto.setDuration(detailsDto.getDuration());
		myFinancierPackagesDetailV3DownDto.setWarrantyType(detailsDto.getWarrantyType().getText());
		myFinancierPackagesDetailV3DownDto.setPayMethod(detailsDto.getPayMethod().getText());
		myFinancierPackagesDetailV3DownDto.setLoanPurpose(detailsDto.getLoanPurpose());
		myFinancierPackagesDetailV3DownDto.setWrtrName(!"--".equals(detailsDto.getWrtrName()) ? 
				detailsDto.getWrtrName() : "/");
		myFinancierPackagesDetailV3DownDto.setOverdue2CmpnsDays(detailsDto.getOverdue2CmpnsDays().toString() + "天");
		myFinancierPackagesDetailV3DownDto.setStatusAfterWaitLoadApproal(String.valueOf(detailsDto.isStatusAfterWaitLoadApproal()));
		myFinancierPackagesDetailV3DownDto.setContractTemplateId(detailsDto.getContractTemplateId());
		myFinancierPackagesDetailV3DownDto.setProductLevel(detailsDto.getProductLevel());
		myFinancierPackagesDetailV3DownDto.setAipFlag(detailsDto.getAipFlag());
		myFinancierPackagesDetailV3DownDto.setAutoSubsFlag(detailsDto.getAutoSubsFlag());
		myFinancierPackagesDetailV3DownDto.setSupplyStartTime(detailsDto.getSupplyStartTime());
		myFinancierPackagesDetailV3DownDto.setSupplyEndTime(detailsDto.getSupplyEndTime());
		myFinancierPackagesDetailV3DownDto.setSupplyUserCount(String.valueOf(detailsDto.getSupplyUserCount()));
		myFinancierPackagesDetailV3DownDto.setSubsPercent(detailsDto.getSubsPercent());
		myFinancierPackagesDetailV3DownDto.setSupplyAmount(tool.formatMoney(detailsDto.getSupplyAmount()));
		return myFinancierPackagesDetailV3DownDto;
	}
	
	
	/**
	 * 融资项目列表查看
	 * 
	 * refer to product/getproductlist
	 * 
	 * @return
	 */
	@RequestMapping(value = "/financing/manage/products", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_VIEW,
            Permissions.PRODUCT_FINANCIER_FINANCING_STATUS_VIEW, Permissions.MEMBER_FINANCING_APPLY_VIEW }, logical = Logical.OR)
	public ResultDto productLists(@RequestBody FinancierManageProductsUpDto financierManageProductPackagesUpDto,
            HttpServletRequest request, HttpSession session, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("productLists() start: ");
		}
		ProductQueryConditionDto searchDto = initFinancierManageProductPackagesUpDto(financierManageProductPackagesUpDto);
		DataTablesResponseDto<ProductListDto> productListDtDataTablesResponseDto = 
				financeListController.getProductList(searchDto, request, session, model);
		
		List<FinancingManageProductsDownDto> financierManageProductPackagesDownDtoList = convert2FinancierManageProductPackagesDownDtoList(productListDtDataTablesResponseDto);
		return ResultDtoFactory.toAck("获取成功", financierManageProductPackagesDownDtoList);
	}

	/**
	 * convert to FinancingManageProductsDownDto
	 * 
	 * @param productListDtDataTablesResponseDto
	 * @return
	 */
	private List<FinancingManageProductsDownDto> convert2FinancierManageProductPackagesDownDtoList(
			DataTablesResponseDto<ProductListDto> productListDtDataTablesResponseDto) {
		List<FinancingManageProductsDownDto> financierManageProductPackagesDownDtoList = null;
		if (productListDtDataTablesResponseDto.getData() != null) {
			financierManageProductPackagesDownDtoList = new ArrayList<FinancingManageProductsDownDto>();
			FinancingManageProductsDownDto financingManageProductsDownDto = null;
			for (ProductListDto productListDto : productListDtDataTablesResponseDto.getData()) {
				financingManageProductsDownDto = ConverterService.convert(productListDto, FinancingManageProductsDownDto.class);
				financierManageProductPackagesDownDtoList.add(financingManageProductsDownDto);
			}
		}
		return financierManageProductPackagesDownDtoList;
	}

	/**
	 * init ProductQueryConditionDto from FinancierManageProductPackagesUpDto	
	 * 
	 * @param financierManageProductPackagesUpDto
	 * @return
	 */
	private ProductQueryConditionDto initFinancierManageProductPackagesUpDto(
			FinancierManageProductsUpDto financierManageProductPackagesUpDto) {
		ProductQueryConditionDto searchDto = new ProductQueryConditionDto();
		// 模拟前端参数-字段排序列
		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
		searchDto.setSortedColumns(aiSortCol);
		searchDto.setSortDirections(asSortDir);
		searchDto.setDataProp(amDataProp);
		aiSortCol.add(0);
		asSortDir.add("desc");
		amDataProp.add("lastMntTs");
		searchDto.setProductActionType(EProductActionType.COMMIT);
		searchDto.setDisplayLength(10); // default 10 item per page
		searchDto.setDisplayStart(Integer.parseInt(financierManageProductPackagesUpDto
				.getDisplayStart()));
		searchDto.setSearchKeyString(StringUtils.EMPTY);
		searchDto.setProductStatus(EProductStatus.NULL);
//		searchDto.setCreateTime(new Date(System.currentTimeMillis())); // default current time
		
		if (financierManageProductPackagesUpDto.getDisplayLength() != 0) {
			searchDto.setDisplayLength(financierManageProductPackagesUpDto.getDisplayLength());
		}
		if (!StringUtils.isEmpty(financierManageProductPackagesUpDto.getSearchKeyString())) {
			searchDto.setSearchKeyString(financierManageProductPackagesUpDto.getSearchKeyString());
		}
		if (!StringUtils.isEmpty(financierManageProductPackagesUpDto.getProductStatus()) &&
				!"NULL".equals(financierManageProductPackagesUpDto.getProductStatus())) {
			searchDto.setProductStatus(EnumHelper.translate(EProductStatus.class,
					financierManageProductPackagesUpDto.getProductStatus()));
		}
//		if (financierManageProductPackagesUpDto.getCreateTime() != 0) {
//			searchDto.setCreateTime(new Date(financierManageProductPackagesUpDto.getCreateTime()));
//		}
		return searchDto;
	}
	
	/**
	 * 
	 * 融资项目管理-项目详情
	 * 
	 * refer to product/details/{productId}/{viewonly}
	 * here, filed "viewonly" is always true.
	 * 
	 * @param productId
	 * @param viewonly
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/financing/manage/product/detail/{productId}", method = RequestMethod.GET)
	@ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_VIEW,
            Permissions.PRODUCT_FINANCIER_FINANCING_STATUS_VIEW, Permissions.MEMBER_FINANCING_APPLY_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_VIEW }, logical = Logical.OR)
    public ResultDto getProductDetails(@PathVariable String productId, HttpServletResponse response, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getProductDetails() start: ");
		}
        boolean viewonly = true;
        
		Product product = productService.getProductBasicInfo(productId);
        ProductContractTemplate template=null;
   		try { 
   			template = productContractTemplateService.getProductContractTemplate(product
   			        .getContractTemplateId().toString());
   		} catch (Exception e) { 
   			LOGGER.error("EX {}", e);
   		}
   		
        ProductDetailsDto productDetailsDto = ConverterService.convert(product, ProductDetailsDto.class);

        financeProductController.populateProductDto(productDetailsDto, product, productId, viewonly);

        financeProductController.fillCashDeposit(productDetailsDto, product);

        financeProductController.populatePermissions(productDetailsDto, product);
        
		if (null != template) {
			productDetailsDto.setContractTemplateName(template.getTemplateName());
		}
		if (product.getWarrantyType() == EWarrantyType.MONITORASSETS
				|| product.getWarrantyType() == EWarrantyType.NOTHING) {
			productDetailsDto.setGuaranteeInstitution("--");
			productDetailsDto.setGuaranteeInstitutionShow("--");
		}

		if (productDetailsDto.getStatus() == EProductStatus.WAITTOAPPROVE
				&& securityContext.canApproveFinancingProduct()) {
			productDetailsDto.setCanFinancingApprove(true);
		} else if (productDetailsDto.getStatus() == EProductStatus.WAITTOPUBLISH
				&& securityContext.canCancelFinancingPackage()) {
			productDetailsDto.setCanCancelFinancingPackage(true);
		} else if (productDetailsDto.getStatus() == EProductStatus.WAITTOEVALUATE
				&& securityContext.canRatingFinancingProduct()) {
			productDetailsDto.setCanFinancingRating(true);
		} else if (productDetailsDto.getStatus() == EProductStatus.WAITTOFREEZE
				&& securityContext.canFreezeFinancingProductDeposit()) {
			productDetailsDto.setCanFinancingFreeze(true);
		} else if (securityContext.canSetFinancingProductTransSettings()) {
			productDetailsDto.setCanFinancingTransSetting(true);
		}

		productDetailsDto.setRate(FormatRate.formatRate(product.getRate()
				.multiply(new BigDecimal(100))));
		productDetailsDto.setAppliedQuota(product.getAppliedQuota());
       
		
		FinancingManageProductDetailsDownDto financingManageProductDetailsDownDto = 
				convert2FinancingManageProductDetailsDownDto(productDetailsDto);
		
		return ResultDtoFactory.toAck("获取成功", financingManageProductDetailsDownDto);
	}

	private FinancingManageProductDetailsDownDto convert2FinancingManageProductDetailsDownDto(
			ProductDetailsDto productDetailsDto) {
		FinancingManageProductDetailsDownDto fMPDDownDto = null;
		if (productDetailsDto != null) {
			NumberTool tool = new NumberTool();
			String defaultDis = "--";
			fMPDDownDto = new FinancingManageProductDetailsDownDto();
			/* 基本信息 */
			fMPDDownDto.setProductId(productDetailsDto.getProductId());
			fMPDDownDto.setProductName(productDetailsDto.getProductName());
			fMPDDownDto.setAppliedQuota(tool.formatMoney(productDetailsDto.getAppliedQuota()));
			fMPDDownDto.setRate(productDetailsDto.getRate() != null ? 
					productDetailsDto.getRate().toString() + "%" : defaultString);
			fMPDDownDto.setTermType(productDetailsDto.getTermType() != null 
					? productDetailsDto.getTermType() : ETermType.NULL);
			fMPDDownDto.setTermToDays(productDetailsDto.getTermToDays() != null 
					? productDetailsDto.getTermToDays().toString() : defaultDis);
			fMPDDownDto.setTermLength(productDetailsDto.getTermLength() != null
					? productDetailsDto.getTermLength().toString() : defaultDis);
			fMPDDownDto.setTerm("D".endsWith(fMPDDownDto.getTermType().getCode()) 
					? fMPDDownDto.getTermToDays() : fMPDDownDto.getTermLength() + fMPDDownDto.getTermType().getText());
			fMPDDownDto.setPayMethod(productDetailsDto.getPayMethod() != null ? productDetailsDto.getPayMethod() : EPayMethodType.NULL);
			fMPDDownDto.setProductLib(productDetailsDto.getProductLib() != null ? productDetailsDto.getProductLib().getLibName() : defaultDis);
			fMPDDownDto.setGuaranteeInstitution(productDetailsDto.getGuaranteeInstitution());
			fMPDDownDto.setGuaranteeInstitutionShow(productDetailsDto.getGuaranteeInstitutionShow());
			fMPDDownDto.setCanNotSeeGuarantors(productDetailsDto.isCanNotSeeGuarantors());
			if (productDetailsDto.isCanNotSeeGuarantors()) {
				fMPDDownDto.setGuaranteeInstitutionShow(StringUtils.isNotEmpty(productDetailsDto.getGuaranteeInstitution()) ? 
						productDetailsDto.getGuaranteeInstitutionShow() : defaultDis);
			}
			fMPDDownDto.setContractTemplateName(productDetailsDto.getContractTemplateName());
			fMPDDownDto.setCreateTs(productDetailsDto.getCreateTs());
			fMPDDownDto.setAcctNo(productDetailsDto.getAcctNo());
			fMPDDownDto.setFinancierName(productDetailsDto.getFinancierName());
			fMPDDownDto.setFinancierMobile(productDetailsDto.getFinancierMobile());
			fMPDDownDto.setDfinancierIndustryType(productDetailsDto.getDfinancierIndustryType());
			fMPDDownDto.setWarrantyType(productDetailsDto.getWarrantyType());
			fMPDDownDto.setLoanPurpose(productDetailsDto.getLoanPurpose());
			fMPDDownDto.setWrtrCreditDesc(productDetailsDto.getWrtrCreditDesc());
			
			fMPDDownDto.setCanNotSeeOverdue2CmpnsGracePeriod(EProductStatus.WAITTOAPPROVE == productDetailsDto.getStatus() 
					|| EProductStatus.STANDBY == productDetailsDto.getStatus());
			fMPDDownDto.setOverdue2CmpnsGracePeriod(productDetailsDto.getOverdue2CmpnsGracePeriod() != null 
					? productDetailsDto.getOverdue2CmpnsGracePeriod().toString() : defaultDis);
			fMPDDownDto.setCanSeeGrages(EProductStatus.WAITTOFREEZE == productDetailsDto.getStatus() 
					|| EProductStatus.WAITTOPUBLISH == productDetailsDto.getStatus() 
					|| EProductStatus.FINISHED == productDetailsDto.getStatus() 
					|| EProductStatus.ABANDONED == productDetailsDto.getStatus());
			fMPDDownDto.setProductGrageClass(productDetailsDto.getProductGrageClass());
			fMPDDownDto.setFinancierGrageClass(productDetailsDto.getFinancierGrageClass());
			fMPDDownDto.setWarrantGrageClass(productDetailsDto.getWarrantGrageClass());
			fMPDDownDto.setWrtrCreditFile(productDetailsDto.getWrtrCreditFile());
			fMPDDownDto.setCanSeeDepositFee(productDetailsDto.isCanSeeDepositFee());
			fMPDDownDto.setCashDepositFinanceServiceFee(productDetailsDto.getCashDeposit() != null ? tool.formatMoney(productDetailsDto.getCashDeposit().getFinanceServiceFee()) + "元" : defaultDis);
			fMPDDownDto.setCashDepositWarrantPercentage(productDetailsDto.getCashDeposit() != null ? productDetailsDto.getCashDeposit().getWarrantPercentage() + "% (" + tool.formatMoney(productDetailsDto.getCashDeposit().getWarrantFee()) + "元)" : defaultDis);
			fMPDDownDto.setCanSeeFee(productDetailsDto.isCanSeeFee());
			fMPDDownDto.setCashDepositContractFeePer(productDetailsDto.getCashDeposit() != null ? productDetailsDto.getCashDeposit().getContractFeePer() + "% (" + tool.formatMoney(productDetailsDto.getCashDeposit().getContractFee()) + "元)" : defaultDis);
			fMPDDownDto.setCashDepositServiceFee(productDetailsDto.getCashDeposit() != null ? tool.formatMoney(productDetailsDto.getCashDeposit().getServiceFee()) + "元" : defaultDis);
			fMPDDownDto.setCashDepositRiskFee(productDetailsDto.getCashDeposit() != null ? tool.formatMoney(productDetailsDto.getCashDeposit().getRiskFee()) + "元" : defaultDis);
			List<InnerProductPackageDto> productPackageDtoList = new ArrayList<InnerProductPackageDto>();
			fMPDDownDto.setProductPackageDtoList(productPackageDtoList);
			InnerProductPackageDto ippd = null;
			for (ProductPackageDto current : productDetailsDto.getProductPackageDtoList()) {
				ippd = fMPDDownDto.new InnerProductPackageDto();
				productPackageDtoList.add(ippd);
				ippd.setId(current.getId());
				ippd.setPackageName(current.getPackageName());
			}
			
			/* 更多详情 */
			/* 保证金及费用参考 */
			fMPDDownDto.setFinanceFee(tool.formatMoney(productDetailsDto.getFinanceFee()) + "元");
			fMPDDownDto.setWarrantFee(tool.formatMoney(productDetailsDto.getWarrantFee()) + "元");
			fMPDDownDto.setContractFee(tool.formatMoney(productDetailsDto.getContractFee()) + "元");
			fMPDDownDto.setServiceFee(tool.formatMoney(productDetailsDto.getServiceFee()) + "元");
			fMPDDownDto.setRiskFee(tool.formatMoney(productDetailsDto.getRiskFee()) + "元");
			fMPDDownDto.setSeatFee(productDetailsDto.getFeeDto() != null && productDetailsDto.getFeeDto().getEndDt() != null 
					?  "到期日：" + productDetailsDto.getFeeDto().getEndDt() : 
						productDetailsDto.getFinancierSeatFee() != null ? "￥" + tool.formatMoney(productDetailsDto.getFinancierSeatFee()) : defaultDis);
			
			/* 反担保情况 */
			/* 抵押产品  */
			List<InnerProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList = 
					new ArrayList<InnerProductMortgageResidentialDetailsDto>(); // 房屋列表
			fMPDDownDto.setProductMortgageResidentialDetailsDtoList(productMortgageResidentialDetailsDtoList);
			InnerProductMortgageResidentialDetailsDto ipmrdd = null;
			for (ProductMortgageResidentialDetailsDto current : productDetailsDto.getProductMortgageResidentialDetailsDtoList()) {
				ipmrdd = fMPDDownDto.new InnerProductMortgageResidentialDetailsDto();
				productMortgageResidentialDetailsDtoList.add(ipmrdd);
				ipmrdd.setMortgageType(current.getDmortgageType() != null ? current.getDmortgageType().getText() : defaultDis);
				ipmrdd.setPremisesPermitNo(current.getPremisesPermitNo());
				ipmrdd.setOwner(current.getOwner());
				ipmrdd.setOwnerType(current.getOwnerType() != null ? current.getOwnerType().getText() : defaultDis);
				ipmrdd.setCoOwner(current.getCoOwner());
				ipmrdd.setCoOwnerAge(current.getCoOwnerAge() != null ? current.getCoOwnerAge().toString() : defaultDis);
				ipmrdd.setLocation(current.getLocation());
				ipmrdd.setArea(current.getArea() != null ? current.getArea().toString() + "平方" : defaultDis);
				ipmrdd.setRegistDate(current.getRegistDate());
				ipmrdd.setPurchasePrice(current.getPurchasePrice() != null ? tool.formatMoney(current.getPurchasePrice()) + "万元" : defaultDis);
				ipmrdd.setEvaluatePrice(current.getEvaluatePrice() != null ? tool.formatMoney(current.getEvaluatePrice()) + "万元" : defaultDis);
				ipmrdd.setMarketPrice(current.getMarketPrice() != null ? tool.formatMoney(current.getMarketPrice()) + "万元" : defaultDis);
			}
			
			List<InnerProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList = 
					new ArrayList<InnerProductMortgageVehicleDetailsDto>(); // 车辆列表
			fMPDDownDto.setProductMortgageVehicleDetailsDtoList(productMortgageVehicleDetailsDtoList);
			InnerProductMortgageVehicleDetailsDto ipmvdd = null;
			for (ProductMortgageVehicleDetailsDto current : productDetailsDto.getProductMortgageVehicleDetailsDtoList()) {
				ipmvdd = fMPDDownDto.new InnerProductMortgageVehicleDetailsDto();
				productMortgageVehicleDetailsDtoList.add(ipmvdd);
				ipmvdd.setOwner(current.getOwner());
				ipmvdd.setRegistNo(current.getRegistNo());
				ipmvdd.setRegistInstitution(current.getRegistInstitution());
				ipmvdd.setType(current.getType());
				ipmvdd.setRegistDt(current.getRegistDt());
				ipmvdd.setBrand(current.getBrand());
			}
			fMPDDownDto.setOtherMortgage(productDetailsDto.getOtherMortgage());;// 其他抵押
			
			/* 质押产品  */
		    List<InnerProductPledgeDetailsDto> productPledgeDetailsDtoList = 
		    		new ArrayList<FinancingManageProductDetailsDownDto.InnerProductPledgeDetailsDto>(); // 动产质押列表
		    fMPDDownDto.setProductPledgeDetailsDtoList(productPledgeDetailsDtoList);
		    InnerProductPledgeDetailsDto ippdd = null;
		    for (ProductPledgeDetailsDto current : productDetailsDto.getProductPledgeDetailsDtoList()) {
		    	ippdd = fMPDDownDto.new InnerProductPledgeDetailsDto();
		    	productPledgeDetailsDtoList.add(ippdd);
				ippdd.setName(current.getName());
				ippdd.setPrice(current.getPrice() != null ? tool.formatMoney(current.getPrice()) + "万元" : defaultDis);
				ippdd.setPledgeClass(current.getPledgeClass());
				
				ippdd.setLocation(current.getLocation());
				ippdd.setType(current.getType());
				ippdd.setNotes(current.getNotes());
				ippdd.setCount(current.getCount() != null ? current.getCount().toString() : defaultDis);
		    }
		    fMPDDownDto.setOtherPledge(productDetailsDto.getOtherPledge());// 其他质押
		    
		    /* 保证担保 */
		    List<InnerProductWarrantPersonDto> productWarrantPersonDtoList = 
		    		new ArrayList<InnerProductWarrantPersonDto>(); // 自然人列表
		    fMPDDownDto.setProductWarrantPersonDtoList(productWarrantPersonDtoList);
		    InnerProductWarrantPersonDto ipwpd = null;
		    for (ProductWarrantPersonDto current : productDetailsDto.getProductWarrantPersonDtoList()) {
		    	ipwpd = fMPDDownDto.new InnerProductWarrantPersonDto();
		    	productWarrantPersonDtoList.add(ipwpd);
		    	ipwpd.setName(current.getName());
		    	ipwpd.setIdNo(current.getIdNo());
		    	ipwpd.setDjob(current.getDjob() != null ? current.getDjob().getText() : defaultDis);
		    	ipwpd.setNotes(current.getNotes());
		    }
		    
		    List<InnerProductWarrantEnterpriseDto> productWarrantEnterpriseDtoList = 
		    		new ArrayList<InnerProductWarrantEnterpriseDto>(); // 法人列表
		    fMPDDownDto.setProductWarrantEnterpriseDtoList(productWarrantEnterpriseDtoList);
		    InnerProductWarrantEnterpriseDto ipwed = null;
		    for (ProductWarrantEnterpriseDto current : productDetailsDto.getProductWarrantEnterpriseDtoList()) {
		    	ipwed = fMPDDownDto.new InnerProductWarrantEnterpriseDto();
		    	productWarrantEnterpriseDtoList.add(ipwed);
		    	ipwed.setEnterpriseName(current.getEnterpriseName());
		    	ipwed.setDenterpriseIndustry(current.getDenterpriseIndustry() != null ? current.getDenterpriseIndustry().getText() : defaultDis);
		    	ipwed.setEnterpriseLicenceNo(current.getEnterpriseLicenceNo());
		    	ipwed.setEnterpriseNotes(current.getEnterpriseNotes());
		    }
		    
			/* 资产及负债 */
		    List<InnerProductAssetDto> productAssetDtoList = 
		    		new ArrayList<InnerProductAssetDto>();// 资产列表
		    fMPDDownDto.setProductAssetDtoList(productAssetDtoList);
		    InnerProductAssetDto ipad = null;
		    for (ProductAssetDto current : productDetailsDto.getProductAssetDtoList()) {
		    	ipad = fMPDDownDto.new InnerProductAssetDto();
		    	productAssetDtoList.add(ipad);
		    	ipad.setDtype(current.getDtype() != null ? current.getDtype().getText() : defaultDis);
		    	ipad.setAssertAmount(current.getAssertAmount() != null ? tool.formatMoney(current.getAssertAmount()) + "万元": defaultDis);
		    	ipad.setNotes(current.getNotes());
		    }
		    
		    List<InnerProductDebtDto> productDebtDtoList =
		    		new ArrayList<InnerProductDebtDto>();// 负债列表
		    fMPDDownDto.setProductDebtDtoList(productDebtDtoList);
		    InnerProductDebtDto ipdd = null;
		    for (ProductDebtDto current : productDetailsDto.getProductDebtDtoList()) {
		    	ipdd = fMPDDownDto.new InnerProductDebtDto();
		    	productDebtDtoList.add(ipdd);
		    	ipdd.setDtype(current.getDtype() != null ? current.getDtype().getText() : defaultDis);
		    	ipdd.setMonthlyPayment(current.getMonthlyPayment() != null ? tool.formatMoney(current.getMonthlyPayment()) + "元" : defaultDis);
		    	ipdd.setDebtAmount(current.getDebtAmount() != null ? tool.formatMoney(current.getDebtAmount()) + "万元" : defaultDis);
		    	ipdd.setNotes(current.getNotes());
		    	
		    }
		    
			/* 资料库 */
		    List<InnerProductAttachmentDetailsDto> productAttachmentDetailsDtoList = 
		    		new ArrayList<InnerProductAttachmentDetailsDto>();
		    fMPDDownDto.setProductAttachmentDetailsDtoList(productAttachmentDetailsDtoList);
		    InnerProductAttachmentDetailsDto ipadd = null;
		    for (ProductAttachmentDetailsDto current : productDetailsDto.getProductAttachmentDetailsDtoList()) {
		    	ipadd = fMPDDownDto.new InnerProductAttachmentDetailsDto();
		    	productAttachmentDetailsDtoList.add(ipadd);
		    	ipadd.setFile(current.getFile());
		    	ipadd.setPath(current.getPath());
//		    	StringUtilTool stringUtilTool = new StringUtilTool();
//		    	if (stringUtilTool.isPdf(current.getPath())) {
		    }
		    
			/* 备注 */
		    fMPDDownDto.setNotes(productDetailsDto.getNotes()); // 备注
			
		}
		
		return fMPDDownDto;
	}
}
