package com.hengxin.platform.account.dto.upstream;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;

/**
 * Class Name: WithdrawalApplDetailDto Description: TODO
 * 
 * @author jishen
 * 
 */

public class WithdrawalApplDetailDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 申请编号
	 */
	private String applNo;
	
	/**
	 * 申请时间
	 */
	private String applDt;
	/**
	 * 提现金额
	 */
	private BigDecimal trxAmt;
	/**
	 * 银行账户名
	 */
	private String bnkAcctName;
	/**
	 * 收款账户开户银行
	 */
	private String bnkName;
	/**
	 * 银行卡号
	 */
	private String bnkAcctNo;
	/**
	 * 交易账号
	 */
	private String acctNo;
	/**
	 * 审批状态
	 */
	private EFundApplStatus applStatus;

	/**
	 * 处理状态
	 */
	private EFundDealStatus dealStatus;
	
	/**
	 * 所属资金池
	 */
	private ECashPool cashPool;

	/**
	 * 交易备注
	 */
	@JsonProperty("memo")
	private String dealMemo;
	
	/**
	 * 申请理由
	 */
	private String trxMemo;

	/**
	 * 审核人
	 */
	@JsonProperty("approveOpId")
	private String apprOpid;

	/**
	 * 审核日期
	 */
	private String apprTs;
	
	/**
	 * 提现编号
	 */
	private String relBnkJnlNo;

	/**
	 * 是否有权限审批
	 */
	private boolean canApprove;
	/**
	 * 是否有权限放款
	 */
	private boolean canConfirm;

	public boolean isCanApprove() {
		return canApprove;
	}

	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}

	public boolean isCanConfirm() {
		return canConfirm;
	}

	public void setCanConfirm(boolean canConfirm) {
		this.canConfirm = canConfirm;
	}

	public String getApplDt() {
		return applDt;
	}

	public void setApplDt(String applDt) {
		this.applDt = applDt;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getBnkAcctName() {
		return bnkAcctName;
	}

	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}

	public String getBnkName() {
		return bnkName;
	}

	public void setBnkName(String bnkName) {
		this.bnkName = bnkName;
	}

	public EFundDealStatus getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(EFundDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getApprOpid() {
		return apprOpid;
	}

	public void setApprOpid(String apprOpid) {
		this.apprOpid = apprOpid;
	}

	public String getApprTs() {
		return apprTs;
	}

	public void setApprTs(String apprTs) {
		this.apprTs = apprTs;
	}

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

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public String getRelBnkJnlNo() {
		return relBnkJnlNo;
	}

	public void setRelBnkJnlNo(String relBnkJnlNo) {
		this.relBnkJnlNo = relBnkJnlNo;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}
}
