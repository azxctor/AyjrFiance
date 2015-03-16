package com.hengxin.platform.member.dto;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class MessageCriteria extends DataTablesRequestDto {

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String keyword;
	
	private String showMethod;
	
	/**
	 * 1 : 提醒<br/>
	 * 2 : 待办<br/>
	 * 3 : 提醒  & 待办
	 */
	private String category;
	
	private boolean readable;

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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

    /**
     * @return return the value of the var readable
     */
    
    public boolean isReadable() {
        return readable;
    }

    /**
     * @param readable Set readable value
     */
    
    public void setReadable(boolean readable) {
        this.readable = readable;
    }

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getShowMethod() {
		return showMethod;
	}

	public void setShowMethod(String showMethod) {
		this.showMethod = showMethod;
	}
	
}
