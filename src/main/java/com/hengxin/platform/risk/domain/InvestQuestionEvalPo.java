package com.hengxin.platform.risk.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SuppressWarnings("serial")
@Table(name = "RK_INVS_QUES_EVAL")
@IdClass(InvestQuestionEvalPoPk.class)
public class InvestQuestionEvalPo implements Serializable {
	
	@Id
	@Column(name = "USER_ID")
    private String userId;
	
	@Id
	@Column(name = "WORK_DT")
	@Temporal(TemporalType.DATE)
	private Date workDate;
	
	@Column(name = "QUES_SCORE")
	private BigDecimal quesScore;
	
	@Column(name = "BEAR_SCORE")
	private BigDecimal bearScore;
	
	@Column(name = "CREATE_OPID")
	private String createOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public BigDecimal getQuesScore() {
		return quesScore;
	}

	public void setQuesScore(BigDecimal quesScore) {
		this.quesScore = quesScore;
	}

	public BigDecimal getBearScore() {
		return bearScore;
	}

	public void setBearScore(BigDecimal bearScore) {
		this.bearScore = bearScore;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}
	
}
