package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * Class Name: TransferDetailsDto
 * 
 * @author jishen
 * 
 */

public class TransferApplDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 申请编号
	 */
	private String applNo;
	
	/**
	 * 申请人ID
	 */
	private String createOpid;

	/**
	 * 申请状态
	 */
	private EFundApplStatus applStatus;

	/**
	 * 审批人
	 */
	private String apprOpid;

	/**
	 * 审批时间
	 */
	private Date apprTs;

	/**
	 * 处理备注
	 */
	private String dealMemo;

	/**
	 * 事件编号
	 */
	private String eventId;

	/**
	 * 申请日期
	 */
	private Date applDt;

	/**
	 * 付款交易账号
	 */
	private String payerAcctNo;

	/**
	 * 付款人姓名
	 */
	private String payerName;

	/**
	 * 收款交易账号
	 */
	private String payeeAcctNo;

	/**
	 * 收款人姓名
	 */
	private String payeeName;

	/**
	 * 用途类型
	 */
	private EFundUseType useType;

	/**
	 * 交易金额
	 */
	private BigDecimal trxAmt;

	/**
	 * 交易备注
	 */
	private String trxMemo;

	/**
	 * 是否允许审批操作
	 */
	@SuppressWarnings("unused")
	private boolean isApprove;
	
	/**
	 * 是否允许打印操作
	 */
	@SuppressWarnings("unused")
	private boolean isPrint;

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public EFundApplStatus getApplStatus() {
		return applStatus;
	}

	public void setApplStatus(EFundApplStatus applStatus) {
		this.applStatus = applStatus;
	}

	public String getApprOpid() {
		return apprOpid;
	}

	public void setApprOpid(String apprOpid) {
		this.apprOpid = apprOpid;
	}

	public Date getApprTs() {
		return apprTs;
	}

	public void setApprTs(Date apprTs) {
		this.apprTs = apprTs;
	}

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Date getApplDt() {
		return applDt;
	}

	public void setApplDt(Date applDt) {
		this.applDt = applDt;
	}

	public String getPayerAcctNo() {
		return payerAcctNo;
	}

	public void setPayerAcctNo(String payerAcctNo) {
		this.payerAcctNo = payerAcctNo;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}

	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public boolean isApprove() {
		return applStatus == EFundApplStatus.WAIT_APPROVAL;
	}

	public boolean isPrint() {
		return applStatus == EFundApplStatus.APPROVED;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

}
