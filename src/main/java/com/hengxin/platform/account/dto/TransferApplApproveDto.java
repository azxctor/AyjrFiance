package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.util.List;

import com.hengxin.platform.fund.enums.EFundApplStatus;


/**
 * Class Name: TransferApplApproveDto
 * 
 * @author jishen
 * 
 */
public class TransferApplApproveDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 申请编号集合
	 */
	private List<String> applNos;
	/**
	 * 审批状态
	 */
	private EFundApplStatus applStatus;
	
	/**
	 * 处理备注
	 */
	private String dealMemo;

	public List<String> getApplNos() {
		return applNos;
	}

	public void setApplNos(List<String> applNos) {
		this.applNos = applNos;
	}

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public EFundApplStatus getApplStatus() {
		return applStatus;
	}

	public void setApplStatus(EFundApplStatus applStatus) {
		this.applStatus = applStatus;
	}
}
