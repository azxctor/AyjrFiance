package com.hengxin.platform.account.dto.downstream;

public class ReverseReqDto {

	private String rvsJnlNo;
	
	private String trxMemo;

	public String getRvsJnlNo() {
		return rvsJnlNo;
	}

	public void setRvsJnlNo(String rvsJnlNo) {
		this.rvsJnlNo = rvsJnlNo;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}
	
}
