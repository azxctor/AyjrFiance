package com.hengxin.platform.market.dto.upstream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransferMarketPurchaseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private BigDecimal amount;

    private BigDecimal fee;
    
    private String sellerId;
    
    private String buyerId;
    
    private String pkgId;
    
    private String financierId;
    
    private String contractId;
    
    private BigDecimal accumCrAmt;
    
    private Date workDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

    /**
     * @return return the value of the var fee
     */

    public BigDecimal getFee() {
        return fee;
    }

    /**
     * @param fee
     *            Set fee value
     */

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getFinancierId() {
		return financierId;
	}

	public void setFinancierId(String financierId) {
		this.financierId = financierId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public BigDecimal getAccumCrAmt() {
		return accumCrAmt;
	}

	public void setAccumCrAmt(BigDecimal accumCrAmt) {
		this.accumCrAmt = accumCrAmt;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

}
