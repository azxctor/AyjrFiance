package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;

/**
 * 冲正查询参数
 * @author dcliu
 */
public class ReverseSearchDto extends DataTablesRequestDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String acctNo; //交易账号
	
	private String bnkAcctNo; // 银行卡号
	
	private String bnkAcctName;// 银行账户明细
	
	private String relBnkId; // 回单编号
	
	private Date fromDate; // 开始日期
	
	private Date toDate; // 截止日期
	
	private EBnkTrxStatus trxStatus;
	
	private EFlagType rvsFlg;
	
	private ERechargeWithdrawFlag rechargeWithdrawFlag; // 冲正提现标识 -- 充值/提现

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
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

	public String getRelBnkId() {
		return relBnkId;
	}

	public void setRelBnkId(String relBnkId) {
		this.relBnkId = relBnkId;
	}

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

	public EFlagType getRvsFlg() {
		return rvsFlg;
	}

	public void setRvsFlg(EFlagType rvsFlg) {
		this.rvsFlg = rvsFlg;
	}
	
}
