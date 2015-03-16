package com.hengxin.platform.risk.dto;

import java.math.BigDecimal;

import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.risk.enums.ERiskBearLevel;

public class PurchaseRiskResultDto {
	
	private String userId;
	
	private boolean qualified = true;
	
	private String message;
	
	/** 可用余额. **/
	private BigDecimal balance = BigDecimal.ZERO;

	/** 申购上限. **/
	private BigDecimal upperLimit = BigDecimal.ZERO;

	/** 申购下限. **/
	private BigDecimal lowerLimit = BigDecimal.ZERO;

	/** 单个融资包已经购买金额. **/
	private BigDecimal subscribeAmount = BigDecimal.ZERO;

	/** 同一风险等级项目累计持有金额. **/
	private BigDecimal accumulativeAmount = BigDecimal.ZERO;

	/** 同一风险等级项目持有 上限. **/
	private BigDecimal upperLimitForSameCategory = BigDecimal.ZERO;

	/** 同一封建等级项目对应的申购上限比例. **/
	private BigDecimal rate = BigDecimal.ZERO;
	
	/** 最大可申购金额. **/
    private BigDecimal maxSubscribeAmount = BigDecimal.ZERO;
	
	private ERiskBearLevel userLevel;
	
	private ERiskLevel projectLevel;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BigDecimal getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(BigDecimal upperLimit) {
		this.upperLimit = upperLimit;
	}

	public BigDecimal getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public BigDecimal getSubscribeAmount() {
		return subscribeAmount;
	}

	public void setSubscribeAmount(BigDecimal subscribeAmount) {
		this.subscribeAmount = subscribeAmount;
	}

	public BigDecimal getAccumulativeAmount() {
		return accumulativeAmount;
	}

	public void setAccumulativeAmount(BigDecimal accumulativeAmount) {
		this.accumulativeAmount = accumulativeAmount;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public ERiskBearLevel getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(ERiskBearLevel userLevel) {
		this.userLevel = userLevel;
	}

	public ERiskLevel getProjectLevel() {
		return projectLevel;
	}

	public void setProjectLevel(ERiskLevel projectLevel) {
		this.projectLevel = projectLevel;
	}

	public boolean isQualified() {
		return qualified;
	}

	public void setQualified(boolean qualified) {
		this.qualified = qualified;
	}

	public BigDecimal getMaxSubscribeAmount() {
		return maxSubscribeAmount;
	}

	public void setMaxSubscribeAmount(BigDecimal maxSubscribeAmount) {
		this.maxSubscribeAmount = maxSubscribeAmount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getUpperLimitForSameCategory() {
		return upperLimitForSameCategory;
	}

	public void setUpperLimitForSameCategory(BigDecimal upperLimitForSameCategory) {
		this.upperLimitForSameCategory = upperLimitForSameCategory;
	}

}
