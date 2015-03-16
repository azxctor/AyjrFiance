package com.hengxin.platform.common.dto;

import java.io.Serializable;

/**
 * Class Name: BusinessSigninJournalDto Description: TODO
 * 
 * @author junwei
 * 
 */

public class BusinessSigninJournalDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String workType;

    private String workDate;// 日期时间

    private String operateType;// 类型：开市闭市

    private String crateOperateId;// 操作者
    
    private String createTime;
    
    private String signDate;//签到日期

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getCrateOperateId() {
        return crateOperateId;
    }

    public void setCrateOperateId(String crateOperateId) {
        this.crateOperateId = crateOperateId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }
    
}
