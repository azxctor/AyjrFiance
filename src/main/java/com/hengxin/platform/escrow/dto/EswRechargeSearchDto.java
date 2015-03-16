package com.hengxin.platform.escrow.dto;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

public class EswRechargeSearchDto extends DataTablesRequestDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date fromDate;
    
    private Date toDate;
    
    // 状态
    private EOrderStatusEnum eOrderStatusEnum;
    
    private String acctNo;
    
    //充值凭证号
    private String vouchNo;
    
    // 查询日期
    private Date searchDate;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public EOrderStatusEnum geteOrderStatusEnum() {
		return eOrderStatusEnum;
	}

	public void seteOrderStatusEnum(EOrderStatusEnum eOrderStatusEnum) {
		this.eOrderStatusEnum = eOrderStatusEnum;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getVouchNo() {
		return vouchNo;
	}

	public void setVouchNo(String vouchNo) {
		this.vouchNo = vouchNo;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}


}
