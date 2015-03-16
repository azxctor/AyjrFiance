package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权转让请求
 * @author jishen
 *
 */
public class TransferCRReq {

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
	 * 转让债权的份额ID
	 */
	private String lotId;
	
	/**
	 * 工作日期
	 */
	private Date workDate;
	
	/**
	 * 份额成本金额（uint * 每份转让价格）
	 */
	private BigDecimal lotBuyPrice;
	
	/**
	 * 申购日期
	 */
	private Date subsDt;
	
	/**
	 * 备注
	 */
	private String trdMemo;
	
	/**
	 * 当前操作人id
	 */
	private String currOpId;

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

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
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

	public String getTrdMemo() {
		return trdMemo;
	}

	public void setTrdMemo(String trdMemo) {
		this.trdMemo = trdMemo;
	}

	public String getCurrOpId() {
		return currOpId;
	}

	public void setCurrOpId(String currOpId) {
		this.currOpId = currOpId;
	}
}
