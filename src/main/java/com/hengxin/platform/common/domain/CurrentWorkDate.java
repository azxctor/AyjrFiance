package com.hengxin.platform.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GL_CUR_WRK_DT")
public class CurrentWorkDate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXCH_NO")
	private String exchangeNo;

	@Column(name = "CUR_WORK_DT")
	private Date currentWorkDate;
	
	@Column(name = "PRE_WORK_DT")
	private Date preWorkDate;
	
	@Column(name = "NEXT_WORK_DT")
	private Date nextWorkDate;

	public String getExchangeNo() {
		return exchangeNo;
	}

	public void setExchangeNo(String exchangeNo) {
		this.exchangeNo = exchangeNo;
	}

	public Date getCurrentWorkDate() {
		return currentWorkDate;
	}

	public void setCurrentWorkDate(Date currentWorkDate) {
		this.currentWorkDate = currentWorkDate;
	}

	public Date getPreWorkDate() {
		return preWorkDate;
	}

	public void setPreWorkDate(Date preWorkDate) {
		this.preWorkDate = preWorkDate;
	}

	public Date getNextWorkDate() {
		return nextWorkDate;
	}

	public void setNextWorkDate(Date nextWorkDate) {
		this.nextWorkDate = nextWorkDate;
	}

}
