package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_GRP_USER")
@IdClass(SubscribeGroupUserPK.class)
public class SubscribeGroupUser implements Serializable{

	@Id
	@OneToOne
	@JoinColumn(name = "GRP_ID", insertable = true, updatable = false)
    private SubscribeGroup groupId;

    @Id
    @Column(name = "USER_ID")
    private String userId;

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
    
    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private InvestorUserInfo user;
    
	public SubscribeGroup getGroupId() {
		return groupId;
	}

	public void setGroupId(SubscribeGroup groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public InvestorUserInfo getUser() {
		return user;
	}

	public void setUser(InvestorUserInfo user) {
		this.user = user;
	}

}
