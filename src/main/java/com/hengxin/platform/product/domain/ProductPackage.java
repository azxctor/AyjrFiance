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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.member.domain.SubscribeGroup;
import com.hengxin.platform.product.domain.converter.AutoSubscribeConverter;
import com.hengxin.platform.product.domain.converter.BooleanStringConverter;
import com.hengxin.platform.product.domain.converter.PackageStatusConverter;
import com.hengxin.platform.product.enums.EAutoSubscribeStatus;
import com.hengxin.platform.product.enums.EPackageStatus;

/**
 * 产品包
 * 
 * @author tiexiyu
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PKG")
@EntityListeners(IdInjectionEntityListener.class)
public class ProductPackage implements Serializable {

    /**
     * 融资包编号
     */
    @Id
    @Column(name = "PKG_ID")
    private String id;

    /**
     * 产品Id
     */
    @Column(name = "PROD_ID")
    private String productId;

    /**
     * 融资包名称
     */
    @Column(name = "PKG_NAME")
    private String packageName;

    /**
     * 融资包金额
     */
    @Column(name = "PKG_QUOTA")
    private BigDecimal packageQuota;

    @Column(name = "UNIT")
    private Integer unit; // 份数

    @Column(name = "UNIT_AMT")
    private BigDecimal unitAmount; // 每份金额

    /**
     * 已申购人数
     */
    @Column(name = "SUBS_USER_CT")
    private int supplyUserCount;

    /**
     * 已申购金额
     */
    @Column(name = "SUBS_AMT")
    private BigDecimal supplyAmount;

    /**
     * 状态
     */
    @Column(name = "STATUS")
    @Convert(converter = PackageStatusConverter.class)
    private EPackageStatus status;

    /**
     * 融资服务合同保证金
     */
    @Column(name = "SERV_FNR_JNL_NO")
    private String servFnrJnlNo;

    /**
     * 借款合同履约保证金
     */
    @Column(name = "LOAN_FNR_JNL_NO")
    private String loanFnrJnlNo;

    /**
     * 签约后提现前融资人资金冻结解冻流水号
     */
    @Column(name = "FNR_JNL_NO")
    private String fnrJnlNo;

    /**
     * 担保机构还款保证金冻结解冻流水号
     */
    @Column(name = "WRTR_FNR_JNL_NO")
    private String wrtrFnrJnlNo;

    /**
	 *
	 */
    @Column(name = "CREATE_OPID")
    private String createOperatorId;

    /**
	 *
	 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTime;

    /**
	 *
	 */
    @Column(name = "LAST_MNT_OPID")
    private String lastOperatorId;

    /**
	 *
	 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastTime;

    /**
     * 满额时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_SUBS_TS")
    private Date lastSubsTime;

    /**
	 *
	 */
    @Version
    @Column(name = "VERSION_CT")
    private Long versionCount;

    /**
     * 申购开始时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUBS_START_TS")
    private Date supplyStartTime;

    /**
     * 申购结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUBS_END_TS")
    private Date supplyEndTime;

    /**
     * 定投开始时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AIP_START_TS")
    private Date aipStartTime;

    /**
     * 定投结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AIP_END_TS")
    private Date aipEndTime;

    /**
     * 放款审批通过时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPR_TS")
    private Date loanTime;

    /**
     * 是否定投,Y/N
     */
    @Column(name = "AIP_FLG")
    @Convert(converter = BooleanStringConverter.class)
    private Boolean aipFlag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PROD_ID", insertable = false, updatable = false)
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.DETACH })
    @JoinTable(name = "FP_PKG_AIP_GRP", inverseJoinColumns = @JoinColumn(name = "GRP_ID"), joinColumns = @JoinColumn(name = "PKG_ID"))
    private List<SubscribeGroup> aipGroups;

    @OneToMany(mappedBy = "productPackage", fetch = FetchType.LAZY)
    private List<PositionPo> positionPos;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRE_PUB_TS")
    private Date prePublicTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SIGNING_TS")
    private Date signContractTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "SIGNING_DT")
    private Date signingDt;

    @Column(name = "AUTO_SUBS_FLG")
    @Convert(converter = AutoSubscribeConverter.class)
    private EAutoSubscribeStatus autoSubscribeFlag;

    @Column(name = "FNCR_ID")
    private String financierId;
    
    @Transient
    private Boolean instantPublish;
    /**
     * //最终放款时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOAN_TS")
    private Date loanTs;

    @Transient
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getPackageQuota() {
        return packageQuota;
    }

    public void setPackageQuota(BigDecimal packageQuota) {
        this.packageQuota = packageQuota;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public int getSupplyUserCount() {
        return supplyUserCount;
    }

    public void setSupplyUserCount(int supplyUserCount) {
        this.supplyUserCount = supplyUserCount;
    }

    public BigDecimal getSupplyAmount() {
        return supplyAmount;
    }

    public void setSupplyAmount(BigDecimal supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    public EPackageStatus getStatus() {
        return status;
    }

    public void setStatus(EPackageStatus status) {
        this.status = status;
    }

    public String getCreateOperatorId() {
        return createOperatorId;
    }

    public void setCreateOperatorId(String createOperatorId) {
        this.createOperatorId = createOperatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLastOperatorId() {
        return lastOperatorId;
    }

    public void setLastOperatorId(String lastOperatorId) {
        this.lastOperatorId = lastOperatorId;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * @return return the value of the var lastSubsTime
     */

    public Date getLastSubsTime() {
        return lastSubsTime;
    }

    /**
     * @param lastSubsTime
     *            Set lastSubsTime value
     */

    public void setLastSubsTime(Date lastSubsTime) {
        this.lastSubsTime = lastSubsTime;
    }

    public Long getVersionCount() {
        return versionCount;
    }

    public void setVersionCount(Long versionCount) {
        this.versionCount = versionCount;
    }

    public Date getSupplyStartTime() {
        return supplyStartTime;
    }

    public void setSupplyStartTime(Date supplyStartTime) {
        this.supplyStartTime = supplyStartTime;
    }

    public Date getSupplyEndTime() {
        return supplyEndTime;
    }

    public void setSupplyEndTime(Date supplyEndTime) {
        this.supplyEndTime = supplyEndTime;
    }

    public Date getAipStartTime() {
        return aipStartTime;
    }

    public void setAipStartTime(Date aipStartTime) {
        this.aipStartTime = aipStartTime;
    }

    public Date getAipEndTime() {
        return aipEndTime;
    }

    public void setAipEndTime(Date aipEndTime) {
        this.aipEndTime = aipEndTime;
    }

    public Boolean getAipFlag() {
        return aipFlag;
    }

    public void setAipFlag(Boolean aipFlag) {
        this.aipFlag = aipFlag;
    }

    /**
     * @return return the value of the var packageName
     */

    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName
     *            Set packageName value
     */

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return return the value of the var product
     */

    public Product getProduct() {
        return product;
    }

    /**
     * @param product
     *            Set product value
     */

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return return the value of the var aipGroups
     */

    public List<SubscribeGroup> getAipGroups() {
        return aipGroups;
    }

    /**
     * @param aipGroups
     *            Set aipGroups value
     */

    public void setAipGroups(List<SubscribeGroup> aipGroups) {
        this.aipGroups = aipGroups;
    }

    public String getServFnrJnlNo() {
        return servFnrJnlNo;
    }

    public void setServFnrJnlNo(String servFnrJnlNo) {
        this.servFnrJnlNo = servFnrJnlNo;
    }

    public String getLoanFnrJnlNo() {
        return loanFnrJnlNo;
    }

    public void setLoanFnrJnlNo(String loanFnrJnlNo) {
        this.loanFnrJnlNo = loanFnrJnlNo;
    }

    public Date getPrePublicTime() {
        return prePublicTime;
    }

    public void setPrePublicTime(Date prePublicTime) {
        this.prePublicTime = prePublicTime;
    }

    public Date getSignContractTime() {
        return signContractTime;
    }

    public void setSignContractTime(Date signContractTime) {
        this.signContractTime = signContractTime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFnrJnlNo() {
        return fnrJnlNo;
    }

    public void setFnrJnlNo(String fnrJnlNo) {
        this.fnrJnlNo = fnrJnlNo;
    }

    public EAutoSubscribeStatus getAutoSubscribeFlag() {
        return autoSubscribeFlag;
    }

    public void setAutoSubscribeFlag(EAutoSubscribeStatus autoSubscribeFlag) {
        this.autoSubscribeFlag = autoSubscribeFlag;
    }

    public String getWrtrFnrJnlNo() {
        return wrtrFnrJnlNo;
    }

    public void setWrtrFnrJnlNo(String wrtrFnrJnlNo) {
        this.wrtrFnrJnlNo = wrtrFnrJnlNo;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public List<PositionPo> getPositionPos() {
        return positionPos;
    }

    public void setPositionPos(List<PositionPo> positionPos) {
        this.positionPos = positionPos;
    }

    public Date getSigningDt() {
        return signingDt;
    }

    public void setSigningDt(Date signingDt) {
        this.signingDt = signingDt;
    }

    public String getFinancierId() {
        return financierId;
    }

    public void setFinancierId(String financierId) {
        this.financierId = financierId;
    }

    public Date getLoanTs() {
        return loanTs;
    }

    public void setLoanTs(Date loanTs) {
        this.loanTs = loanTs;
    }

    public Boolean getInstantPublish() {
        return instantPublish;
    }

    public void setInstantPublish(Boolean instantPublish) {
        this.instantPublish = instantPublish;
    }

}
