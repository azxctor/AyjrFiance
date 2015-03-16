package com.hengxin.platform.escrow.dto.bank;

public class Bank {

	public Bank(String bankcode, String bankname) {
		super();
		this.bankcode = bankcode;
		this.bankname = bankname;
	}

	private String bankcode;

	private String bankname;

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

}
