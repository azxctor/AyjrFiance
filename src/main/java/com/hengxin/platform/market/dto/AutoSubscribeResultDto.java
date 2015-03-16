package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AutoSubscribeResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String accountId;
	private String name;
	/** 可用余额. **/
	private BigDecimal balance;
	/** 资金账户保留额. **/
	private BigDecimal minBalance;
	/** 单笔申购最大金额. **/
	private BigDecimal maxBalance;
	/** 最小申购. **/
	private BigDecimal minAmount;
	/** 最大申购. **/
	private BigDecimal maxAmount;
	/** 成交金额. **/
	private BigDecimal dealAmount;
	private BigDecimal score;

	public AutoSubscribeResultDto() {

	}

	public AutoSubscribeResultDto(AutoSubscribeCandidateDto candidate) {
		this.userId = candidate.getUserId();
		this.accountId = candidate.getAccountId();
		this.balance = candidate.getBalance();
		this.minAmount = candidate.getMinAmount();
		this.maxAmount = candidate.getMaxAmount();
		this.score = candidate.getScore();
	}

	@Override
	public String toString() {
		return new StringBuilder("AutoSubscribeResultDto [").append("userId=")
				.append(userId).append(",accountId=").append(accountId)
				.append(",balance=").append(balance).append(",minAmount=")
				.append(minAmount).append(",maxAmount=").append(maxAmount)
				.append(",dealAmount=").append(dealAmount).append(",score=")
				.append(score).append("]").toString();
	}

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

	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
