package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 提现还款请求
 * @author jishen
 *
 */
public class PrePayMentReq {

	/**
	 * 买方会员编号
	 */
	private String buyerUserId;
	
	/**
	 * 融资包编号
	 */
	private String pkgId;
	
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
