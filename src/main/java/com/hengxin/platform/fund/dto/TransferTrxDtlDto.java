package com.hengxin.platform.fund.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundTxDir;
import com.hengxin.platform.fund.enums.EFundTxOpt;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;

public class TransferTrxDtlDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 会员名称
	 */
    private String userName;

    /**
     * 综合账户编号
     */
    private String acctNo;

    /**
     * 收付标识
     */
    private ERechargeWithdrawFlag payRecvFlag;
	
	private Date trxDt;
    
	private EFlagType rvsFlg;
	
	private EBnkTrxStatus status;

    /**
     * 交易金额
     */
    private BigDecimal trxAmt;

    /**
     * 交易批次
     */
    private String batchNo;

    /**
	 * 银行编号
	 */
    private String bankId;

    /**
     * 交易日期
     */
    private String txDate;

    /**
     * 银行流水号
     */
    private String bkSerial;
    
    /**
     * 银行账号
     */
    private String bankAccNo;
    
    private String custName;
    
    /**
     * 交易所交易账号
     */
    private String fundAccNo;
    
    /**
     * 交易发起方
     */
    private EFundTxOpt txOpt;
    
    /**
     * 转账方向
     */
    private EFundTxDir txDir;
    
    /**
     * 转账金额
     */
    private BigDecimal txAmt;
    
    /**
     * 结果
     */
    private Boolean result;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public String getBkSerial() {
		return bkSerial;
	}

	public void setBkSerial(String bkSerial) {
		this.bkSerial = bkSerial;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public String getFundAccNo() {
		return fundAccNo;
	}

	public void setFundAccNo(String fundAccNo) {
		this.fundAccNo = fundAccNo;
	}

	public EFundTxOpt getTxOpt() {
		return txOpt;
	}

	public void setTxOpt(EFundTxOpt txOpt) {
		this.txOpt = txOpt;
	}

	public EFundTxDir getTxDir() {
		return txDir;
	}

	public void setTxDir(EFundTxDir txDir) {
		this.txDir = txDir;
	}

	public BigDecimal getTxAmt() {
		return txAmt;
	}

	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public ERechargeWithdrawFlag getPayRecvFlag() {
		return payRecvFlag;
	}

	public void setPayRecvFlag(ERechargeWithdrawFlag payRecvFlag) {
		this.payRecvFlag = payRecvFlag;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public EFlagType getRvsFlg() {
		return rvsFlg;
	}

	public void setRvsFlg(EFlagType rvsFlg) {
		this.rvsFlg = rvsFlg;
	}

	public EBnkTrxStatus getStatus() {
		return status;
	}

	public void setStatus(EBnkTrxStatus status) {
		this.status = status;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}
}
