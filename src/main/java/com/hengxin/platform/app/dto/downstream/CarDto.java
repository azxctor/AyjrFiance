package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * CarDto.
 *
 */
public class CarDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String brand;				 //车辆品牌
	private String type;				 //车辆型号
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
