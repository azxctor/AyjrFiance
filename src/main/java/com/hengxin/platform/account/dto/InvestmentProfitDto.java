package com.hengxin.platform.account.dto;

import java.math.BigDecimal;

public class InvestmentProfitDto {
	
	/**
	 * 预计年化总收益
	 */
	private BigDecimal expectTotalProfit;
	/**
	 * 已实现总收益
	 */
	private BigDecimal realizedTotalRecvProfit;
	/**
	 * 未实现总收益
	 */
	private BigDecimal expectTotalRecvProfit;
	/**
	 * 预计总收益率
	 */
	private BigDecimal expectTotalProfitRate;
	/**
	 * 已实现总收益率
	 */
	private BigDecimal realizedTotalRecvProfitRate;
	
	public BigDecimal getExpectTotalProfit() {
		return expectTotalProfit;
	}
	
	public void setExpectTotalProfit(BigDecimal expectTotalProfit) {
		this.expectTotalProfit = expectTotalProfit;
	}
	
	public BigDecimal getRealizedTotalRecvProfit() {
		return realizedTotalRecvProfit;
	}
	
	public void setRealizedTotalRecvProfit(BigDecimal realizedTotalRecvProfit) {
		this.realizedTotalRecvProfit = realizedTotalRecvProfit;
	}
	
	public BigDecimal getExpectTotalRecvProfit() {
		return expectTotalRecvProfit;
	}
	
	public void setExpectTotalRecvProfit(BigDecimal expectTotalRecvProfit) {
		this.expectTotalRecvProfit = expectTotalRecvProfit;
	}
	
	public BigDecimal getExpectTotalProfitRate() {
		return expectTotalProfitRate;
	}
	
	public void setExpectTotalProfitRate(BigDecimal expectTotalProfitRate) {
		this.expectTotalProfitRate = expectTotalProfitRate;
	}
	
	public BigDecimal getRealizedTotalRecvProfitRate() {
		return realizedTotalRecvProfitRate;
	}
	
	public void setRealizedTotalRecvProfitRate(
			BigDecimal realizedTotalRecvProfitRate) {
		this.realizedTotalRecvProfitRate = realizedTotalRecvProfitRate;
	}
	
}
