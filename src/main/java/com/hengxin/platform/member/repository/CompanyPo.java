package com.hengxin.platform.member.repository;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_COMPANY")
public class CompanyPo implements Serializable{
	@Id
    @Column(name = "COMPANY_ID")
    private String companyId;
	
	@Column(name = "PARENT_COMPANY_ID")
	private String parentCompanyId;
	
	@Column(name = "COMPANY_INFO")
	private String companyInfo;
	
	@Column(name = "LEVEL_")
	private int level;
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getParentCompanyId() {
		return parentCompanyId;
	}

	public void setParentCompanyId(String parentCompanyId) {
		this.parentCompanyId = parentCompanyId;
	}

	public String getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(String companyInfo) {
		this.companyInfo = companyInfo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
