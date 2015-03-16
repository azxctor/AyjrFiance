package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class ReverseApplSearchDto extends DataTablesRequestDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rvsJnlNo;

	public String getRvsJnlNo() {
		return rvsJnlNo;
	}

	public void setRvsJnlNo(String rvsJnlNo) {
		this.rvsJnlNo = rvsJnlNo;
	}
	
}
