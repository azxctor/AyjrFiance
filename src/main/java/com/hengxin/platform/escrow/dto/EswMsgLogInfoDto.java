package com.hengxin.platform.escrow.dto;

import java.io.Serializable;

public class EswMsgLogInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 流水号
	 */
	private String jnlNo;

	/**
	 * 交易类型
	 */
	private String trxType;

	/**
	 * 报文类型
	 */
	private String msgType;

	/**
	 * 报文体
	 */
	private String msgBody;

	/**
	 * 是否同步标识
	 */
	private String syncFlag;

	/**
	 * 返回码
	 */
	private String retCode;

	/**
	 * 返回信息
	 */
	private String retMsg;

	/**
	 * 关联流水编号
	 */
	private String relJnlNo;

	/**
	 * 创建人
	 */
	private String createOpId;

	/**
	 * 创建时间
	 */
	private String createTs;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public String getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
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

	public String getRelJnlNo() {
		return relJnlNo;
	}

	public void setRelJnlNo(String relJnlNo) {
		this.relJnlNo = relJnlNo;
	}

	public String getCreateOpId() {
		return createOpId;
	}

	public void setCreateOpId(String createOpId) {
		this.createOpId = createOpId;
	}

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

}
