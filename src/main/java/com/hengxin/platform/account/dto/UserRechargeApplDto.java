package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Convert;

import com.hengxin.platform.fund.entity.converter.CashPoolEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundApplStatusEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundDealStatusEnumConverter;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;

public class UserRechargeApplDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applNo; //申请编号
	
	private Date applDt;  //申请日期
	
	private String userId; //会员编号
	
	private String userName; // 会员名称
	
	private String acctNo; //会员综合账户编号
	
	@Convert(converter = FundApplStatusEnumConverter.class)
	private EFundApplStatus applStatus; // 申请审批状态
	
	@Convert(converter = FundDealStatusEnumConverter.class)
	private EFundDealStatus dealStatus;// 申请处理状态
	
	@Convert(converter = CashPoolEnumConverter.class)
	private ECashPool cashPool;
	
	private String dealMemo; // 申请处理结果
	
	private String bnkCd; // 所属银行类型
	
	private String bnkAcctNo; // 银行卡号

	private String bnkAcctName; // 银行账户名称
	
	private BigDecimal trxAmt; // 充值金额

	private String trxMemo; // 申请备注信息
	
	private String relBnkJnlNo; // 充值日志流水
	
	private String voucherNo; // 收款凭证，回单号
	
	private String apprOpid; // 审批人
	
	private boolean apprBtnEnable; // 审批按钮是否可用
	
	private boolean printBtnEnable;// 打印按钮是否可用
	
	public boolean getApprBtnEnable(){
		apprBtnEnable = false;
		if(applStatus==EFundApplStatus.WAIT_APPROVAL){
			apprBtnEnable = true;
		}
		return apprBtnEnable;
	}
	
	public boolean getPrintBtnEnable(){
		printBtnEnable = false;
		if(dealStatus==EFundDealStatus.SUCCED){
			printBtnEnable = true;
		}
		return printBtnEnable;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public Date getApplDt() {
		return applDt;
	}

	public void setApplDt(Date applDt) {
		this.applDt = applDt;
	}

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

	public EFundApplStatus getApplStatus() {
		return applStatus;
	}

	public void setApplStatus(EFundApplStatus applStatus) {
		this.applStatus = applStatus;
	}

	public EFundDealStatus getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(EFundDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getBnkCd() {
		return bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
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

	public String getRelBnkJnlNo() {
		return relBnkJnlNo;
	}

	public void setRelBnkJnlNo(String relBnkJnlNo) {
		this.relBnkJnlNo = relBnkJnlNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public String getApprOpid() {
		return apprOpid;
	}

	public void setApprOpid(String apprOpid) {
		this.apprOpid = apprOpid;
	}

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}
	
}
