/**
 * 
 */
package com.hengxin.platform.product.dto;

import java.io.Serializable;

/**
 * @author shuyingwu
 *
 */
public class ApprovalRemarksDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String throughNotes;//通过备注
	
	private String dismissedNotes;//驳回备注
	
	private String returnReason;//退回理由

	public String getThroughNotes() {
		return throughNotes;
	}

	public void setThroughNotes(String throughNotes) {
		this.throughNotes = throughNotes;
	}

	public String getDismissedNotes() {
		return dismissedNotes;
	}

	public void setDismissedNotes(String dismissedNotes) {
		this.dismissedNotes = dismissedNotes;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	
}
