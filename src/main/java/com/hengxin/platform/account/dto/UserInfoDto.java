package com.hengxin.platform.account.dto;

import java.io.Serializable;

/**
 * Class Name: userInfo
 * 
 * @author jishen
 * 
 */
public class UserInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户Id
	 */
	private String username;
	
	/**
	 * email
	 */
	private String email;
	
	/**
	 * 绑定手机号
	 */
	private String mobile;
	
	/**
	 * 签约会员标记
	 */
	private String signedFlag;
	
	/**
	 * 上次登录时间
	 */
	private String lastLoginTs;
	
	/**
	 * 交易账号
	 */
	private String acctNo;
	
	/**
	 * 银行卡号
	 */
	private String bankAcctNo;
	
	/**
	 * 是否投资人
	 */
	private boolean isInvestor;
	
	/**
	 * 是否融资人
	 */
	private boolean isFinancier;
	
	/**
	 * 是否开启小微宝
	 */
	private boolean isOpenXwb;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSignedFlag() {
		return signedFlag;
	}

	public void setSignedFlag(String signedFlag) {
		this.signedFlag = signedFlag;
	}

    public String getLastLoginTs() {
        return lastLoginTs;
    }

    public void setLastLoginTs(String lastLoginTs) {
        this.lastLoginTs = lastLoginTs;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }

    public boolean isInvestor() {
        return isInvestor;
    }

    public void setInvestor(boolean isInvestor) {
        this.isInvestor = isInvestor;
    }

    public boolean isFinancier() {
        return isFinancier;
    }

    public void setFinancier(boolean isFinancier) {
        this.isFinancier = isFinancier;
    }

	public boolean isOpenXwb() {
		return isOpenXwb;
	}

	public void setOpenXwb(boolean isOpenXwb) {
		this.isOpenXwb = isOpenXwb;
	}
}
