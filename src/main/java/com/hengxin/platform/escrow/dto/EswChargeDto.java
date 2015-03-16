package com.hengxin.platform.escrow.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.hengxin.platform.escrow.enums.EEbcUserType;



/**
 * 
 * @author juhuahuang
 * 
 */
public class EswChargeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productDesc; // 产品描述(备注)
	
	@NotNull(message = "{escrow.error.amount.empty}")
	private BigDecimal amount;	//金额
	
	private String ebcBankId; 	// ebc发卡行ID
	
	private EEbcUserType userType;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getEbcBankId() {
		return ebcBankId;
	}

	public void setEbcBankId(String ebcBankId) {
		this.ebcBankId = ebcBankId;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public EEbcUserType getUserType() {
		return userType;
	}

	public void setUserType(EEbcUserType userType) {
		this.userType = userType;
	}
	
}
