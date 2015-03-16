package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * Class Name: TransferDetailsSearchDto
 * 
 * @author jishen
 * 
 */

public class TransferDetailsSearchDto extends DataTablesRequestDto implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 开始时间
	 */
	private Date fromDate;
	/**
	 * 截止时间
	 */
	private Date toDate;
	/**
	 * 转账类型
	 */
	private EFundUseType useType;
	/**
	 * 审核状态
	 */
	private EFundApplStatus applStatus;
	/**
	 * 事件编号
	 */
	private String eventId;
	
	/**
	 * 付款交易账号
	 */
	private String payerAcctNo;

	/**
	 * 收款交易账号
	 */
	private String payeeAcctNo;
    
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

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public EFundApplStatus getApplStatus() {
		return applStatus;
	}

	public void setApplStatus(EFundApplStatus applStatus) {
		this.applStatus = applStatus;
	}

	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}

	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}

	public String getPayerAcctNo() {
		return payerAcctNo;
	}

	public void setPayerAcctNo(String payerAcctNo) {
		this.payerAcctNo = payerAcctNo;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
}
