/*
 * Project Name: kmfex-platform
 * File Name: FinanceProductController.java
 * Class Name: FinanceProductController
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

package com.hengxin.platform.product.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.FeeDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.FeePayStatePo;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.UserRoleService;
import com.hengxin.platform.product.ProductUtil;
import com.hengxin.platform.product.domain.Fee;
import com.hengxin.platform.product.domain.MortgageResidential;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductAsset;
import com.hengxin.platform.product.domain.ProductAttachment;
import com.hengxin.platform.product.domain.ProductDebt;
import com.hengxin.platform.product.domain.ProductMortgageVehicle;
import com.hengxin.platform.product.domain.ProductPledge;
import com.hengxin.platform.product.domain.ProductWarrantEnterprise;
import com.hengxin.platform.product.domain.ProductWarrantPerson;
import com.hengxin.platform.product.dto.AccountDtoForAccountNo;
import com.hengxin.platform.product.dto.ProductAssetDto;
import com.hengxin.platform.product.dto.ProductAttachmentDetailsDto;
import com.hengxin.platform.product.dto.ProductDebtDto;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductGetFeeDto;
import com.hengxin.platform.product.dto.ProductMortgageResidentialDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageVehicleDetailsDto;
import com.hengxin.platform.product.dto.ProductPledgeDetailsDto;
import com.hengxin.platform.product.dto.ProductSeatFeeDto;
import com.hengxin.platform.product.dto.ProductSystemFeeDto;
import com.hengxin.platform.product.dto.ProductUserDto;
import com.hengxin.platform.product.dto.ProductWarrantEnterpriseDto;
import com.hengxin.platform.product.dto.ProductWarrantPersonDto;
import com.hengxin.platform.product.dto.ProductDetailsDto.SubmitProductApply;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.service.FeeGroupService;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: FinanceProductApplyController
 * 
 * @author Ryan
 * 
 */
@Controller
public class FinanceProductApplyController extends BaseController {
	private static final String DEFAULT_PRODUCT_DEFAULT_FEE_GROUP_ID = "1";
	private static final int LENGTH_TYPE_DAY = 13;
	private static final String SAVE = "SAVE";
	private static final String SUBMIT = "SUBMIT";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FinanceProductApplyController.class);
	@Autowired
	private ProductService productService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private FeeGroupService feeGroupService;

	@Autowired
	private AcctService acctService;

	@Autowired
	private FinancingPackageService financingPackageService;
	@Autowired
	private UserRoleService roleservice;
	@Autowired
	private MemberMessageService messageService;

	@Autowired
	private UserRepository userRepository;

	/**
	 * 
	 * Description:获得apply VM
	 * 
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/financeproductapply")
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY })
	public String toApply(HttpServletResponse response, Model model) {
		model.addAttribute("productLibList", productService.getProductLib());
		model.addAttribute("industryList",
				getDynamicOptions(EOptionCategory.ORG_INDUSTRY, true));
		model.addAttribute("jobList",
				getDynamicOptions(EOptionCategory.JOB, true));
		model.addAttribute("hoseTypeList",
				getDynamicOptions(EOptionCategory.REAL_ESTATE_TYPE, true));
		model.addAttribute("assetTypeList",
				getDynamicOptions(EOptionCategory.ASSET_TYPE, true));
		model.addAttribute("debtTypeList",
				getDynamicOptions(EOptionCategory.DEBT_TYPE, true));
		return "product/financeproduct_apply";
	}

	/**
	 * 
	 * Description:融资人项目申请页面加载
	 * 
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "product/financier/financeproductapply")
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER })
	public String toFinancierApply(HttpServletResponse response, Model model) {
		model.addAttribute("financierApplication", true);
		String currentUserId = securityContext.getCurrentUserId();
		AcctPo acct = acctService.getAcctByUserId(currentUserId);
		if (acct != null) {
			model.addAttribute("accountNo", acct.getAcctNo());
			model.addAttribute("financierName",
					securityContext.getCurrentUserName());
		}
		return this.toApply(response, model);
	}

	/**
	 * 
	 * Description: 通过交易账号获得名字
	 * 
	 * @param accName
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/getName/{accName}", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ProductUserDto getName(@PathVariable String accName) {

		String userID = productService.getUserId(accName);
		if (!StringUtils.EMPTY.equals(userID)) {
			if (roleservice.getUserRole(userID, EBizRole.FINANCIER.getCode()) == null) {
				return new ProductUserDto();
			}
			ProductUserDto userDto = ConverterService.convert(
					productService.getUser(userID), ProductUserDto.class);
			FeePayStatePo feePayStatePo = productService.findSeatFee(userID);

			if (feePayStatePo == null) {
				return userDto;
			} else {
				if (feePayStatePo.getEndDt().compareTo(ProductUtil.getDate()) == 1) {
					userDto.setSeatFee(ConverterService.convert(feePayStatePo,
							ProductSeatFeeDto.class));
				}
			}
			return userDto;
		}
		return new ProductUserDto();
	}

	/**
	 * 
	 * Description: 通过名字获得交易账号
	 * 
	 * @param accName
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/getAccNo/{userName}", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto getAccNoByName(@PathVariable String userName, Model model) {

		List<AcctPo> accList = productService.getAccNoByUserName(userName);
		List<AccountDtoForAccountNo> result = new ArrayList<AccountDtoForAccountNo>();

		for (AcctPo acc : accList) {
			if (roleservice.getUserRole(acc.getUserId(),
					EBizRole.FINANCIER.getCode()) != null) {
				result.add(ConverterService.convert(acc,
						AccountDtoForAccountNo.class));
			}
		}
		if (result.size() == 0) {
			return ResultDtoFactory.toNack(ApplicationConstant.GET_FAIL_FLAG,
					result);
		}

		return ResultDtoFactory.toAck(ApplicationConstant.GET_SUCCESS_FLAG,
				result);

	}

	/**
	 * 
	 * Description: 查询项目的各种费率
	 * 
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/getFeeRate", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ProductSystemFeeDto getFeeRate() {
		ProductSystemFeeDto feeDto = new ProductSystemFeeDto();
		feeDto.setContractFee(String.valueOf(CommonBusinessUtil
				.getLoanMarginRate()));

		Map<String, BigDecimal> feeMap = new HashMap<String, BigDecimal>();
		for (FeeDto fee : getFeeList()) {
			BigDecimal feeRate = fee.getFeeRate();
			feeMap.put(fee.getFeeName(), feeRate);
		}
		feeDto.setFeeMap(feeMap);
		feeDto.setWarrantFee(String.valueOf(CommonBusinessUtil
				.getWarrantorMarginRate()));
		return feeDto;
	}

	/**
	 * 
	 * Description: 查询项目中的各种费用
	 * 
	 * @param productGetFeeDto
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/getFee", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ProductSystemFeeDto getFee(
			@RequestBody ProductGetFeeDto productGetFeeDto) {
		BigDecimal auota = new BigDecimal(productGetFeeDto.getAppliedQuota());
		ProductSystemFeeDto feeDto = new ProductSystemFeeDto();

		if (auota.compareTo(CommonBusinessUtil.getSeatFeeThreshold()) == 1) {
			feeDto.setFinancierSeatFee(String.valueOf(CommonBusinessUtil
					.getSeatFeeHighAmt()));
		} else {
			feeDto.setFinancierSeatFee(String.valueOf(CommonBusinessUtil
					.getSeatFeeLowAmt()));
		}
		return feeDto;
	}

	/**
	 * 
	 * Description: 申请保存
	 * 
	 * @param productApply
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/save/apply", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto productApply(
			@OnValid(SubmitProductApply.class) @RequestBody ProductDetailsDto productApply,
			Model model) {
		return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE,
				prepareApply(productApply, EProductStatus.STANDBY, SAVE));
	}

	/**
	 * 
	 * Description: 申请提交
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/submit/apply", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto financeApply(
			@OnValid(SubmitProductApply.class) @RequestBody ProductDetailsDto productApply) {
		prepareApply(productApply, null, SUBMIT);
		sendApplyMessage(productApply);
		return ResultDtoFactory
				.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE);
	}

	/**
	 * 融资项目申请提交发送代办消息
	 */
	private void sendApplyMessage(ProductDetailsDto productApply) {
		String userId = productService.getUserId(StringUtils.trim(productApply
				.getAcctNo()));
		String messageKey = "product.apply.message";
		messageService.sendMessage(EMessageType.MESSAGE, messageKey,
				userId, productApply.getProductName(),
				securityContext.getCurrentUserName());
		//TODO add new role 平台风控部主管、项目审核员
		List<EBizRole> roleList = Arrays.asList(
				EBizRole.PLATFORM_RISK_CONTROL_SUPERVISOR,
				EBizRole.PLATFORM_RISK_CONTROL_PRODUCT_APPROVE,
				EBizRole.PLATFORM_RISK_CONTROL_MAKER,
				EBizRole.PLATFORM_RISK_CONTROL_LOAD_APPROVE);
		messageService.sendMessage(EMessageType.TODO, messageKey,
				roleList, productApply.getProductName());
	}

	/**
	 * 
	 * Description: 获取编辑VM
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "product/edit/getView", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequiresPermissions(value = {
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public String getEditView(HttpServletResponse response, Model model) {
		model.addAttribute("productLibList", productService.getProductLib());
		model.addAttribute("industryList",
				getDynamicOptions(EOptionCategory.ORG_INDUSTRY, true));
		model.addAttribute("jobList",
				getDynamicOptions(EOptionCategory.JOB, true));
		model.addAttribute("hoseTypeList",
				getDynamicOptions(EOptionCategory.REAL_ESTATE_TYPE, true));
		model.addAttribute("assetTypeList",
				getDynamicOptions(EOptionCategory.ASSET_TYPE, true));
		model.addAttribute("debtTypeList",
				getDynamicOptions(EOptionCategory.DEBT_TYPE, true));
		return "product/financeproduct_apply";
	}

	/**
	 * 
	 * Description: 编辑申请
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/save/edit", method = RequestMethod.POST)
	@RequiresPermissions(value = {
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	@ResponseBody
	public ResultDto productApplyEditSave(
			@OnValid(SubmitProductApply.class) @RequestBody ProductDetailsDto productApply) {
		return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE,
				prepareEditApply(productApply, EProductStatus.STANDBY, SAVE));
	}

	/**
	 * 
	 * Description: 编辑申请
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/submit/edit", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = {
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto productApplyEditSubmit(
			@OnValid(SubmitProductApply.class) @RequestBody ProductDetailsDto productApply) {
		prepareEditApply(productApply, null, SUBMIT);
		sendApplyMessage(productApply);
		return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
	}

	/**
	 * 
	 * Description: 准备申请
	 * 
	 * @param productApply
	 * @param eProductStatus
	 * @return
	 */
	public String prepareApply(ProductDetailsDto productApply,
			EProductStatus eProductStatus, final String flag) {
		Product product = new Product();
		ConverterService.convert(productApply, product);
		if (LENGTH_TYPE_DAY == productApply.getTermLength()) {
			product.setTermType(ETermType.DAY);
			product.setTermLength(productApply.getTermToDays());
			// product.setTermToDays(productApply.getTermToDays());
		} else {
			product.setTermType(ETermType.MONTH);
		}
		Date date = new Date();
		product.setStatus(eProductStatus);
		product.setCreateTs(date);
		product.setLastMntTs(date);
		product.setApplDate(date);
		product.setCreateOpid(securityContext.getCurrentUserId());

		product.setServMrgnAmt(productApply.getFinanceFee() == null ? new BigDecimal(
				0) : productApply.getFinanceFee());
		product.setLoanMrgnRt(CommonBusinessUtil.getLoanMarginRate());
		product.setWrtrMrgnRt(CommonBusinessUtil.getWarrantorMarginRate());

		return productService.saveFinanceApply(
				populateProduct(product, productApply, date), flag);
	}

	/**
	 * 
	 * Description: 编辑操作项目
	 * 
	 * @param productApply
	 * @param eProductStatus
	 * @param flag
	 * @return
	 */
	public String prepareEditApply(ProductDetailsDto productApply,
			EProductStatus eProductStatus, final String flag) {

		Product product = new Product();
		ConverterService.convert(productApply, product);
		if (LENGTH_TYPE_DAY == productApply.getTermLength()) {
			product.setTermType(ETermType.DAY);
			product.setTermLength(productApply.getTermToDays());
			// product.setTermToDays(productApply.getTermToDays());
		} else {
			product.setTermType(ETermType.MONTH);
		}
		Date date = new Date();
		product.setStatus(eProductStatus);
		product.setLastMntTs(date);
		Date srcdate = product.getCreateTs();
		product.setApplDate(srcdate);
		product.setCreateOpid(securityContext.getCurrentUserId());

		product.setServMrgnAmt(productApply.getFinanceFee() == null ? new BigDecimal(
				0) : productApply.getFinanceFee());
		product.setLoanMrgnRt(CommonBusinessUtil.getLoanMarginRate());
		product.setWrtrMrgnRt(CommonBusinessUtil.getWarrantorMarginRate());

		String productId = productService.saveFinanceApply(
				populateProduct(product, productApply, date), flag);
		return productId;
	}

	/**
	 * 
	 * Description: 填充项目申请/编辑时的各种参数
	 * 
	 * @param product
	 * @param productApply
	 * @param date
	 * @return
	 */
	private Product populateProduct(Product product,
			ProductDetailsDto productApply, Date date) {
		product.setRate(productApply.getRate() == null ? new BigDecimal(0)
				: productApply.getRate().divide(NumberUtil.getHundred()));
		product.setAppliedQuota(productApply.getAppliedQuota());
		product.setProductAssetList(getAssetDomainList(
				productApply.getProductAssetDtoList(), date));
		product.setProductAttachmentList(getAttachmentDomainList(
				productApply.getProductAttachmentDetailsDtoList(), date));
		product.setProductMortgageResidentialList(getMortgageResidentialDomainList(
				productApply.getProductMortgageResidentialDetailsDtoList(),
				date));
		product.setProductMortgageVehicleList(getMortgageVehicleDomainList(
				productApply.getProductMortgageVehicleDetailsDtoList(), date));
		product.setProductPledgeList(getPledgeDomainList(
				productApply.getProductPledgeDetailsDtoList(), date));
		product.setProductWarrantPersonList(getProductWarrantPersonDomainList(
				productApply.getProductWarrantPersonDtoList(), date));
		product.setProductWarrantEnterpriseList(getProductWarrantEnterpriseDomainList(
				productApply.getProductWarrantEnterpriseDtoList(), date));
		product.setProductDebtList(getProductDomainList(
				productApply.getProductDebtDtoList(), date));
		product.setOrigServMrgnRt(CommonBusinessUtil.getPublishMarginRate());
		product.setWarrantId(securityContext.getCurrentUserId());
		product.setWarrantIdShow(securityContext.getCurrentUserId());
		product.setApplUserId(productService.getUserId(StringUtils
				.trim(productApply.getAcctNo())));
		product.setLastMntOpid(securityContext.getCurrentUserId());
		product.setProductQuota(productApply.getAppliedQuota());
		product.setUnitAmount(SubscribeUtils.getUnitFaceValue());
		int unit = 0;
		BigDecimal applQuota = AmtUtils.processNegativeAmt(product.getAppliedQuota(), BigDecimal.ZERO);
		if(applQuota.compareTo(BigDecimal.ZERO)>0){
			unit = applQuota.divide(SubscribeUtils.getUnitFaceValue()).intValue();
		}
		product.setUnit(unit);
		return product;
	}

	/**
	 * 
	 * Description: 获取编辑详情
	 * 
	 * @param productId
	 * @param response
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "product/edit/{productId}", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = {
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ProductDetailsDto getDetailsForEdit(@PathVariable String productId,
			HttpServletResponse response, Model model) {
		Product product = productService.getProductBasicInfo(productId);
		ProductDetailsDto productDetailsDto = ConverterService.convert(product,
				ProductDetailsDto.class);
		if (productDetailsDto.getTermType() != null
				&& productDetailsDto.getTermType() == ETermType.DAY) {
			productDetailsDto.setTermLength(LENGTH_TYPE_DAY);
		}
		populateProductDto(productDetailsDto, product, productId);

		productDetailsDto.setAcctNo(productService.getAccNo(productDetailsDto
				.getApplUserId()));
		productDetailsDto.setRate(FormatRate.formatRate(product.getRate()
				.multiply(new BigDecimal(100))));
		productDetailsDto.setAppliedQuota(product.getAppliedQuota());
		return productDetailsDto;
	}

	/**
	 * 
	 * Description: 填充数据
	 * 
	 * @param productDetailsDto
	 * @param product
	 * @param productId
	 */
	private void populateProductDto(ProductDetailsDto productDetailsDto,
			Product product, String productId) {
		productDetailsDto.setProductUserDto(ConverterService.convert(
				product.getUser(), ProductUserDto.class));
		productDetailsDto.setProductAssetDtoList(getAssertList(productId));

		productDetailsDto
				.setProductAttachmentDetailsDtoList(getAttachmentList(productId));
		productDetailsDto
				.setProductMortgageResidentialDetailsDtoList(getMortgageResidentialList(productId));
		productDetailsDto
				.setProductMortgageVehicleDetailsDtoList(getMortgageVehicleList(productId));
		productDetailsDto
				.setProductPledgeDetailsDtoList(getPledgeList(productId));

		productDetailsDto
				.setProductWarrantEnterpriseDtoList(getProductWarrantEnterpriseList(productId));
		productDetailsDto
				.setProductWarrantPersonDtoList(getProductWarrantPersonList(productId));
		productDetailsDto.setProductDebtDtoList(getProductDebtList(productId));
	}

	/**
	 * 
	 * Description:获得 风险管理费比例，融资服务费比例
	 * 
	 * @return
	 */
	private List<FeeDto> getFeeList() {
		List<Fee> feeListByGroupId = feeGroupService
				.getFeeListByGroupId(DEFAULT_PRODUCT_DEFAULT_FEE_GROUP_ID);
		List<FeeDto> defaultFeeDtoList = null;
		if (feeListByGroupId != null) {
			defaultFeeDtoList = new ArrayList<FeeDto>();
			for (Fee fee : feeListByGroupId) {
				defaultFeeDtoList.add(ConverterService.convert(fee,
						FeeDto.class));
			}
			return defaultFeeDtoList;
		}
		return null;

	}

	/**
	 * Get product pledge list, 质押
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductPledgeDetailsDto> getPledgeList(String productId) {
		List<ProductPledge> productPledgeList = productService
				.getProductWarrantPledge(productId);
		List<ProductPledgeDetailsDto> productPledgeDetailsDtoList = new ArrayList<ProductPledgeDetailsDto>();
		for (ProductPledge productPledge : productPledgeList) {
			productPledgeDetailsDtoList.add(ConverterService.convert(
					productPledge, ProductPledgeDetailsDto.class));
		}
		return productPledgeDetailsDtoList;
	}

	/**
	 * Get product mortgage vehicle list, 抵押车辆
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductMortgageVehicleDetailsDto> getMortgageVehicleList(
			String productId) {
		List<ProductMortgageVehicle> productMortgageVehicleList = productService
				.getProductWarrantMortgageForVE(productId);
		List<ProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList = new ArrayList<ProductMortgageVehicleDetailsDto>();
		for (ProductMortgageVehicle productMortgageVehicle : productMortgageVehicleList) {
			productMortgageVehicleDetailsDtoList.add(ConverterService.convert(
					productMortgageVehicle,
					ProductMortgageVehicleDetailsDto.class));
		}
		return productMortgageVehicleDetailsDtoList;
	}

	/**
	 * Get product mortgage residential list, 抵押房产
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductMortgageResidentialDetailsDto> getMortgageResidentialList(
			String productId) {
		List<MortgageResidential> productMortgageResidentialList = productService
				.getProductWarrantMortgageForRE(productId);
		List<ProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList = new ArrayList<ProductMortgageResidentialDetailsDto>();
		for (MortgageResidential mortgageResidential : productMortgageResidentialList) {
			ProductMortgageResidentialDetailsDto detailsDto = ConverterService
					.convert(mortgageResidential,
							ProductMortgageResidentialDetailsDto.class);
			detailsDto.setDmortgageType(ConverterService.convert(productService
					.getDicByCode(EOptionCategory.REAL_ESTATE_TYPE.getCode(),
							mortgageResidential.getMortgageType()),
					DynamicOption.class));
			productMortgageResidentialDetailsDtoList.add(detailsDto);
		}
		return productMortgageResidentialDetailsDtoList;
	}

	/**
	 * Get product attachment list, 附件信息
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductAttachmentDetailsDto> getAttachmentList(String productId) {
		List<ProductAttachment> productAttachmentList = productService
				.getProductFile(productId);

		List<ProductAttachmentDetailsDto> productAttachmentDetailsDtoList = new ArrayList<ProductAttachmentDetailsDto>();
		for (ProductAttachment productAttachment : productAttachmentList) {
			ProductAttachmentDetailsDto attachmentDetailsDto = new ProductAttachmentDetailsDto();
			ConverterService.convert(productAttachment, attachmentDetailsDto);
			ConverterService.convert(productAttachment.getFilePo(),
					attachmentDetailsDto);
			productAttachmentDetailsDtoList.add(attachmentDetailsDto);
		}
		return productAttachmentDetailsDtoList;
	}

	/**
	 * Get product asset vehicle list, 资产
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductAssetDto> getAssertList(String productId) {
		List<ProductAsset> productAssetList = productService
				.getProductAsset(productId);
		List<ProductAssetDto> productAssetDtoList = new ArrayList<ProductAssetDto>();
		for (ProductAsset productAsset : productAssetList) {
			ProductAssetDto productAssetDto = ConverterService.convert(
					productAsset, ProductAssetDto.class);
			productAssetDto.setDtype(ConverterService.convert(
					productService.getDicByCode(
							EOptionCategory.ASSET_TYPE.getCode(),
							productAsset.getType()), DynamicOption.class));
			productAssetDto.setAssertAmount(NumberUtil.formatTenThousandUnitAmt(productAsset.getAssertAmount()));
			productAssetDtoList.add(productAssetDto);
		}
		return productAssetDtoList;
	}

	/**
	 * Get product real estate assets list, 担保人
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductWarrantPersonDto> getProductWarrantPersonList(
			String productId) {
		List<ProductWarrantPerson> productWarrantList = productService
				.getProductWarrantPerson(productId);
		List<ProductWarrantPersonDto> productWarrantDtoList = new ArrayList<ProductWarrantPersonDto>();
		for (ProductWarrantPerson productWarrant : productWarrantList) {
			ProductWarrantPersonDto dto = ConverterService.convert(
					productWarrant, ProductWarrantPersonDto.class);
			dto.setDjob(ConverterService.convert(productService.getDicByCode(
					EOptionCategory.JOB.getCode(), productWarrant.getJob()),
					DynamicOption.class));
			productWarrantDtoList.add(dto);
		}
		return productWarrantDtoList;
	}

	/**
	 * Get product real estate assets list, 担保法人
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductWarrantEnterpriseDto> getProductWarrantEnterpriseList(
			String productId) {
		List<ProductWarrantEnterprise> productWarrantList = productService
				.getProductWarrantEnterprise(productId);
		List<ProductWarrantEnterpriseDto> productWarrantDtoList = new ArrayList<ProductWarrantEnterpriseDto>();
		for (ProductWarrantEnterprise productWarrant : productWarrantList) {
			ProductWarrantEnterpriseDto dto = ConverterService.convert(
					productWarrant, ProductWarrantEnterpriseDto.class);
			dto.setDenterpriseIndustry(ConverterService.convert(productService
					.getDicByCode(EOptionCategory.ORG_INDUSTRY.getCode(),
							productWarrant.getEnterpriseIndustry()),
					DynamicOption.class));

			productWarrantDtoList.add(dto);
		}
		return productWarrantDtoList;
	}

	/**
	 * 
	 * Description: 负债
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductDebtDto> getProductDebtList(String productId) {

		List<ProductDebt> productDebtList = productService
				.getProductDebt(productId);

		List<ProductDebtDto> productDebtDtoList = new ArrayList<ProductDebtDto>();
		for (ProductDebt productDebt : productDebtList) {
			ProductDebtDto debtDto = ConverterService.convert(productDebt,
					ProductDebtDto.class);
			debtDto.setDtype(ConverterService.convert(
					productService.getDicByCode(
							EOptionCategory.DEBT_TYPE.getCode(),
							productDebt.getType()), DynamicOption.class));
			debtDto.setDebtAmount(NumberUtil.formatTenThousandUnitAmt(productDebt.getDebtAmount()));
			productDebtDtoList.add(debtDto);
		}
		return productDebtDtoList;
	}

	/**
	 * Get product pledge list, 质押
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductPledge> getPledgeDomainList(
			List<ProductPledgeDetailsDto> productPledgeDetailsDtoList, Date date) {
		List<ProductPledge> productPledgeList = new ArrayList<ProductPledge>();
		for (ProductPledgeDetailsDto productPledge : productPledgeDetailsDtoList) {
			ProductPledge pledge = new ProductPledge();
			ConverterService.convert(productPledge, pledge);
			pledge.setCreateTs(date);
			pledge.setLastMntTs(date);
			pledge.setCreateOpid(securityContext.getCurrentUserId());
			pledge.setLastMntOpid(securityContext.getCurrentUserId());
			productPledgeList.add(pledge);
		}
		return productPledgeList;
	}

	/**
	 * Get product mortgage vehicle list, 抵押车辆
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductMortgageVehicle> getMortgageVehicleDomainList(
			List<ProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList,
			Date date) {
		List<ProductMortgageVehicle> productMortgageVehicleList = new ArrayList<ProductMortgageVehicle>();
		for (ProductMortgageVehicleDetailsDto productMortgageVehicle : productMortgageVehicleDetailsDtoList) {
			ProductMortgageVehicle mortgageVehicle = new ProductMortgageVehicle();
			ConverterService.convert(productMortgageVehicle, mortgageVehicle);
			mortgageVehicle.setCreateTs(date);
			mortgageVehicle.setLastMntTs(date);
			mortgageVehicle.setCreateOpid(securityContext.getCurrentUserId());
			mortgageVehicle.setLastMntOpid(securityContext.getCurrentUserId());
			productMortgageVehicleList.add(mortgageVehicle);
		}
		return productMortgageVehicleList;
	}

	/**
	 * Get product mortgage residential list, 抵押房产
	 * 
	 * @param productId
	 * @return
	 */
	private List<MortgageResidential> getMortgageResidentialDomainList(
			List<ProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList,
			Date date) {
		List<MortgageResidential> productMortgageResidentialList = new ArrayList<MortgageResidential>();
		for (ProductMortgageResidentialDetailsDto mortgageResidential : productMortgageResidentialDetailsDtoList) {
			MortgageResidential residential = new MortgageResidential();
			ConverterService.convert(mortgageResidential, residential);

			residential.setCreateTime(date);
			residential.setLastTime(date);
			residential
					.setCreateOperateorId(securityContext.getCurrentUserId());
			residential.setLastOperatorId(securityContext.getCurrentUserId());
			productMortgageResidentialList.add(residential);
		}
		return productMortgageResidentialList;
	}

	/**
	 * Get product attachment list, 附件信息
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductAttachment> getAttachmentDomainList(
			List<ProductAttachmentDetailsDto> productAttachmentDetailsDtoList,
			Date date) {
		List<ProductAttachment> productAttachmentList = new ArrayList<ProductAttachment>();
		for (ProductAttachmentDetailsDto productAttachment : productAttachmentDetailsDtoList) {
			ProductAttachment attachment = new ProductAttachment();
			ConverterService.convert(productAttachment, attachment);
			attachment.setCreateTs(date);
			attachment.setLastMntTs(date);
			attachment.setCreateOpid(securityContext.getCurrentUserId());
			attachment.setLastMntOpid(securityContext.getCurrentUserId());
			productAttachmentList.add(attachment);
		}
		return productAttachmentList;
	}

	/**
	 * Get product real estate assets list, 资产
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductAsset> getAssetDomainList(
			List<ProductAssetDto> productAssetDtoList, Date date) {
		List<ProductAsset> productAssetList = new ArrayList<ProductAsset>();
		for (ProductAssetDto productRealEstateAssets : productAssetDtoList) {
			ProductAsset asset = new ProductAsset();
			ConverterService.convert(productRealEstateAssets, asset);
			asset.setCreateTs(date);
			asset.setLastMntTs(date);
			asset.setCreateOpid(securityContext.getCurrentUserId());
			asset.setLastMntOpid(securityContext.getCurrentUserId());
			BigDecimal prodRealEstatAssets = AmtUtils.processNullAmt(productRealEstateAssets.getAssertAmount(), BigDecimal.ZERO);
			prodRealEstatAssets = prodRealEstatAssets.multiply(NumberUtil.getTenThousand());
			asset.setAssertAmount(prodRealEstatAssets);
			productAssetList.add(asset);
		}
		return productAssetList;
	}

	/**
	 * Get product real estate assets list, 担保人
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductWarrantEnterprise> getProductWarrantEnterpriseDomainList(
			List<ProductWarrantEnterpriseDto> productWarrantDtoList, Date date) {
		List<ProductWarrantEnterprise> productWarrantList = new ArrayList<ProductWarrantEnterprise>();
		for (ProductWarrantEnterpriseDto productWarrant : productWarrantDtoList) {
			ProductWarrantEnterprise enterprise = new ProductWarrantEnterprise();
			ConverterService.convert(productWarrant, enterprise);
			enterprise.setCreateTs(date);
			enterprise.setLastMntTs(date);
			enterprise.setCreateOpid(securityContext.getCurrentUserId());
			enterprise.setLastMntOpid(securityContext.getCurrentUserId());
			productWarrantList.add(enterprise);
		}
		return productWarrantList;
	}

	/**
	 * Get product real estate assets list, 担保法人
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductWarrantPerson> getProductWarrantPersonDomainList(
			List<ProductWarrantPersonDto> productWarrantDtoList, Date date) {
		List<ProductWarrantPerson> productWarrantList = new ArrayList<ProductWarrantPerson>();

		for (ProductWarrantPersonDto productWarrant : productWarrantDtoList) {
			ProductWarrantPerson person = new ProductWarrantPerson();
			ConverterService.convert(productWarrant, person);
			person.setCreateTs(date);
			person.setLastMntTs(date);
			person.setCreateOpid(securityContext.getCurrentUserId());
			person.setLastMntOpid(securityContext.getCurrentUserId());
			productWarrantList.add(person);
		}
		return productWarrantList;
	}

	/**
	 * Get product real estate assets list, 负债
	 * 
	 * @param productId
	 * @return
	 */
	private List<ProductDebt> getProductDomainList(
			List<ProductDebtDto> productDebtDtoList, Date date) {
		List<ProductDebt> productDebtList = new ArrayList<ProductDebt>();

		for (ProductDebtDto productWarrant : productDebtDtoList) {
			ProductDebt debt = new ProductDebt();
			ConverterService.convert(productWarrant, debt);
			debt.setCreateTs(date);
			debt.setLastMntTs(date);
			debt.setCreateOpid(securityContext.getCurrentUserId());
			debt.setLastMntOpid(securityContext.getCurrentUserId());
			BigDecimal debtAmt = AmtUtils.processNullAmt(productWarrant.getDebtAmount(), BigDecimal.ZERO);
			debtAmt = debtAmt.multiply(NumberUtil.getTenThousand());
			debt.setDebtAmount(debtAmt);
			BigDecimal monthlyPaymentAmt = AmtUtils.processNullAmt(productWarrant.getMonthlyPayment(), BigDecimal.ZERO);
			debt.setMonthlyPayment(monthlyPaymentAmt);
			productDebtList.add(debt);
		}
		return productDebtList;
	}

	/**
	 * 
	 * Description: 后台分步验证
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/validate/productMortgageVehicleDetailsDto", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto getValidationPMV(
			@OnValid(SubmitProductApply.class) @RequestBody ProductMortgageVehicleDetailsDto productApply) {
		return ResultDtoFactory.toAck(ApplicationConstant.ADD_SUCCESS_MESSAGE);
	}

	/**
	 * 
	 * Description: 验证
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/validate/productPledgeDetailsDto", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto getValidationPP(
			@OnValid(SubmitProductApply.class) @RequestBody ProductPledgeDetailsDto productApply) {
		return ResultDtoFactory.toAck(ApplicationConstant.ADD_SUCCESS_MESSAGE);
	}

	/**
	 * 
	 * Description: 验证
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/validate/productAssetDto", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto getValidationPA(
			@OnValid(SubmitProductApply.class) @RequestBody ProductAssetDto productApply) {
		return ResultDtoFactory.toAck(ApplicationConstant.ADD_SUCCESS_MESSAGE);
	}

	/**
	 * 
	 * Description: 验证
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/validate/productDebtDto", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto getValidationPD(
			@OnValid(SubmitProductApply.class) @RequestBody ProductDebtDto productApply) {
		return ResultDtoFactory.toAck(ApplicationConstant.ADD_SUCCESS_MESSAGE);
	}

	/**
	 * 
	 * Description: 验证
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/validate/productWarrantEnterpriseDto", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto getValidationPWE(
			@OnValid(SubmitProductApply.class) @RequestBody ProductWarrantEnterpriseDto productApply) {
		return ResultDtoFactory.toAck(ApplicationConstant.ADD_SUCCESS_MESSAGE);
	}

	/**
	 * 
	 * Description: 验证
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/validate/productWarrantPersonDto", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto getValidationPWP(
			@OnValid(SubmitProductApply.class) @RequestBody ProductWarrantPersonDto productApply) {
		return ResultDtoFactory.toAck(ApplicationConstant.ADD_SUCCESS_MESSAGE);
	}

	/**
	 * 
	 * Description: 验证
	 * 
	 * @param productApply
	 * @return
	 */
	@RequestMapping(value = "product/financeapply/validate/productMortgageResidentialDetailsDto", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY,
			Permissions.PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE,
			Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
	public ResultDto getValidationPMR(
			@OnValid(SubmitProductApply.class) @RequestBody ProductMortgageResidentialDetailsDto productApply) {
		return ResultDtoFactory.toAck(ApplicationConstant.ADD_SUCCESS_MESSAGE);
	}

}
