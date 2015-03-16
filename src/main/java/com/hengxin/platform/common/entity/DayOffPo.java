package com.hengxin.platform.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "GL_DAY_OFF") 
public class DayOffPo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "WORK_DT")
    private Date workDate;
    
    @Column(name = "DF_TYPE")
    private String dayOffType;
    
    @Column(name = "MEMO")
    private String memo;
	
    @Column(name="LAST_MNT_OPID")
    private String lastMntOpId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;
	
    @Column(name="CREATE_OPID")
    private String creatorOpId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;

    @Version
    @Column(name = "VERSION_CT")
    private Long versionCt;

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public String getDayOffType() {
		return dayOffType;
	}

	public void setDayOffType(String dayOffType) {
		this.dayOffType = dayOffType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getLastMntOpId() {
		return lastMntOpId;
	}

	public void setLastMntOpId(String lastMntOpId) {
		this.lastMntOpId = lastMntOpId;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public String getCreatorOpId() {
		return creatorOpId;
	}

	public void setCreatorOpId(String creatorOpId) {
		this.creatorOpId = creatorOpId;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}
    
}
