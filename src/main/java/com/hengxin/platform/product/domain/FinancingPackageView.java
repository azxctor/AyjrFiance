/*
 * Project Name: kmfex-platform
 * File Name: BaseDo.java
 * Class Name: BaseDo
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
package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.product.domain.converter.ERiskLevelConverter;
import com.hengxin.platform.product.domain.converter.PackageStatusConverter;
import com.hengxin.platform.product.domain.converter.PayMethodConverter;
import com.hengxin.platform.product.domain.converter.WarrantyTypeConverter;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * 
 * @author yingchangwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_PKG_SEARCH")
public class FinancingPackageView implements Serializable {

    @Id
    @Column(name = "PKG_ID")
    private String id;// 融资包编号

    @Column(name = "PROD_ID")
    private String productId;

    @Column(name = "PKG_NAME")
    private String packageName;// 融资包简称

    @Column(name = "PKG_QUOTA")
    private BigDecimal packageQuota;// 融资包金额

    @Column(name = "UNIT")
    private Integer unit;

    @Column(name = "UNIT_AMT")
    private BigDecimal unitAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUBS_START_TS")
    private Date supplyStartTime;// 申购起始时间

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUBS_END_TS")
    private Date supplyEndTime;// 申购截止时间

    @Column(name = "SUBS_AMT")
    private BigDecimal supplyAmount;

    @Column(name = "SUBS_USER_CT")
    private BigDecimal supplyUserCount;// 已申购人数

    @Column(name = "STATUS")
    @Convert(converter = PackageStatusConverter.class)
    private EPackageStatus status;

    @Column(name = "AIP_FLG")
    private String aipFlag;// 是否定投

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AIP_START_TS")
    private Date aipStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AIP_END_TS")
    private Date aipEndTime;

    @Column(name = "CREATE_OPID")
    private String createOperatorId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRE_PUB_TS")
    private Date prePublicTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SIGNING_TS")
    private Date signContractTime;// 签约时间

    @Column(name = "AUTO_SUBS_FLG")
    private String autoSubsFlag;// 是否自动申购

    @Column(name = "WRTR_ID")
    private String watrid;
    
    @Column(name = "WRTR_ID_SW")
    private String watridSw;

    @Column(name = "PAY_METHOD")
    @Convert(converter = PayMethodConverter.class)
    private EPayMethodType payMethod;// 还款方式

    @Column(name = "DURATION")
    private String duration;// 融资期限

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DURATION_DATE")
    private Date durationDate;

    @Column(name = "SUBS_PERCENT")
    private String subsPercent;// 申购进度

    @Column(name = " FINANCIER_ID")
    private String financierId;// 融资人

    @Column(name = "FINANCIER_NAME")
    private String financierName;// 融资人名字

    @Column(name = "WRTR_NAME")
    private String wrtrName;// 担保机构
    
    @Column(name = "WRTR_NAME_SW")
    private String wrtrNameShow;// 担保机构

    @Column(name = "RATE")
    private BigDecimal rate;// 年利率

    @Column(name = "WRTY_TYPE")
    @Convert(converter = WarrantyTypeConverter.class)
    private EWarrantyType warrantyType;// 风险保障

    @Column(name = "LOAN_PURPOSE")
    private String loanPurpose;// 用途

    @Column(name = "TOT_GRADE")
    private BigDecimal totalGrade;// 总体评分（风险等级）

    @Column(name = "CNTRCT_TMPLT_ID")
    private String contractTemplateId;// 合同模板ID

    @Column(name = "ACCOUNT_NO")
    private String accountNo;

    @Column(name = "PROD_NAME")
    private String productName;

    @Column(name = "APPR_TS")
    private Date loanApprTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_SUBS_TS")
    private Date lastSubsTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SIGNING_DT")
    private Date signingDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOAN_TS")
    private Date loanTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastModifyTs;

    @Column(name = "OVRD2CMPNS_GRCE_PRD")
    private Long overdue2CmpnsDays;

    @Column(name = "CMPNS_GRCE_PRD")
    private Long cmpnsGracePriod;

    @Column(name = "LOAN_FNR_JNL_NO")
    private String loanFnrJnl;

    @Column(name = "WRTR_FNR_JNL_NO")
    private String wrtrFnrJNL;

    @Column(name = "PROD_LVL")
	@Convert(converter = ERiskLevelConverter.class)
	private ERiskLevel productLevel = ERiskLevel.NULL; // 融资项目级别

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "packageId")
    private List<PaymentSchedule> paymentScheduleList;

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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    public BigDecimal getSupplyAmount() {
        return supplyAmount;
    }

    public void setSupplyAmount(BigDecimal supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    public BigDecimal getSupplyUserCount() {
        return supplyUserCount;
    }

    public void setSupplyUserCount(BigDecimal supplyUserCount) {
        this.supplyUserCount = supplyUserCount;
    }

    public EPackageStatus getStatus() {
        return status;
    }

    public void setStatus(EPackageStatus status) {
        this.status = status;
    }

    public String getAipFlag() {
        return aipFlag;
    }

    public void setAipFlag(String aipFlag) {
        this.aipFlag = aipFlag;
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

    public String getAutoSubsFlag() {
        return autoSubsFlag;
    }

    public void setAutoSubsFlag(String autoSubsFlag) {
        this.autoSubsFlag = autoSubsFlag;
    }

    public String getWatrid() {
        return watrid;
    }

    public void setWatrid(String watrid) {
        this.watrid = watrid;
    }

    public EPayMethodType getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(EPayMethodType payMethod) {
        this.payMethod = payMethod;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getDurationDate() {
        return durationDate;
    }

    public void setDurationDate(Date durationDate) {
        this.durationDate = durationDate;
    }

    public String getSubsPercent() {
        return subsPercent;
    }

    public void setSubsPercent(String subsPercent) {
        this.subsPercent = subsPercent;
    }

    public String getFinancierName() {
        return financierName;
    }

    public void setFinancierName(String financierName) {
        this.financierName = financierName;
    }

    public String getFinancierId() {
        return financierId;
    }

    public void setFinancierId(String financierId) {
        this.financierId = financierId;
    }

    public String getWrtrName() {
        return wrtrName;
    }

    public void setWrtrName(String wrtrName) {
        this.wrtrName = wrtrName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public EWarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(EWarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public BigDecimal getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(BigDecimal totalGrade) {
        this.totalGrade = totalGrade;
    }

    public String getContractTemplateId() {
        return contractTemplateId;
    }

    public void setContractTemplateId(String contractTemplateId) {
        this.contractTemplateId = contractTemplateId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public List<PaymentSchedule> getPaymentScheduleList() {
        return paymentScheduleList;
    }

    public void setPaymentScheduleList(List<PaymentSchedule> paymentScheduleList) {
        this.paymentScheduleList = paymentScheduleList;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getLastSubsTime() {
        return lastSubsTime;
    }

    public void setLastSubsTime(Date lastSubsTime) {
        this.lastSubsTime = lastSubsTime;
    }

    public Date getLoanApprTs() {
        return loanApprTs;
    }

    public void setLoanApprTs(Date loanApprTs) {
        this.loanApprTs = loanApprTs;
    }

    public Date getSigningDt() {
        return signingDt;
    }

    public void setSigningDt(Date signingDt) {
        this.signingDt = signingDt;
    }

    public Date getLoanTs() {
        return loanTs;
    }

    public void setLoanTs(Date loanTs) {
        this.loanTs = loanTs;
    }

    public Date getLastModifyTs() {
        return lastModifyTs;
    }

    public void setLastModifyTs(Date lastModifyTs) {
        this.lastModifyTs = lastModifyTs;
    }

    public Long getOverdue2CmpnsDays() {
        return overdue2CmpnsDays;
    }

    public void setOverdue2CmpnsDays(Long overdue2CmpnsDays) {
        this.overdue2CmpnsDays = overdue2CmpnsDays;
    }

    public Long getCmpnsGracePriod() {
        return cmpnsGracePriod;
    }

    public void setCmpnsGracePriod(Long cmpnsGracePriod) {
        this.cmpnsGracePriod = cmpnsGracePriod;
    }

    public String getLoanFnrJnl() {
        return loanFnrJnl;
    }

    public void setLoanFnrJnl(String loanFnrJnl) {
        this.loanFnrJnl = loanFnrJnl;
    }

    public String getWrtrFnrJNL() {
        return wrtrFnrJNL;
    }

    public void setWrtrFnrJNL(String wrtrFnrJNL) {
        this.wrtrFnrJNL = wrtrFnrJNL;
    }

	public ERiskLevel getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(ERiskLevel productLevel) {
		this.productLevel = productLevel;
	}

	public String getWatridSw() {
		return watridSw;
	}

	public void setWatridSw(String watridSw) {
		this.watridSw = watridSw;
	}

	public String getWrtrNameShow() {
		return wrtrNameShow;
	}

	public void setWrtrNameShow(String wrtrNameShow) {
		this.wrtrNameShow = wrtrNameShow;
	}

}
