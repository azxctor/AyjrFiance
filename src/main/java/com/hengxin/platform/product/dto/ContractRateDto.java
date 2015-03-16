package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

public class ContractRateDto implements Serializable{
private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String contractId;
	
	private String contractName;
	
	@NotEmpty
	private String productLevelId;
	
	private String productLevelTextShort;
	
	/**
	 *  融资人提前还款违约金率  及用于页面显示的String类型.
	 */
	@NotEmpty
	@Range(min=0,max=1)
	private BigDecimal financierPrepaymentPenaltyRate;
	
	private String financierRate;
	
	/**
	 * 平台提前还款违约金率 及用于页面显示的String类型.
	 */
	@NotEmpty
	@Range(min=0,max=1)
	private BigDecimal platformPrepaymentPenaltyRate; 
	
	private String platformRate;
	
	/**
	 * 还款违约时计算罚金的违约金率（滞纳金）  及用于页面显示的String类型.
	 */
	@NotEmpty
	@Range(min=0,max=1)
	private BigDecimal paymentPenaltyFineRate;
	
	private String paymentRate;
	
	@NotEmpty
	private boolean deductFlg;
	
	
	public String getContractId() {
		return contractId;
	}


	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	
	public String getContractName() {
		return contractName;
	}


	public void setContractName(String contractName) {
		this.contractName = contractName;
	}


	public String getProductLevelId() {
		return productLevelId;
	}


	public void setProductLevelId(String productLevelId) {
		this.productLevelId = productLevelId;
	}


	public String getProductLevelTextShort() {
		return productLevelTextShort;
	}


	public void setProductLevelTextShort(String productLevelTextShort) {
		this.productLevelTextShort = productLevelTextShort;
	}


	public BigDecimal getFinancierPrepaymentPenaltyRate() {
		return financierPrepaymentPenaltyRate;
	}



	public void setFinancierPrepaymentPenaltyRate(
			BigDecimal financierPrepaymentPenaltyRate) {
		this.financierPrepaymentPenaltyRate = financierPrepaymentPenaltyRate;
	}


	public BigDecimal getPlatformPrepaymentPenaltyRate() {
		return platformPrepaymentPenaltyRate;
	}



	public void setPlatformPrepaymentPenaltyRate(
			BigDecimal platformPrepaymentPenaltyRate) {
		this.platformPrepaymentPenaltyRate = platformPrepaymentPenaltyRate;
	}



	public BigDecimal getPaymentPenaltyFineRate() {
		return paymentPenaltyFineRate;
	}



	public void setPaymentPenaltyFineRate(BigDecimal paymentPenaltyFineRate) {
		this.paymentPenaltyFineRate = paymentPenaltyFineRate;
	}


	public boolean isDeductFlg() {
		return deductFlg;
	}


	public void setDeductFlg(boolean deductFlg) {
		this.deductFlg = deductFlg;
	}


	public String getFinancierRate() {
		return financierRate;
	}


	public void setFinancierRate(String financierRate) {
		this.financierRate = financierRate;
	}


	public String getPlatformRate() {
		return platformRate;
	}


	public void setPlatformRate(String platformRate) {
		this.platformRate = platformRate;
	}


	public String getPaymentRate() {
		return paymentRate;
	}


	public void setPaymentRate(String paymentRate) {
		this.paymentRate = paymentRate;
	}
}
