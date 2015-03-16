package com.hengxin.platform.report.dto;

import java.io.Serializable;

public class TransferContractDetailsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String sellerName;

	private String buyerName;

	private String pkgId;

	private String financierName;

	private String loanContractId;

	private String lastPayDate;

	private String remainAmount;

	private String remainAmountRMB;

	private String price;

	private String priceRMB;

	private String feeRate;

	private String sellerAcctNo;

	private String buyerAcctNo;

	private String createTimestamp;

	private String signTimestamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getFinancierName() {
		return financierName;
	}

	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}

	public String getLoanContractId() {
		return loanContractId;
	}

	public void setLoanContractId(String loanContractId) {
		this.loanContractId = loanContractId;
	}

	public String getLastPayDate() {
		return lastPayDate;
	}

	public void setLastPayDate(String lastPayDate) {
		this.lastPayDate = lastPayDate;
	}

	public String getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(String remainAmount) {
		this.remainAmount = remainAmount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}

	public String getSellerAcctNo() {
		return sellerAcctNo;
	}

	public void setSellerAcctNo(String sellerAcctNo) {
		this.sellerAcctNo = sellerAcctNo;
	}

	public String getBuyerAcctNo() {
		return buyerAcctNo;
	}

	public void setBuyerAcctNo(String buyerAcctNo) {
		this.buyerAcctNo = buyerAcctNo;
	}

	public String getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getSignTimestamp() {
		return signTimestamp;
	}

	public void setSignTimestamp(String signTimestamp) {
		this.signTimestamp = signTimestamp;
	}

	public String getRemainAmountRMB() {
		return remainAmountRMB;
	}

	public void setRemainAmountRMB(String remainAmountRMB) {
		this.remainAmountRMB = remainAmountRMB;
	}

	public String getPriceRMB() {
		return priceRMB;
	}

	public void setPriceRMB(String priceRMB) {
		this.priceRMB = priceRMB;
	}

}
