package com.hengxin.platform.member.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.member.enums.EInvestorLevel;
import com.hengxin.platform.risk.enums.ERiskBearLevel;

public class InvestorLevelDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean display;

	private ERiskBearLevel riskBearLevel;
	
	private EInvestorLevel level;
	
	private BigDecimal rate;
	
	private String desc;

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public ERiskBearLevel getRiskBearLevel() {
		return riskBearLevel;
	}

	public void setRiskBearLevel(ERiskBearLevel riskBearLevel) {
		this.riskBearLevel = riskBearLevel;
	}

	public EInvestorLevel getLevel() {
		return level;
	}

	public void setLevel(EInvestorLevel level) {
		this.level = level;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
