package com.hengxin.platform.escrow.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

public class EswTransferOrderSearchDto extends DataTablesRequestDto implements Serializable {
	
	private static final long serialVersionUID = 1;

	private String payerAcctNo;
	
	private String payeeAcctNo;
	
	private String trxDate;

	private EOrderStatusEnum status;

	public String getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
	}

	public String getPayerAcctNo() {
		return payerAcctNo;
	}

	public void setPayerAcctNo(String payerAcctNo) {
		this.payerAcctNo = payerAcctNo;
	}

	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}

	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}
	
}
