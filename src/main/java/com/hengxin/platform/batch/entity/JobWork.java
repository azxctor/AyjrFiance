package com.hengxin.platform.batch.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.batch.entity.converter.EJobWorkStatusConverter;
import com.hengxin.platform.batch.enums.EJobWorkStatus;

/**
 * Class Name: JobWork Description:
 * 
 * @author junwei
 * 
 */

@Entity
@Table(name = "GL_JOB_WRK")
public class JobWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "JOB_WRK_ID")
    private String workId;

    @Column(name = "JOB_ID")
    private String jobId;
    
    @Column(name = "JOB_NAME")
    private String jobName;
    
    @Column(name = "GRP_ID")
    private String taskGroupId;

    @Column(name = "SEQ_NO")
    private int seqNum;

    @Temporal(TemporalType.DATE)
    @Column(name = "WORK_DT")
    private Date workDate;

    @Column(name = "EXEC_STATUS")
    @Convert(converter = EJobWorkStatusConverter.class)
    private EJobWorkStatus execStatus;

    @Column(name = "EXEC_RESULT")
    private String execResult;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TS")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TS")
    private Date endTime;

    @Column(name = "CREATE_OPID")
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTs;

    @Column(name = "LAST_MNT_OPID")
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getExecResult() {
        return execResult;
    }

    public void setExecResult(String execResult) {
        this.execResult = execResult;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getLastMntOpid() {
        return lastMntOpid;
    }

    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    public Date getLastMntTs() {
        return lastMntTs;
    }

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

	public String getTaskGroupId() {
		return taskGroupId;
	}

	public void setTaskGroupId(String taskGroupId) {
		this.taskGroupId = taskGroupId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public EJobWorkStatus getExecStatus() {
		return execStatus;
	}

	public void setExecStatus(EJobWorkStatus execStatus) {
		this.execStatus = execStatus;
	}

}
