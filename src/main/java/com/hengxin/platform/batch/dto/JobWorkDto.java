package com.hengxin.platform.batch.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hengxin.platform.batch.entity.JobWork;

public class JobWorkDto {
	
	private String taskGroupId;
	
	private Date workDate;
	
	private Boolean execBtnEnable;
	
	private Boolean createBtnEnable;
	
	private Date currentWorkDate;
	
	private String retUrl;
	
	private List<JobWork> list = new ArrayList<JobWork>();

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

	public Boolean getExecBtnEnable() {
		return execBtnEnable;
	}

	public void setExecBtnEnable(Boolean execBtnEnable) {
		this.execBtnEnable = execBtnEnable;
	}

	public List<JobWork> getList() {
		return list;
	}

	public void setList(List<JobWork> list) {
		this.list = list;
	}

	public Date getCurrentWorkDate() {
		return currentWorkDate;
	}

	public void setCurrentWorkDate(Date currentWorkDate) {
		this.currentWorkDate = currentWorkDate;
	}

	public Boolean getCreateBtnEnable() {
		return createBtnEnable;
	}

	public void setCreateBtnEnable(Boolean createBtnEnable) {
		this.createBtnEnable = createBtnEnable;
	}

	public String getRetUrl() {
		return retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}
	
}
