package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * Class Name: fundTransferDto
 * 
 * @author jishen
 * 
 */
public class FundTransferDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 转账金额
	 */
	private BigDecimal trxAmt;
	
	/**
	 * 转账备注 
	 */
	private String trxMemo;
	
	/**
	 * 付款人名称
	 */
	private String payerName;
	
	/**
	 * 付款方账户号
	 */
	private String payerAcctNo;
	
	/**
	 * 收款人名称
	 */
	private String payeeName;
	
	/**
	 * 收款方账户号
	 */
	private String payeeAcctNo;
	
	/**
	 * 资金用途
	 */
	private EFundUseType useType;

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

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

}
