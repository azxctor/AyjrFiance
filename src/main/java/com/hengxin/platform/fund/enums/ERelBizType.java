package com.hengxin.platform.fund.enums;

/**
 * 资金支付业务类型
 * @author tingwang
 *
 */
public enum ERelBizType {

	NULL("", ""), PURCHASE("P", "Purchase"), REFUND("R", "Refund"), PAY("P", "Pay");
    
	private String code;

    private String label;

	private ERelBizType(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
    
}
