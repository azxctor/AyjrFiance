package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.BaseMaintainablePo;
import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.security.entity.SimpleUserPo;

@Entity
@Table(name = "UM_USER_SUBS_PARAM")
@EntityListeners(IdInjectionEntityListener.class)
public class AutoSubscribeParam extends BaseMaintainablePo implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "USER_ID", insertable = true, updatable = false)
	private String userId;
	
	@Version
	@Column(name = "VERSION_CT")
	private Long version;
	
	@Column(name = "RISK_PARAM")
	private String riskParam;
	
	@Column(name = "MIN_DAY_CT", length = 1)
	private Integer minDateRange;
	
	@Column(name = "MAX_DAY_CT", length = 1)
	private Integer maxDateRange;
	
	/** Annual Percentage Rate. **/
	@Column(name = "MIN_DAY_RT", precision = 2)
	private BigDecimal minAPRForDate;
	
	@Column(name = "MIN_MTH_CT")
	private Integer minMonthRange;
	
	@Column(name = "MAX_MTH_CT")
	private Integer maxMonthRange;
	
	@Column(name = "MIN_MTH_RT", precision = 2)
	private BigDecimal minAPRForMonth;
	
	@Column(name = "PAY_METH_PARAM")
	private String repayment;
	
	@Column(name = "WRT_TYPE_PARAM")
	private String riskGuarantee;
	
	@Column(name = "ACTIVE_FLG", length = 1)
	private String activeFlag;
	
	@Column(name = "TERM_FLG", length = 1)
	private String terminationFlag;
	
	@Column(name = "SCORE")
	private BigDecimal score;

	@Column(name = "MIN_BAL")
	private BigDecimal minBalance;

	@Column(name = "MAX_SUBS_AMT")
	private BigDecimal maxSubscribeAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private SimpleUserPo user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private AcctPo acct;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private InvestorInfo investorInfo;

	@Override
	public String toString() {
		return "AutoSubscribeParam [id=" + id + ", userId=" + userId
				+ ", version=" + version + ", riskParam=" + riskParam
				+ ", minDateRange=" + minDateRange + ", maxDateRange="
				+ maxDateRange + ", minAPRForDate=" + minAPRForDate
				+ ", minMonthRange=" + minMonthRange + ", maxMonthRange="
				+ maxMonthRange + ", minAPRForMonth=" + minAPRForMonth
				+ ", repayment=" + repayment + ", riskGuarantee="
				+ riskGuarantee + ", activeFlag=" + activeFlag
				+ ", terminationFlag=" + terminationFlag + ", score=" + score
				+ "]";
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the riskParam
	 */
	public String getRiskParam() {
		return riskParam;
	}

	/**
	 * @param riskParam the riskParam to set
	 */
	public void setRiskParam(String riskParam) {
		this.riskParam = riskParam;
	}

	/**
	 * @return the minDateRange
	 */
	public Integer getMinDateRange() {
		return minDateRange;
	}

	/**
	 * @param minDateRange the minDateRange to set
	 */
	public void setMinDateRange(Integer minDateRange) {
		this.minDateRange = minDateRange;
	}

	/**
	 * @return the maxDateRange
	 */
	public Integer getMaxDateRange() {
		return maxDateRange;
	}

	/**
	 * @param maxDateRange the maxDateRange to set
	 */
	public void setMaxDateRange(Integer maxDateRange) {
		this.maxDateRange = maxDateRange;
	}

	/**
	 * @return the minAPRForDate
	 */
	public BigDecimal getMinAPRForDate() {
		return minAPRForDate;
	}

	/**
	 * @param minAPRForDate the minAPRForDate to set
	 */
	public void setMinAPRForDate(BigDecimal minAPRForDate) {
		this.minAPRForDate = minAPRForDate;
	}

	/**
	 * @return the minMonthRange
	 */
	public Integer getMinMonthRange() {
		return minMonthRange;
	}

	/**
	 * @param minMonthRange the minMonthRange to set
	 */
	public void setMinMonthRange(Integer minMonthRange) {
		this.minMonthRange = minMonthRange;
	}

	/**
	 * @return the maxMonthRange
	 */
	public Integer getMaxMonthRange() {
		return maxMonthRange;
	}

	/**
	 * @param maxMonthRange the maxMonthRange to set
	 */
	public void setMaxMonthRange(Integer maxMonthRange) {
		this.maxMonthRange = maxMonthRange;
	}

	/**
	 * @return the minAPRForMonth
	 */
	public BigDecimal getMinAPRForMonth() {
		return minAPRForMonth;
	}

	/**
	 * @param minAPRForMonth the minAPRForMonth to set
	 */
	public void setMinAPRForMonth(BigDecimal minAPRForMonth) {
		this.minAPRForMonth = minAPRForMonth;
	}

	/**
	 * @return the repayment
	 */
	public String getRepayment() {
		return repayment;
	}

	/**
	 * @param repayment the repayment to set
	 */
	public void setRepayment(String repayment) {
		this.repayment = repayment;
	}

	/**
	 * @return the riskGuarantee
	 */
	public String getRiskGuarantee() {
		return riskGuarantee;
	}

	/**
	 * @param riskGuarantee the riskGuarantee to set
	 */
	public void setRiskGuarantee(String riskGuarantee) {
		this.riskGuarantee = riskGuarantee;
	}

	/**
	 * @return the activeFlag
	 */
	public String getActiveFlag() {
		return activeFlag;
	}

	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	/**
	 * @return the terminationFlag
	 */
	public String getTerminationFlag() {
		return terminationFlag;
	}

	/**
	 * @param terminationFlag the terminationFlag to set
	 */
	public void setTerminationFlag(String terminationFlag) {
		this.terminationFlag = terminationFlag;
	}

	/**
	 * @return the score
	 */
	public BigDecimal getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public SimpleUserPo getUser() {
		return user;
	}

	public void setUser(SimpleUserPo user) {
		this.user = user;
	}

	public BigDecimal getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(BigDecimal minBalance) {
		this.minBalance = minBalance;
	}

	public InvestorInfo getInvestorInfo() {
		return investorInfo;
	}

	public void setInvestorInfo(InvestorInfo investorInfo) {
		this.investorInfo = investorInfo;
	}

	public BigDecimal getMaxSubscribeAmount() {
		return maxSubscribeAmount;
	}

	public void setMaxSubscribeAmount(BigDecimal maxSubscribeAmount) {
		this.maxSubscribeAmount = maxSubscribeAmount;
	}

	public AcctPo getAcct() {
		return acct;
	}

	public void setAcct(AcctPo acct) {
		this.acct = acct;
	}

}
