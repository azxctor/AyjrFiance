package com.hengxin.platform.member.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.hengxin.platform.common.service.validator.SubscriberGroupCheck;

/**
 * Class Name: SubscribeGroupDto Description: TODO
 * 
 * @author junwei
 * 
 */
@SubscriberGroupCheck
public class SubscribeGroupDto extends BaseApplicationDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer groupId;

    @Length(max = 100)
    private String groupName;

    @Length(max = 200)
    private String groupDescribe;

    @Deprecated
    private String groupType;

    private boolean actvie;

    private String renderUser;
    
    private List<SkinnyUserDto> users = new ArrayList<SkinnyUserDto>();

    private List<String> serviceCenterIds = new ArrayList<String>();

    @Override
	public String toString() {
		return "SubscribeGroupDto [groupId=" + groupId + ", groupName="
				+ groupName + ", groupDescribe=" + groupDescribe
				+ ", groupType=" + groupType + ", users=" + users.size() + "]";
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

    /**
     * @return the users
     */
    public List<SkinnyUserDto> getUsers() {
        return users;
    }

    /**
     * @param users
     *            the users to set
     */
    public void setUsers(List<SkinnyUserDto> users) {
        this.users = users;
    }

	/**
	 * @return the renderUser
	 */
	public String getRenderUser() {
		return renderUser;
	}

	/**
	 * @param renderUser the renderUser to set
	 */
	public void setRenderUser(String renderUser) {
		this.renderUser = renderUser;
	}

	/**
	 * @return the serviceCenterIds
	 */
	public List<String> getServiceCenterIds() {
		return serviceCenterIds;
	}

	/**
	 * @param serviceCenterIds the serviceCenterIds to set
	 */
	public void setServiceCenterIds(List<String> serviceCenterIds) {
		this.serviceCenterIds = serviceCenterIds;
	}

	public boolean isActvie() {
		return actvie;
	}

	public void setActvie(boolean actvie) {
		this.actvie = actvie;
	}

}
