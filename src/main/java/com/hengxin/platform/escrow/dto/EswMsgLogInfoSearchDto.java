package com.hengxin.platform.escrow.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class EswMsgLogInfoSearchDto extends DataTablesRequestDto implements Serializable {

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

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

}
