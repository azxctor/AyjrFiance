package com.hengxin.platform.escrow.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 第三方托管账户激活页面传入信息
 * 
 * @author chenwulou
 * 
 */
public class EswSignupDto implements Serializable {

	private static final long serialVersionUID = 1L;

	// @UserIdCheck()
	private String userId;

	@NotEmpty(groups = { SubmitSignup.class }, message = "{member.error.field.empty}")
	private String payPwd;

	@NotEmpty(groups = { SubmitSignup.class }, message = "{member.error.field.empty}")
	private String confirmPayPwd;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getConfirmPayPwd() {
		return confirmPayPwd;
	}

	public void setConfirmPayPwd(String confirmPayPwd) {
		this.confirmPayPwd = confirmPayPwd;
	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface SubmitSignup {

	}

}
