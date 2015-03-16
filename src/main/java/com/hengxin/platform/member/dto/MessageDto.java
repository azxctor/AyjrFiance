package com.hengxin.platform.member.dto;

import java.io.Serializable;

/**
 * 
 * Message DTO.
 *
 */
public class MessageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageId;
	
	private String userId;
	
	private String createTs;
	
	private String type;
	
	private boolean read;
	
	private String title;
	
	/**
	 * If it is a rich message, add URL in it.
	 */
	private String message;
	
	private String url;

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
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

	/**
     * @return return the value of the var createTs
     */
    
    public String getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs Set createTs value
     */
    
    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the read
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @param read the read to set
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}
