package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: TransApplPriInfoDto Description: TODO
 * 
 * @author jishen
 * 
 */

public class TransApplPriInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 日期
	 */
	private String dt;

	/**
	 * 付款交易账号
	 */
	private String payerAcctNo;

	/**
	 * 付款人姓名
	 */
	private String payerName;
	
	/**
	 * 付款人银行账号
	 */
	private String payerBnkAcctNo;

	/**
	 * 收款交易账号
	 */
	private String payeeAcctNo;

	/**
	 * 收款人姓名
	 */
	private String payeeName;
	
	/**
	 * 收款人银行账号
	 */
	private String payeeBnkAcctNo;
	
	/**
	 * 交易金额
	 */
	private BigDecimal trxAmt;
	
	/**
	 * 大写交易金额
	 */
	private String trxAmtStr;
	
	/**
	 * 交易备注
	 */
	private String trxMemo;
	
	/**
	 * 经办人
	 */
	private String handler;
	
	/**
	 * 复核人
	 */
	private String checker;
	
	/**
	 * 主管
	 */
	private String supervisor;

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

	public String getPayerBnkAcctNo() {
		return payerBnkAcctNo;
	}

	public void setPayerBnkAcctNo(String payerBnkAcctNo) {
		this.payerBnkAcctNo = payerBnkAcctNo;
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

	public String getPayeeBnkAcctNo() {
		return payeeBnkAcctNo;
	}

	public void setPayeeBnkAcctNo(String payeeBnkAcctNo) {
		this.payeeBnkAcctNo = payeeBnkAcctNo;
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

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public String getTrxAmtStr() {
		return trxAmtStr;
	}

	public void setTrxAmtStr(String trxAmtStr) {
		this.trxAmtStr = trxAmtStr;
	}
}
