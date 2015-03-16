package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * PersonalLiabilityDto.
 *
 */
public class PersonalLiabilityDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dtype;				 //负债类型
	private String debtAmount;			 //负债额
	private String monthlyPayment;		 //每月还款额
	private String notes;				 //说明
	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	public String getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(String debtAmount) {
		this.debtAmount = debtAmount;
	}
	public String getMonthlyPayment() {
		return monthlyPayment;
	}
	public void setMonthlyPayment(String monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	

}
