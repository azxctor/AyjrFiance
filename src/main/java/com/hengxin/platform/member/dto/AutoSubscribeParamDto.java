package com.hengxin.platform.member.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hengxin.platform.common.service.validator.AutoSubscribeCheck;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * 自动申购
 * AutoSubscribeParam.
 * 
 */
@AutoSubscribeCheck
public class AutoSubscribeParamDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private boolean firstTime;

	/** status. **/
	private boolean pending;

	private String relieve;
	
	private boolean existPending;

	/** rending message. **/
	private String riskMessage;
	private String aprMessage;
	private String repayMessage;
	private String guaranteeMessage;
	
	/** 优质. **/
	private boolean riskA;
	
	/** 中等. **/
	private boolean riskB;
	
	/** 合格. **/
	private boolean riskC;
	
	/** 高风险. **/
	private boolean riskD;
	
	/** 按日计. **/
	@JsonProperty("byDate")
	private boolean accordingToDate;
	
	/** Annual Percentage Rate. **/
	private BigDecimal minAPRForDate;
	
	private int minDateRange;
	
	private int maxDateRange;
	
	/** 按日计. **/
	@JsonProperty("byMonth")
	private boolean accordingToMonth;
	
	private BigDecimal minAPRForMonth;

	private int minMonthRange;
	
	private int maxMonthRange;
	
	/** 等额本息. **/
	private boolean repaymentA;
	
	/** 等本等息. **/
	private boolean repaymentB;
	
	/** 按月还息, 到期还本. **/
	private boolean repaymentC;
	
	/** 到期一次还本付息.  **/
	private boolean repaymentD;
	
	/** 本金担保. **/
	private boolean guaranteeA;
	
	/** 本息担保. **/
	private boolean guaranteeB;
	
	/** 资产监管. **/
	private boolean guaranteeC;
	
	/** 无担保. **/ 
	private boolean guaranteeD;

	private BigDecimal minBalance;

	private BigDecimal maxSubscribeAmount;
	
	/**
	 * @param accordingToDate the accordingToDate to set
	 */
	public void setAccordingToDate(boolean accordingToDate) {
		this.accordingToDate = accordingToDate;
	}
	
	/**
	 * 按日计.
	 * @return the accordingToDate
	 */
	public boolean isAccordingToDate() {
		if (this.minAPRForDate == null) {
			return false;
		}
		return this.minAPRForDate.doubleValue() != 0;
	}

	/**
	 * @param accordingToDate the accordingToDate to set
	 */
	public void setAccordingToMonth(boolean accordingToMonth) {
		this.accordingToMonth = accordingToMonth;
	}
	
	/**
	 * 按月计.
	 * @return the accordingToMonth
	 */
	public boolean isAccordingToMonth() {
		if (this.minAPRForMonth == null) {
			return false;
		}
		return this.minAPRForMonth.doubleValue() != 0;
	}
	
	public void renderRiskParam(String riskParam) {
		if (riskParam == null) {
			return;
		}
		String[] riskParams = riskParam.split("\\,");
		for (String risk : riskParams) {
			if (risk.equalsIgnoreCase(ERiskLevel.HIGH_QUALITY.getCode())) {
				this.setRiskA(true);
			} else if (risk.equalsIgnoreCase(ERiskLevel.MEDIUM.getCode())) {
				this.setRiskB(true);
			} else if (risk.equalsIgnoreCase(ERiskLevel.ACCEPTABLE.getCode())) {
				this.setRiskC(true);
			} else if (risk.equalsIgnoreCase(ERiskLevel.HIGH_RISK.getCode())) {
				this.setRiskD(true);
			}
		}
	}
	
	public void renderRepaymentParam(String repayParam) {
		if (repayParam == null) {
			return;
		}
		String[] repayParams = repayParam.split("\\,");
		for (String repay : repayParams) {
			if (repay.equalsIgnoreCase(EPayMethodType.MONTH_AVERAGE_INTEREST.getCode())) {
				this.setRepaymentA(true);
			} else if (repay.equalsIgnoreCase(EPayMethodType.MONTH_PRINCIPAL_INTEREST.getCode())) {
				this.setRepaymentB(true);
			} else if (repay.equalsIgnoreCase(EPayMethodType.MONTH_INTEREST.getCode())) {
				this.setRepaymentC(true);
			} else if (repay.equalsIgnoreCase(EPayMethodType.ONCE_FOR_ALL.getCode())) {
				this.setRepaymentD(true);
			}
		}
	}
	
	public void renderGuarantee(String guaranteeParam) {
		if (guaranteeParam == null) {
			return;
		}
		String[] guaranteeParams = guaranteeParam.split("\\,");
		for (String guarantee : guaranteeParams) {
			if (guarantee.equalsIgnoreCase(EWarrantyType.PRINCIPAL.getCode())) {
				this.setGuaranteeA(true);
			} else if (guarantee.equalsIgnoreCase(EWarrantyType.PRINCIPALINTEREST.getCode())) {
				this.setGuaranteeB(true);
			} else if (guarantee.equalsIgnoreCase(EWarrantyType.MONITORASSETS.getCode())) {
				this.setGuaranteeC(true);
			} else if (guarantee.equalsIgnoreCase(EWarrantyType.NOTHING.getCode())) {
				this.setGuaranteeD(true);
			}
		}
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the pending
	 */
	public boolean isPending() {
		return pending;
	}

	/**
	 * @param pending the pending to set
	 */
	public void setPending(boolean pending) {
		this.pending = pending;
	}

	/**
	 * @return the relieve
	 */
	public String getRelieve() {
		return relieve;
	}

	/**
	 * @param relieve the relieve to set
	 */
	public void setRelieve(String relieve) {
		this.relieve = relieve;
	}

	/**
	 * @return the existPending
	 */
	public boolean isExistPending() {
		return existPending;
	}

	/**
	 * @param existPending the existPending to set
	 */
	public void setExistPending(boolean existPending) {
		this.existPending = existPending;
	}

	/**
	 * @return the riskA
	 */
	public boolean isRiskA() {
		return riskA;
	}

	/**
	 * @param riskA the riskA to set
	 */
	public void setRiskA(boolean riskA) {
		this.riskA = riskA;
	}

	/**
	 * @return the riskB
	 */
	public boolean isRiskB() {
		return riskB;
	}

	/**
	 * @param riskB the riskB to set
	 */
	public void setRiskB(boolean riskB) {
		this.riskB = riskB;
	}

	/**
	 * @return the riskC
	 */
	public boolean isRiskC() {
		return riskC;
	}

	/**
	 * @param riskC the riskC to set
	 */
	public void setRiskC(boolean riskC) {
		this.riskC = riskC;
	}

	/**
	 * @return the riskD
	 */
	public boolean isRiskD() {
		return riskD;
	}

	/**
	 * @param riskD the riskD to set
	 */
	public void setRiskD(boolean riskD) {
		this.riskD = riskD;
	}

	/**
	 * @return the minDateRange
	 */
	public int getMinDateRange() {
		return minDateRange;
	}

	/**
	 * @param minDateRange the minDateRange to set
	 */
	public void setMinDateRange(int minDateRange) {
		this.minDateRange = minDateRange;
	}

	/**
	 * @return the maxDateRange
	 */
	public int getMaxDateRange() {
		return maxDateRange;
	}

	/**
	 * @param maxDateRange the maxDateRange to set
	 */
	public void setMaxDateRange(int maxDateRange) {
		this.maxDateRange = maxDateRange;
	}

	/**
	 * @return the minAPRForDate
	 */
	public BigDecimal getMinAPRForDate() {
		return minAPRForDate;
	}

	/**
	 * @param minAPRForDate the minAPRForDate to set
	 */
	public void setMinAPRForDate(BigDecimal minAPRForDate) {
		this.minAPRForDate = minAPRForDate;
	}

	/**
	 * @return the minAPRForMonth
	 */
	public BigDecimal getMinAPRForMonth() {
		return minAPRForMonth;
	}

	/**
	 * @param minAPRForMonth the minAPRForMonth to set
	 */
	public void setMinAPRForMonth(BigDecimal minAPRForMonth) {
		this.minAPRForMonth = minAPRForMonth;
	}

	/**
	 * @return the minMonthRange
	 */
	public int getMinMonthRange() {
		return minMonthRange;
	}

	/**
	 * @param minMonthRange the minMonthRange to set
	 */
	public void setMinMonthRange(int minMonthRange) {
		this.minMonthRange = minMonthRange;
	}

	/**
	 * @return the maxMonthRange
	 */
	public int getMaxMonthRange() {
		return maxMonthRange;
	}

	/**
	 * @param maxMonthRange the maxMonthRange to set
	 */
	public void setMaxMonthRange(int maxMonthRange) {
		this.maxMonthRange = maxMonthRange;
	}

	/**
	 * @return the repaymentA
	 */
	public boolean isRepaymentA() {
		return repaymentA;
	}

	/**
	 * @param repaymentA the repaymentA to set
	 */
	public void setRepaymentA(boolean repaymentA) {
		this.repaymentA = repaymentA;
	}

	/**
	 * @return the repaymentB
	 */
	public boolean isRepaymentB() {
		return repaymentB;
	}

	/**
	 * @param repaymentB the repaymentB to set
	 */
	public void setRepaymentB(boolean repaymentB) {
		this.repaymentB = repaymentB;
	}

	/**
	 * @return the repaymentC
	 */
	public boolean isRepaymentC() {
		return repaymentC;
	}

	/**
	 * @param repaymentC the repaymentC to set
	 */
	public void setRepaymentC(boolean repaymentC) {
		this.repaymentC = repaymentC;
	}

	/**
	 * @return the repaymentD
	 */
	public boolean isRepaymentD() {
		return repaymentD;
	}

	/**
	 * @param repaymentD the repaymentD to set
	 */
	public void setRepaymentD(boolean repaymentD) {
		this.repaymentD = repaymentD;
	}

	/**
	 * @return the guaranteeA
	 */
	public boolean isGuaranteeA() {
		return guaranteeA;
	}

	/**
	 * @param guaranteeA the guaranteeA to set
	 */
	public void setGuaranteeA(boolean guaranteeA) {
		this.guaranteeA = guaranteeA;
	}

	/**
	 * @return the guaranteeB
	 */
	public boolean isGuaranteeB() {
		return guaranteeB;
	}

	/**
	 * @param guaranteeB the guaranteeB to set
	 */
	public void setGuaranteeB(boolean guaranteeB) {
		this.guaranteeB = guaranteeB;
	}

	/**
	 * @return the guaranteeC
	 */
	public boolean isGuaranteeC() {
		return guaranteeC;
	}

	/**
	 * @param guaranteeC the guaranteeC to set
	 */
	public void setGuaranteeC(boolean guaranteeC) {
		this.guaranteeC = guaranteeC;
	}

	/**
	 * @return the guaranteeD
	 */
	public boolean isGuaranteeD() {
		return guaranteeD;
	}

	/**
	 * @param guaranteeD the guaranteeD to set
	 */
	public void setGuaranteeD(boolean guaranteeD) {
		this.guaranteeD = guaranteeD;
	}

	/**
	 * @return the riskMessage
	 */
	public String getRiskMessage() {
		return riskMessage;
	}

	/**
	 * @param riskMessage the riskMessage to set
	 */
	public void setRiskMessage(String riskMessage) {
		this.riskMessage = riskMessage;
	}

	/**
	 * @return the aprMessage
	 */
	public String getAprMessage() {
		return aprMessage;
	}

	/**
	 * @param aprMessage the aprMessage to set
	 */
	public void setAprMessage(String aprMessage) {
		this.aprMessage = aprMessage;
	}

	/**
	 * @return the repayMessage
	 */
	public String getRepayMessage() {
		return repayMessage;
	}

	/**
	 * @param repayMessage the repayMessage to set
	 */
	public void setRepayMessage(String repayMessage) {
		this.repayMessage = repayMessage;
	}

	/**
	 * @return the guaranteeMessage
	 */
	public String getGuaranteeMessage() {
		return guaranteeMessage;
	}

	/**
	 * @param guaranteeMessage the guaranteeMessage to set
	 */
	public void setGuaranteeMessage(String guaranteeMessage) {
		this.guaranteeMessage = guaranteeMessage;
	}

	/**
	 * @return the minBalance
	 */
	public BigDecimal getMinBalance() {
		return minBalance;
	}

	/**
	 * @param minBalance the minBalance to set
	 */
	public void setMinBalance(BigDecimal minBalance) {
		this.minBalance = minBalance;
	}

	/**
	 * @return the maxSubscribeAmount
	 */
	public BigDecimal getMaxSubscribeAmount() {
		return maxSubscribeAmount;
	}

	/**
	 * @param maxSubscribeAmount the maxSubscribeAmount to set
	 */
	public void setMaxSubscribeAmount(BigDecimal maxSubscribeAmount) {
		this.maxSubscribeAmount = maxSubscribeAmount;
	}

	/**
	 * @return the firstTime
	 */
	public boolean isFirstTime() {
		return firstTime;
	}

	/**
	 * @param firstTime the firstTime to set
	 */
	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

}
