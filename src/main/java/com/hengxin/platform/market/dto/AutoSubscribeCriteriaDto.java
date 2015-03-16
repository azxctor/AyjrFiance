package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.product.enums.ETermType;

public class AutoSubscribeCriteriaDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal minAmount = new BigDecimal(0);
	private String riskParam;
	private String payMethodParam;
	private String warrantyTypeParam;
	@Deprecated
	private Integer minDayCount = 0;
	@Deprecated
	private Integer maxDayCount = 0;
	private BigDecimal minDayRate = new BigDecimal(0);
	@Deprecated
	private Integer minMonthCount = 0;
	@Deprecated
	private Integer maxMonthCount = 0;
	private BigDecimal minMonthRate = new BigDecimal(0);
	private ETermType termType;
	private Integer termCount = 0;
	private BigDecimal totalAmount = new BigDecimal(0);
	private BigDecimal minBalance;
	private BigDecimal maxSubscribeAmount;
	private String packageId;
	private boolean isAip;
	private String applyUserId;

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
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

	public ETermType getTermType() {
		return termType;
	}

	public void setTermType(ETermType termType) {
		this.termType = termType;
	}

	public Integer getTermCount() {
		return termCount;
	}

	public void setTermCount(Integer termCount) {
		this.termCount = termCount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getMaxSubscribeAmount() {
		return maxSubscribeAmount;
	}

	public void setMaxSubscribeAmount(BigDecimal maxSubscribeAmount) {
		this.maxSubscribeAmount = maxSubscribeAmount;
	}

	public BigDecimal getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(BigDecimal minBalance) {
		this.minBalance = minBalance;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public boolean isAip() {
		return isAip;
	}

	public void setAip(boolean isAip) {
		this.isAip = isAip;
	}

	public String getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

}
