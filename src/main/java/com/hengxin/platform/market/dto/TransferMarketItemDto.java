package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransferMarketItemDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private BigDecimal quota;

	private String warrantyType;

	private String period;

	private BigDecimal rate;

	private String payMethod;

	private String warrantor;

	private Date subscribeStartTime;

	private Date subscribeEndTime;

	private Integer riskLevel;

	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getQuota() {
		return quota;
	}

	public void setQuota(BigDecimal quota) {
		this.quota = quota;
	}

	public String getWarrantyType() {
		return warrantyType;
	}

	public void setWarrantyType(String warrantyType) {
		this.warrantyType = warrantyType;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getWarrantor() {
		return warrantor;
	}

	public void setWarrantor(String warrantor) {
		this.warrantor = warrantor;
	}

	public Date getSubscribeStartTime() {
		return subscribeStartTime;
	}

	public void setSubscribeStartTime(Date subscribeStartTime) {
		this.subscribeStartTime = subscribeStartTime;
	}

	public Date getSubscribeEndTime() {
		return subscribeEndTime;
	}

	public void setSubscribeEndTime(Date subscribeEndTime) {
		this.subscribeEndTime = subscribeEndTime;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
