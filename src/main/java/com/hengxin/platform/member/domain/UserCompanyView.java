package com.hengxin.platform.member.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "v_user_company")
public class UserCompanyView implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LEVEL_")
    private String level;

    @Column(name = "COMPANY_ID")
    private String company_id;

    @Column(name = "COMPANY_INFO")
    private String company_info;

    @Column(name = "PARENT_COMPANY_ID")
    private String parent_company_id;

    @Column(name = "PARENT_COMPANY_INFO")
    private String parent_company_info;

    @Column(name = "SUBS")
    private String subs;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public String getCompany_info() {
		return company_info;
	}

	public void setCompany_info(String company_info) {
		this.company_info = company_info;
	}

	public String getParent_company_id() {
		return parent_company_id;
	}

	public void setParent_company_id(String parent_company_id) {
		this.parent_company_id = parent_company_id;
	}

	public String getParent_company_info() {
		return parent_company_info;
	}

	public void setParent_company_info(String parent_company_info) {
		this.parent_company_info = parent_company_info;
	}

	public String getSubs() {
		return subs;
	}

	public void setSubs(String subs) {
		this.subs = subs;
	}
}
