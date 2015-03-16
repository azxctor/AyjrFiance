package com.hengxin.platform.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hengxin.platform.product.domain.BaseInfo;

@Entity
@Table(name = "GL_BIZ_SIGN")
public class BusinessSigninStatus extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BIZ_TYPE")
    private String businessType;

    @Column(name = "WORK_DT")
    private Date workDate;

    @Column(name = "SIGN_STATUS")
    private String status;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

}
