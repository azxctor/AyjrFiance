package com.hengxin.platform.app.dto.upstream;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.service.validator.OriginalPasswordCheck;
import com.hengxin.platform.common.service.validator.PasswordCheck;

/**
 * 密码修改上行DTO.
 * 
 * @author yicai
 *
 */
@PasswordCheck(password = "newPassword", passwordConfirm = "confirmPassword", message = "{member.error.password.confirm}")
public class PasswordV2UpDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "{member.error.password.empty}")
    @Length(min = 1, max = 20, message = "{member.error.password.length}")
	@OriginalPasswordCheck()
	private String originalPassword;
	
	@NotEmpty(message = "{member.error.password.empty}")
    @Length(min = 1, max = 20, message = "{member.error.password.length}")
	private String newPassword;
	
	@NotEmpty(message = "{member.error.password.empty}")
    @Length(min = 1, max = 20, message = "{member.error.password.length}")
	private String confirmPassword;

	public String getOriginalPassword() {
		return originalPassword;
	}

	public void setOriginalPassword(String originalPassword) {
		this.originalPassword = originalPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
