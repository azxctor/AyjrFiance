package com.hengxin.platform.fund.dto.biz.req.atom;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundTrdType;

public class PositionChangeReq {

	/**
	 * 买方会员编号
	 */
	private String buyerUserId;

	/**
	 * 卖方会员编号
	 */
	private String sellerUserId;

	/**
	 * 融资包编号
	 */
	private String pkgId;

	/**
	 * 份数
	 */
	private Integer unit;

	/**
	 * 每份金额(票面金额)
	 */
	private BigDecimal unitFaceValue;

	/**
	 * 份额成本金额
	 */
	private BigDecimal lotBuyPrice;

	/**
	 * 申购日期
	 */
	private Date subsDt;

	/**
	 * 交割日期
	 */
	private Date settleDt;

	/**
	 * 交易类型
	 */
	private EFundTrdType trdType;

	/**
	 * 交易备注
	 */
	private String trdMemo;

	/**
	 * 份额ID
	 */
	private String lotId;

	/**
	 * 还款金额
	 */
	private BigDecimal paymentAmt;

	/**
	 * 当前操作人id
	 */
	private String currOpId;

	private Date workDate;

	@Override
	public String toString() {
		return "Person [buyerUserId=" + buyerUserId + ", sellerUserId="
				+ sellerUserId + ", pkgId=" + pkgId + ", unit=" + unit
				+ ", unitFaceValue=" + unitFaceValue + ", lotBuyPrice="
				+ lotBuyPrice + ", subsDt=" + subsDt + ", settleDt=" + settleDt
				+ ", trdType=" + trdType + ", trdMemo="
				+ trdMemo + ", lotId=" + lotId + ", paymentAmt=" + paymentAmt
				+ ", currOpId=" + currOpId + "]";
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public Date getSubsDt() {
		return subsDt;
	}

	public void setSubsDt(Date subsDt) {
		this.subsDt = subsDt;
	}

	public Date getSettleDt() {
		return settleDt;
	}

	public void setSettleDt(Date settleDt) {
		this.settleDt = settleDt;
	}

	public BigDecimal getUnitFaceValue() {
		return unitFaceValue;
	}

	public void setUnitFaceValue(BigDecimal unitFaceValue) {
		this.unitFaceValue = unitFaceValue;
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

	public String getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public String getBuyerUserId() {
		return buyerUserId;
	}

	public void setBuyerUserId(String buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public String getTrdMemo() {
		return trdMemo;
	}

	public void setTrdMemo(String trdMemo) {
		this.trdMemo = trdMemo;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public BigDecimal getPaymentAmt() {
		return paymentAmt;
	}

	public void setPaymentAmt(BigDecimal paymentAmt) {
		this.paymentAmt = paymentAmt;
	}

	public String getCurrOpId() {
		return currOpId;
	}

	public void setCurrOpId(String currOpId) {
		this.currOpId = currOpId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
}
