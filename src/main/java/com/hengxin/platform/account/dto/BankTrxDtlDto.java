package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;

public class BankTrxDtlDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jnlNo; // 流水号
	
	private String userId; //会员编号
	
	private String bnkAcctNo;// 银行卡号
	
	private String bnkAcctName;// 银行账户名
	
	private String acctNo;//综合账户号
	
	private ERechargeWithdrawFlag rechargeWithdrawFlag;// 充值提现类型

	private EBnkTrxStatus trxStatus;// 状态
	
	private EFlagType signedFlg; // 签约状态 
	
	private EBankType bankCode;// 所属银行
	
	private Date trxDt;// 交易日期

	private BigDecimal trxAmt;// 交易金额
	
	private BigDecimal acctReversableAmt; // 账户可冲正金额
	
	private String trxMemo;// 备注信息
	
    private ECashPool cashPool;// 资金池
	
	private EFlagType rvsFlg; // 是否冲正
    
	private Date rvsDt;// 冲正日期
	
	private String rvsApplNo;// 冲正申请编号
	
	private String rvsJnlNo; // 被冲正的流水编号
	
	private String dealMemo;// 处理备注
	
	private String relBnkId;// 关联流水号(回单号)
	
	private String lastMntOpid;
	
	private Date lastMntTs;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

	public String getBnkAcctName() {
		return bnkAcctName;
	}

	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public ERechargeWithdrawFlag getRechargeWithdrawFlag() {
		return rechargeWithdrawFlag;
	}

	public void setRechargeWithdrawFlag(ERechargeWithdrawFlag rechargeWithdrawFlag) {
		this.rechargeWithdrawFlag = rechargeWithdrawFlag;
	}

	public EBnkTrxStatus getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(EBnkTrxStatus trxStatus) {
		this.trxStatus = trxStatus;
	}

	public EBankType getBankCode() {
		return bankCode;
	}

	public void setBankCode(EBankType bankCode) {
		this.bankCode = bankCode;
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

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}

	public Date getRvsDt() {
		return rvsDt;
	}

	public void setRvsDt(Date rvsDt) {
		this.rvsDt = rvsDt;
	}

	public String getRelBnkId() {
		return relBnkId;
	}

	public void setRelBnkId(String relBnkId) {
		this.relBnkId = relBnkId;
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

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public String getRvsApplNo() {
		return rvsApplNo;
	}

	public void setRvsApplNo(String rvsApplNo) {
		this.rvsApplNo = rvsApplNo;
	}

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public BigDecimal getAcctReversableAmt() {
		return acctReversableAmt;
	}

	public void setAcctReversableAmt(BigDecimal acctReversableAmt) {
		this.acctReversableAmt = acctReversableAmt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public EFlagType getSignedFlg() {
		return signedFlg;
	}

	public void setSignedFlg(EFlagType signedFlg) {
		this.signedFlg = signedFlg;
	}

	public EFlagType getRvsFlg() {
		return rvsFlg;
	}

	public void setRvsFlg(EFlagType rvsFlg) {
		this.rvsFlg = rvsFlg;
	}

	public String getRvsJnlNo() {
		return rvsJnlNo;
	}

	public void setRvsJnlNo(String rvsJnlNo) {
		this.rvsJnlNo = rvsJnlNo;
	}
}
