package com.hengxin.platform.market.dto;

import java.math.BigDecimal;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.product.enums.ETermType;

/**
 * 自动申购投资人设置查询条件.
 */
public class CandidateCriteria extends DataTablesRequestDto {

	private static final long serialVersionUID = 1L;

	private String userId;

	/** 交易账号. **/
	private String accountId;
	/** 用户姓名. **/
	private String name;

	private ETermType termType;
	private BigDecimal minBalance;
	private BigDecimal maxSubscribeAmount;
	private String riskParam;
	private String payMethodParam;
	private String warrantyTypeParam;
	private Integer minDayCount = 0;
	private Integer maxDayCount = 0;
	private BigDecimal minDayRate = new BigDecimal(0);
	private Integer minMonthCount = 0;
	private Integer maxMonthCount = 0;
	private BigDecimal minMonthRate = new BigDecimal(0);
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

	public BigDecimal getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(BigDecimal minBalance) {
		this.minBalance = minBalance;
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

	public ETermType getTermType() {
		return termType;
	}

	public void setTermType(ETermType termType) {
		this.termType = termType;
	}

	public BigDecimal getMaxSubscribeAmount() {
		return maxSubscribeAmount;
	}

	public void setMaxSubscribeAmount(BigDecimal maxSubscribeAmount) {
		this.maxSubscribeAmount = maxSubscribeAmount;
	}

}
