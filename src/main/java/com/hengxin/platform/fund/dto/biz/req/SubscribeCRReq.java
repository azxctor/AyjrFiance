package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 申购债权
 * @author jishen
 *
 */
public class SubscribeCRReq {

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
	 * 份额成本金额（份数 * 每份金额）
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
	 * 当前操作人id
	 */
	private String currOpId;

	/**
	 * 工作日期
	 */
	private Date workDate;

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

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
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
