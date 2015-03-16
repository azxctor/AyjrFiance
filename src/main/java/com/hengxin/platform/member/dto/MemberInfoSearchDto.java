package com.hengxin.platform.member.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.member.enums.EUserType;

public class MemberInfoSearchDto extends DataTablesRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fromDate;

	private String toDate;

	/**
	 * 机构/个人
	 */
	private EUserType userType;

	/**
	 * 会员类型：游客/投资/...
	 */
	private String roleId;
	
	
	private String roleName;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 企业名称
	 */
	private String orgName;

	/**
	 * 姓名
	 */
	private String name;

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public EUserType getUserType() {
		return userType;
	}

	public void setUserType(EUserType userType) {
		this.userType = userType;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
