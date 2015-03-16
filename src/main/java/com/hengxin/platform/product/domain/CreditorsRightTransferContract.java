package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_CR_TSFR_CNTRCT")
@EntityListeners(IdInjectionEntityListener.class)
public class CreditorsRightTransferContract implements Serializable {

	@Id
	@Column(name = "TSFR_CNTRCT_ID")
	private String id;

	@Column(name = "SELLER_ID")
	private String sellerId;

	@Column(name = "BUYER_ID")
	private String buyerId;

	@Column(name = "PKG_ID")
	private String packageId;

	@Temporal(TemporalType.DATE)
	@Column(name = "TRD_DT")
	private Date tradeDate;

	@Column(name = "TRD_PRICE")
	private BigDecimal tradePrice;

	@Column(name = "CR_ID")
	private String creditorsRightId;

	@Column(name = "CR_REM_AMT")
	private BigDecimal crRemainAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "CR_MATURITY_DT")
	private Date crMaturityDate;

	@Column(name = "LOAN_CNTRCT_ID")
	private String loanContractId;

	@Column(name = "SELLER_FEE_RT")
	private BigDecimal sellerFeeRate;

	@Column(name = "BUYER_FEE_RT")
	private BigDecimal buyerFeeRate;

	@Column(name = "SELLER_LOT_ID")
	private String sellerLotId;

	@Column(name = "BUYER_LOT_ID")
	private String buyerLotId;

	@Column(name = "PKG_TRD_JNL_NO")
	private String packageTradeJnlNo;

	@Column(name = "CREATE_OPID")
	private String createOpid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public BigDecimal getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}

	public String getCreditorsRightId() {
		return creditorsRightId;
	}

	public void setCreditorsRightId(String creditorsRightId) {
		this.creditorsRightId = creditorsRightId;
	}

	public BigDecimal getCrRemainAmount() {
		return crRemainAmount;
	}

	public void setCrRemainAmount(BigDecimal crRemainAmount) {
		this.crRemainAmount = crRemainAmount;
	}

	public Date getCrMaturityDate() {
		return crMaturityDate;
	}

	public void setCrMaturityDate(Date crMaturityDate) {
		this.crMaturityDate = crMaturityDate;
	}

	public String getLoanContractId() {
		return loanContractId;
	}

	public void setLoanContractId(String loanContractId) {
		this.loanContractId = loanContractId;
	}

	public BigDecimal getSellerFeeRate() {
		return sellerFeeRate;
	}

	public void setSellerFeeRate(BigDecimal sellerFeeRate) {
		this.sellerFeeRate = sellerFeeRate;
	}

	public BigDecimal getBuyerFeeRate() {
		return buyerFeeRate;
	}

	public void setBuyerFeeRate(BigDecimal buyerFeeRate) {
		this.buyerFeeRate = buyerFeeRate;
	}

	public String getSellerLotId() {
		return sellerLotId;
	}

	public void setSellerLotId(String sellerLotId) {
		this.sellerLotId = sellerLotId;
	}

	public String getBuyerLotId() {
		return buyerLotId;
	}

	public void setBuyerLotId(String buyerLotId) {
		this.buyerLotId = buyerLotId;
	}

	public String getPackageTradeJnlNo() {
		return packageTradeJnlNo;
	}

	public void setPackageTradeJnlNo(String packageTradeJnlNo) {
		this.packageTradeJnlNo = packageTradeJnlNo;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

}
