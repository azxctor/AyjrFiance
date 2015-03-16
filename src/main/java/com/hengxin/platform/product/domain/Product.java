package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.member.domain.ProductProviderInfo;
import com.hengxin.platform.product.domain.converter.BooleanStringConverter;
import com.hengxin.platform.product.domain.converter.ERiskLevelConverter;
import com.hengxin.platform.product.domain.converter.PayMethodConverter;
import com.hengxin.platform.product.domain.converter.ProductStatusConverter;
import com.hengxin.platform.product.domain.converter.ProductTermConverter;
import com.hengxin.platform.product.domain.converter.WarrantyTypeConverter;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.security.entity.UserPo;

/**
 * 融资产品
 * 
 * @author tiexiyu
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PROD")
@EntityListeners(IdInjectionEntityListener.class)
public class Product extends BaseInfo implements Serializable {

    @Id
    @Column(name = "PROD_ID")
    private String productId; // 产品编号

    @Column(name = "PROD_NAME")
    private String productName; // 产品包名称

    @Column(name = "OTHER_MTGE_TX")
    private String otherMortgage; // 其他抵押

    @Column(name = "OTHER_PLEDGE_TX")
    private String otherPledge; // 其他质押

    @Column(name = "APPLIED_QUOTA")
    private BigDecimal appliedQuota; // 申请金额

    @Column(name = "PROD_QUOTA")
    private BigDecimal productQuota; // 可融资金额

    @Column(name = "UNIT")
    private Integer unit; // 份数

    @Column(name = "UNIT_AMT")
    private BigDecimal unitAmount; // 每份金额

    @Column(name = "RATE")
    private BigDecimal rate; // 年利率

    @Column(name = "TERM_TYPE")
    @Convert(converter = ProductTermConverter.class)
    private ETermType termType; // 期限类型 D-Day,M-Month,Y-Year';

    @Column(name = "TERM_LENGTH")
    private Integer termLength; // 期限长度

    @Column(name = "PAY_METHOD")
    @Convert(converter = PayMethodConverter.class)
    private EPayMethodType payMethod; // 还款方式 01-到期一次还本付息,02-按月等额还息，到期一次还本, 03-按月等本等息

    @Column(name = "FNCR_MOBILE")
    private String financierMobile; // 融资人手机

    @Column(name = "FNCR_INDUSTRY_CD")
    private String financierIndustryType; // 融资人行业

    @Column(name = "WRTY_TYPE")
    @Convert(converter = WarrantyTypeConverter.class)
    private EWarrantyType warrantyType; // 担保类型 01-本息担保, 02-本金担保, 03-资产监管, 04-无担保

    @Column(name = "LOAN_PURPOSE")
    private String loanPurpose; // 贷款用途

    @Column(name = "NOTES")
    private String notes; // 备注

    @Column(name = "APPL_USER_ID")
    private String applUserId; // 申请人

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPL_TS")
    private Date applDate;// 申请日期

    @Column(name = "APPR_USER_ID")
    private String apprUserId; // 审批人

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPR_TS")
    // 审核日期
    private Date apprDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RTNG_TS")
    // 风险评级日期
    private Date evaluateDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FRZ_TS")
    // 保证金冻结时间
    private Date freezeDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PUB_TS")
    // 发布时间
    private Date publishDate;

    @Column(name = "WRTR_ID")
    private String warrantId; // 担保人

    @Column(name = "WRTR_ID_SW")
    private String warrantIdShow; // 担保人(显示)

    @Column(name = "STATUS")
    @Convert(converter = ProductStatusConverter.class)
    private EProductStatus status; // 申请状态

    @Column(name = "CNTRCT_TMPLT_ID")
    private BigDecimal contractTemplateId; // 合同模板号

    @Column(name = "TERM_DAYS")
    private Integer termToDays; // 期限转化为天数

    @Deprecated
    @Column(name = "PROD_GRADE")
    private BigDecimal productGrage; // 产品评分

    @Deprecated
    @Column(name = "FNCR_GRADE")
    private BigDecimal financierGrage; // 融资人评分

    @Deprecated
    @Column(name = "WRTR_GRADE")
    private BigDecimal warrantGrage; // 担保人评分

    @Deprecated
    @Column(name = "TOT_GRADE")
    private BigDecimal totalGrage; // 总体评分

    @Deprecated
    @Column(name = "PROD_GRADE_DESC")
    private String productGrageComment; // 产品评分说明

    @Deprecated
    @Column(name = "FNCR_GRADE_DESC")
    private String financierGrageComment; // 融资人评分说明

    @Deprecated
    @Column(name = "WRTR_GRADE_DESC")
    private String warrantGrageComment; // 担保人评分说明

    @Deprecated
    @Column(name = "TOT_GRADE_DESC")
    private String totalGrageComment; // 总体评分说明

    @Column(name = "SERV_MRGN_RT")
    private BigDecimal origServMrgnRt; // 原融资服务合同保证金月利息单位值

    @Column(name = "SERV_MRGN_AMT")
    private BigDecimal servMrgnAmt; // 融资服务合同保证金金额

    @Column(name = "LOAN_MRGN_RT")
    private BigDecimal loanMrgnRt; // 借款履约保证金比例

    @Column(name = "WRTR_MRGN_RT")
    private BigDecimal wrtrMrgnRt; // 担保机构还款保证金比例

    @Column(name = "SERV_FNR_JNL_NO")
    private String servFnrJnlNo; // 融资服务合同保证金冻结解冻流水号

    @Column(name = "WRTR_FNR_JNL_NO")
    private String wrtrFnrJnlNo; // 担保机构还款保证金冻结解冻流水号

    @Column(name = "LIB_ID")
    private Integer libId;// 产品库Id

    @Column(name = "AVG_PKG_FLG")
    @Convert(converter = BooleanStringConverter.class)
    private Boolean avgPkgFlg; // 是否平均分包

    @Column(name = "INVS2LOAN_FLG")
    @Convert(converter = BooleanStringConverter.class)
    private Boolean invsToLoanFlag; // 是否投转贷产品
    
    @Column(name="OVRD2CMPNS_GRCE_PRD")
    private Long overdue2CmpnsGracePeriod; //逾期转代偿天数
    
    @Column(name="CMPNS_GRCE_PRD")
    private Long cmpnsGracePeriod; // 代偿违约金免交宽限期

	@Column(name = "PROD_LVL")
	@Convert(converter = ERiskLevelConverter.class)
	private ERiskLevel productLevel = ERiskLevel.NULL; // 融资项目级别

	@Column(name = "FNCR_LVL")
	private String financierLevel; // 融资会员信用积分

	@Column(name = "WRTR_LVL")
	private String warrantorLevel; // 担保机构信用积分
	
    @ManyToOne
    @JoinColumn(name = "WRTR_ID_SW", insertable = false, updatable = false)
    private ProductProviderInfo productProviderInfo;

    @ManyToOne(optional = true)
    @JoinColumn(name = "WRTR_ID_SW", insertable = false, updatable = false)
    private UserPo userPoWrtrShow;

    @ManyToOne(optional = true)
    @JoinColumn(name = "APPL_USER_ID", insertable = false, updatable = false)
    private UserPo userPo;

    @ManyToOne
    @JoinColumn(name = "CNTRCT_TMPLT_ID", insertable = false, updatable = false)
    private ProductContractTemplate productContractTemplate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "APPL_USER_ID", insertable = false, updatable = false)
    private UserPo user;

    @ManyToOne
    @JoinColumn(name = "LIB_ID", insertable = false, updatable = false)
    private ProductLib productLib;

    @OneToMany(mappedBy = "product")
    private List<ProductPackage> productPackages; // 产品包

    /**
     * 反担保情况
     */
    // 房产抵押
    @Transient
    private List<MortgageResidential> productMortgageResidentialList;

    // 车辆抵押
    @Transient
    private List<ProductMortgageVehicle> productMortgageVehicleList;

    // 动产质押
    @Transient
    private List<ProductPledge> productPledgeList;

    // 保证人
    @Transient
    private List<ProductWarrantEnterprise> productWarrantEnterpriseList;

    // 保证法人
    @Transient
    private List<ProductWarrantPerson> productWarrantPersonList;

    // 资产
    @Transient
    private List<ProductAsset> productAssetList;

    @Transient
    private List<ProductAttachment> productAttachmentList;

    @Transient
    private List<ProductDebt> productDebtList;

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
     * @return return the value of the var productDebtList
     */

    public List<ProductDebt> getProductDebtList() {
        return productDebtList;
    }

    /**
     * @param productDebtList
     *            Set productDebtList value
     */

    public void setProductDebtList(List<ProductDebt> productDebtList) {
        this.productDebtList = productDebtList;
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
     * @return return the value of the var unitAmount
     */

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    /**
     * @param unitAmount
     *            Set unitAmount value
     */

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
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
        this.calTermToDays();// call this method in 2 places in case one of termType and termLength is not initialized.
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
        this.calTermToDays();// call this method in 2 places in case one of termType and termLength is not initialized.
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
     * @return return the value of the var financierIndustryType
     */

    public String getFinancierIndustryType() {
        return financierIndustryType;
    }

    /**
     * @param financierIndustryType
     *            Set financierIndustryType value
     */

    public void setFinancierIndustryType(String financierIndustryType) {
        this.financierIndustryType = financierIndustryType;
    }

    /**
     * @return return the value of the var warrantyType
     */
    public EWarrantyType getWarrantyType() {
        return warrantyType;
    }

    /**
     * 
     * @param warrantyType
     */
    public void setWarrantyType(EWarrantyType warrantyType) {
        this.warrantyType = warrantyType;
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
     * @return return the value of the var notes
     */

    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     *            Set notes value
     */

    public void setNotes(String notes) {
        this.notes = notes;
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
     * @return return the value of the var applDate
     */

    public Date getApplDate() {
        return applDate;
    }

    /**
     * @param applDate
     *            Set applDate value
     */

    public void setApplDate(Date applDate) {
        this.applDate = applDate;
    }

    /**
     * @return return the value of the var apprUserId
     */

    public String getApprUserId() {
        return apprUserId;
    }

    /**
     * @param apprUserId
     *            Set apprUserId value
     */

    public void setApprUserId(String apprUserId) {
        this.apprUserId = apprUserId;
    }

    /**
     * @return return the value of the var apprDate
     */

    public Date getApprDate() {
        return apprDate;
    }

    /**
     * @param apprDate
     *            Set apprDate value
     */

    public void setApprDate(Date apprDate) {
        this.apprDate = apprDate;
    }

    public Date getFreezeDate() {
        return freezeDate;
    }

    public Date getEvaluateDate() {
        return evaluateDate;
    }

    public void setEvaluateDate(Date evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

    public void setFreezeDate(Date freezeDate) {
        this.freezeDate = freezeDate;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
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
     * @return return the value of the var contractTemplateId
     */

    public BigDecimal getContractTemplateId() {
        return contractTemplateId;
    }

    /**
     * @param contractTemplateId
     *            Set contractTemplateId value
     */

    public void setContractTemplateId(BigDecimal contractTemplateId) {
        this.contractTemplateId = contractTemplateId;
    }

    public Integer getTermToDays() {
        return termToDays;
    }

    public BigDecimal getProductGrage() {
        return productGrage;
    }

    public void setProductGrage(BigDecimal productGrage) {
        this.productGrage = productGrage;
    }

    public BigDecimal getFinancierGrage() {
        return financierGrage;
    }

    public void setFinancierGrage(BigDecimal financierGrage) {
        this.financierGrage = financierGrage;
    }

    public BigDecimal getWarrantGrage() {
        return warrantGrage;
    }

    public void setWarrantGrage(BigDecimal warrantGrage) {
        this.warrantGrage = warrantGrage;
    }

    public BigDecimal getTotalGrage() {
        return totalGrage;
    }

    public void setTotalGrage(BigDecimal totalGrage) {
        this.totalGrage = totalGrage;
    }

    public ProductProviderInfo getProductProviderInfo() {
        return productProviderInfo;
    }

    public void setProductProviderInfo(ProductProviderInfo productProviderInfo) {
        this.productProviderInfo = productProviderInfo;
    }

    private void calTermToDays() {
        if (this.termType == ETermType.DAY) {
            this.termToDays = this.termLength;
        } else if (this.termType == ETermType.MONTH) {
            this.termToDays = this.termLength * 30;
        } else if (this.termType == ETermType.YEAR) {
            this.termToDays = this.termLength * 12 * 30;
        }
    }

    /**
     * @return return the value of the var productMortgageResidentialList
     */

    public List<MortgageResidential> getProductMortgageResidentialList() {
        return productMortgageResidentialList;
    }

    /**
     * @param productMortgageResidentialList
     *            Set productMortgageResidentialList value
     */

    public void setProductMortgageResidentialList(List<MortgageResidential> productMortgageResidentialList) {
        this.productMortgageResidentialList = productMortgageResidentialList;
    }

    /**
     * @return return the value of the var productMortgageVehicleList
     */

    public List<ProductMortgageVehicle> getProductMortgageVehicleList() {
        return productMortgageVehicleList;
    }

    /**
     * @param productMortgageVehicleList
     *            Set productMortgageVehicleList value
     */

    public void setProductMortgageVehicleList(List<ProductMortgageVehicle> productMortgageVehicleList) {
        this.productMortgageVehicleList = productMortgageVehicleList;
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
     * @return return the value of the var productPledgeList
     */

    public List<ProductPledge> getProductPledgeList() {
        return productPledgeList;
    }

    /**
     * @param productPledgeList
     *            Set productPledgeList value
     */

    public void setProductPledgeList(List<ProductPledge> productPledgeList) {
        this.productPledgeList = productPledgeList;
    }

    /**
     * @return return the value of the var productAttachmentList
     */

    public List<ProductAttachment> getProductAttachmentList() {
        return productAttachmentList;
    }

    /**
     * @param productAttachmentList
     *            Set productAttachmentList value
     */

    public void setProductAttachmentList(List<ProductAttachment> productAttachmentList) {
        this.productAttachmentList = productAttachmentList;
    }

    /**
     * @param termToDays
     *            Set termToDays value
     */

    public void setTermToDays(Integer termToDays) {
        this.termToDays = termToDays;
    }

    /**
     * @return return the value of the var productContractTemplate
     */

    public ProductContractTemplate getProductContractTemplate() {
        return productContractTemplate;
    }

    /**
     * @param productContractTemplate
     *            Set productContractTemplate value
     */

    public void setProductContractTemplate(ProductContractTemplate productContractTemplate) {
        this.productContractTemplate = productContractTemplate;
    }

    /**
     * @return return the value of the var productWarrantEnterpriseList
     */

    public List<ProductWarrantEnterprise> getProductWarrantEnterpriseList() {
        return productWarrantEnterpriseList;
    }

    /**
     * @param productWarrantEnterpriseList
     *            Set productWarrantEnterpriseList value
     */

    public void setProductWarrantEnterpriseList(List<ProductWarrantEnterprise> productWarrantEnterpriseList) {
        this.productWarrantEnterpriseList = productWarrantEnterpriseList;
    }

    /**
     * @return return the value of the var productWarrantPersonList
     */

    public List<ProductWarrantPerson> getProductWarrantPersonList() {
        return productWarrantPersonList;
    }

    /**
     * @param productWarrantPersonList
     *            Set productWarrantPersonList value
     */

    public void setProductWarrantPersonList(List<ProductWarrantPerson> productWarrantPersonList) {
        this.productWarrantPersonList = productWarrantPersonList;
    }

    /**
     * @return return the value of the var productAssetList
     */

    public List<ProductAsset> getProductAssetList() {
        return productAssetList;
    }

    /**
     * @param productAssetList
     *            Set productAssetList value
     */

    public void setProductAssetList(List<ProductAsset> productAssetList) {
        this.productAssetList = productAssetList;
    }

    /**
     * @return return the value of the var user
     */

    public UserPo getUser() {
        return user;
    }

    /**
     * @param user
     *            Set user value
     */

    public void setUser(UserPo user) {
        this.user = user;
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

    public List<ProductPackage> getProductPackages() {
        return productPackages;
    }

    public void setProductPackages(List<ProductPackage> productPackages) {
        this.productPackages = productPackages;
    }

    public BigDecimal getServMrgnAmt() {
        return servMrgnAmt;
    }

    public void setServMrgnAmt(BigDecimal servMrgnAmt) {
        this.servMrgnAmt = servMrgnAmt;
    }

    public BigDecimal getLoanMrgnRt() {
        return loanMrgnRt;
    }

    public void setLoanMrgnRt(BigDecimal loanMrgnRt) {
        this.loanMrgnRt = loanMrgnRt;
    }

    public BigDecimal getWrtrMrgnRt() {
        return wrtrMrgnRt;
    }

    public void setWrtrMrgnRt(BigDecimal wrtrMrgnRt) {
        this.wrtrMrgnRt = wrtrMrgnRt;
    }

    public String getServFnrJnlNo() {
        return servFnrJnlNo;
    }

    public void setServFnrJnlNo(String servFnrJnlNo) {
        this.servFnrJnlNo = servFnrJnlNo;
    }

    public String getWrtrFnrJnlNo() {
        return wrtrFnrJnlNo;
    }

    public void setWrtrFnrJnlNo(String wrtrFnrJnlNo) {
        this.wrtrFnrJnlNo = wrtrFnrJnlNo;
    }

    public Boolean getAvgPkgFlg() {
        return avgPkgFlg;
    }

    public void setAvgPkgFlg(Boolean avgPkgFlg) {
        this.avgPkgFlg = avgPkgFlg;
    }

    public UserPo getUserPo() {
        return userPo;
    }

    public void setUserPo(UserPo userPo) {
        this.userPo = userPo;
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

    public Boolean getInvsToLoanFlag() {
        return invsToLoanFlag;
    }

    public void setInvsToLoanFlag(Boolean invsToLoanFlag) {
        this.invsToLoanFlag = invsToLoanFlag;
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
     * @return return the value of the var productLib
     */

    public ProductLib getProductLib() {
        return productLib;
    }

    /**
     * @param productLib
     *            Set productLib value
     */

    public void setProductLib(ProductLib productLib) {
        this.productLib = productLib;
    }

	public BigDecimal getOrigServMrgnRt() {
		return origServMrgnRt;
	}

	public void setOrigServMrgnRt(BigDecimal origServMrgnRt) {
		this.origServMrgnRt = origServMrgnRt;
	}

	public Long getOverdue2CmpnsGracePeriod() {
		return overdue2CmpnsGracePeriod;
	}

	public void setOverdue2CmpnsGracePeriod(Long overdue2CmpnsGracePeriod) {
		this.overdue2CmpnsGracePeriod = overdue2CmpnsGracePeriod;
	}

	public Long getCmpnsGracePeriod() {
		return cmpnsGracePeriod;
	}

	public void setCmpnsGracePeriod(Long cmpnsGracePeriod) {
		this.cmpnsGracePeriod = cmpnsGracePeriod;
	}

	public String getWarrantIdShow() {
		return warrantIdShow;
	}

	public void setWarrantIdShow(String warrantIdShow) {
		this.warrantIdShow = warrantIdShow;
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

	public UserPo getUserPoWrtrShow() {
		return userPoWrtrShow;
	}

	public void setUserPoWrtrShow(UserPo userPoWrtrShow) {
		this.userPoWrtrShow = userPoWrtrShow;
	}

	/**
	 * @return the wrtrCreditFile
	 */
//	public String getWrtrCreditFile() {
//		return wrtrCreditFile;
//	}

	/**
	 * @param wrtrCreditFile the wrtrCreditFile to set
	 */
//	public void setWrtrCreditFile(String wrtrCreditFile) {
//		this.wrtrCreditFile = wrtrCreditFile;
//	}

}
