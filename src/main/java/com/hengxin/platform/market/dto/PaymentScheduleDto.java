package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.product.enums.EPayStatus;

public class PaymentScheduleDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sequenceId; // 序列号
	private Date paymentDate; // 应还日期
	private Date lastPayTs; // 实还日期
	
	private BigDecimal principalAmt; // 应付本金
	private BigDecimal interestAmt; // 应付利息
	private BigDecimal feeAmt; // 应付费用
    private BigDecimal principalForfeit; // 应付本金罚金
    private BigDecimal interestForfeit; // 应付利息罚金
    private BigDecimal feeForfeit; // 应付费用罚金
    private BigDecimal wrtrPrinForfeit; // 担保方应付本金罚金
    private BigDecimal wrtrInterestForfeit; // 担保方应付利息罚金
    
    private BigDecimal payPrincipalAmt; // 实付本金
    private BigDecimal payInterestAmt; // 实付利息
    private BigDecimal payAmt; // 实付费用
    private BigDecimal payPrincipalForfeit; // 实付本金罚金
    private BigDecimal payInterestForfeit; // 实付利息罚金
    private BigDecimal payFeeForfeit; // 实付费用罚金
    private BigDecimal payWrtrPrinForfeit; // 担保方实付本金罚金
    private BigDecimal payWrtrInterestForfeit; // 担保方实付利息罚金
    
    private EPayStatus status; // 支付状态
    
    private BigDecimal rate;
    
    // 应付金额
    public String getAmount() {
    	BigDecimal amount = principalAmt.add(interestAmt).add(feeAmt).add(principalForfeit).add(interestForfeit)
    			.add(feeForfeit).add(wrtrPrinForfeit).add(wrtrInterestForfeit);
    	return NumberUtil.formatMoney(amount.multiply(rate).setScale(2, RoundingMode.DOWN));
    }
    
    // 实付金额
    public String getPayAmount() {
    	BigDecimal payAmount = payPrincipalAmt.add(payInterestAmt).add(payAmt).add(payPrincipalForfeit).add(payInterestForfeit)
    			.add(payFeeForfeit).add(payWrtrPrinForfeit).add(payWrtrInterestForfeit);
    	return NumberUtil.formatMoney(payAmount.multiply(rate).setScale(2, RoundingMode.DOWN));
    }

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getLastPayTs() {
		return lastPayTs;
	}
	
	public String getLastPayDateStr() {
		if (lastPayTs == null) {
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(lastPayTs);
	}

	public void setLastPayTs(Date lastPayTs) {
		this.lastPayTs = lastPayTs;
	}

	public BigDecimal getPrincipalAmt() {
		return principalAmt;
	}

	public void setPrincipalAmt(BigDecimal principalAmt) {
		this.principalAmt = principalAmt;
	}

	public BigDecimal getInterestAmt() {
		return interestAmt;
	}

	public void setInterestAmt(BigDecimal interestAmt) {
		this.interestAmt = interestAmt;
	}

	public BigDecimal getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(BigDecimal feeAmt) {
		this.feeAmt = feeAmt;
	}

	public BigDecimal getPrincipalForfeit() {
		return principalForfeit;
	}

	public void setPrincipalForfeit(BigDecimal principalForfeit) {
		this.principalForfeit = principalForfeit;
	}

	public BigDecimal getInterestForfeit() {
		return interestForfeit;
	}

	public void setInterestForfeit(BigDecimal interestForfeit) {
		this.interestForfeit = interestForfeit;
	}

	public BigDecimal getFeeForfeit() {
		return feeForfeit;
	}

	public void setFeeForfeit(BigDecimal feeForfeit) {
		this.feeForfeit = feeForfeit;
	}

	public BigDecimal getWrtrPrinForfeit() {
		return wrtrPrinForfeit;
	}

	public void setWrtrPrinForfeit(BigDecimal wrtrPrinForfeit) {
		this.wrtrPrinForfeit = wrtrPrinForfeit;
	}

	public BigDecimal getWrtrInterestForfeit() {
		return wrtrInterestForfeit;
	}

	public void setWrtrInterestForfeit(BigDecimal wrtrInterestForfeit) {
		this.wrtrInterestForfeit = wrtrInterestForfeit;
	}

	public BigDecimal getPayPrincipalAmt() {
		return payPrincipalAmt;
	}

	public void setPayPrincipalAmt(BigDecimal payPrincipalAmt) {
		this.payPrincipalAmt = payPrincipalAmt;
	}

	public BigDecimal getPayInterestAmt() {
		return payInterestAmt;
	}

	public void setPayInterestAmt(BigDecimal payInterestAmt) {
		this.payInterestAmt = payInterestAmt;
	}

	public BigDecimal getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}

	public BigDecimal getPayPrincipalForfeit() {
		return payPrincipalForfeit;
	}

	public void setPayPrincipalForfeit(BigDecimal payPrincipalForfeit) {
		this.payPrincipalForfeit = payPrincipalForfeit;
	}

	public BigDecimal getPayInterestForfeit() {
		return payInterestForfeit;
	}

	public void setPayInterestForfeit(BigDecimal payInterestForfeit) {
		this.payInterestForfeit = payInterestForfeit;
	}

	public BigDecimal getPayFeeForfeit() {
		return payFeeForfeit;
	}

	public void setPayFeeForfeit(BigDecimal payFeeForfeit) {
		this.payFeeForfeit = payFeeForfeit;
	}

	public BigDecimal getPayWrtrPrinForfeit() {
		return payWrtrPrinForfeit;
	}

	public void setPayWrtrPrinForfeit(BigDecimal payWrtrPrinForfeit) {
		this.payWrtrPrinForfeit = payWrtrPrinForfeit;
	}

	public BigDecimal getPayWrtrInterestForfeit() {
		return payWrtrInterestForfeit;
	}

	public void setPayWrtrInterestForfeit(BigDecimal payWrtrInterestForfeit) {
		this.payWrtrInterestForfeit = payWrtrInterestForfeit;
	}

	public EPayStatus getStatus() {
		return status;
	}

	public void setStatus(EPayStatus status) {
		this.status = status;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

}
