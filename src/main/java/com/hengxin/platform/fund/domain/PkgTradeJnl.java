package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundTrdType;

/**
 * 
 *  Class Name: AcExchJnl
 *  
 * @author liudc
 *
 */
@SuppressWarnings("serial")
public class PkgTradeJnl implements Serializable{
	
	private String jnlNo;
	
	private String buyerUserId;
	
	private String sellerUserId;
	
	private String pkgId;
	
	private String lotId;
	
	private Integer unit;

	private BigDecimal lotBuyPrice;
	
	private EFundTrdType trdType;
	
	private Date trdDt;
	
	private String trdMemo;
	
	private String createOpid;
	
	private Date createTs;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getBuyerUserId() {
		return buyerUserId;
	}

	public void setBuyerUserId(String buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public String getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
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

	public BigDecimal getLotBuyPrice() {
		return lotBuyPrice;
	}

	public void setLotBuyPrice(BigDecimal lotBuyPrice) {
		this.lotBuyPrice = lotBuyPrice;
	}

	public EFundTrdType getTrdType() {
		return trdType;
	}

	public void setTrdType(EFundTrdType trdType) {
		this.trdType = trdType;
	}

	public Date getTrdDt() {
		return trdDt;
	}

	public void setTrdDt(Date trdDt) {
		this.trdDt = trdDt;
	}

	public String getTrdMemo() {
		return trdMemo;
	}

	public void setTrdMemo(String trdMemo) {
		this.trdMemo = trdMemo;
	}



}
