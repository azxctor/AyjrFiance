package com.hengxin.platform.risk.enums;

/**
 * @deprecated
 * @see ERiskLevel
 */
public enum EProjectRiskLevel {

	CAUTIOUS("A","安全型"),
	STEADY("B","稳健型"),
	NEUTRAL("C","平衡型"),
	AGGRESSIVE("D","进取型");
	
	private String code;

    private String text;
	
	private EProjectRiskLevel(String code, String text){
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
