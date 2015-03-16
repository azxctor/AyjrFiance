package com.hengxin.platform.member.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class GroupSearchDto extends DataTablesRequestDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String groupId;

    private String groupName;
    
    private String status;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
