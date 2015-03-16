package com.hengxin.platform.escrow.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.escrow.dto.CommandRequest;

/**
 * 充值请求
 * 
 * @author juhuahuang
 *
 */
public class EswRechargeRequestDto extends CommandRequest implements
		Serializable {

	private static final long serialVersionUID = 1L;

	// 系统流水号
	private String ordersn;
	// 商户号
	private String merchno;
	// P2P的订单号
	private String dsorderid;
	// 用户id
	private String userno;
	// 钱包介质id
	private String mediumno;
	// 运营商
	private String ownerid;
	// 卡号
	private String cardno;
	// 币种
	private String currency;
	// 金额
	private BigDecimal amount;
	// ebc发卡行
	private String ebcbankid;
	// 用户类型
	private String usertype;
	// 页面跳转地址
	private String dsyburl;
	// 页面跳转地址
	private String dstburl;
	// 订单类型
	private String ordertype;
	// 校验值
	private String dstbdatasign;

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}

	public String getMerchno() {
		return merchno;
	}

	public void setMerchno(String merchno) {
		this.merchno = merchno;
	}

	public String getDsorderid() {
		return dsorderid;
	}

	public void setDsorderid(String dsorderid) {
		this.dsorderid = dsorderid;
	}

	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	public String getMediumno() {
		return mediumno;
	}

	public void setMediumno(String mediumno) {
		this.mediumno = mediumno;
	}

	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getEbcbankid() {
		return ebcbankid;
	}

	public void setEbcbankid(String ebcbankid) {
		this.ebcbankid = ebcbankid;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getDsyburl() {
		return dsyburl;
	}

	public void setDsyburl(String dsyburl) {
		this.dsyburl = dsyburl;
	}

	public String getDstburl() {
		return dstburl;
	}

	public void setDstburl(String dstburl) {
		this.dstburl = dstburl;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public String getDstbdatasign() {
		return dstbdatasign;
	}

	public void setDstbdatasign(String dstbdatasign) {
		this.dstbdatasign = dstbdatasign;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
