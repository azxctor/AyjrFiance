package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.converter.FundCurCodeEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundCurFlagEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundTxDirEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundTxOptEnumConverter;
import com.hengxin.platform.fund.enums.EFundCurCode;
import com.hengxin.platform.fund.enums.EFundCurFlag;
import com.hengxin.platform.fund.enums.EFundTxDir;
import com.hengxin.platform.fund.enums.EFundTxOpt;

/**
 * Class Name: TransferTrxDtl
 * 
 * @author jishen 转账交易对账明细表
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AC_RECON_TRX_DTL")
@EntityListeners(IdInjectionEntityListener.class)
public class ReconTrxDtlPo implements Serializable {

	/**
	 * 交易批次号
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
	@Id
	@Column(name = "BK_SERIAL")
	private String bkSerial;

	/**
	 * 合作方流水号
	 */
	@Column(name = "CO_SERIAL")
	private String coSerial;

	/**
	 * 银行账号
	 */
	@Column(name = "BANK_ACC_NO")
	private String bankAccNo;

	/**
	 * 交易所交易账号
	 */
	@Column(name = "FUND_ACC_NO")
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
	@Column(name = "TRX_AMT")
	private BigDecimal txAmt;

	@Column(name = "CREATE_OPID")
	private String createOpid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MNT_TS")
	private Date lastMntTs;

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

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
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

	public String getCoSerial() {
		return coSerial;
	}

	public void setCoSerial(String coSerial) {
		this.coSerial = coSerial;
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

	public BigDecimal getTxAmt() {
		return txAmt;
	}

	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
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
}
