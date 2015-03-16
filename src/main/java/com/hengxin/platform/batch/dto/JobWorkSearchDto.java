package com.hengxin.platform.batch.dto;

import java.util.Date;

public class JobWorkSearchDto{

	private String taskGroupId;
	
	private Date workDate;

	public String getTaskGroupId() {
		return taskGroupId;
	}

	public void setTaskGroupId(String taskGroupId) {
		this.taskGroupId = taskGroupId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
}
