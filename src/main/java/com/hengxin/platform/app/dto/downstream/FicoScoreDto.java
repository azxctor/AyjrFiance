package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * FicoScoreDto.
 * 
 */
public class FicoScoreDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String riskLevelCode;				 //风险等级
	private String totalGrage;					 //总评分
	private String productGrage;				 //产品
	private String financierGrage;				 //融资人
	private String warrantGrage;				 //担保人
	public String getRiskLevelCode() {
		return riskLevelCode;
	}
	public void setRiskLevelCode(String riskLevelCode) {
		this.riskLevelCode = riskLevelCode;
	}
	public String getTotalGrage() {
		return totalGrage;
	}
	public void setTotalGrage(String totalGrage) {
		this.totalGrage = totalGrage;
	}
	public String getProductGrage() {
		return productGrage;
	}
	public void setProductGrage(String productGrage) {
		this.productGrage = productGrage;
	}
	public String getFinancierGrage() {
		return financierGrage;
	}
	public void setFinancierGrage(String financierGrage) {
		this.financierGrage = financierGrage;
	}
	public String getWarrantGrage() {
		return warrantGrage;
	}
	public void setWarrantGrage(String warrantGrage) {
		this.warrantGrage = warrantGrage;
	}
	
	

}
