package com.hengxin.platform.netting.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.netting.enums.NettingStatusEnum;

public class RecvPaySearchDto extends DataTablesRequestDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String acctNo;

	private String trxDate;
	
	private NettingStatusEnum status;

	public String getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}

	public NettingStatusEnum getStatus() {
		return status;
	}

	public void setStatus(NettingStatusEnum status) {
		this.status = status;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	
}
