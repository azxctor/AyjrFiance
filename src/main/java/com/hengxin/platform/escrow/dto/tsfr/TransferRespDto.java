package com.hengxin.platform.escrow.dto.tsfr;

import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

public class TransferRespDto {
	
	private String orderId;
	
	private EOrderStatusEnum status;
	
	private String retCode;
	
	private String retMsg;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
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
	
}
