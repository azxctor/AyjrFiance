package com.hengxin.platform.escrow.dto.bank;

public class PayeeBank {

	public PayeeBank(String payeebankcode, String payeebankname) {
		super();
		this.payeebankcode = payeebankcode;
		this.payeebankname = payeebankname;
	}

	private String payeebankcode;

	private String payeebankname;

	public String getPayeebankcode() {
		return payeebankcode;
	}

	public void setPayeebankcode(String payeebankcode) {
		this.payeebankcode = payeebankcode;
	}

	public String getPayeebankname() {
		return payeebankname;
	}

	public void setPayeebankname(String payeebankname) {
		this.payeebankname = payeebankname;
	}

}
