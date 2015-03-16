package com.hengxin.platform.account.dto.upstream;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: BnkCardBalDto
 * Description: TODO
 * @author jishen
 *
 */

public class BnkCardBalDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 银行卡可取金额
	 */
	private BigDecimal cashableAmt;
	
	/**
	 * 查询是否成功
	 */
	private boolean isSucceed;
	
	/**
	 * 错误信息
	 */
	private String errorMsg;

	public BigDecimal getCashableAmt() {
		return cashableAmt;
	}

	public void setCashableAmt(BigDecimal cashableAmt) {
		this.cashableAmt = cashableAmt;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isSucceed() {
		return isSucceed;
	}

	public void setSucceed(boolean isSucceed) {
		this.isSucceed = isSucceed;
	}
}
