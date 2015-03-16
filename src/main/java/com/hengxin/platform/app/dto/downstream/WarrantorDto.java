package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * WarrantorDto.
 */
public class WarrantorDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String warrantPersonStr;				 //自然人
	private String warrantEnterpriseStr;			 //法人
	public String getWarrantPersonStr() {
		return warrantPersonStr;
	}
	public void setWarrantPersonStr(String warrantPersonStr) {
		this.warrantPersonStr = warrantPersonStr;
	}
	public String getWarrantEnterpriseStr() {
		return warrantEnterpriseStr;
	}
	public void setWarrantEnterpriseStr(String warrantEnterpriseStr) {
		this.warrantEnterpriseStr = warrantEnterpriseStr;
	}
	
	

}
