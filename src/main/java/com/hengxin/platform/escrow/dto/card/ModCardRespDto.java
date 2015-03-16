package com.hengxin.platform.escrow.dto.card;

import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

public class ModCardRespDto {
	
	private String orderId;
	
	private String retCode;
	
	private String retMsg;
	
	private EOrderStatusEnum status;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
	}
}
