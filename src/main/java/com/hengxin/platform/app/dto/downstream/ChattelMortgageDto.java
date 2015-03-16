package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * ChattelMortgageDto.
 *
 */
public class ChattelMortgageDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;				 //质押物名称
	private String pledgeClass;			 //品种
	private String type;				 //型号
	private String count;				 //数量
	private String price;				 //价格
	private String location;			 //存放地
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPledgeClass() {
		return pledgeClass;
	}
	public void setPledgeClass(String pledgeClass) {
		this.pledgeClass = pledgeClass;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	

}
