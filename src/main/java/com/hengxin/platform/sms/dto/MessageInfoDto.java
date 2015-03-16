package com.hengxin.platform.sms.dto;

import java.io.Serializable;
import com.hengxin.platform.member.enums.EMessageType;

/**
 * 
 * MessageInfo DTO.
 *
 */
public class MessageInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private EMessageType messageType;
	
	private String messageKey;
	
	private String userId;
	
	private String name;
	
	private String userName;
	
	private String content;

	public EMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(EMessageType messageType) {
		this.messageType = messageType;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
