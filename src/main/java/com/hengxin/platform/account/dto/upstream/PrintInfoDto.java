package com.hengxin.platform.account.dto.upstream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.account.enums.ERoleType;

/**
 * Class Name: PrintInfoDto
 * Description: TODO
 * @author jishen
 *
 */

public class PrintInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 交易日期
	 */
	private Date trxDt;

	/**
	 * 账户名
	 */
	private String bnkAcctName;
	
	/**
	 * 交易账号
	 */
	private String acctNo;
	
	/**
	 * 金额
	 */
	private BigDecimal trxAmt;
	
	/**
	 * 人民币大写金额
	 */
	private String rmbStr;
	
	/**
	 * 账户余额
	 */
	private BigDecimal bal;
	
	/**
	 * 银行卡号
	 */
	private String bnkAcctNo;
	
	/**
	 * 开户行
	 */
	private String bnkName;

	/**
	 * 备注
	 */
	private String trxMemo;
	
	/**
	 * 角色
	 */
	private ERoleType roleType;
	
	/**
	 * 经办人
	 */
	private String handler;
	
	/**
	 * 复核人
	 */
	private String checker;
	

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
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

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

	public String getBnkName() {
		return bnkName;
	}

	public void setBnkName(String bnkName) {
		this.bnkName = bnkName;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public ERoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(ERoleType roleType) {
		this.roleType = roleType;
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

	public String getRmbStr() {
		return rmbStr;
	}

	public void setRmbStr(String rmbStr) {
		this.rmbStr = rmbStr;
	}
}
