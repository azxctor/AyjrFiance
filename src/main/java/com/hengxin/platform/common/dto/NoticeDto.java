package com.hengxin.platform.common.dto;

import java.io.Serializable;

/**
 * 
 * Notice DTO.
 *
 */
public class NoticeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String noticeId;
	
	private String showfilter;
	
	private String createTs;
	
	private String type; 
	
	private String title;
	
	private String content;

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getShowfilter() {
		return showfilter;
	}

	public void setShowfilter(String showfilter) {
		this.showfilter = showfilter;
	}

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
 
 
}
