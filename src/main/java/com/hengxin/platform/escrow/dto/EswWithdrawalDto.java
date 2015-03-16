package com.hengxin.platform.escrow.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.escrow.validator.PayPwdForWithdrawalCheck;
import com.hengxin.platform.escrow.validator.WithdrawalAmountCheck;


/**
 * 提现传入信息
 * 
 * @author chenwulou
 * 
 */
public class EswWithdrawalDto implements Serializable {

	private static final long serialVersionUID = 1L;

	// 支付密码
	@NotEmpty(groups = { SubmitWithdrawal.class }, message = "{member.error.field.empty}")
	@PayPwdForWithdrawalCheck(groups = { SubmitWithdrawal.class })
	private String payPwd;

	// 验证码
	@NotEmpty(groups = { SubmitWithdrawal.class }, message = "{member.error.field.empty}")
	private String authCode;

	// 提现金额
	@NotNull(groups = { SubmitWithdrawal.class }, message = "{member.error.field.empty}")
	@WithdrawalAmountCheck(groups = { SubmitWithdrawal.class })
	private BigDecimal amount;
	
	// 银行卡号
	private String bankCardNo;
	
	// 持卡人姓名
	private String bankCardName;
	
	// 备注
	private String trxMemo;

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCardName() {
		return bankCardName;
	}

	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}
	
	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface SubmitWithdrawal {

	}

}
