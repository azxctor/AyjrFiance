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

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.FeeDto;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.common.util.Word2HtmlUtil;
import com.hengxin.platform.fund.entity.FeePayStatePo;
import com.hengxin.platform.member.domain.SubscribeGroup;
import com.hengxin.platform.product.ProductUtil;
import com.hengxin.platform.product.converter.FinancingPackageConverter;
import com.hengxin.platform.product.converter.ProductFeeConverter;
import com.hengxin.platform.product.domain.Fee;
import com.hengxin.platform.product.domain.MortgageResidential;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductAsset;
import com.hengxin.platform.product.domain.ProductAttachment;
import com.hengxin.platform.product.domain.ProductContractTemplate;
import com.hengxin.platform.product.domain.ProductDebt;
import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.domain.ProductMortgageVehicle;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.domain.ProductPledge;
import com.hengxin.platform.product.domain.ProductWarrantEnterprise;
import com.hengxin.platform.product.domain.ProductWarrantPerson;
import com.hengxin.platform.product.dto.AIPGroupDto;
import com.hengxin.platform.product.dto.CashDepositDto;
import com.hengxin.platform.product.dto.ProductAssetDto;
import com.hengxin.platform.product.dto.ProductAttachmentDetailsDto;
import com.hengxin.platform.product.dto.ProductDebtDto;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductFeeDto;
import com.hengxin.platform.product.dto.ProductMortgageResidentialDetailsDto;
import com.hengxin.platform.product.dto.ProductMortgageVehicleDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.dto.ProductPledgeDetailsDto;
import com.hengxin.platform.product.dto.ProductSeatFeeDto;
import com.hengxin.platform.product.dto.ProductUserDto;
import com.hengxin.platform.product.dto.ProductWarrantEnterpriseDto;
import com.hengxin.platform.product.dto.ProductWarrantPersonDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.service.FeeGroupService;
import com.hengxin.platform.product.service.FeeService;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.ProductContractTemplateService;
import com.hengxin.platform.product.service.ProductFreezeService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: FinanceProductController.
 * 
 * @author huanbinzhang
 * 
 */
@Controller
public class FinanceProductController extends BaseController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FinanceProductController.class);
	 
    private static final String DEFAULT_PRODUCT_DEFAULT_FEE_GROUP_ID = "1";
    private static final String DEFAULT_PRODUCT_TRANS_SETTINGS_FEE_GROUP_ID = "2";
    private static final String RISK_MANAGE_FEE = "风险管理费";
    private static final String FINANCE_SERVICE_FEE = "融资服务费";
    private static final String NUMTWL = "12";
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductContractTemplateService productContractTemplateService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FinancingPackageService productPackageService;

    @Autowired
    private FeeGroupService feeGroupService;

    @Autowired
    private FinancingPackageService financingPackageService;

    @Autowired
    private FeeService feeService;

    @Autowired
    private ProductFreezeService productFreezeService;

    /**
     * populateProductDto.
     * @param productDetailsDto
     * @param product
     * @param productId
     * @param viewonly
     */
    public void populateProductDto(ProductDetailsDto productDetailsDto, Product product, String productId,
            boolean viewonly) {
    	if (product.getProductProviderInfo() != null) {
    		productDetailsDto.setWrtrCreditDesc(product.getProductProviderInfo().getDesc());
    		productDetailsDto.setWrtrCreditFile(product.getProductProviderInfo().getWrtrCreditFile());
    	}
        productDetailsDto
                .setDfinancierIndustryType(ConverterService.convert(
                        productService.getDicByCode(EOptionCategory.ORG_INDUSTRY.getCode(),
                                product.getFinancierIndustryType()), DynamicOption.class));
        productDetailsDto.setProductUserDto(ConverterService.convert(product.getUser(), ProductUserDto.class));
        productDetailsDto.setProductAssetDtoList(getAssertList(productId));

        productDetailsDto.setProductAttachmentDetailsDtoList(getAttachmentList(productId));
        productDetailsDto.setProductMortgageResidentialDetailsDtoList(getMortgageResidentialList(productId));
        productDetailsDto.setProductMortgageVehicleDetailsDtoList(getMortgageVehicleList(productId));
        productDetailsDto.setProductPledgeDetailsDtoList(getPledgeList(productId));

        productDetailsDto.setProductWarrantEnterpriseDtoList(getProductWarrantEnterpriseList(productId));
        productDetailsDto.setProductWarrantPersonDtoList(getProductWarrantPersonList(productId));
        productDetailsDto.setProductDebtDtoList(getProductDebtList(productId));
        productDetailsDto.setProductPackageDtoList(getProductPackageList(productId));

        productDetailsDto.setProductGrageClass(product.getProductLevel().getText());
        productDetailsDto.setFinancierGrageClass(product.getFinancierLevel());
        productDetailsDto.setWarrantGrageClass(product.getWarrantorLevel());

        productDetailsDto.setCurrentUserType(securityContext.getCurrentUser().getType().getCode());
        productDetailsDto.setAcctNo(productService.getAccNo(productDetailsDto.getApplUserId()));
        UserPo user = productService.getUser(product.getWarrantId());
        String wrtrName = user == null ? null : user.getName();
        productDetailsDto.setWarrantId(product.getWarrantId());
        productDetailsDto.setGuaranteeInstitution(wrtrName);
        if(StringUtils.equals(product.getWarrantId(), product.getWarrantIdShow())){
            productDetailsDto.setGuaranteeInstitutionShow(wrtrName);
        } else {
        	if(StringUtils.isNotBlank(product.getWarrantIdShow())){
            	UserPo user2 = productService.getUser(product.getWarrantIdShow());
                productDetailsDto.setGuaranteeInstitutionShow(user2 == null ? null : user2.getName());
        	} else {
                productDetailsDto.setGuaranteeInstitutionShow(wrtrName);
        	}
        }
        productDetailsDto.setFlag(viewonly);

        productDetailsDto.setCreateTs(ProductUtil.formatDate(product.getCreateTs()));
        
        if (securityContext.cannotViewRealName(productDetailsDto.getApplUserId(), false)) {
            productDetailsDto.setFinancierName(MaskUtil.maskChinsesName(product.getUser().getName()));
        } else {
            productDetailsDto.setFinancierName(product.getUser().getName());
        }
        if (securityContext.cannotViewRealPhoneNo(productDetailsDto.getApplUserId(), false)) {
            productDetailsDto.setFinancierMobile(MaskUtil.maskPhone(productDetailsDto.getFinancierMobile()));
        }

    }
    
    @RequestMapping(value = "product/details/{wrtrCreditFile:[a-zA-Z0-9_\\.]+}", method = RequestMethod.GET)
	public void viewWrtrCreditFile(@PathVariable(value = "wrtrCreditFile") String wrtrCreditFile,
			HttpServletRequest req, HttpServletResponse resp) {
    	
    	String path = FileUploadUtil.getFolder() + wrtrCreditFile;
    	PrintWriter out = null;
    	try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html");
			String html = Word2HtmlUtil.convert(path);
			out = resp.getWriter();
			out.println(html);
			out.close();
		} catch (Exception e) {
			LOGGER.error("EX {}", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
    }

    /**
     * 
     * Description: 详情.
     * 
     * @param productId
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "product/details/{productId}/{viewonly}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_VIEW,
            Permissions.PRODUCT_FINANCIER_FINANCING_STATUS_VIEW, Permissions.MEMBER_FINANCING_APPLY_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_VIEW }, logical = Logical.OR)
    public String getProductDetails(@PathVariable String productId, @PathVariable boolean viewonly,
            HttpServletResponse response, Model model) {

        Product product = productService.getProductBasicInfo(productId);
        
        ProductDetailsDto productDetailsDto = ConverterService.convert(product, ProductDetailsDto.class);

        populateProductDto(productDetailsDto, product, productId, viewonly);

        fillCashDeposit(productDetailsDto, product);

        populatePermissions(productDetailsDto, product);
        
   		try {
   			if (product.getContractTemplateId() != null) {
   				ProductContractTemplate template = productContractTemplateService.getProductContractTemplate(
   	   					product.getContractTemplateId().toString());	
   				productDetailsDto.setContractTemplateName(template.getTemplateName());
			}
   		} catch (Exception e) { 
   			LOGGER.error("EX {}", e);
   		}
   
        if(product.getWarrantyType()==EWarrantyType.MONITORASSETS||product.getWarrantyType()==EWarrantyType.NOTHING){
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
            if (!viewonly) {
                String returnPage = processTransSettings(productId, model, product);
                List<EnumOption> staticOptions = new ArrayList<EnumOption>();
                staticOptions.add(new EnumOption("MONTH", "按期收取"));
                staticOptions.add(new EnumOption("ONCE", "一次性收取"));
                model.addAttribute("staticOptions", staticOptions);
                model.addAttribute("isMarketOpen", CommonBusinessUtil.isMarketOpen());
                productDetailsDto.setRate(FormatRate.formatRate(product.getRate().multiply(NumberUtil.getHundred())));
                productDetailsDto.setAppliedQuota(product.getAppliedQuota());
                model.addAttribute("productDetails", productDetailsDto);
                return returnPage;
            }
        }
        
        
        
        productDetailsDto.setRate(FormatRate.formatRate(product.getRate().multiply(NumberUtil.getHundred())));
        productDetailsDto.setAppliedQuota(product.getAppliedQuota());
        model.addAttribute("defaultFeeDtoList", getFeeList());
        model.addAttribute("productDetails", productDetailsDto);
        model.addAttribute("user", productDetailsDto.getProductUserDto());
        model.addAttribute("riskLevels", EnumHelper.inspectConstants(ERiskLevel.class, false));
        return "product/financeproduct_details";
    }

    public void populatePermissions(ProductDetailsDto productDetailsDto, Product product) {

        if (EProductStatus.WAITTOPUBLISH == product.getStatus() || EProductStatus.FINISHED == product.getStatus()) {
            productDetailsDto.setCanSeeDepositFee(true);
        } else {
            productDetailsDto.setCanSeeDepositFee(false);
        }

        if (EProductStatus.FINISHED == product.getStatus()) {
            productDetailsDto.setCanSeeFee(true);
        } else {
            productDetailsDto.setCanSeeFee(false);
        }

        if (securityContext.hasPermission(Permissions.PRODUCT_FINANCING_DEPOSIT_UPDATE)) {
            productDetailsDto.setCanCashDepositEdit(true);
        }
        if (securityContext.isProdServ()
                && securityContext.getCurrentUserId().equals(product.getWarrantId())) {
            productDetailsDto.setCanNotSeeGuarantors(true);
        }

    }

    /**
     * 
     * Description: 保证金及参考值
     * 
     * @param productDetailsDto
     * @param product
     */
    public void fillCashDeposit(ProductDetailsDto productDetailsDto, Product product) {
        BigDecimal realFinanceServiceFee = product.getServMrgnAmt() == null ? BigDecimal.ZERO : product
                .getServMrgnAmt();
        String realWarrantPer = product.getWrtrMrgnRt() == null ? StringUtils.EMPTY : product.getWrtrMrgnRt()
                .multiply(NumberUtil.getHundred()).toPlainString();
        String realLoanMarginPer = product.getLoanMrgnRt() == null ? StringUtils.EMPTY : product.getLoanMrgnRt()
                .multiply(NumberUtil.getHundred()).toPlainString();
        BigDecimal realLoanMarginPerBd = product.getLoanMrgnRt() == null ? BigDecimal.ZERO : product.getLoanMrgnRt();
        String referWarrantPer = CommonBusinessUtil.getWarrantorMarginRate().multiply(NumberUtil.getHundred())
                .setScale(2, RoundingMode.HALF_UP).toPlainString();
        BigDecimal referWarrantPerBd = CommonBusinessUtil.getWarrantorMarginRate();
        String referLoanMarginPer = CommonBusinessUtil.getLoanMarginRate().multiply(NumberUtil.getHundred())
                .toPlainString();
        BigDecimal referLoanMarginPerBd = CommonBusinessUtil.getLoanMarginRate();

        BigDecimal quota = product.getAppliedQuota();
        BigDecimal rat = product.getRate();

        BigDecimal realWarrantFee = productFreezeService.calculateWrtrMrgnAmount(product);

        CashDepositDto cashDeposit = new CashDepositDto();
        // 实际融资服务合同保证金
        cashDeposit.setFinanceServiceFee(realFinanceServiceFee);
        // 实际担保公司-还款保证金百分比
        cashDeposit.setWarrantPercentage(realWarrantPer);
        // 实际担保公司-还款保证金
        cashDeposit.setWarrantFee(realWarrantFee);
        // 实际借款合同履约保证金
        cashDeposit.setContractFee(realLoanMarginPerBd.multiply(quota));
        // 实际借款合同履约保证金百分比
        cashDeposit.setContractFeePer(realLoanMarginPer);
        // 实际风险管理费和融资服务费
        for (ProductFee productFee : feeGroupService.getFeeListByProductId(product.getProductId())) {
            Fee fee = feeService.getFeeById(productFee.getFeeId());
            if (fee != null && RISK_MANAGE_FEE.equals(fee.getFeeName())) {
                if (ETermType.DAY == product.getTermType()) {
                    cashDeposit.setRiskFee((productFee.getFeeRt().multiply(quota)
                            .multiply(new BigDecimal(product.getTermLength())).divide(new BigDecimal(30), 2,
                            RoundingMode.HALF_UP)));
                } else if (ETermType.MONTH == product.getTermType()) {
                    cashDeposit.setRiskFee((productFee.getFeeRt().multiply(quota).multiply(new BigDecimal(product
                            .getTermLength()))));
                }
            }
            if (fee != null && FINANCE_SERVICE_FEE.equals(fee.getFeeName())) {
                if (ETermType.DAY == product.getTermType()) {
                    cashDeposit.setServiceFee((productFee.getFeeRt().multiply(quota)
                            .multiply(new BigDecimal(product.getTermLength())).divide(new BigDecimal(30), 2,
                            RoundingMode.HALF_UP)));
                } else if (ETermType.MONTH == product.getTermType()) {
                    cashDeposit.setServiceFee((productFee.getFeeRt().multiply(quota).multiply(new BigDecimal(product
                            .getTermLength()))));
                }
            }
        }

        productDetailsDto.setCashDeposit(cashDeposit);
        // 参考融资服务合同保证金
        productDetailsDto.setFinanceFee(quota.multiply(rat).divide(new BigDecimal(NUMTWL), 2, RoundingMode.HALF_UP));
        // 参考担保公司-还款保证金百分比
        productDetailsDto.setWarrantFeePer(referWarrantPer);

        if (EWarrantyType.MONITORASSETS == product.getWarrantyType()
                || EWarrantyType.NOTHING == product.getWarrantyType()) {
            productDetailsDto.setWarrantFee(BigDecimal.ZERO);
        } else {
            // 参考担保公司-还款保证金
            productDetailsDto.setWarrantFee(quota.multiply(referWarrantPerBd));
        }
        // 参考借款合同履约保证金
        productDetailsDto.setContractFee(referLoanMarginPerBd.multiply(quota));
        // 参考借款合同履约保证金百分比
        productDetailsDto.setReferContractFeePer(referLoanMarginPer);
        // 参考风险管理费和融资服务费
        for (FeeDto feeDto : getFeeList()) {
            if (RISK_MANAGE_FEE.equals(feeDto.getFeeName())) {
                productDetailsDto.setRiskFeeRate(feeDto.getFeeRate().multiply(NumberUtil.getHundred()).setScale(6, RoundingMode.DOWN).toPlainString());
                if (ETermType.DAY == product.getTermType()) {
                    productDetailsDto.setRiskFee((feeDto.getFeeRate().multiply(quota)
                            .multiply(new BigDecimal(product.getTermLength())).divide(new BigDecimal(30), 2,
                            RoundingMode.HALF_UP)));

                } else if (ETermType.MONTH == product.getTermType()) {
                    productDetailsDto.setRiskFee((feeDto.getFeeRate().multiply(quota).multiply(new BigDecimal(product
                            .getTermLength()))));
                }
            }
            if (FINANCE_SERVICE_FEE.equals(feeDto.getFeeName())) {
                productDetailsDto.setServiceFeeRate(feeDto.getFeeRate().multiply(NumberUtil.getHundred()).setScale(6, RoundingMode.DOWN).toPlainString());
                if (ETermType.DAY == product.getTermType()) {
                    productDetailsDto.setServiceFee((feeDto.getFeeRate().multiply(quota)
                            .multiply(new BigDecimal(product.getTermLength())).divide(new BigDecimal(30), 2,
                            RoundingMode.HALF_UP)));

                } else if (ETermType.MONTH == product.getTermType()) {
                    productDetailsDto.setServiceFee((feeDto.getFeeRate().multiply(quota).multiply(new BigDecimal(
                            product.getTermLength()))));
                }

            }
        }
        // 融资会员席位费
        if ((product.getAppliedQuota() == null ? BigDecimal.ZERO : product.getAppliedQuota())
                .compareTo(CommonBusinessUtil.getSeatFeeThreshold()) == 1) {
            productDetailsDto.setFinancierSeatFee(CommonBusinessUtil.getSeatFeeHighAmt());
        } else {
            productDetailsDto.setFinancierSeatFee(CommonBusinessUtil.getSeatFeeLowAmt());
        }

        FeePayStatePo feePayStatePo = productService.findSeatFee(product.getApplUserId());

        if (feePayStatePo != null && (feePayStatePo.getEndDt().compareTo(ProductUtil.getDate()) == 1)) {
            ProductSeatFeeDto dto = new ProductSeatFeeDto();
            dto.setEndDt(ProductUtil.formatDate(feePayStatePo.getEndDt()));
            productDetailsDto.setFeeDto(dto);
        }
    }

    /**
     * 
     * Description:获得 风险管理费比例，融资服务费比例
     * 
     * @return
     */
    private List<FeeDto> getFeeList() {
        List<Fee> feeListByGroupId = feeGroupService.getFeeListByGroupId(DEFAULT_PRODUCT_DEFAULT_FEE_GROUP_ID);
        List<FeeDto> defaultFeeDtoList = null;
        if (feeListByGroupId != null) {
            defaultFeeDtoList = new ArrayList<FeeDto>();
            for (Fee fee : feeListByGroupId) {
                defaultFeeDtoList.add(ConverterService.convert(fee, FeeDto.class));
            }
            return defaultFeeDtoList;
        }
        return null;

    }

    /**
     * 
     * Description: 交易参数设置
     * 
     * @param productId
     * @param model
     */
    private String processTransSettings(String productId, Model model, Product product) {
        String returnPage = "product/financeproduct_details";
        boolean statusFlag = false;
        Product currentProductInfo = productService.getProductById(productId);
        
        if (currentProductInfo.getStatus() == EProductStatus.WAITTOPUBLISH) {
            List<Fee> feeListByGroupId = feeGroupService
                    .getFeeListByGroupId(DEFAULT_PRODUCT_TRANS_SETTINGS_FEE_GROUP_ID);
            List<FeeDto> feeDtoList = null;
            if (feeListByGroupId != null) {
                feeDtoList = new ArrayList<FeeDto>();
                FeeDto feeDto = null;
                for (Fee fee : feeListByGroupId) {
                    feeDto = new FeeDto();
                    feeDto.setId(fee.getId());
                    feeDto.setFeeName(fee.getFeeName());
                    feeDto.setFeeRate(NumberUtil.getHundred().multiply(fee.getFeeRate()).setScale(6));
                    feeDtoList.add(feeDto);
                }
            }
            BigDecimal loanMarginRate = CommonBusinessUtil.getLoanMarginRate().setScale(1);
            model.addAttribute("loanMarginRate", NumberUtil.getHundred().multiply(loanMarginRate));
            model.addAttribute("feeList", feeDtoList);
            model.addAttribute("isEdit", false);
            returnPage = "product/transactionparams_detail";
        } else if (currentProductInfo.getStatus() == EProductStatus.FINISHED) {
            // product current fee list
            List<ProductFeeDto> productFeeDtoList = getProdcutFeeList(productId);

            // product current package
            List<ProductPackage> packageList = financingPackageService.getProductPackageListByProductId(productId);
            List<ProductPackageDto> packageDtoList = new ArrayList<ProductPackageDto>();
            List<AIPGroupDto> aipGroupDtoList = null;
            ProductPackageDto packageDto = null;
            for (ProductPackage productPackage : packageList) {
                aipGroupDtoList = new ArrayList<AIPGroupDto>();
                if (productPackage.getStatus() != EPackageStatus.PRE_PUBLISH
                        && productPackage.getStatus() != EPackageStatus.WAIT_SUBSCRIBE) {
                    statusFlag = true;
                }
                packageDto = ConverterService.convert(productPackage, ProductPackageDto.class,
                        FinancingPackageConverter.class);
                List<SubscribeGroup> aipGroups = productPackage.getAipGroups();
                if (aipGroups != null && !aipGroups.isEmpty()) {
                    String aipGroupIds = "";
                    AIPGroupDto aipGroupDto = null;
                    for (SubscribeGroup subscribeGroup : aipGroups) {
                        aipGroupDto = new AIPGroupDto();
                        aipGroupDto.setId(subscribeGroup.getGroupId());
                        aipGroupDto.setText(subscribeGroup.getGroupName());
                        aipGroupDtoList.add(aipGroupDto);
                        aipGroupIds += subscribeGroup.getGroupId() + ",";
                    }
                    packageDto.setAipGroupIds(aipGroupIds.substring(0, aipGroupIds.length() - 1));
                    packageDto.setAipGroupList(aipGroupDtoList);
                }
                /** check update fields. **/
                if (productPackage.getStatus() == EPackageStatus.PRE_PUBLISH
                        || productPackage.getStatus() == EPackageStatus.WAIT_SUBSCRIBE) {
                	/**update all.**/
                    packageDto.setUpdateStatus(1); 
                } else if (productPackage.getStatus() == EPackageStatus.SUBSCRIBE) {
                	/**update supply end time.**/
                	packageDto.setUpdateStatus(2);
                } else {
                	/**cannot update.**/
                    packageDto.setUpdateStatus(3); 
                }
                packageDto.setProductId(productId);
                packageDtoList.add(packageDto);
            }
            model.addAttribute("performanceBondRate", product.getLoanMrgnRt().multiply(NumberUtil.getHundred()).setScale(1));
            model.addAttribute("avgPkgFlg", product.getAvgPkgFlg());
            model.addAttribute("productId", productId);
            model.addAttribute("statusFlag", statusFlag);
            model.addAttribute("productFeeList", productFeeDtoList);
            model.addAttribute("packageList", packageDtoList);
            model.addAttribute("isEdit", true);
            returnPage = "product/transaction_params_edit";

        }
        return returnPage;
    }

    /**
     * Description: Get product fee list
     * 
     * @param productId
     * @return
     */

    private List<ProductFeeDto> getProdcutFeeList(String productId) {
        List<ProductFee> productFeeList = feeGroupService.getFeeListByProductId(productId);
        List<ProductFeeDto> productFeeDtoList = new ArrayList<ProductFeeDto>();
        for (ProductFee productFee : productFeeList) {
            ProductFeeDto productFeeDto = ConverterService.convert(productFee, ProductFeeDto.class,
                    ProductFeeConverter.class);
            Fee fee = feeService.getFeeById(productFeeDto.getFeeId());
            productFeeDto.setFeeDto(ConverterService.convert(fee, FeeDto.class));
            productFeeDtoList.add(productFeeDto);
        }
        return productFeeDtoList;
    }

    /**
     * Get product pledge list, 质押
     * 
     * @param productId
     * @return
     */
    private List<ProductPledgeDetailsDto> getPledgeList(String productId) {
        List<ProductPledge> productPledgeList = productService.getProductWarrantPledge(productId);
        List<ProductPledgeDetailsDto> productPledgeDetailsDtoList = new ArrayList<ProductPledgeDetailsDto>();
        for (ProductPledge productPledge : productPledgeList) {
            productPledgeDetailsDtoList.add(ConverterService.convert(productPledge, ProductPledgeDetailsDto.class));
        }
        return productPledgeDetailsDtoList;
    }

    /**
     * Get product mortgage vehicle list, 抵押车辆
     * 
     * @param productId
     * @return
     */
    private List<ProductMortgageVehicleDetailsDto> getMortgageVehicleList(String productId) {
        List<ProductMortgageVehicle> productMortgageVehicleList = productService
                .getProductWarrantMortgageForVE(productId);
        List<ProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList = new ArrayList<ProductMortgageVehicleDetailsDto>();
        for (ProductMortgageVehicle productMortgageVehicle : productMortgageVehicleList) {

            ProductMortgageVehicleDetailsDto dto = ConverterService.convert(productMortgageVehicle,
                    ProductMortgageVehicleDetailsDto.class);

            dto.setRegistDt(ProductUtil.formatDate(productMortgageVehicle.getRegistDt()));
            productMortgageVehicleDetailsDtoList.add(dto);
        }
        return productMortgageVehicleDetailsDtoList;
    }

    /**
     * Get product mortgage residential list, 抵押房产
     * 
     * @param productId
     * @return
     */
    private List<ProductMortgageResidentialDetailsDto> getMortgageResidentialList(String productId) {
        List<MortgageResidential> productMortgageResidentialList = productService
                .getProductWarrantMortgageForRE(productId);
        List<ProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList = new ArrayList<ProductMortgageResidentialDetailsDto>();
        for (MortgageResidential mortgageResidential : productMortgageResidentialList) {
            ProductMortgageResidentialDetailsDto detailsDto = ConverterService.convert(mortgageResidential,
                    ProductMortgageResidentialDetailsDto.class);
            detailsDto.setDmortgageType(ConverterService.convert(
                    productService.getDicByCode(EOptionCategory.REAL_ESTATE_TYPE.getCode(),
                            mortgageResidential.getMortgageType()), DynamicOption.class));
            detailsDto.setRegistDate(ProductUtil.formatDate(mortgageResidential.getRegistDate()));
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
        List<ProductAttachment> productAttachmentList = productService.getProductFile(productId);

        List<ProductAttachmentDetailsDto> productAttachmentDetailsDtoList = new ArrayList<ProductAttachmentDetailsDto>();
        for (ProductAttachment productAttachment : productAttachmentList) {
            ProductAttachmentDetailsDto attachmentDetailsDto = new ProductAttachmentDetailsDto();
            ConverterService.convert(productAttachment, attachmentDetailsDto);
            ConverterService.convert(productAttachment.getFilePo(), attachmentDetailsDto);
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
        List<ProductAsset> productAssetList = productService.getProductAsset(productId);
        List<ProductAssetDto> productAssetDtoList = new ArrayList<ProductAssetDto>();
        for (ProductAsset productAsset : productAssetList) {
            ProductAssetDto productAssetDto = ConverterService.convert(productAsset, ProductAssetDto.class);
            productAssetDto.setDtype(ConverterService.convert(
                    productService.getDicByCode(EOptionCategory.ASSET_TYPE.getCode(), productAsset.getType()),
                    DynamicOption.class));
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
    private List<ProductWarrantPersonDto> getProductWarrantPersonList(String productId) {
        List<ProductWarrantPerson> productWarrantList = productService.getProductWarrantPerson(productId);
        List<ProductWarrantPersonDto> productWarrantDtoList = new ArrayList<ProductWarrantPersonDto>();
        for (ProductWarrantPerson productWarrant : productWarrantList) {
            ProductWarrantPersonDto dto = ConverterService.convert(productWarrant, ProductWarrantPersonDto.class);
            dto.setDjob(ConverterService.convert(
                    productService.getDicByCode(EOptionCategory.JOB.getCode(), productWarrant.getJob()),
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
    private List<ProductWarrantEnterpriseDto> getProductWarrantEnterpriseList(String productId) {
        List<ProductWarrantEnterprise> productWarrantList = productService.getProductWarrantEnterprise(productId);
        List<ProductWarrantEnterpriseDto> productWarrantDtoList = new ArrayList<ProductWarrantEnterpriseDto>();
        for (ProductWarrantEnterprise productWarrant : productWarrantList) {
            ProductWarrantEnterpriseDto dto = ConverterService.convert(productWarrant,
                    ProductWarrantEnterpriseDto.class);
            dto.setDenterpriseIndustry(ConverterService.convert(
                    productService.getDicByCode(EOptionCategory.ORG_INDUSTRY.getCode(),
                            productWarrant.getEnterpriseIndustry()), DynamicOption.class));

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

        List<ProductDebt> productDebtList = productService.getProductDebt(productId);

        List<ProductDebtDto> productDebtDtoList = new ArrayList<ProductDebtDto>();
        for (ProductDebt productDebt : productDebtList) {
            ProductDebtDto debtDto = ConverterService.convert(productDebt, ProductDebtDto.class);
            debtDto.setDtype(ConverterService.convert(
                    productService.getDicByCode(EOptionCategory.DEBT_TYPE.getCode(), productDebt.getType()),
                    DynamicOption.class));
            debtDto.setDebtAmount(NumberUtil.formatTenThousandUnitAmt(productDebt.getDebtAmount()));
            productDebtDtoList.add(debtDto);
        }
        return productDebtDtoList;
    }

    /**
     * 
     * Description: 融资包
     * 
     * @param productId
     * @return
     */
    private List<ProductPackageDto> getProductPackageList(String productId) {
        List<ProductPackage> productPackageList = productPackageService.getProductPackageInfo(productId);
        List<ProductPackageDto> productPackageDtoList = new ArrayList<ProductPackageDto>();
        for (ProductPackage productPackage : productPackageList) {
            ProductPackageDto packageDto = new ProductPackageDto();
            ConverterService.convert(productPackage, packageDto);
            productPackageDtoList.add(packageDto);
        }
        sortPackageList(productPackageDtoList);
        return productPackageDtoList;
    }

    private void sortPackageList(List<ProductPackageDto> packageList) {
        Collections.sort(packageList, new Comparator<ProductPackageDto>() {
            @Override
            public int compare(ProductPackageDto o1, ProductPackageDto o2) {
                if (o1 == null || o2 == null || StringUtils.isBlank(o1.getId()) || StringUtils.isBlank(o2.getId())) {
                    return 0;
                }
                return o1.getId().compareToIgnoreCase(o2.getId());
            }
        });
    }
    
    public static void main(String[] args){
        BigDecimal i = new BigDecimal(0.003).setScale(6, RoundingMode.DOWN);
        String str = i.multiply(NumberUtil.getHundred()).setScale(4, RoundingMode.DOWN).toPlainString();      
        System.out.println(str);
    }


    /* getter && setter */
    public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public FeeGroupService getFeeGroupService() {
		return feeGroupService;
	}

	public void setFeeGroupService(FeeGroupService feeGroupService) {
		this.feeGroupService = feeGroupService;
	}

	public FeeService getFeeService() {
		return feeService;
	}

	public void setFeeService(FeeService feeService) {
		this.feeService = feeService;
	}

	public ProductFreezeService getProductFreezeService() {
		return productFreezeService;
	}

	public void setProductFreezeService(ProductFreezeService productFreezeService) {
		this.productFreezeService = productFreezeService;
	}

	public FinancingPackageService getProductPackageService() {
		return productPackageService;
	}

	public void setProductPackageService(
			FinancingPackageService productPackageService) {
		this.productPackageService = productPackageService;
	}
}
