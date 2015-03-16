package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * PersonalAssetDto.
 *
 */
public class PersonalAssetDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dtype;					 //资产类型
	private String assertAmount;			 //资产额    注意格式化
	private String notes;					 //说明
	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	public String getAssertAmount() {
		return assertAmount;
	}
	public void setAssertAmount(String assertAmount) {
		this.assertAmount = assertAmount;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
}
