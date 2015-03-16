package com.hengxin.platform.member.domain;

import java.io.Serializable;

/**
 * SubscribeGroupUser PK.
 *
 */
public class SubscribeGroupUserPK implements Serializable {

	private static final long serialVersionUID = 1L;

    private Integer groupId;

    private String userId;

	public SubscribeGroupUserPK() {
		super();
	}

	public SubscribeGroupUserPK(Integer groupId, String userId) {
		super();
		this.groupId = groupId;
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + groupId;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SubscribeGroupUserPK other = (SubscribeGroupUserPK) obj;
		if (!groupId.equals(other.groupId)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the groupId
	 */
	public Integer getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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

    
}
