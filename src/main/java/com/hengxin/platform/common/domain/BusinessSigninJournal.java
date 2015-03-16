package com.hengxin.platform.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;

/**
 * Class Name: BusinessSigninJournal Description: TODO
 * 
 * @author junwei
 * 
 */

@Entity
@Table(name = "GL_BIZ_SIGN_JNL")
@EntityListeners(IdInjectionEntityListener.class)
public class BusinessSigninJournal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "JNL_NO")
    private String serialNumber;

    @Column(name = "BIZ_TYPE")
    private String workType;

    @Column(name = "WORK_DT")
    @Temporal(TemporalType.DATE)
    private Date workDate;
    
    @Column(name = "SIGN_DT")
    @Temporal(TemporalType.DATE)
    private Date signDate;

    @Column(name = "OPER_TYPE")
    private String operateType;

    @Column(name = "OPER_DT")
    @Temporal(TemporalType.DATE)
    private Date operateDate;

    @Column(name = "CREATE_OPID")
    private String crateOperateId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
    private Date createTime;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getCrateOperateId() {
        return crateOperateId;
    }

    public void setCrateOperateId(String crateOperateId) {
        this.crateOperateId = crateOperateId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

}
