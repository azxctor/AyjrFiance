package com.hengxin.platform.member.dto;

import java.io.Serializable;

public class SkinnyUserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String userName;
	
	private String accountNo;

	private String level;

	private String authorizedCenterName;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the authorizedCenterName
	 */
	public String getAuthorizedCenterName() {
		return authorizedCenterName;
	}

	/**
	 * @param authorizedCenterName the authorizedCenterName to set
	 */
	public void setAuthorizedCenterName(String authorizedCenterName) {
		this.authorizedCenterName = authorizedCenterName;
	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
}
