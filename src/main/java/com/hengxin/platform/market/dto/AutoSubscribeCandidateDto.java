package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AutoSubscribeCandidateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String accountId;
	/** 用户姓名. **/
	private String name;
	/** 可用余额. **/
	private BigDecimal balance;
	/** 资金账户保留额. **/
	private BigDecimal minBalance;
	/** 单笔申购最大金额. **/
	private BigDecimal maxBalance;
	/** 成交金额 . **/
	private BigDecimal dealAmount;
	/** 最小申购. **/
	private BigDecimal minAmount;
	/** 最大申购. **/
	private BigDecimal maxAmount;
	private String riskParam;
	private String payMethodParam;
	private String warrantyTypeParam;
	private Integer minDayCount;
	private Integer maxDayCount;
	private BigDecimal minDayRate;
	private Integer minMonthCount;
	private Integer maxMonthCount;
	private BigDecimal minMonthRate;
	private BigDecimal score;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getRiskParam() {
		return riskParam;
	}

	public void setRiskParam(String riskParam) {
		this.riskParam = riskParam;
	}

	public String getPayMethodParam() {
		return payMethodParam;
	}

	public void setPayMethodParam(String payMethodParam) {
		this.payMethodParam = payMethodParam;
	}

	public String getWarrantyTypeParam() {
		return warrantyTypeParam;
	}

	public void setWarrantyTypeParam(String warrantyTypeParam) {
		this.warrantyTypeParam = warrantyTypeParam;
	}

	public Integer getMinDayCount() {
		return minDayCount;
	}

	public void setMinDayCount(Integer minDayCount) {
		this.minDayCount = minDayCount;
	}

	public Integer getMaxDayCount() {
		return maxDayCount;
	}

	public void setMaxDayCount(Integer maxDayCount) {
		this.maxDayCount = maxDayCount;
	}

	public BigDecimal getMinDayRate() {
		return minDayRate;
	}

	public void setMinDayRate(BigDecimal minDayRate) {
		this.minDayRate = minDayRate;
	}

	public Integer getMinMonthCount() {
		return minMonthCount;
	}

	public void setMinMonthCount(Integer minMonthCount) {
		this.minMonthCount = minMonthCount;
	}

	public Integer getMaxMonthCount() {
		return maxMonthCount;
	}

	public void setMaxMonthCount(Integer maxMonthCount) {
		this.maxMonthCount = maxMonthCount;
	}

	public BigDecimal getMinMonthRate() {
		return minMonthRate;
	}

	public void setMinMonthRate(BigDecimal minMonthRate) {
		this.minMonthRate = minMonthRate;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public BigDecimal getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(BigDecimal minBalance) {
		this.minBalance = minBalance;
	}

	public BigDecimal getMaxBalance() {
		return maxBalance;
	}

	public void setMaxBalance(BigDecimal maxBalance) {
		this.maxBalance = maxBalance;
	}

	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
	}

}
