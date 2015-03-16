package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.fund.entity.converter.EBnkTrxStatusEnumConverter;
import com.hengxin.platform.fund.entity.converter.ERechargeWithdrawFlagEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundCurCodeEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundCurFlagEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundFlagTypeEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundTxDirEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundTxOptEnumConverter;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundCurCode;
import com.hengxin.platform.fund.enums.EFundCurFlag;
import com.hengxin.platform.fund.enums.EFundTxDir;
import com.hengxin.platform.fund.enums.EFundTxOpt;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;


/**
 * 
 * @author jishen
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_TRSF_TRX_DTL")
public class TransferTrxDtlView implements Serializable{
	
	/**
	 * ID
	 */
	@Id
	@Column(name = "ID")
	private String id;
	
	/**
	 * 会员编号
	 */
	@Column(name = "USER_ID")
	private String userId;

	/**
	 * 会员名称
	 */
	@Column(name = "USER_NAME")
    private String userName;

    /**
     * 综合账户编号
     */
	@Column(name = "ACCT_NO")
    private String acctNo;
	
	/**
	 * 子账户编号
	 */
	@Column(name = "SUB_ACCT_NO")
	private String subAcctNo;
	
	/**
	 * 是否签约会员
	 */
	@Column(name = "SIGNED_FLG")
	@Convert(converter = FundFlagTypeEnumConverter.class)
	private EFlagType signedFlg;
	
	/**
	 * 是否冲正交易
	 */
	@Column(name = "RVS_FLG")
	@Convert(converter = FundFlagTypeEnumConverter.class)
	private EFlagType rvsFlg;
	
	/**
	 * 交易状态
	 */
	@Column(name = "STATUS")
	@Convert(converter = EBnkTrxStatusEnumConverter.class)
	private EBnkTrxStatus status;

    /**
     * 收付标识
     */
	@Column(name = "PAY_RECV_FLG")
	@Convert(converter = ERechargeWithdrawFlagEnumConverter.class)
	private ERechargeWithdrawFlag payRecvFlag;
	
	/**
     * 交易日期
     */
	@Column(name = "TRX_DT")
	@Temporal(TemporalType.DATE)
	private Date trxDt;

    /**
     * 交易金额
     */
	@Column(name = "TRX_AMT")
    private BigDecimal trxAmt;
	
	/**
	 * 关联银行交易流水
	 */
	@Column(name = "REL_BNK_ID")
	private String relBnkId;

    /**
     * 交易批次
     */
	@Column(name = "BATCH_NO")
    private String batchNo;

    /**
	 * 银行编号
	 */
	@Column(name = "BANK_ID")
    private String bankId;
	
	/**
	 * 合作方编号
	 */
	@Column(name = "DEAL_ID")
	private String dealId;

	/**
	 * 交易日期
	 */
	@Column(name = "TRX_DATE")
	private String txDate;
	
	/**
	 * 交易时间
	 */
	@Column(name = "TRX_TIME")
	private String txTime;

    /**
     * 银行流水号
     */
	@Column(name = "BNK_SERIAL")
    private String bkSerial;
    
    /**
     * 银行账号
     */
	@Column(name = "BNK_ACCT_NO")
    private String bankAccNo;
    
    /**
     * 交易所交易账号
     */
	@Column(name = "FUND_ACCT_NO")
    private String fundAccNo;
	
	/**
	 * 客户姓名
	 */
	@Column(name = "CUST_NAME")
	private String custName;
    
    /**
     * 交易发起方
     */
	@Column(name = "TRX_OPT")
	@Convert(converter = FundTxOptEnumConverter.class)
    private EFundTxOpt txOpt;
    
    /**
     * 转账方向
     */
	@Column(name = "TRX_DIR")
	@Convert(converter = FundTxDirEnumConverter.class)
    private EFundTxDir txDir;
	
	/**
	 * 货币代码
	 */
	@Column(name = "CUR_CODE")
	@Convert(converter = FundCurCodeEnumConverter.class)
	private EFundCurCode curCode;

	/**
	 * 钞汇标志
	 */
	@Column(name = "CUR_FLAG")
	@Convert(converter = FundCurFlagEnumConverter.class)
	private EFundCurFlag curFlag;
    
	/**
	 * 转账金额
	 */
	@Column(name = "BNK_TRX_AMT")
	private BigDecimal txAmt;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public String getSubAcctNo() {
		return subAcctNo;
	}

	public void setSubAcctNo(String subAcctNo) {
		this.subAcctNo = subAcctNo;
	}

	public EFlagType getSignedFlg() {
		return signedFlg;
	}

	public void setSignedFlg(EFlagType signedFlg) {
		this.signedFlg = signedFlg;
	}

	public ERechargeWithdrawFlag getPayRecvFlag() {
		return payRecvFlag;
	}

	public void setPayRecvFlag(ERechargeWithdrawFlag payRecvFlag) {
		this.payRecvFlag = payRecvFlag;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getRelBnkId() {
		return relBnkId;
	}

	public void setRelBnkId(String relBnkId) {
		this.relBnkId = relBnkId;
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

	public String getTxTime() {
		return txTime;
	}

	public void setTxTime(String txTime) {
		this.txTime = txTime;
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

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
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

	public EFundCurCode getCurCode() {
		return curCode;
	}

	public void setCurCode(EFundCurCode curCode) {
		this.curCode = curCode;
	}

	public EFundCurFlag getCurFlag() {
		return curFlag;
	}

	public void setCurFlag(EFundCurFlag curFlag) {
		this.curFlag = curFlag;
	}

	public BigDecimal getTxAmt() {
		return txAmt;
	}

	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}
	
	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
