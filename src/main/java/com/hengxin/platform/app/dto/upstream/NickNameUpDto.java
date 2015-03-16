package com.hengxin.platform.app.dto.upstream;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.dto.upstream.RegisterDto.RegisterGroup;
import com.hengxin.platform.common.service.validator.ExistUserNameAndIdCheck;
import com.hengxin.platform.common.service.validator.ExistUserNameCheck;
import com.hengxin.platform.common.service.validator.UserNameLengthCheck;
import com.hengxin.platform.security.domain.User.NickNameGroup;

/**
 * 用户名修改上行DTO.
 * 
 * @author yicai
 *
 */
public class NickNameUpDto implements Serializable {
	private static final long serialVersionUID = 5L;
	
	
	@NotEmpty(message = "{member.error.username.empty}", groups = { RegisterGroup.class, NickNameGroup.class})
	//  @Length(min = 4, max=15, message = "{member.error.username.length}", groups = { RegisterGroup.class, NickNameGroup.class })
	@UserNameLengthCheck(groups = { RegisterGroup.class, NickNameGroup.class })
	@Pattern(regexp=ApplicationConstant.USER_NAME_REGEXP, groups = { RegisterGroup.class, NickNameGroup.class }, message = "{member.error.username.format}")
	@ExistUserNameCheck(groups = { RegisterGroup.class})
	@ExistUserNameAndIdCheck(groups = {NickNameGroup.class})
	private String username;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
