package com.hengxin.platform.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Batch DTO.
 *
 */
public class IdListDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<String> idList;

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}

     

}
