package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.product.domain.converter.BooleanStringConverter;

/**
 * Class Name: SubscribeGroup 申购组 Description.
 * 
 * @author junwei
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_GRP")
@SequenceGenerator(name="group_seq", sequenceName="UM_GRP_GRP_ID_SEQ", allocationSize=1)
public class SubscribeGroup implements Serializable {

    @Id
    @Column(name = "GRP_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq")
    private Integer groupId;

    @Column(name = "GRP_NAME")
    private String groupName;

    @Column(name = "GRP_DESC")
    private String groupDescribe;

    @Column(name = "GRP_STATUS")
    @Convert(converter = BooleanStringConverter.class)
    private Boolean status = true;
    
    @Deprecated
    @Column(name = "GRP_TYPE")
    private String groupType;

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
    
    @OneToMany(mappedBy="groupId", fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval=true)
    private Set<SubscribeGroupUser> users = new HashSet<SubscribeGroupUser>();
    
    public void addUser(SubscribeGroupUser user) {
    	user.setGroupId(this);
    	this.users.add(user);
    }
    
	public Set<SubscribeGroupUser> getUsers() {
		return users;
	}

    public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescribe() {
        return groupDescribe;
    }

    public void setGroupDescribe(String groupDescribe) {
        this.groupDescribe = groupDescribe;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
    
}
