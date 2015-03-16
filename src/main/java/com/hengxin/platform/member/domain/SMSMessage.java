package com.hengxin.platform.member.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;

@Entity
@Table(name = "UM_SMS_MSG")
@EntityListeners(IdInjectionEntityListener.class)
public class SMSMessage {

    @Id
    @Column(name = "MSG_ID")
    private String id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SEND_TS", insertable = true, updatable = false)
    private Date sendTs;
    
    @Column(name = "MOBILE", insertable = true, updatable = false)
    private String mobile;

    @Column(name="USER_ID", insertable = true, updatable = false)
    private String userId;
    
    @Column(name = "MSG_BODY", insertable = true, updatable = false)
    private String message;
    
    @Column(name = "STATUS")
    private String status;
    
    @Column(name="CREATE_OPID", insertable = true, updatable = false)
    private String creatorOpId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS", insertable = true, updatable = false)
    private Date createTs;
    
    @Column(name="LAST_MNT_OPID", insertable = true, updatable = true)
    private String lastMntOpid;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS", insertable = true, updatable = true)
    private Date lastMntTs;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the sendTs
	 */
	public Date getSendTs() {
		return sendTs;
	}

	/**
	 * @param sendTs the sendTs to set
	 */
	public void setSendTs(Date sendTs) {
		this.sendTs = sendTs;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the creatorOpId
	 */
	public String getCreatorOpId() {
		return creatorOpId;
	}

	/**
	 * @param creatorOpId the creatorOpId to set
	 */
	public void setCreatorOpId(String creatorOpId) {
		this.creatorOpId = creatorOpId;
	}

	/**
	 * @return the createTs
	 */
	public Date getCreateTs() {
		return createTs;
	}

	/**
	 * @param createTs the createTs to set
	 */
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
    
    
}
