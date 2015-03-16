package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Class Name: SubAcctBal
 * 
 * @author liudc
 * 
 */
@SuppressWarnings("serial")
public class SubAcct implements Serializable {

	//综合账户编号
	private String acctNo;
	
	//子账户编号
	private String subAcctNo;
	
	//账户类型
	private String acctType;
	
	//账户余额
	private BigDecimal bal;
	
	//可提现金额
	private BigDecimal maxCashableAmt;
	
	//应冻结金额
	private BigDecimal freezableAmt;
	
	//保留金额
	private BigDecimal reservedAmt;
	
	//可计息余额
	private BigDecimal intrBal;
	
	//息余
	private BigDecimal accruedIntrAmt;
	
	//欠款金额
	private BigDecimal debtAmt;
	
	//创建用户
	private String createOpid;
	
	//创建日期
	private Date createTs;
	
	//更新用户
	private String lastMntOpid;
	
	//更新时间
	private Date lastMntTs;

	/**
	 * 获取综合账户编号
	 * @return
	 */
	public String getAcctNo() {
		return acctNo;
	}

	/**
	 * 设置综合账户编号
	 * @param acctNo
	 */
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	/**
	 * 获取子账户编号
	 * @return
	 */
	public String getSubAcctNo() {
		return subAcctNo;
	}

	/**
	 * 设置子账户编号
	 * @param subAcctNo
	 */
	public void setSubAcctNo(String subAcctNo) {
		this.subAcctNo = subAcctNo;
	}

	/**
	 * 获取账户类型
	 * @return
	 */
	public String getAcctType() {
		return acctType;
	}

	/**
	 * 设置账户类型
	 * @param acctType
	 */
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	/**
	 * 获取账户余额
	 * @return
	 */
	public BigDecimal getBal() {
		return bal;
	}

	/**
	 * 设置账户余额
	 * @param bal
	 */
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	
	/**
	 * 获取可提现金额
	 * @return
	 */
	public BigDecimal getMaxCashableAmt() {
		return maxCashableAmt;
	}

	/**
	 * 设置可提现金额
	 * @param cashableAmt
	 */
	public void setMaxCashableAmt(BigDecimal maxCashableAmt) {
		this.maxCashableAmt = maxCashableAmt;
	}

	/**
	 * 获取应冻结金额
	 * @return
	 */
	public BigDecimal getFreezableAmt() {
		return freezableAmt;
	}

	/**
	 * 设置应冻结金额
	 * @param freezableAmt
	 */
	public void setFreezableAmt(BigDecimal freezableAmt) {
		this.freezableAmt = freezableAmt;
	}

	/**
	 * 获取保留金额
	 * @return
	 */
	public BigDecimal getReservedAmt() {
		return reservedAmt;
	}

	/**
	 * 设置保留金额
	 * @param reservedAmt
	 */
	public void setReservedAmt(BigDecimal reservedAmt) {
		this.reservedAmt = reservedAmt;
	}

	/**
	 * 获取可计息余额
	 * @return
	 */
	public BigDecimal getIntrBal() {
		return intrBal;
	}

	/**
	 * 设置可计息余额
	 * @param intrBal
	 */
	public void setIntrBal(BigDecimal intrBal) {
		this.intrBal = intrBal;
	}

	/**
	 * 获取息余
	 * @return
	 */
	public BigDecimal getAccruedIntrAmt() {
		return accruedIntrAmt;
	}

	/**
	 * 设置息余
	 * @param accruedIntrAmt
	 */
	public void setAccruedIntrAmt(BigDecimal accruedIntrAmt) {
		this.accruedIntrAmt = accruedIntrAmt;
	}

	/**
	 * 获取欠款金额
	 * @return
	 */
	public BigDecimal getDebtAmt() {
		return debtAmt;
	}

	/**
	 * 设置欠款金额
	 * @param debtAmt
	 */
	public void setDebtAmt(BigDecimal debtAmt) {
		this.debtAmt = debtAmt;
	}


	/**
	 * 获取创建用户
	 * @return
	 */
	public String getCreateOpid() {
		return createOpid;
	}

	/**
	 * 设置创建用户
	 * @param createOpid
	 */
	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	/**
	 * 获取创建日期
	 * @return
	 */
	public Date getCreateTs() {
		return createTs;
	}

	/**
	 * 设置创建日期
	 * @param createTs
	 */
	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	/**
	 * 获取更新用户
	 * @return
	 */
	public String getLastMntOpid() {
		return lastMntOpid;
	}

	/**
	 * 设置更新用户
	 * @param lastMntOpid
	 */
	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	/**
	 * 获取更新时间
	 * @return
	 */
	public Date getLastMntTs() {
		return lastMntTs;
	}

	/**
	 * 设置更新时间
	 * @param lastMntTs
	 */
	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}
	
}
