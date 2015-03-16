package com.hengxin.platform.account.dto;

import java.io.Serializable;

import com.hengxin.platform.fund.enums.ECashPool;

/**
 * 
 * @author jishen
 *
 */
public class UnsignedRechargeInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 渠道
	 */
	private ECashPool channel;
	
	/**
	 * 银行开户名
	 */
	private String bnkAcctName;
	
	/**
	 * 平台账号
	 */
	private String acctNo;
	
	/**
	 * 银行账户号
	 */
	private String bnkAcctNo;
	
	/**
	 * 会员登录ID
	 */
	private String userId;
	
	/**
	 * 会员类型
	 */
	private String userType;

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


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public ECashPool getChannel() {
		return channel;
	}


	public void setChannel(ECashPool channel) {
		this.channel = channel;
	}


	public String getBnkAcctName() {
		return bnkAcctName;
	}


	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}


	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}
}
