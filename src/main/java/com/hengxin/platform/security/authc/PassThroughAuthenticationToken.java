package com.hengxin.platform.security.authc;

import org.apache.shiro.authc.UsernamePasswordToken;

public class PassThroughAuthenticationToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 5L;

	public PassThroughAuthenticationToken(String userName) {
		setUsername(userName);
	}

}
