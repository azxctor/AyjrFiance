package com.hengxin.platform.escrow.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.escrow.converter.EOrderStatusEnumConverter;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

/**
 * 
 * @author juhuahuang
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "AC_ESW_RECH_ORD")
@EntityListeners(IdInjectionEntityListener.class)
public class EswRechargeOrderPo implements Serializable {
	// 指令流水号
	@Id
	@Column(name = "ORD_ID")
	private String orderId;

	// 交易日期
	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDt;

	// 会员综合账户
	@Column(name = "ACCT_NO")
	private String acctNo;

	// 会员编号
	@Column(name = "USER_ID")
	private String userId;

	// 托管商户账号
	@Column(name = "ESW_CUST_ACCT_NO")
	private String eswCustAcctNo;

	// 托管会员账号
	@Column(name = "ESW_ACCT_NO")
	private String eswAcctNo;

	// 托管子账户编号
	@Column(name = "ESW_SUB_ACCT_NO")
	private String eswSubAcctNo;

	// 托管会员编号
	@Column(name = "ESW_USER_NO")
	private String eswUserNo;

	// 金额 TRX_AMT NUMBER(17,2)
	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;

	// 交易备注
	@Column(name = "TRX_MEMO")
	private String trxMemo;

	// 发卡行编号
	@Column(name = "OPEN_BANK_ID")
	private String openBankId;

	// 订单类型
	@Column(name = "ORD_TYPE")
	private String ordType;

	// 请求签名信息
	@Column(name = "REQ_SIGN_VAL")
	private String reqSignVal;

	// 指令状态 STATUS
	@Column(name = "STATUS")
	@Convert(converter = EOrderStatusEnumConverter.class)
	private EOrderStatusEnum status;

	// 返回编码
	@Column(name = "RET_CODE")
	private String retCode;

	// 返回信息 RET_MSG
	@Column(name = "RET_MSG")
	private String retMsg;

	// 反馈时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESP_TS")
	private Date respTs;

	// 充值凭证号
	@Column(name = "VOUCH_NO")
	private String vouchNo;

	// 反馈交易编号 TRX_NO
	@Column(name = "TRX_NO")
	private String trxNo;

	// 扣款日期 TRANS_DT
	@Column(name = "TRANS_DT")
	private String transDt;

	// 扣款时间
	@Column(name = "TRANS_TM")
	private String transTm;

	// 反馈签名信息
	@Column(name = "RESP_SIGN_VAL")
	private String respSignVal;

	// 创建用户
	@Column(name = "CREATE_OPID")
	private String createOpId;

	// 创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	// 更新用户
	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;

	// 更新时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MNT_TS")
	private Date lastMntTs;

	// 版本字段 VERSION_CT
	@Version
	@Column(name = "VERSION_CT")
	private Long versionCt;

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

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

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public String getOpenBankId() {
		return openBankId;
	}

	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}

	public String getOrdType() {
		return ordType;
	}

	public void setOrdType(String ordType) {
		this.ordType = ordType;
	}

	public String getReqSignVal() {
		return reqSignVal;
	}

	public void setReqSignVal(String reqSignVal) {
		this.reqSignVal = reqSignVal;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Date getRespTs() {
		return respTs;
	}

	public void setRespTs(Date respTs) {
		this.respTs = respTs;
	}

	public String getVouchNo() {
		return vouchNo;
	}

	public void setVouchNo(String vouchNo) {
		this.vouchNo = vouchNo;
	}

	public String getTrxNo() {
		return trxNo;
	}

	public void setTrxNo(String trxNo) {
		this.trxNo = trxNo;
	}

	public String getTransDt() {
		return transDt;
	}

	public void setTransDt(String transDt) {
		this.transDt = transDt;
	}

	public String getTransTm() {
		return transTm;
	}

	public void setTransTm(String transTm) {
		this.transTm = transTm;
	}

	public String getRespSignVal() {
		return respSignVal;
	}

	public void setRespSignVal(String respSignVal) {
		this.respSignVal = respSignVal;
	}

	public String getCreateOpId() {
		return createOpId;
	}

	public void setCreateOpId(String createOpId) {
		this.createOpId = createOpId;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
	}

}
