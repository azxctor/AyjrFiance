package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * DetailListDto.
 *
 */
public class DetailListDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type = "";
	private String amount = "";
	private String remarks = "";
	private String time = "";
	private String payRecvFlg = "";

	public String getPayRecvFlg() {
		return payRecvFlg;
	}

	public void setPayRecvFlg(String payRecvFlg) {
		this.payRecvFlg = payRecvFlg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
