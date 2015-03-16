package com.hengxin.platform.escrow.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.escrow.validator.MobileCheck;

public class EswMobileDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	// 手机号不能为空
	@NotEmpty(groups = { UpdateMobile.class }, message = "{escrow.error.mobile.empty}")
	@MobileCheck(groups = { UpdateMobile.class })
	private String mobile;

	// 验证码不能为空
	@NotEmpty(groups = { UpdateMobile.class }, message = "{escrow.error.authcode.empty}")
	private String authCode;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface UpdateMobile {

	}
}
