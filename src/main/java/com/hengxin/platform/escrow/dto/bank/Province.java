package com.hengxin.platform.escrow.dto.bank;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Province implements Serializable {

	public Province(String provincecode, String provincename) {
		super();
		this.provincecode = provincecode;
		this.provincename = provincename;
	}

	private String provincecode;

	private String provincename;

	public String getProvincecode() {
		return provincecode;
	}

	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

}
