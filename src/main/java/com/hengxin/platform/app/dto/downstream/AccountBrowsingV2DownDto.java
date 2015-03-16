/*
 * Project Name: kmfex-platform
 * File Name: AccountBrowsingV2DownDto.java
 * Class Name: AccountBrowsingV2DownDto
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 账户总览二期DTO.
 * 
 * @author yicai
 *
 */
public class AccountBrowsingV2DownDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String totalAsset = ""; // 总资产金额
	private String totalPrincipal = ""; // 持有债权
	private String bal = ""; // 资金账户总额
//	private String availableAsset = ""; // 可用金额
//	private String availableCash = ""; // 可提现金
	
	private String rechargeSum = ""; // 充值
	private String rechargeNum = ""; // 充值数值
	private String rechargeProp = ""; // 充值比例
	
	private String withdrawSum = ""; // 提现
	private String withdrawNum = ""; // 提现数值
	private String withdrawProp = ""; // 提现比例
	
	private String currentSettlementSum = ""; // 活期结算
	private String currentSettlementNum = ""; // 活期结算数值
	private String currentSettlementProp = ""; // 活期结算比例
	
	private String investmentSum = ""; // 投资申购
	private String investmentNum = ""; // 投资申购数值
	private String investmentProp = ""; // 投资申购比例
	
	private String financingRepaymentSum = ""; // 融资还款
	private String financingRepaymentNum = ""; // 融资还款数值
	private String financingRepaymentProp = ""; // 融资还款比例
	
	private String totalNextEarnings = ""; // 下期预期总收益
	private String totalPrincipalApplications = ""; // 申购本金总额
	private String totalRestAmt = ""; // 剩余本息总额
	
	private String expectTotalProfit; // 待实现收益
	private String realizedTotalRecvProfit; // 预期总收益
	private String expectTotalRecvProfit; // 已实现收益
	private String expectTotalProfitRate; // 预期年化总收益率
	
	private List<InvestBenefitDto> investBenefit = new ArrayList<InvestBenefitDto>(); // 每周收益列表 

	
	
	public String getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}

//	public String getAvailableAsset() {
//		return availableAsset;
//	}
//
//	public void setAvailableAsset(String availableAsset) {
//		this.availableAsset = availableAsset;
//	}
//
//	public String getAvailableCash() {
//		return availableCash;
//	}
//
//	public void setAvailableCash(String availableCash) {
//		this.availableCash = availableCash;
//	}

	public String getRechargeSum() {
		return rechargeSum;
	}

	public void setRechargeSum(String rechargeSum) {
		this.rechargeSum = rechargeSum;
	}

	public String getRechargeNum() {
		return rechargeNum;
	}

	public void setRechargeNum(String rechargeNum) {
		this.rechargeNum = rechargeNum;
	}

	public String getRechargeProp() {
		return rechargeProp;
	}

	public void setRechargeProp(String rechargeProp) {
		this.rechargeProp = rechargeProp;
	}

	public String getWithdrawSum() {
		return withdrawSum;
	}

	public void setWithdrawSum(String withdrawSum) {
		this.withdrawSum = withdrawSum;
	}

	public String getWithdrawNum() {
		return withdrawNum;
	}

	public void setWithdrawNum(String withdrawNum) {
		this.withdrawNum = withdrawNum;
	}

	public String getWithdrawProp() {
		return withdrawProp;
	}

	public void setWithdrawProp(String withdrawProp) {
		this.withdrawProp = withdrawProp;
	}

	public String getCurrentSettlementSum() {
		return currentSettlementSum;
	}

	public void setCurrentSettlementSum(String currentSettlementSum) {
		this.currentSettlementSum = currentSettlementSum;
	}

	public String getCurrentSettlementNum() {
		return currentSettlementNum;
	}

	public void setCurrentSettlementNum(String currentSettlementNum) {
		this.currentSettlementNum = currentSettlementNum;
	}

	public String getCurrentSettlementProp() {
		return currentSettlementProp;
	}

	public void setCurrentSettlementProp(String currentSettlementProp) {
		this.currentSettlementProp = currentSettlementProp;
	}

	public String getInvestmentSum() {
		return investmentSum;
	}

	public void setInvestmentSum(String investmentSum) {
		this.investmentSum = investmentSum;
	}

	public String getInvestmentNum() {
		return investmentNum;
	}

	public void setInvestmentNum(String investmentNum) {
		this.investmentNum = investmentNum;
	}

	public String getInvestmentProp() {
		return investmentProp;
	}

	public void setInvestmentProp(String investmentProp) {
		this.investmentProp = investmentProp;
	}

	public String getFinancingRepaymentSum() {
		return financingRepaymentSum;
	}

	public void setFinancingRepaymentSum(String financingRepaymentSum) {
		this.financingRepaymentSum = financingRepaymentSum;
	}

	public String getFinancingRepaymentNum() {
		return financingRepaymentNum;
	}

	public void setFinancingRepaymentNum(String financingRepaymentNum) {
		this.financingRepaymentNum = financingRepaymentNum;
	}

	public String getFinancingRepaymentProp() {
		return financingRepaymentProp;
	}

	public void setFinancingRepaymentProp(String financingRepaymentProp) {
		this.financingRepaymentProp = financingRepaymentProp;
	}

	public List<InvestBenefitDto> getInvestBenefit() {
		return investBenefit;
	}

	public void setInvestBenefit(List<InvestBenefitDto> investBenefit) {
		this.investBenefit = investBenefit;
	}

	public String getTotalPrincipal() {
		return totalPrincipal;
	}

	public void setTotalPrincipal(String totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}

	public String getTotalRestAmt() {
		return totalRestAmt;
	}

	public void setTotalRestAmt(String totalRestAmt) {
		this.totalRestAmt = totalRestAmt;
	}

	public String getBal() {
		return bal;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public String getTotalNextEarnings() {
		return totalNextEarnings;
	}

	public void setTotalNextEarnings(String totalNextEarnings) {
		this.totalNextEarnings = totalNextEarnings;
	}

	public String getTotalPrincipalApplications() {
		return totalPrincipalApplications;
	}

	public void setTotalPrincipalApplications(String totalPrincipalApplications) {
		this.totalPrincipalApplications = totalPrincipalApplications;
	}

	public String getExpectTotalProfit() {
		return expectTotalProfit;
	}

	public void setExpectTotalProfit(String expectTotalProfit) {
		this.expectTotalProfit = expectTotalProfit;
	}

	public String getRealizedTotalRecvProfit() {
		return realizedTotalRecvProfit;
	}

	public void setRealizedTotalRecvProfit(String realizedTotalRecvProfit) {
		this.realizedTotalRecvProfit = realizedTotalRecvProfit;
	}

	public String getExpectTotalRecvProfit() {
		return expectTotalRecvProfit;
	}

	public void setExpectTotalRecvProfit(String expectTotalRecvProfit) {
		this.expectTotalRecvProfit = expectTotalRecvProfit;
	}

	public String getExpectTotalProfitRate() {
		return expectTotalProfitRate;
	}

	public void setExpectTotalProfitRate(String expectTotalProfitRate) {
		this.expectTotalProfitRate = expectTotalProfitRate;
	}
}
