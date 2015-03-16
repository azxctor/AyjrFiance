package com.hengxin.platform.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hengxin.platform.common.entity.converter.NoticeTypeEnumConverter;
import com.hengxin.platform.common.enums.ENoticeType;

@Entity
@Table(name = "GL_NOTICE") 
public class Notice extends BasePo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NOTICE_ID")
    private String noticeId; 
 
    @Column(name = "NOTICE_TITLE")
    private String title;

    @Column(name = "NOTICE_TX")
    private String content; 
    
    @Column(name = "NOTICE_TYPE")
    @Convert(converter = NoticeTypeEnumConverter.class)
    private ENoticeType noticeType;
    
    @Column(name = "NOTICE_FILTER")
    private String showfilter; 
    
    @Column(name = "NOTICE_SHOWMETHOD")
    private String showMethod;  
     
    @Column(name = "END_TS")
    private Long endTime;
  
    @Column(name = "START_TS")
    private Long startTime;
 
    
	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
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

	public ENoticeType getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(ENoticeType noticeType) {
		this.noticeType = noticeType;
	}

	public String getShowfilter() {
		return showfilter;
	}

	public void setShowfilter(String showfilter) {
		this.showfilter = showfilter;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public String getShowMethod() {
		return showMethod;
	}

	public void setShowMethod(String showMethod) {
		this.showMethod = showMethod;
	}

	 
 
    
}
