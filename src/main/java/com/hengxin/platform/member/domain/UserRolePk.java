package com.hengxin.platform.member.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserRolePk implements Serializable {

    private String userId;
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleid(String roleId) {
        this.roleId = roleId;
    }

}
