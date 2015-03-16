package com.hengxin.platform.member.dto;

import java.io.Serializable;

public class MaskFixedDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean nameFixed;
	
	private boolean mobileFixed;
	
	private boolean idCardFixed;
	
	private boolean bankCardFixed;
	
	private boolean applicationFormFixed;

	/** 
	 * 介绍人  而不是  经办人 ;
	 * Dailiren   NOT  Jingbanren; 
	 * AgentName rather than Agent.
	 * **/
	private boolean agentNameFixed;

	/**
	 * @return the nameFixed
	 */
	public boolean isNameFixed() {
		return nameFixed;
	}

	/**
	 * @param nameFixed the nameFixed to set
	 */
	public void setNameFixed(boolean nameFixed) {
		this.nameFixed = nameFixed;
	}

	/**
	 * @return the mobileFixed
	 */
	public boolean isMobileFixed() {
		return mobileFixed;
	}

	/**
	 * @param mobileFixed the mobileFixed to set
	 */
	public void setMobileFixed(boolean mobileFixed) {
		this.mobileFixed = mobileFixed;
	}

	/**
	 * @return the idCardFixed
	 */
	public boolean isIdCardFixed() {
		return idCardFixed;
	}

	/**
	 * @param idCardFixed the idCardFixed to set
	 */
	public void setIdCardFixed(boolean idCardFixed) {
		this.idCardFixed = idCardFixed;
	}

	/**
	 * @return the bankCardFixed
	 */
	public boolean isBankCardFixed() {
		return bankCardFixed;
	}

	/**
	 * @param bankCardFixed the bankCardFixed to set
	 */
	public void setBankCardFixed(boolean bankCardFixed) {
		this.bankCardFixed = bankCardFixed;
	}

	/**
	 * @return the applicationFormFixed
	 */
	public boolean isApplicationFormFixed() {
		return applicationFormFixed;
	}

	/**
	 * @param applicationFormFixed the applicationFormFixed to set
	 */
	public void setApplicationFormFixed(boolean applicationFormFixed) {
		this.applicationFormFixed = applicationFormFixed;
	}

	/**
	 * @return the agentNameFixed
	 */
	public boolean isAgentNameFixed() {
		return agentNameFixed;
	}

	/**
	 * @param agentNameFixed the agentNameFixed to set
	 */
	public void setAgentNameFixed(boolean agentNameFixed) {
		this.agentNameFixed = agentNameFixed;
	}
	
}
