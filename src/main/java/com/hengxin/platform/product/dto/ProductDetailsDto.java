package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.validator.ProductAppliedQuota;

/**
 * Class Name: ProductDetailsDto.
 * @author yeliangjin.
 *
 */
public class ProductDetailsDto implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String wrtrCreditFile;
    
    private String wrtrCreditDesc;
	
    private String productId;

    private String currentUserType;// O机构 P 个人

    private ProductUserDto user;

    private String createOpid;

    private String lastMntOpid;

    private String createTs;

    private Date lastMntTs;

    private String applUserId; // 申请人

    private String warrantId; // 担保人
    
    private String warrantIdShow;
    
    private boolean flag;//判断是否是链接进来
    
    private ProductSeatFeeDto feeDto;//席位费到期日

	private BigDecimal contractTemplateId;// 合同模板ID
	private String     contractTemplateName;// 合同名称

    /**
     * 基本信息
     */

    @NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    @Length(min = 1, max = 30, message = "{member.error.password.length}", groups = { SubmitProductApply.class })
    private String productName;// 项目简称

    @Digits(fraction = 1, integer = 12, message = "{product.error.applyQuota}", groups = { SubmitProductApply.class })
    @Min(value = 10000, message = "{product.error.applyQuota}", groups = { SubmitProductApply.class })
    @ProductAppliedQuota(groups = { SubmitProductApply.class })
    private BigDecimal appliedQuota;// 融资申请额

    @Digits(fraction = 2, integer = 15, message = "{member.error.password.length}", groups = { SubmitProductApply.class })
    private BigDecimal productQuota; // 可融资金额

    @Digits(fraction = 6, integer = 4, message = "{product.error.rate}", groups = { SubmitProductApply.class })
    private BigDecimal rate;// 年利率

    private Integer termLength;// 融资期限

    private ETermType termType = ETermType.NULL; // 期限类型 D-Day,M-Month,Y-Year';

    private EPayMethodType payMethod = EPayMethodType.NULL;// 还款方式

    private Integer libId;// 产品库Id

    private ProductLibDto productLib;//产品库

    private String financierName;// 融资人姓名

    @Pattern(regexp = ApplicationConstant.MOBILE_REGEXP, groups = { SubmitProductApply.class }, message = "{member.error.mobile.invaild}")
    private String financierMobile;// 联系方式

    @OptionCategory(EOptionCategory.ORG_INDUSTRY)
    private DynamicOption financierIndustryType;// 行业

    private EWarrantyType warrantyType = EWarrantyType.NULL;// 风险保障 担保类型

    private BigDecimal financierSalary;// 平均月收入（删）

    @NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    @Length(min = 1, max = 200, message = "{product.error.applyQuota}", groups = { SubmitProductApply.class })
    private String loanPurpose;// 用途

    private Integer unit;// 融资包分数

    private String guaranteeInstitution;// 担保机构（详情查看页面用）
    private String guaranteeInstitutionShow;
    private String guaranteeLicenseNoImg;
    @SuppressWarnings("unused")
	private String guaranteeLicenseNoImgUrl;

    private EProductStatus status = EProductStatus.NULL; // 融资项目状态

    /**
     * 反担保情况
     */
    // 房产抵押
    @Valid
    private List<ProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList;

    // 车辆抵押
    @Valid
    private List<ProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList;

    // 其他抵押
    @Length(min = 0, max = 40, message = "{member.error.password.length}", groups = { SubmitProductApply.class })
    private String otherMortgage;// 其他抵押

    // 动产质押
    @Valid
    private List<ProductPledgeDetailsDto> productPledgeDetailsDtoList;

    // 其他质押
    @Length(min = 0, max = 100, message = "{member.error.password.length}", groups = { SubmitProductApply.class })
    private String otherPledge;// 其他质押

    // 保证人
    @Valid
    private List<ProductWarrantPersonDto> productWarrantPersonDtoList;

    // 保证法人
    @Valid
    private List<ProductWarrantEnterpriseDto> productWarrantEnterpriseDtoList;
    /**
     * 资产及负债
     */
    @Valid
    private List<ProductAssetDto> productAssetDtoList;// 资产
    @Valid
    private List<ProductDebtDto> productDebtDtoList;// 负债

    /**
     * 资料库
     */
    @Valid
    private List<ProductAttachmentDetailsDto> productAttachmentDetailsDtoList;
    /**
     * 备注
     */
    private String notes;// 备注

    private Long versionCt;

    /**
     * 权限控制
     */
    private boolean canFinancingApprove;

    private boolean canFinancingRating;

    private boolean canFinancingFreeze;

    private boolean canFinancingTransSetting;

    private boolean canCancelFinancingPackage;

    private boolean canCashDepositEdit; // 能编辑保证金

    private boolean canNotSeeGuarantors; // 能看见担保机构名称

    private boolean canSeeDepositFee;// 能否看见保证金

    private boolean canSeeFee;// 能否看见费

    /**
     * 融资包参数
     */

    private List<ProductPackageDto> productPackageDtoList;

    /**
     * 风险等级 评级
     */
    // FIXME 行情详情更改，如果别的地方要用说下，by runchen
    @Deprecated
    private BigDecimal productGrage; // 产品评分
    @Deprecated
    private BigDecimal financierGrage; // 融资人评分
    @Deprecated
    private BigDecimal warrantGrage; // 担保人评分
    @Deprecated
    private BigDecimal totalGrage; // 总体评分
    @Deprecated
    private String productClass;// 项目评级
    /**
     * 评分级别
     */
    private String productGrageClass; // 产品评分
    private String financierGrageClass; // 融资人评分
    private String warrantGrageClass; // 担保人评分

    /**
     * 评分说明
     */
    @Deprecated
    private String productGrageComment; // 产品评分说明
    @Deprecated
    private String financierGrageComment; // 融资人评分说明
    @Deprecated
    private String warrantGrageComment; // 担保人评分说明
    @Deprecated
    private String totalGrageComment; // 总体评分说明

    
    /**
     * 新评级方式
     */
	private ERiskLevel productLevel = ERiskLevel.NULL; // 融资项目级别

	private String financierLevel; // 融资会员信用积分

	private String warrantorLevel; // 担保机构信用积分

    /**
     * 保证金及费用
     */
    private BigDecimal financierSeatFee;// 融资会员席位费（参考值）

    private BigDecimal contractFee; // 参考借款合同履约保证金

    private String referContractFeePer; // 参考借款合同履约保证金百分比

    private BigDecimal financeFee; // 融资服务合同保证金

    private BigDecimal warrantFee; // 担保公司-还款保证金

    private String warrantFeePer; // 担保公司-还款保证金百分比

    private BigDecimal riskFee;// 风险管理费

    private BigDecimal serviceFee;// 融资服务费
    
    private String riskFeeRate;// 风险管理费率

    private String serviceFeeRate;// 融资服务费率

    private Integer termToDays; // 计息天数（加）

    @NotEmpty(groups = { SubmitProductApply.class }, message = "{member.error.field.empty}")
    @Length(min = 0, max = 17, message = "{member.error.password.length}", groups = { SubmitProductApply.class })
    private String acctNo;// 交易帐号（加）

    private CashDepositDto cashDeposit;
    /**
     * DynamicOption
     */
    private DynamicOption DfinancierIndustryType;
    /**
     * 合同模板
     */
    private ProductContractTemplateDto productContractTemplate;
    
    private Long overdue2CmpnsGracePeriod;

    public String[] getRatePercent() {
        return NumberUtil.getPercentSplitStr(rate, 1);
    }

    /**
     * @return return the value of the var productName
     */

    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     *            Set productName value
     */

    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return return the value of the var appliedQuota
     */

    public BigDecimal getAppliedQuota() {
        return appliedQuota;
    }

    /**
     * @param appliedQuota
     *            Set appliedQuota value
     */

    public void setAppliedQuota(BigDecimal appliedQuota) {
        this.appliedQuota = appliedQuota;
    }

    /**
     * @return return the value of the var rate
     */

    public BigDecimal getRate() {
        return rate;
    }

    /**
     * @param rate
     *            Set rate value
     */

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return return the value of the var termLength
     */

    public Integer getTermLength() {
        return termLength;
    }

    /**
     * @param termLength
     *            Set termLength value
     */

    public void setTermLength(Integer termLength) {
        this.termLength = termLength;
    }

    /**
     * @return return the value of the var financierMobile
     */

    public String getFinancierMobile() {
        return financierMobile;
    }

    /**
     * @param financierMobile
     *            Set financierMobile value
     */

    public void setFinancierMobile(String financierMobile) {
        this.financierMobile = financierMobile;
    }

    /**
     * @return return the value of the var financierSalary
     */

    public BigDecimal getFinancierSalary() {
        return financierSalary;
    }

    /**
     * @param financierSalary
     *            Set financierSalary value
     */

    public void setFinancierSalary(BigDecimal financierSalary) {
        this.financierSalary = financierSalary;
    }

    /**
     * @return return the value of the var loanPurpose
     */

    public String getLoanPurpose() {
        return loanPurpose;
    }

    /**
     * @param loanPurpose
     *            Set loanPurpose value
     */

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    /**
     * @return return the value of the var unit
     */

    public Integer getUnit() {
        return unit;
    }

    /**
     * @param unit
     *            Set unit value
     */

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    /**
     * @return return the value of the var guaranteeInstitution
     */

    public String getGuaranteeInstitution() {
        return guaranteeInstitution;
    }

    /**
     * @param guaranteeInstitution
     *            Set guaranteeInstitution value
     */

    public void setGuaranteeInstitution(String guaranteeInstitution) {
        this.guaranteeInstitution = guaranteeInstitution;
    }


    /**
     * @return return the value of the var productMortgageResidentialDetailsDtoList
     */

    public List<ProductMortgageResidentialDetailsDto> getProductMortgageResidentialDetailsDtoList() {
        return productMortgageResidentialDetailsDtoList;
    }

    /**
     * @param productMortgageResidentialDetailsDtoList
     *            Set productMortgageResidentialDetailsDtoList value
     */

    public void setProductMortgageResidentialDetailsDtoList(
            List<ProductMortgageResidentialDetailsDto> productMortgageResidentialDetailsDtoList) {
        this.productMortgageResidentialDetailsDtoList = productMortgageResidentialDetailsDtoList;
    }

    /**
     * @return return the value of the var productMortgageVehicleDetailsDtoList
     */

    public List<ProductMortgageVehicleDetailsDto> getProductMortgageVehicleDetailsDtoList() {
        return productMortgageVehicleDetailsDtoList;
    }

    /**
     * @param productMortgageVehicleDetailsDtoList
     *            Set productMortgageVehicleDetailsDtoList value
     */

    public void setProductMortgageVehicleDetailsDtoList(
            List<ProductMortgageVehicleDetailsDto> productMortgageVehicleDetailsDtoList) {
        this.productMortgageVehicleDetailsDtoList = productMortgageVehicleDetailsDtoList;
    }

    /**
     * @return return the value of the var otherMortgage
     */

    public String getOtherMortgage() {
        return otherMortgage;
    }

    /**
     * @param otherMortgage
     *            Set otherMortgage value
     */

    public void setOtherMortgage(String otherMortgage) {
        this.otherMortgage = otherMortgage;
    }

    /**
     * @return return the value of the var productPledgeDetailsDtoList
     */

    public List<ProductPledgeDetailsDto> getProductPledgeDetailsDtoList() {
        return productPledgeDetailsDtoList;
    }

    /**
     * @param productPledgeDetailsDtoList
     *            Set productPledgeDetailsDtoList value
     */

    public void setProductPledgeDetailsDtoList(List<ProductPledgeDetailsDto> productPledgeDetailsDtoList) {
        this.productPledgeDetailsDtoList = productPledgeDetailsDtoList;
    }

    /**
     * @return return the value of the var otherPledge
     */

    public String getOtherPledge() {
        return otherPledge;
    }

    /**
     * @param otherPledge
     *            Set otherPledge value
     */

    public void setOtherPledge(String otherPledge) {
        this.otherPledge = otherPledge;
    }

    /**
     * @return return the value of the var productAttachmentDetailsDtoList
     */

    public List<ProductAttachmentDetailsDto> getProductAttachmentDetailsDtoList() {
        return productAttachmentDetailsDtoList;
    }

    /**
     * @param productAttachmentDetailsDtoList
     *            Set productAttachmentDetailsDtoList value
     */

    public void setProductAttachmentDetailsDtoList(List<ProductAttachmentDetailsDto> productAttachmentDetailsDtoList) {
        this.productAttachmentDetailsDtoList = productAttachmentDetailsDtoList;
    }

    /**
     * @return return the value of the var remark
     */

    public String getNotes() {
        return notes;
    }

    /**
     * @param remark
     *            Set remark value
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return return the value of the var productQuota
     */

    public BigDecimal getProductQuota() {
        return productQuota;
    }

    /**
     * @param productQuota
     *            Set productQuota value
     */

    public void setProductQuota(BigDecimal productQuota) {
        this.productQuota = productQuota;
    }

    /**
     * @return return the value of the var versionCt
     */

    public Long getVersionCt() {
        return versionCt;
    }

    /**
     * @param versionCt
     *            Set versionCt value
     */

    public void setVersionCt(Long versionCt) {
        this.versionCt = versionCt;
    }

    /**
     * @return return the value of the var libId
     */

    public Integer getLibId() {
        return libId;
    }

    /**
     * @param libId
     *            Set libId value
     */

    public void setLibId(Integer libId) {
        this.libId = libId;
    }

    /**
     * @return return the value of the var status
     */

    public EProductStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            Set status value
     */

    public void setStatus(EProductStatus status) {
        this.status = status;
    }

    /**
     * @return return the value of the var canFinancingApprove
     */

    public boolean isCanFinancingApprove() {
        return canFinancingApprove;
    }

    /**
     * @param canFinancingApprove
     *            Set canFinancingApprove value
     */

    public void setCanFinancingApprove(boolean canFinancingApprove) {
        this.canFinancingApprove = canFinancingApprove;
    }

    /**
     * @return return the value of the var canFinancingRating
     */

    public boolean isCanFinancingRating() {
        return canFinancingRating;
    }

    /**
     * @param canFinancingRating
     *            Set canFinancingRating value
     */

    public void setCanFinancingRating(boolean canFinancingRating) {
        this.canFinancingRating = canFinancingRating;
    }

    /**
     * @return return the value of the var canFinancingFreeze
     */

    public boolean isCanFinancingFreeze() {
        return canFinancingFreeze;
    }

    /**
     * @param canFinancingFreeze
     *            Set canFinancingFreeze value
     */

    public void setCanFinancingFreeze(boolean canFinancingFreeze) {
        this.canFinancingFreeze = canFinancingFreeze;
    }

    /**
     * @return return the value of the var canFinancingTransSetting
     */

    public boolean isCanFinancingTransSetting() {
        return canFinancingTransSetting;
    }

    /**
     * @param canFinancingTransSetting
     *            Set canFinancingTransSetting value
     */

    public void setCanFinancingTransSetting(boolean canFinancingTransSetting) {
        this.canFinancingTransSetting = canFinancingTransSetting;
    }

    /**
     * @return return the value of the var financierName
     */

    public String getFinancierName() {
        return financierName;
    }

    /**
     * @param financierName
     *            Set financierName value
     */

    public void setFinancierName(String financierName) {
        this.financierName = financierName;
    }

    /**
     * @return return the value of the var payMethod
     */

    public EPayMethodType getPayMethod() {
        return payMethod;
    }

    /**
     * @param payMethod
     *            Set payMethod value
     */

    public void setPayMethod(EPayMethodType payMethod) {
        this.payMethod = payMethod;
    }

    /**
     * @return return the value of the var warrantyType
     */

    public EWarrantyType getWarrantyType() {
        return warrantyType;
    }

    /**
     * @param warrantyType
     *            Set warrantyType value
     */

    public void setWarrantyType(EWarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    /**
     * @return return the value of the var productPackageDtoList
     */

    public List<ProductPackageDto> getProductPackageDtoList() {
        return productPackageDtoList;
    }

    /**
     * @param productPackageDtoList
     *            Set productPackageDtoList value
     */

    public void setProductPackageDtoList(List<ProductPackageDto> productPackageDtoList) {
        this.productPackageDtoList = productPackageDtoList;
    }

    /**
     * @return return the value of the var productId
     */

    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            Set productId value
     */

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return return the value of the var productGrage
     */

    public BigDecimal getProductGrage() {
        return productGrage;
    }

    /**
     * @param productGrage
     *            Set productGrage value
     */

    public void setProductGrage(BigDecimal productGrage) {
        this.productGrage = productGrage;
    }

    /**
     * @return return the value of the var financierGrage
     */

    public BigDecimal getFinancierGrage() {
        return financierGrage;
    }

    /**
     * @param financierGrage
     *            Set financierGrage value
     */

    public void setFinancierGrage(BigDecimal financierGrage) {
        this.financierGrage = financierGrage;
    }

    /**
     * @return return the value of the var warrantGrage
     */

    public BigDecimal getWarrantGrage() {
        return warrantGrage;
    }

    /**
     * @param warrantGrage
     *            Set warrantGrage value
     */

    public void setWarrantGrage(BigDecimal warrantGrage) {
        this.warrantGrage = warrantGrage;
    }

    /**
     * @return return the value of the var totalGrage
     */

    public BigDecimal getTotalGrage() {
        return totalGrage;
    }

    /**
     * @param totalGrage
     *            Set totalGrage value
     */

    public void setTotalGrage(BigDecimal totalGrage) {
        this.totalGrage = totalGrage;
    }


    /**
     * @return return the value of the var contractFee
     */
    
    public BigDecimal getContractFee() {
        return contractFee;
    }

    /**
     * @param contractFee Set contractFee value
     */
    
    public void setContractFee(BigDecimal contractFee) {
        this.contractFee = contractFee;
    }

    /**
     * @return return the value of the var productUserDto
     */

    public ProductUserDto getProductUserDto() {
        return user;
    }

    /**
     * @param productUserDto
     *            Set productUserDto value
     */

    public void setProductUserDto(ProductUserDto user) {
        this.user = user;
    }

    /**
     * @return return the value of the var productAssetsDtoList
     */

    public List<ProductAssetDto> getProductAssetDtoList() {
        return productAssetDtoList;
    }

    /**
     * @param productAssetsDtoList
     *            Set productAssetsDtoList value
     */

    public void setProductAssetDtoList(List<ProductAssetDto> productAssetDtoList) {
        this.productAssetDtoList = productAssetDtoList;
    }

    /**
     * @return return the value of the var productWarrantPersonDtoList
     */

    public List<ProductWarrantPersonDto> getProductWarrantPersonDtoList() {
        return productWarrantPersonDtoList;
    }

    /**
     * @param productWarrantPersonDtoList
     *            Set productWarrantPersonDtoList value
     */

    public void setProductWarrantPersonDtoList(List<ProductWarrantPersonDto> productWarrantPersonDtoList) {
        this.productWarrantPersonDtoList = productWarrantPersonDtoList;
    }

    /**
     * @return return the value of the var productWarrantEnterpriseDtoList
     */

    public List<ProductWarrantEnterpriseDto> getProductWarrantEnterpriseDtoList() {
        return productWarrantEnterpriseDtoList;
    }

    /**
     * @param productWarrantEnterpriseDtoList
     *            Set productWarrantEnterpriseDtoList value
     */

    public void setProductWarrantEnterpriseDtoList(List<ProductWarrantEnterpriseDto> productWarrantEnterpriseDtoList) {
        this.productWarrantEnterpriseDtoList = productWarrantEnterpriseDtoList;
    }

    /**
     * @return return the value of the var productContractTemplate
     */

    public ProductContractTemplateDto getProductContractTemplate() {
        return productContractTemplate;
    }

    /**
     * @param productContractTemplate
     *            Set productContractTemplate value
     */

    public void setProductContractTemplate(ProductContractTemplateDto productContractTemplate) {
        this.productContractTemplate = productContractTemplate;
    }

    /**
     * @return return the value of the var termToDays
     */

    public Integer getTermToDays() {
        return termToDays;
    }

    /**
     * @param termToDays
     *            Set termToDays value
     */

    public void setTermToDays(Integer termToDays) {
        this.termToDays = termToDays;
    }

    /**
     * @return return the value of the var acctNo
     */

    public String getAcctNo() {
        return acctNo;
    }

    /**
     * @param acctNo
     *            Set acctNo value
     */

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    /**
     * @return return the value of the var termType
     */

    public ETermType getTermType() {
        return termType;
    }

    /**
     * @param termType
     *            Set termType value
     */

    public void setTermType(ETermType termType) {
        this.termType = termType;
    }

    /**
     * @return return the value of the var user
     */

    public ProductUserDto getUser() {
        return user;
    }

    /**
     * @param user
     *            Set user value
     */

    public void setUser(ProductUserDto user) {
        this.user = user;
    }

    /**
     * @return return the value of the var createOpid
     */

    public String getCreateOpid() {
        return createOpid;
    }

    /**
     * @param createOpid
     *            Set createOpid value
     */

    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    /**
     * @return return the value of the var lastMntOpid
     */

    public String getLastMntOpid() {
        return lastMntOpid;
    }

    /**
     * @param lastMntOpid
     *            Set lastMntOpid value
     */

    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }


    /**
     * @return return the value of the var createTs
     */
    
    public String getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs Set createTs value
     */
    
    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

    /**
     * @return return the value of the var lastMntTs
     */

    public Date getLastMntTs() {
        return lastMntTs;
    }

    /**
     * @param lastMntTs
     *            Set lastMntTs value
     */

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    /**
     * @return return the value of the var applUserId
     */

    public String getApplUserId() {
        return applUserId;
    }

    /**
     * @param applUserId
     *            Set applUserId value
     */

    public void setApplUserId(String applUserId) {
        this.applUserId = applUserId;
    }

    /**
     * @return return the value of the var warrantId
     */

    public String getWarrantId() {
        return warrantId;
    }

    /**
     * @param warrantId
     *            Set warrantId value
     */

    public void setWarrantId(String warrantId) {
        this.warrantId = warrantId;
    }

    /**
     * @return return the value of the var canCancelFinancingPackage
     */

    public boolean isCanCancelFinancingPackage() {
        return canCancelFinancingPackage;
    }

    /**
     * @param canCancelFinancingPackage
     *            Set canCancelFinancingPackage value
     */

    public void setCanCancelFinancingPackage(boolean canCancelFinancingPackage) {
        this.canCancelFinancingPackage = canCancelFinancingPackage;
    }


    /**
     * @return return the value of the var currentUserType
     */

    public String getCurrentUserType() {
        return currentUserType;
    }

    /**
     * @param currentUserType
     *            Set currentUserType value
     */

    public void setCurrentUserType(String currentUserType) {
        this.currentUserType = currentUserType;
    }

    public CashDepositDto getCashDeposit() {
        return cashDeposit;
    }

    public void setCashDeposit(CashDepositDto cashDeposit) {
        this.cashDeposit = cashDeposit;
    }

    public boolean isCanCashDepositEdit() {
        return canCashDepositEdit;
    }

    public void setCanCashDepositEdit(boolean canCashDepositEdit) {
        this.canCashDepositEdit = canCashDepositEdit;
    }

    public boolean isCanNotSeeGuarantors() {
        return canNotSeeGuarantors;
    }

    public void setCanNotSeeGuarantors(boolean canNotSeeGuarantors) {
        this.canNotSeeGuarantors = canNotSeeGuarantors;
    }

    /**
     * @return return the value of the var productDebtDtoList
     */

    public List<ProductDebtDto> getProductDebtDtoList() {
        return productDebtDtoList;
    }

    /**
     * @param productDebtDtoList
     *            Set productDebtDtoList value
     */

    public void setProductDebtDtoList(List<ProductDebtDto> productDebtDtoList) {
        this.productDebtDtoList = productDebtDtoList;
    }

    /**
     * @return return the value of the var referContractFeePer
     */

    public String getReferContractFeePer() {
        return referContractFeePer;
    }

    /**
     * @param referContractFeePer
     *            Set referContractFeePer value
     */

    public void setReferContractFeePer(String referContractFeePer) {
        this.referContractFeePer = referContractFeePer;
    }

    /**
     * @return return the value of the var warrantFeePer
     */

    public String getWarrantFeePer() {
        return warrantFeePer;
    }

    /**
     * @param warrantFeePer
     *            Set warrantFeePer value
     */

    public void setWarrantFeePer(String warrantFeePer) {
        this.warrantFeePer = warrantFeePer;
    }

    /**
     * @return return the value of the var financierIndustryType
     */

    public DynamicOption getFinancierIndustryType() {
        return financierIndustryType;
    }

    /**
     * @param financierIndustryType
     *            Set financierIndustryType value
     */

    public void setFinancierIndustryType(DynamicOption financierIndustryType) {
        this.financierIndustryType = financierIndustryType;
    }

    /**
     * @return return the value of the var dfinancierIndustryType
     */

    public DynamicOption getDfinancierIndustryType() {
        return DfinancierIndustryType;
    }

    /**
     * @param dfinancierIndustryType
     *            Set dfinancierIndustryType value
     */

    public void setDfinancierIndustryType(DynamicOption dfinancierIndustryType) {
        DfinancierIndustryType = dfinancierIndustryType;
    }

    /**
     * @return return the value of the var productGrageComment
     */

    public String getProductGrageComment() {
        return productGrageComment;
    }

    /**
     * @param productGrageComment
     *            Set productGrageComment value
     */

    public void setProductGrageComment(String productGrageComment) {
        this.productGrageComment = productGrageComment;
    }

    /**
     * @return return the value of the var financierGrageComment
     */

    public String getFinancierGrageComment() {
        return financierGrageComment;
    }

    /**
     * @param financierGrageComment
     *            Set financierGrageComment value
     */

    public void setFinancierGrageComment(String financierGrageComment) {
        this.financierGrageComment = financierGrageComment;
    }

    /**
     * @return return the value of the var warrantGrageComment
     */

    public String getWarrantGrageComment() {
        return warrantGrageComment;
    }

    /**
     * @param warrantGrageComment
     *            Set warrantGrageComment value
     */

    public void setWarrantGrageComment(String warrantGrageComment) {
        this.warrantGrageComment = warrantGrageComment;
    }

    /**
     * @return return the value of the var totalGrageComment
     */

    public String getTotalGrageComment() {
        return totalGrageComment;
    }

    /**
     * @param totalGrageComment
     *            Set totalGrageComment value
     */

    public void setTotalGrageComment(String totalGrageComment) {
        this.totalGrageComment = totalGrageComment;
    }

    /**
     * @return return the value of the var productLib
     */

    public ProductLibDto getProductLib() {
        return productLib;
    }

    /**
     * @param productLib
     *            Set productLib value
     */

    public void setProductLib(ProductLibDto productLib) {
        this.productLib = productLib;
    }

    /**
     * @return return the value of the var productClass
     */

    public String getProductClass() {
        return productClass;
    }

    /**
     * @param productClass
     *            Set productClass value
     */

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    /**
     * @return return the value of the var canSeeFee
     */

    public boolean isCanSeeFee() {
        return canSeeFee;
    }

    /**
     * @param canSeeFee
     *            Set canSeeFee value
     */

    public void setCanSeeFee(boolean canSeeFee) {
        this.canSeeFee = canSeeFee;
    }

    /**
     * @return return the value of the var canSeeDepositFee
     */

    public boolean isCanSeeDepositFee() {
        return canSeeDepositFee;
    }

    /**
     * @param canSeeDepositFee
     *            Set canSeeDepositFee value
     */

    public void setCanSeeDepositFee(boolean canSeeDepositFee) {
        this.canSeeDepositFee = canSeeDepositFee;
    }

    /**
     * @return return the value of the var productGrageClass
     */

    public String getProductGrageClass() {
        return productGrageClass;
    }

    /**
     * @param productGrageClass
     *            Set productGrageClass value
     */

    public void setProductGrageClass(String productGrageClass) {
        this.productGrageClass = productGrageClass;
    }

    /**
     * @return return the value of the var financierGrageClass
     */

    public String getFinancierGrageClass() {
        return financierGrageClass;
    }

    /**
     * @param financierGrageClass
     *            Set financierGrageClass value
     */

    public void setFinancierGrageClass(String financierGrageClass) {
        this.financierGrageClass = financierGrageClass;
    }

    /**
     * @return return the value of the var warrantGrageClass
     */

    public String getWarrantGrageClass() {
        return warrantGrageClass;
    }

    /**
     * @param warrantGrageClass
     *            Set warrantGrageClass value
     */

    public void setWarrantGrageClass(String warrantGrageClass) {
        this.warrantGrageClass = warrantGrageClass;
    }

    /**
     * @return return the value of the var riskFeeRate
     */
    
    public String getRiskFeeRate() {
        return riskFeeRate;
    }

    /**
     * @param riskFeeRate Set riskFeeRate value
     */
    
    public void setRiskFeeRate(String riskFeeRate) {
        this.riskFeeRate = riskFeeRate;
    }

    /**
     * @return return the value of the var serviceFeeRate
     */
    
    public String getServiceFeeRate() {
        return serviceFeeRate;
    }

    /**
     * @param serviceFeeRate Set serviceFeeRate value
     */
    
    public void setServiceFeeRate(String serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    /**
     * @return return the value of the var financierSeatFee
     */
    
    public BigDecimal getFinancierSeatFee() {
        return financierSeatFee;
    }

    /**
     * @param financierSeatFee Set financierSeatFee value
     */
    
    public void setFinancierSeatFee(BigDecimal financierSeatFee) {
        this.financierSeatFee = financierSeatFee;
    }

    /**
     * @return return the value of the var financeFee
     */
    
    public BigDecimal getFinanceFee() {
        return financeFee;
    }

    /**
     * @param financeFee Set financeFee value
     */
    
    public void setFinanceFee(BigDecimal financeFee) {
        this.financeFee = financeFee;
    }

    /**
     * @return return the value of the var warrantFee
     */
    
    public BigDecimal getWarrantFee() {
        return warrantFee;
    }

    /**
     * @param warrantFee Set warrantFee value
     */
    
    public void setWarrantFee(BigDecimal warrantFee) {
        this.warrantFee = warrantFee;
    }

    /**
     * @return return the value of the var riskFee
     */
    
    public BigDecimal getRiskFee() {
        return riskFee;
    }

    /**
     * @param riskFee Set riskFee value
     */
    
    public void setRiskFee(BigDecimal riskFee) {
        this.riskFee = riskFee;
    }

    /**
     * @return return the value of the var serviceFee
     */
    
    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    /**
     * @param serviceFee Set serviceFee value
     */
    
    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    /**
     * @return return the value of the var flag
     */
    
    public boolean isFlag() {
        return flag;
    }

    /**
     * @param flag Set flag value
     */
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * @return return the value of the var feeDto
     */
    
    public ProductSeatFeeDto getFeeDto() {
        return feeDto;
    }

    /**
     * @param feeDto Set feeDto value
     */
    
    public void setFeeDto(ProductSeatFeeDto feeDto) {
        this.feeDto = feeDto;
    }

	public BigDecimal getContractTemplateId() {
		return contractTemplateId;
	}

	public void setContractTemplateId(BigDecimal contractTemplateId) {
		this.contractTemplateId = contractTemplateId;
	}


    public String getGuaranteeLicenseNoImg() {
        return guaranteeLicenseNoImg;
    }

    public void setGuaranteeLicenseNoImg(String guaranteeLicenseNoImg) {
        this.guaranteeLicenseNoImg = guaranteeLicenseNoImg;
    }

    public String getGuaranteeLicenseNoImgUrl() {
        if (StringUtils.isNotBlank(guaranteeLicenseNoImg)) {
            return FileUploadUtil.getTempFolder() + guaranteeLicenseNoImg;
        }
        return "";
    }

    public Long getOverdue2CmpnsGracePeriod() {
        return overdue2CmpnsGracePeriod;
    }

    public void setOverdue2CmpnsGracePeriod(Long overdue2CmpnsGracePeriod) {
        this.overdue2CmpnsGracePeriod = overdue2CmpnsGracePeriod;
    }

	public ERiskLevel getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(ERiskLevel productLevel) {
		this.productLevel = productLevel;
	}

	public String getFinancierLevel() {
		return financierLevel;
	}

	public void setFinancierLevel(String financierLevel) {
		this.financierLevel = financierLevel;
	}

	public String getWarrantorLevel() {
		return warrantorLevel;
	}

	public void setWarrantorLevel(String warrantorLevel) {
		this.warrantorLevel = warrantorLevel;
	}

	public String getWarrantIdShow() {
		return warrantIdShow;
	}

	public void setWarrantIdShow(String warrantIdShow) {
		this.warrantIdShow = warrantIdShow;
	}

	public String getGuaranteeInstitutionShow() {
		return guaranteeInstitutionShow;
	}

	public void setGuaranteeInstitutionShow(String guaranteeInstitutionShow) {
		this.guaranteeInstitutionShow = guaranteeInstitutionShow;
	}

	public String getContractTemplateName() {
		return contractTemplateName;
	}

	public void setContractTemplateName(String contractTemplateName) {
		this.contractTemplateName = contractTemplateName;
	}

	/**
	 * @return the wrtrCreditFile
	 */
	public String getWrtrCreditFile() {
		return wrtrCreditFile;
	}

	/**
	 * @param wrtrCreditFile the wrtrCreditFile to set
	 */
	public void setWrtrCreditFile(String wrtrCreditFile) {
		this.wrtrCreditFile = wrtrCreditFile;
	}

	/**
	 * @return the wrtrCreditDesc
	 */
	public String getWrtrCreditDesc() {
		return wrtrCreditDesc;
	}

	/**
	 * @param wrtrCreditDesc the wrtrCreditDesc to set
	 */
	public void setWrtrCreditDesc(String wrtrCreditDesc) {
		this.wrtrCreditDesc = wrtrCreditDesc;
	}
	
	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
    public interface SubmitProductApply {

    }
}
