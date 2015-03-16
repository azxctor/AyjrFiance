package com.hengxin.platform.escrow.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.escrow.validator.OriginalPayPwdCheck;
import com.hengxin.platform.escrow.validator.PayPwdCheck;

/**
 * payPwdDto update
 * 
 * @author chenwulou
 * 
 */
@PayPwdCheck(payPwd = "newPayPwd", confirmPayPwd = "confirmPayPwd", message = "{member.error.password.confirm}")
public class EswPayPwdDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	@NotEmpty(groups = { UpdatePayPwd.class }, message = "{member.error.password.empty}")
	// @Length(min = 1, max = 20, message = "{member.error.password.length}")
	@OriginalPayPwdCheck(groups = { UpdatePayPwd.class })
	private String originalPayPwd;

	@NotEmpty(groups = { UpdatePayPwd.class }, message = "{member.error.password.empty}")
	// @Length(min = 1, max = 20, message = "{member.error.password.length}")
	private String newPayPwd;

	@NotEmpty(groups = { UpdatePayPwd.class }, message = "{member.error.password.empty}")
	// @Length(min = 1, max = 20, message = "{member.error.password.length}")
	private String confirmPayPwd;

	@NotEmpty(groups = { UpdatePayPwd.class }, message = "{escrow.error.authcode.empty}")
	private String authCode;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOriginalPayPwd() {
		return originalPayPwd;
	}

	public void setOriginalPayPwd(String originalPayPwd) {
		this.originalPayPwd = originalPayPwd;
	}

	public String getNewPayPwd() {
		return newPayPwd;
	}

	public void setNewPayPwd(String newPayPwd) {
		this.newPayPwd = newPayPwd;
	}

	public String getConfirmPayPwd() {
		return confirmPayPwd;
	}

	public void setConfirmPayPwd(String confirmPayPwd) {
		this.confirmPayPwd = confirmPayPwd;
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
	public interface UpdatePayPwd {

	}
}
