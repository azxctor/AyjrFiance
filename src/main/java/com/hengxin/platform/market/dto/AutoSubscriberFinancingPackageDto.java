package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AutoSubscriberFinancingPackageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; // 融资包编号
	
	private String productName;  //融资包名称
	
	private String financingRealName;  //融资人名称
	
	private String financingName;  //融资人
	
	private String warrantor; // 担保机构 (发包方)
	
	private String warrantorShow; //
	
	private BigDecimal packageQuota; //融资包金额
	
	private BigDecimal availableQuota; //可融金额
	
	private BigDecimal supplyAmount;	//已融金额
	
	/**
	 * nested product for sort column. 
	 */
	private SkinnyMarketProductDto product;
	
	/**
	 * @deprecated
	 */
	private String rate; // 年利率
	
	/**
	 * @deprecated
	 */
	private String termLength; // 期限长度
	
	private String payMethod; // 还款方式 01-到期一次还本付息,02-按月等额还息，到期一次还本, 03-按月等本等息
	
	private String status;
	
	private String percent;
	
	private String region;
	
	private String industry;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the financingName
	 */
	public String getFinancingName() {
		return financingName;
	}

	/**
	 * @param financingName the financingName to set
	 */
	public void setFinancingName(String financingName) {
		this.financingName = financingName;
	}

	/**
	 * @return the warrantor
	 */
	public String getWarrantor() {
		return warrantor;
	}

	/**
	 * @param warrantor the warrantor to set
	 */
	public void setWarrantor(String warrantor) {
		this.warrantor = warrantor;
	}

	/**
	 * @return the packageQuota
	 */
	public BigDecimal getPackageQuota() {
		return packageQuota;
	}

	/**
	 * @param packageQuota the packageQuota to set
	 */
	public void setPackageQuota(BigDecimal packageQuota) {
		this.packageQuota = packageQuota;
	}

	/**
	 * @return the availableQuota
	 */
	public BigDecimal getAvailableQuota() {
		return availableQuota;
	}

	/**
	 * @param availableQuota the availableQuota to set
	 */
	public void setAvailableQuota(BigDecimal availableQuota) {
		this.availableQuota = availableQuota;
	}

	/**
	 * @return the supplyAmount
	 */
	public BigDecimal getSupplyAmount() {
		return supplyAmount;
	}

	/**
	 * @param supplyAmount the supplyAmount to set
	 */
	public void setSupplyAmount(BigDecimal supplyAmount) {
		this.supplyAmount = supplyAmount;
	}

	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

	/**
	 * @return the termLength
	 */
	public String getTermLength() {
		return termLength;
	}

	/**
	 * @param termLength the termLength to set
	 */
	public void setTermLength(String termLength) {
		this.termLength = termLength;
	}

	/**
	 * @return the payMethod
	 */
	public String getPayMethod() {
		return payMethod;
	}

	/**
	 * @param payMethod the payMethod to set
	 */
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the percent
	 */
	public String getPercent() {
		return percent;
	}

	/**
	 * @param percent the percent to set
	 */
	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getFinancingRealName() {
		return financingRealName;
	}

	public void setFinancingRealName(String financingRealName) {
		this.financingRealName = financingRealName;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}

	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}

	/**
	 * @return the product
	 */
	public SkinnyMarketProductDto getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(SkinnyMarketProductDto product) {
		this.product = product;
	}

	public String getWarrantorShow() {
		return warrantorShow;
	}

	public void setWarrantorShow(String warrantorShow) {
		this.warrantorShow = warrantorShow;
	}

}
