/*
 * Project Name: kmfex-platform
 * File Name: TransferMarketDownDto.java
 * Class Name: TransferMarketDownDto
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

import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * 债权转让市场包下行DTO.
 * 
 * @author yicai
 *
 */
public class TransferMarketDownDto implements Serializable {
	
	private static final long serialVersionUID = 5L;

	private String transferId; // 转让记录ID
	private String packageId; // 融资包编号
	private String pakcageName; // 融资包名称
	private ERiskLevel riskLevel; // 融资项目级别
	private EWarrantyType warrantyType; // 风险保障
	private String annualProfit; // 年利率
	private String quotedPrice; // 报价
	private EPayMethodType payMethod; // 还款方式
	private String maturityDate; // 到期日
	private String remTermLength; // 剩余期数
	private String remainingAmount; // 剩余本息
	private String residualPrincipalAmt; // 剩余本金
	private String residualInterestAmt; // 剩余利息
	private String expectedReturnStr; // 预期收益
	private String expectedReturnRate; // 预期收益率
	private String transferFeeStr; // 转让手续费
	private String reason; // 不可买入的原因
	private Boolean aipFlag; // 是否定投
	private Boolean mine; // 是否是自己的债权，转让用
	private Boolean overdue; // 是否存在历史逾期记录，转让用
	
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getPakcageName() {
		return pakcageName;
	}
	public void setPakcageName(String pakcageName) {
		this.pakcageName = pakcageName;
	}
	public ERiskLevel getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(ERiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	public EWarrantyType getWarrantyType() {
		return warrantyType;
	}
	public void setWarrantyType(EWarrantyType warrantyType) {
		this.warrantyType = warrantyType;
	}
	public String getAnnualProfit() {
		return annualProfit;
	}
	public void setAnnualProfit(String annualProfit) {
		this.annualProfit = annualProfit;
	}
	public String getQuotedPrice() {
		return quotedPrice;
	}
	public void setQuotedPrice(String quotedPrice) {
		this.quotedPrice = quotedPrice;
	}
	public EPayMethodType getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(EPayMethodType payMethod) {
		this.payMethod = payMethod;
	}
	public String getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}
	public String getRemTermLength() {
		return remTermLength;
	}
	public void setRemTermLength(String remTermLength) {
		this.remTermLength = remTermLength;
	}
	public String getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(String remainingAmount) {
		this.remainingAmount = remainingAmount;
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
	public String getExpectedReturnStr() {
		return expectedReturnStr;
	}
	public void setExpectedReturnStr(String expectedReturnStr) {
		this.expectedReturnStr = expectedReturnStr;
	}
	public String getExpectedReturnRate() {
		return expectedReturnRate;
	}
	public void setExpectedReturnRate(String expectedReturnRate) {
		this.expectedReturnRate = expectedReturnRate;
	}
	public String getTransferFeeStr() {
		return transferFeeStr;
	}
	public void setTransferFeeStr(String transferFeeStr) {
		this.transferFeeStr = transferFeeStr;
	}
	public Boolean getAipFlag() {
		return aipFlag;
	}
	public void setAipFlag(Boolean aipFlag) {
		this.aipFlag = aipFlag;
	}
	public Boolean getMine() {
		return mine;
	}
	public void setMine(Boolean mine) {
		this.mine = mine;
	}
	public Boolean getOverdue() {
		return overdue;
	}
	public void setOverdue(Boolean overdue) {
		this.overdue = overdue;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
