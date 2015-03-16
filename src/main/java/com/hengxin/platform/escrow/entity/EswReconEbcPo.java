package com.hengxin.platform.escrow.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hengxin.platform.common.entity.BasePo;

@Entity
@SuppressWarnings("serial")
@Table(name = "AC_ESW_RECON_EBC")
public class EswReconEbcPo extends BasePo implements Serializable {

	// 商户号
	@Column(name = "ESW_CUST_ACCT_NO")
	private String merchNo;

	// 订单号
	@Id
	@Column(name = "ORD_ID")
	private String orderId;

	// 订单类型
	@Column(name = "ORD_TYPE")
	private String orderType;

	// 交易流水号
	@Column(name = "ORD_SN")
	private String orderSn;

	// 出方钱包ID
	@Column(name = "OUT_ESW_ACCT_NO")
	private String outMediumNo;

	// 出方电子账户ID
	@Column(name = "OUT_ESW_SUB_ACCT_NO")
	private String outCardNo;

	// 入方钱包ID
	@Column(name = "IN_ESW_ACCT_NO")
	private String inMediumNo;

	// 入方电子账户ID
	@Column(name = "IN_ESW_SUB_ACCT_NO")
	private String inCardNo;

	// 交易币种
	@Column(name = "CUR")
	private String currency;

	// 交易金额
	@Column(name = "AMT")
	private String amount;

	// 交易状态
	@Column(name = "STATUS")
	private String status;

	// 交易时间
	@Column(name = "TRX_DT")
	private String trxDate;

	public String getMerchNo() {
		return merchNo;
	}

	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getOutMediumNo() {
		return outMediumNo;
	}

	public void setOutMediumNo(String outMediumNo) {
		this.outMediumNo = outMediumNo;
	}

	public String getOutCardNo() {
		return outCardNo;
	}

	public void setOutCardNo(String outCardNo) {
		this.outCardNo = outCardNo;
	}

	public String getInMediumNo() {
		return inMediumNo;
	}

	public void setInMediumNo(String inMediumNo) {
		this.inMediumNo = inMediumNo;
	}

	public String getInCardNo() {
		return inCardNo;
	}

	public void setInCardNo(String inCardNo) {
		this.inCardNo = inCardNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}

}
