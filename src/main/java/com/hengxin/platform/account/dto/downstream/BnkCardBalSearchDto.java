package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Class Name: BnkCardBalSearchDto
 * Description: TODO
 * @author jishen
 *
 */

public class BnkCardBalSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 银行卡号
	 */
    @NotNull
	private String bnkAcctNo;

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}
}
