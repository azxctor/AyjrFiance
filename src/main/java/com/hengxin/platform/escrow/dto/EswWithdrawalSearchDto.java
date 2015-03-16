package com.hengxin.platform.escrow.dto;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

/**
 * 提现信息查询条件
 * 
 * @author chenwulou
 *
 */
public class EswWithdrawalSearchDto extends DataTablesRequestDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date fromDate;
    
    private Date toDate;
    
    // 状态
    private EOrderStatusEnum eOrderStatusEnum;
    
    // 用户Id 平台人员查询可用
    private String acctNo;
    
    // 交易编号
    private String cashId;
    
    // 查询日期
    private Date searchDate;
    
    // 银行卡号
    private String bankCardNo;
    
    // 提现说明
    private String explain;

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

	public String getCashId() {
		return cashId;
	}

	public void setCashId(String cashId) {
		this.cashId = cashId;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

}
