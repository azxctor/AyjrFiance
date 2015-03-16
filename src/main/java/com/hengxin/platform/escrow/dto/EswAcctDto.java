package com.hengxin.platform.escrow.dto;

import java.io.Serializable;
import java.util.Date;

public class EswAcctDto implements Serializable {

	private static final long serialVersionUID = 1L;
	// 会员综合账户
	private String acctNo;
	// 会员编号
	private String userId;
	// 托管商户账号
	private String eswCustAcctNo;
	// 托管会员账号
	private String eswAcctNo;
	// 托管子账户编号
	private String eswSubAcctNo;
	// 托管会员编号
	private String eswUserNo;
	// 用户类型
	private String eswUserType;
	// 状态
	private String status;
	// 银行卡号
	private String bankCardNo;
	// 持卡人姓名
	private String bankCardName;
	// 银行卡资产编号
	private String bankAssetsId;
	// 总行联行号
	private String bankId;
	// 分行联行号
	private String bankCode;
	// 发卡行名称
	private String bankName;
	// 行别码
	private String bankTypeCode;
	// 省份编码
	private String provCode;
	// 城市编码
	private String cityCode;
	private String lastMntOpId;
	private Date lastMntTs;

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEswCustAcctNo() {
		return eswCustAcctNo;
	}

	public void setEswCustAcctNo(String eswCustAcctNo) {
		this.eswCustAcctNo = eswCustAcctNo;
	}

	public String getEswAcctNo() {
		return eswAcctNo;
	}

	public void setEswAcctNo(String eswAcctNo) {
		this.eswAcctNo = eswAcctNo;
	}

	public String getEswSubAcctNo() {
		return eswSubAcctNo;
	}

	public void setEswSubAcctNo(String eswSubAcctNo) {
		this.eswSubAcctNo = eswSubAcctNo;
	}

	public String getEswUserNo() {
		return eswUserNo;
	}

	public void setEswUserNo(String eswUserNo) {
		this.eswUserNo = eswUserNo;
	}

	public String getEswUserType() {
		return eswUserType;
	}

	public void setEswUserType(String eswUserType) {
		this.eswUserType = eswUserType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCardName() {
		return bankCardName;
	}

	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}

	public String getBankAssetsId() {
		return bankAssetsId;
	}

	public void setBankAssetsId(String bankAssetsId) {
		this.bankAssetsId = bankAssetsId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankTypeCode() {
		return bankTypeCode;
	}

	public void setBankTypeCode(String bankTypeCode) {
		this.bankTypeCode = bankTypeCode;
	}

	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getLastMntOpId() {
		return lastMntOpId;
	}

	public void setLastMntOpId(String lastMntOpId) {
		this.lastMntOpId = lastMntOpId;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

}
