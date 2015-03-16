/*
 * Project Name: kmfex-platform
 * File Name: ProductPackageInvestorDetailsDto.java
 * Class Name: ProductPackageInvestorDetailsDto
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
import java.math.BigDecimal;

/**
 * MyInvestListdownDto: 我的债权列表下行DTO.
 * 
 * @author yicai
 *
 */
public class MyInvestListV2DownDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pkgId; // 融资包编号
	private String lotId; // 份额编号
	private String packageName; // 融资包名称
	private String rate; // 预期年化收益率
	private String term; // 融资期限，如：13天，1个月，6个月
	private String signContractTime; // 签约时间
	private String subsDate; // 申购时间
	private String amount; // 申购金额（元）
	private String nextPayAmount; // 总收益（元）

	private String nextPayDay; // 下期还款日
	private String lastAmount; // 剩余本息
	private String residualPrincipalAmt; // 剩余本金
	private String residualInterestAmt; // 剩余利息
	private BigDecimal sysRate; // 费用率，费用=费用率*提交报价
	private String lastPayDay; // 债权到期日
	private String costPrice; // 成本价，债权转让成本价 = 买入价 - 已还本息 +  转让手续费
	private String transferMinPrice; // 报价下限
	private String transferMaxPrice; // 报价上限
	
	private String aipFlag; // 是否定投
	private Boolean canTransferCancel; // 是否可以撤销转让
	private Boolean canCreditorRightsTransfer; // 是否可以转让
	
	
	private long currentTime = System.currentTimeMillis(); // 当前时间戳, for test

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getSignContractTime() {
		return signContractTime;
	}

	public void setSignContractTime(String signContractTime) {
		this.signContractTime = signContractTime;
	}

	public String getSubsDate() {
		return subsDate;
	}

	public void setSubsDate(String subsDate) {
		this.subsDate = subsDate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getNextPayAmount() {
		return nextPayAmount;
	}

	public void setNextPayAmount(String nextPayAmount) {
		this.nextPayAmount = nextPayAmount;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public String getAipFlag() {
		return aipFlag;
	}

	public void setAipFlag(String aipFlag) {
		this.aipFlag = aipFlag;
	}

	public Boolean getCanTransferCancel() {
		return canTransferCancel;
	}

	public void setCanTransferCancel(Boolean canTransferCancel) {
		this.canTransferCancel = canTransferCancel;
	}

	public Boolean getCanCreditorRightsTransfer() {
		return canCreditorRightsTransfer;
	}

	public void setCanCreditorRightsTransfer(Boolean canCreditorRightsTransfer) {
		this.canCreditorRightsTransfer = canCreditorRightsTransfer;
	}

	public String getNextPayDay() {
		return nextPayDay;
	}

	public void setNextPayDay(String nextPayDay) {
		this.nextPayDay = nextPayDay;
	}

	public String getLastAmount() {
		return lastAmount;
	}

	public void setLastAmount(String lastAmount) {
		this.lastAmount = lastAmount;
	}

	public BigDecimal getSysRate() {
		return sysRate;
	}

	public void setSysRate(BigDecimal sysRate) {
		this.sysRate = sysRate;
	}

	public String getResidualPrincipalAmt() {
		return residualPrincipalAmt;
	}

	public void setResidualPrincipalAmt(String residualPrincipalAmt) {
		this.residualPrincipalAmt = residualPrincipalAmt;
	}

	public String getResidualInterestAmt() {
		return residualInterestAmt;
	}

	public void setResidualInterestAmt(String residualInterestAmt) {
		this.residualInterestAmt = residualInterestAmt;
	}

	public String getLastPayDay() {
		return lastPayDay;
	}

	public void setLastPayDay(String lastPayDay) {
		this.lastPayDay = lastPayDay;
	}

	public String getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}

	public String getTransferMinPrice() {
		return transferMinPrice;
	}

	public void setTransferMinPrice(String transferMinPrice) {
		this.transferMinPrice = transferMinPrice;
	}

	public String getTransferMaxPrice() {
		return transferMaxPrice;
	}

	public void setTransferMaxPrice(String transferMaxPrice) {
		this.transferMaxPrice = transferMaxPrice;
	}

}
