package com.hengxin.platform.app.dto.upstream;

import java.io.Serializable;
import java.util.Date;

/**
 * AccountDetailListDto.
 * 
 */
public class AccountDetailListDto extends PagingDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// @NotEmpty(message = "{member.error.field.empty}")
	private String type;
	// @NotEmpty(message = "{member.error.field.empty}")
	private Date startTime;

	// @NotEmpty(message = "{member.error.field.empty}")
	private Date endTime;

	private long createTime;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
