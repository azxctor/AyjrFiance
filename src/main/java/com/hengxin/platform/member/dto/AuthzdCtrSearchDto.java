package com.hengxin.platform.member.dto;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

@SuppressWarnings("serial")
public class AuthzdCtrSearchDto extends DataTablesRequestDto implements Serializable {
	/**
	 * 统计区间 - 开始时间
	 */
	private Date beginDate;

	/**
	 * 统计区间 - 结束时间
	 */
	private Date endDate;

	/**
	 * 关键字
	 */
	private String keyword;
	/**
	 * 查询明细时用来指定明细所属服务中心
	 */
	private String authzd_id;
	/**
	 * 用户所属分公司
	 */
	private String company_id;

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getAuthzd_id() {
		return authzd_id;
	}

	public void setAuthzd_id(String authzd_id) {
		this.authzd_id = authzd_id;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

}