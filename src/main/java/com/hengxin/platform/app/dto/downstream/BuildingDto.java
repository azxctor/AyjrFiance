package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * 房产抵押
 *
 */
public class BuildingDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String area;                     //建筑面积
	private String purchasePrice; 			 //购买价格
	private String evaluatePrice;			 //市场评估值
	private String marketPrice;				 //市场价格
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getEvaluatePrice() {
		return evaluatePrice;
	}
	public void setEvaluatePrice(String evaluatePrice) {
		this.evaluatePrice = evaluatePrice;
	}
	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	
	

}
