package com.hengxin.platform.member.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Message DTO.
 *
 */
public class MessageIdListDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<String> messageIdList;

    /**
     * @return return the value of the var messageIdList
     */
    
    public List<String> getMessageIdList() {
        return messageIdList;
    }

    /**
     * @param messageIdList Set messageIdList value
     */
    
    public void setMessageIdList(List<String> messageIdList) {
        this.messageIdList = messageIdList;
    }

}
