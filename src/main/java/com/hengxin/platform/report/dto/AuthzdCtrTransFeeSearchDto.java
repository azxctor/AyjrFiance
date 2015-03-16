package com.hengxin.platform.report.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class AuthzdCtrTransFeeSearchDto extends DataTablesRequestDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 统计区间 - 开始时间
	 */
	private String beginDate;

	/**
	 * 统计区间 - 结束时间
	 */
	private String endDate;

	/**
	 * 关键字
	 */
	private String keyword;

	/**
	 * @return the beginDate
	 */
	public String getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
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
}
