package com.hengxin.platform.report.dto;

import java.math.BigDecimal;

/**
 * 
 * @author juhuahuang
 *
 */
public class PlatformOperDataDto extends TimeSelectDto {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 1、成交金额（万元）
	 */
	private BigDecimal quotoAmt;
	private BigDecimal quotoAmtMon;
	/**
	 * 2、融资人融资项目数（笔）
	 */
	private Integer pkgAmt;
	private Integer pkgAmtMon;
	
	/**
	 * 单笔0-100万的融资项目数
	 */
	private Integer pkgAmtLessOne;
	private Integer pkgAmtLessOneMon;
	
	/**
	 * 单笔100-300万的融资项目数
	 */
	private Integer pkgAmtLessThree;
	private Integer pkgAmtLessThreeMon;
	
	/**
	 * 单笔300万以上的融资项目数
	 */
	private Integer pkgAmtMoreThree;
	private Integer pkgAmtMoreThreeMon;
	/**
	 * 3、投资人借出笔数（笔）
	 */
	private Integer lendAmt;
	private Integer lendAmtMon;
	/**
	 * 单笔0-100万的借出笔数
	 */
	private Integer lendAmtLessOne;
	private Integer lendAmtLessOneMon;
	/**
	 * 单笔100-300万的借出笔数
	 */
	private Integer lendAmtLessThree;
	private Integer lendAmtLessThreeMon;
	/**
	 * 单笔300万以上的借出笔数
	 */
	private Integer lendAmtMoreThree;
	private Integer lendAmtMoreThreeMon;
	/**
	 * 4、项目平均融资额
	 */
	
	private BigDecimal avgPkgAmt;
	private BigDecimal avgPkgAmtMon;
	/**
	 * 5、平均借款金额
	 */
	private BigDecimal avgLendAmt;
	private BigDecimal avgLendAmtMon;
	/**
	 * 6、单一项目最高融资额
	 */
	private BigDecimal maxPkgAmt;
	private BigDecimal maxPkgAmtMon;
	/**
	 * 7、单一项目平均持有人数
	 */
	private Integer inveSizePerPkg;
	private Integer inveSizePerPkgMon;
	/**
	 * 8、累计融资人数（人）
	 */
	private Integer financingAmt;
	private Integer financingAmtMon;
	/**
	 * 其中：企业数
	 */
	private Integer financingComAmt;
	private Integer financingComAmtMon;
	/**
	 * 个人数
	 */
	private Integer financingPerAmt;
	private Integer financingPerAmtMon;
	/**
	 * 9、累计投资人数（人）
	 */
	private Integer investorAmt;
	private Integer investorAmtMon;
	/**
	 * 10、融资人年化平均综合成本
	 */
	private BigDecimal pkgCostPerYear;
	private BigDecimal pkgCostPerYearMon;
	/**
	 * 11、投资人年化平均收益
	 */
	private BigDecimal invesProfitPerYear;
	private BigDecimal invesProfitPerYearMon;
	/**
	 * 12、平均借款期限（月）
	 */
	private BigDecimal avgMonth;
	private BigDecimal avgMonthMon;
	/**
	 * 其中：期限≤3个月的笔数
	 */
	private Integer avgMonthAmtLessThree;
	private Integer avgMonthAmtLessThreeMon;
	/**
	 * 3个月＜期限≤6个月的笔数
	 */
	private Integer avgMonthAmtLessSix;
	private Integer avgMonthAmtLessSixMon;
	/**
	 * 6个月＜期限≤12个月的笔数
	 */
	private Integer avgMonthAmtLessTwelve;
	private Integer avgMonthAmtLessTwelveMon;
	/**
	 * 期限＞12个月的笔数
	 */
	private Integer avgMonthAmtMoreTwelve;
	private Integer avgMonthAmtMoreTwelveMon;
	/**
	 * 13、借款种类分布
	 * 其中：有担保机制的借款占比
	 */
	private BigDecimal pkgByGuarantee;
	private BigDecimal pkgByGuaranteeMon;
	/**
	 * 其他风险防范方式占比
	 */
	private BigDecimal pkgByRisk;
	private BigDecimal pkgByRiskMon;
	/**
	 * 无担保机制的借款占比
	 */
	private BigDecimal pkgByNoGua;
	private BigDecimal pkgByNoGuaMon;

	
	public BigDecimal getAvgMonth() {
		return avgMonth;
	}

	public void setAvgMonth(BigDecimal avgMonth) {
		this.avgMonth = avgMonth;
	}

	public BigDecimal getAvgMonthMon() {
		return avgMonthMon;
	}

	public void setAvgMonthMon(BigDecimal avgMonthMon) {
		this.avgMonthMon = avgMonthMon;
	}

	public Integer getFinancingAmt() {
		return financingAmt;
	}

	public void setFinancingAmt(Integer financingAmt) {
		this.financingAmt = financingAmt;
	}

	public Integer getFinancingAmtMon() {
		return financingAmtMon;
	}

	public void setFinancingAmtMon(Integer financingAmtMon) {
		this.financingAmtMon = financingAmtMon;
	}

	public Integer getFinancingComAmt() {
		return financingComAmt;
	}

	public void setFinancingComAmt(Integer financingComAmt) {
		this.financingComAmt = financingComAmt;
	}

	public Integer getFinancingComAmtMon() {
		return financingComAmtMon;
	}

	public void setFinancingComAmtMon(Integer financingComAmtMon) {
		this.financingComAmtMon = financingComAmtMon;
	}

	public Integer getFinancingPerAmt() {
		return financingPerAmt;
	}

	public void setFinancingPerAmt(Integer financingPerAmt) {
		this.financingPerAmt = financingPerAmt;
	}

	public Integer getFinancingPerAmtMon() {
		return financingPerAmtMon;
	}

	public void setFinancingPerAmtMon(Integer financingPerAmtMon) {
		this.financingPerAmtMon = financingPerAmtMon;
	}

	public Integer getInvestorAmt() {
		return investorAmt;
	}

	public void setInvestorAmt(Integer investorAmt) {
		this.investorAmt = investorAmt;
	}

	public Integer getInvestorAmtMon() {
		return investorAmtMon;
	}

	public void setInvestorAmtMon(Integer investorAmtMon) {
		this.investorAmtMon = investorAmtMon;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getQuotoAmtMon() {
		return quotoAmtMon;
	}

	public void setQuotoAmtMon(BigDecimal quotoAmtMon) {
		this.quotoAmtMon = quotoAmtMon;
	}

	public Integer getPkgAmtMon() {
		return pkgAmtMon;
	}

	public void setPkgAmtMon(Integer pkgAmtMon) {
		this.pkgAmtMon = pkgAmtMon;
	}

	public Integer getPkgAmtLessOneMon() {
		return pkgAmtLessOneMon;
	}

	public void setPkgAmtLessOneMon(Integer pkgAmtLessOneMon) {
		this.pkgAmtLessOneMon = pkgAmtLessOneMon;
	}

	public Integer getPkgAmtLessThreeMon() {
		return pkgAmtLessThreeMon;
	}

	public void setPkgAmtLessThreeMon(Integer pkgAmtLessThreeMon) {
		this.pkgAmtLessThreeMon = pkgAmtLessThreeMon;
	}

	public Integer getPkgAmtMoreThreeMon() {
		return pkgAmtMoreThreeMon;
	}

	public void setPkgAmtMoreThreeMon(Integer pkgAmtMoreThreeMon) {
		this.pkgAmtMoreThreeMon = pkgAmtMoreThreeMon;
	}

	public Integer getLendAmtMon() {
		return lendAmtMon;
	}

	public void setLendAmtMon(Integer lendAmtMon) {
		this.lendAmtMon = lendAmtMon;
	}

	public Integer getLendAmtLessOneMon() {
		return lendAmtLessOneMon;
	}

	public void setLendAmtLessOneMon(Integer lendAmtLessOneMon) {
		this.lendAmtLessOneMon = lendAmtLessOneMon;
	}

	public Integer getLendAmtLessThreeMon() {
		return lendAmtLessThreeMon;
	}

	public void setLendAmtLessThreeMon(Integer lendAmtLessThreeMon) {
		this.lendAmtLessThreeMon = lendAmtLessThreeMon;
	}

	public Integer getLendAmtMoreThreeMon() {
		return lendAmtMoreThreeMon;
	}

	public void setLendAmtMoreThreeMon(Integer lendAmtMoreThreeMon) {
		this.lendAmtMoreThreeMon = lendAmtMoreThreeMon;
	}

	public BigDecimal getAvgPkgAmtMon() {
		return avgPkgAmtMon;
	}

	public void setAvgPkgAmtMon(BigDecimal avgPkgAmtMon) {
		this.avgPkgAmtMon = avgPkgAmtMon;
	}

	public BigDecimal getAvgLendAmtMon() {
		return avgLendAmtMon;
	}

	public void setAvgLendAmtMon(BigDecimal avgLendAmtMon) {
		this.avgLendAmtMon = avgLendAmtMon;
	}

	public BigDecimal getMaxPkgAmtMon() {
		return maxPkgAmtMon;
	}

	public void setMaxPkgAmtMon(BigDecimal maxPkgAmtMon) {
		this.maxPkgAmtMon = maxPkgAmtMon;
	}

	public Integer getInveSizePerPkgMon() {
		return inveSizePerPkgMon;
	}

	public void setInveSizePerPkgMon(Integer inveSizePerPkgMon) {
		this.inveSizePerPkgMon = inveSizePerPkgMon;
	}

	public BigDecimal getPkgCostPerYearMon() {
		return pkgCostPerYearMon;
	}

	public void setPkgCostPerYearMon(BigDecimal pkgCostPerYearMon) {
		this.pkgCostPerYearMon = pkgCostPerYearMon;
	}

	public BigDecimal getInvesProfitPerYearMon() {
		return invesProfitPerYearMon;
	}

	public void setInvesProfitPerYearMon(BigDecimal invesProfitPerYearMon) {
		this.invesProfitPerYearMon = invesProfitPerYearMon;
	}

	public Integer getAvgMonthAmtLessThreeMon() {
		return avgMonthAmtLessThreeMon;
	}

	public void setAvgMonthAmtLessThreeMon(Integer avgMonthAmtLessThreeMon) {
		this.avgMonthAmtLessThreeMon = avgMonthAmtLessThreeMon;
	}

	public Integer getAvgMonthAmtLessSixMon() {
		return avgMonthAmtLessSixMon;
	}

	public void setAvgMonthAmtLessSixMon(Integer avgMonthAmtLessSixMon) {
		this.avgMonthAmtLessSixMon = avgMonthAmtLessSixMon;
	}

	public Integer getAvgMonthAmtLessTwelveMon() {
		return avgMonthAmtLessTwelveMon;
	}

	public void setAvgMonthAmtLessTwelveMon(Integer avgMonthAmtLessTwelveMon) {
		this.avgMonthAmtLessTwelveMon = avgMonthAmtLessTwelveMon;
	}

	public Integer getAvgMonthAmtMoreTwelveMon() {
		return avgMonthAmtMoreTwelveMon;
	}

	public void setAvgMonthAmtMoreTwelveMon(Integer avgMonthAmtMoreTwelveMon) {
		this.avgMonthAmtMoreTwelveMon = avgMonthAmtMoreTwelveMon;
	}

	public BigDecimal getPkgByGuaranteeMon() {
		return pkgByGuaranteeMon;
	}

	public void setPkgByGuaranteeMon(BigDecimal pkgByGuaranteeMon) {
		this.pkgByGuaranteeMon = pkgByGuaranteeMon;
	}

	public BigDecimal getPkgByRiskMon() {
		return pkgByRiskMon;
	}

	public void setPkgByRiskMon(BigDecimal pkgByRiskMon) {
		this.pkgByRiskMon = pkgByRiskMon;
	}

	public BigDecimal getPkgByNoGuaMon() {
		return pkgByNoGuaMon;
	}

	public void setPkgByNoGuaMon(BigDecimal pkgByNoGuaMon) {
		this.pkgByNoGuaMon = pkgByNoGuaMon;
	}

	public BigDecimal getQuotoAmt() {
		return quotoAmt;
	}

	public void setQuotoAmt(BigDecimal quotoAmt) {
		this.quotoAmt = quotoAmt;
	}

	public Integer getPkgAmt() {
		return pkgAmt;
	}

	public void setPkgAmt(Integer pkgAmt) {
		this.pkgAmt = pkgAmt;
	}

	public Integer getPkgAmtLessOne() {
		return pkgAmtLessOne;
	}

	public void setPkgAmtLessOne(Integer pkgAmtLessOne) {
		this.pkgAmtLessOne = pkgAmtLessOne;
	}

	public Integer getPkgAmtLessThree() {
		return pkgAmtLessThree;
	}

	public void setPkgAmtLessThree(Integer pkgAmtLessThree) {
		this.pkgAmtLessThree = pkgAmtLessThree;
	}

	public Integer getPkgAmtMoreThree() {
		return pkgAmtMoreThree;
	}

	public void setPkgAmtMoreThree(Integer pkgAmtMoreThree) {
		this.pkgAmtMoreThree = pkgAmtMoreThree;
	}

	public Integer getLendAmt() {
		return lendAmt;
	}

	public void setLendAmt(Integer lendAmt) {
		this.lendAmt = lendAmt;
	}

	public Integer getLendAmtLessOne() {
		return lendAmtLessOne;
	}

	public void setLendAmtLessOne(Integer lendAmtLessOne) {
		this.lendAmtLessOne = lendAmtLessOne;
	}

	public Integer getLendAmtLessThree() {
		return lendAmtLessThree;
	}

	public void setLendAmtLessThree(Integer lendAmtLessThree) {
		this.lendAmtLessThree = lendAmtLessThree;
	}

	public Integer getLendAmtMoreThree() {
		return lendAmtMoreThree;
	}

	public void setLendAmtMoreThree(Integer lendAmtMoreThree) {
		this.lendAmtMoreThree = lendAmtMoreThree;
	}

	public BigDecimal getAvgPkgAmt() {
		return avgPkgAmt;
	}

	public void setAvgPkgAmt(BigDecimal avgPkgAmt) {
		this.avgPkgAmt = avgPkgAmt;
	}

	public BigDecimal getAvgLendAmt() {
		return avgLendAmt;
	}

	public void setAvgLendAmt(BigDecimal avgLendAmt) {
		this.avgLendAmt = avgLendAmt;
	}

	public BigDecimal getMaxPkgAmt() {
		return maxPkgAmt;
	}

	public void setMaxPkgAmt(BigDecimal maxPkgAmt) {
		this.maxPkgAmt = maxPkgAmt;
	}

	public Integer getInveSizePerPkg() {
		return inveSizePerPkg;
	}

	public void setInveSizePerPkg(Integer inveSizePerPkg) {
		this.inveSizePerPkg = inveSizePerPkg;
	}

	public BigDecimal getPkgCostPerYear() {
		return pkgCostPerYear;
	}

	public void setPkgCostPerYear(BigDecimal pkgCostPerYear) {
		this.pkgCostPerYear = pkgCostPerYear;
	}

	public BigDecimal getInvesProfitPerYear() {
		return invesProfitPerYear;
	}

	public void setInvesProfitPerYear(BigDecimal invesProfitPerYear) {
		this.invesProfitPerYear = invesProfitPerYear;
	}

	public Integer getAvgMonthAmtLessThree() {
		return avgMonthAmtLessThree;
	}

	public void setAvgMonthAmtLessThree(Integer avgMonthAmtLessThree) {
		this.avgMonthAmtLessThree = avgMonthAmtLessThree;
	}

	public Integer getAvgMonthAmtLessSix() {
		return avgMonthAmtLessSix;
	}

	public void setAvgMonthAmtLessSix(Integer avgMonthAmtLessSix) {
		this.avgMonthAmtLessSix = avgMonthAmtLessSix;
	}

	public Integer getAvgMonthAmtLessTwelve() {
		return avgMonthAmtLessTwelve;
	}

	public void setAvgMonthAmtLessTwelve(Integer avgMonthAmtLessTwelve) {
		this.avgMonthAmtLessTwelve = avgMonthAmtLessTwelve;
	}

	public Integer getAvgMonthAmtMoreTwelve() {
		return avgMonthAmtMoreTwelve;
	}

	public void setAvgMonthAmtMoreTwelve(Integer avgMonthAmtMoreTwelve) {
		this.avgMonthAmtMoreTwelve = avgMonthAmtMoreTwelve;
	}

	public BigDecimal getPkgByGuarantee() {
		return pkgByGuarantee;
	}

	public void setPkgByGuarantee(BigDecimal pkgByGuarantee) {
		this.pkgByGuarantee = pkgByGuarantee;
	}

	public BigDecimal getPkgByRisk() {
		return pkgByRisk;
	}

	public void setPkgByRisk(BigDecimal pkgByRisk) {
		this.pkgByRisk = pkgByRisk;
	}

	public BigDecimal getPkgByNoGua() {
		return pkgByNoGua;
	}

	public void setPkgByNoGua(BigDecimal pkgByNoGua) {
		this.pkgByNoGua = pkgByNoGua;
	}
	


}
