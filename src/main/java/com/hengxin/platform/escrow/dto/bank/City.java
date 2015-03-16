package com.hengxin.platform.escrow.dto.bank;

public class City {

	public City(String citycode, String cityname) {
		super();
		this.citycode = citycode;
		this.cityname = cityname;
	}

	private String citycode;

	private String cityname;

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

}
