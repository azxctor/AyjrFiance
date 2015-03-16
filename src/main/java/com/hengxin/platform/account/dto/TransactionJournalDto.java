package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * @author qimingzou
 * 
 */
public class TransactionJournalDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 开户行类型
	 */
	private EBankType bnkType;

	/**
	 * 开户行所在省
	 */
	private String bnkOpenProv;

	/**
	 * 开户行所在市
	 */
	private String bnkOpenCity;

	/**
	 * 开户行支行
	 */
	private String bnkBrch;

	/**
	 * 银转商签约
	 */
	private EFlagType signedFlg;

	/**
	 * 交易账户
	 */
	private String acctNo;

	/**
	 * 账户余额
	 */
	private BigDecimal bal;

	/**
	 * 资金收付类型, R-收, P-付
	 */
	private EFundPayRecvFlag payRecvFlg;

	/**
	 * 交易金额
	 */
	private BigDecimal trxAmt;

	/**
	 * 交易日期
	 */
	private Date trxDt;

	/**
	 * 备注
	 */
	private String trxMemo;

	/**
	 * 交易类型
	 */
	private EFundUseType useType;

	/**
	 * 经办人
	 */
	private String agent;

	/**
	 * 代办人
	 */
	private String agentName;

	/**
	 * 会员姓名
	 */
	private String name;
	
	/**
	 * 返回存入金额
	 */
	public BigDecimal getReceiveAmt() {
		if (EFundPayRecvFlag.RECV == payRecvFlg) {
			return trxAmt;
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 返回支出金额
	 */
	public BigDecimal getPayAmt() {
		if (EFundPayRecvFlag.PAY == payRecvFlg) {
			return trxAmt;
		}
		return BigDecimal.ZERO;
	}

	public String getBnkOpenProv() {
		return bnkOpenProv;
	}

	public void setBnkOpenProv(String bnkOpenProv) {
		this.bnkOpenProv = bnkOpenProv;
	}

	public String getBnkOpenCity() {
		return bnkOpenCity;
	}

	public void setBnkOpenCity(String bnkOpenCity) {
		this.bnkOpenCity = bnkOpenCity;
	}

	public String getBnkBrch() {
		return bnkBrch;
	}

	public void setBnkBrch(String bnkBrch) {
		this.bnkBrch = bnkBrch;
	}

	/**
	 * 返回银行全名
	 */
	public String getFullBackName() {
		String bankName = (null == bnkType ? "" : SystemDictUtil
				.getDictByCategoryAndCode(EOptionCategory.BANK,
						bnkType.getCode()).getText());
		StringBuilder bank = new StringBuilder();
		String province = "";
		if (bnkOpenProv != null) {
			DynamicOption provOption = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, bnkOpenProv);
			if (provOption != null) {
				province = provOption.getText();
			} else {
				province = bnkOpenProv;
			}
		}
		String city = "";
		if (bnkOpenCity != null) {
			DynamicOption cityOption = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, bnkOpenCity);
			if (cityOption != null) {
				city = cityOption.getText();
			} else {
				city = bnkOpenCity;
			}
		}
		bank.append(province).append(" ").append(city).append(" ")
				.append(bankName).append(" ")
				.append(bnkBrch == null ? "" : bnkBrch);
		return bank.toString().trim();
	}

	public EBankType getBnkType() {
		return bnkType;
	}

	public void setBnkType(EBankType bnkType) {
		this.bnkType = bnkType;
	}

	public EFlagType getSignedFlg() {
		return signedFlg;
	}

	public void setSignedFlg(EFlagType signedFlg) {
		this.signedFlg = signedFlg;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public EFundPayRecvFlag getPayRecvFlg() {
		return payRecvFlg;
	}

	public void setPayRecvFlg(EFundPayRecvFlag payRecvFlg) {
		this.payRecvFlg = payRecvFlg;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
