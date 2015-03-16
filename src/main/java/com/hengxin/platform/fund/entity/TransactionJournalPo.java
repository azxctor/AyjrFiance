package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.converter.EBankTypeEnumConverter;
import com.hengxin.platform.fund.entity.converter.EFlagTypeEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundPayRecvFlagEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundUseTypeEnumConverter;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * @author qimingzou
 * 
 */
@Entity
@Table(name = "V_INVSTR_TRX_JNL")
@SuppressWarnings("serial")
@EntityListeners(IdInjectionEntityListener.class)
public class TransactionJournalPo implements Serializable {
	
	@Id
	@Column(name = "JNL_NO")
	private String jnlNo;
	
	@Column(name = "BNK_CD")
	@Convert(converter = EBankTypeEnumConverter.class)
    private EBankType bnkType;
	
	@Column(name = "BNK_OPEN_PROV")
    private String bnkOpenProv;

    @Column(name = "BNK_OPEN_CITY")
    private String bnkOpenCity;

    @Column(name = "BNK_OPEN_BRCH")
    private String bnkBrch;
	
	@Column(name = "SIGNED_FLG")
	@Convert(converter = EFlagTypeEnumConverter.class)
    private EFlagType signedFlg;

	@Column(name = "ACCT_NO")
	private String acctNo;
	
	@Column(name = "BAL")
	private BigDecimal bal;
	
	@Column(name = "PAY_RECV_FLG")
	@Convert(converter = FundPayRecvFlagEnumConverter.class)
	private EFundPayRecvFlag payRecvFlg;
	
	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;

	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDt;

	@Column(name = "TRX_MEMO")
	private String trxMemo;
	
	@Column(name = "USE_TYPE")
	@Convert(converter = FundUseTypeEnumConverter.class)
	private EFundUseType useType;
	
	@Column(name = "AGENT")
	private String agent;
	
	@Column(name = "AGENT_NAME")
	private String agentName;
	
	@Column(name = "AUTHZD_CTR_ID")
	private String authzdCtrId;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "OWNER_AUTHZD_CTR_ID")
	private String ownerAthzdCtrId;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public EBankType getBnkType() {
		return bnkType;
	}

	public void setBnkType(EBankType bnkType) {
		this.bnkType = bnkType;
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

	public String getAuthzdCtrId() {
		return authzdCtrId;
	}

	public void setAuthzdCtrId(String authzdCtrId) {
		this.authzdCtrId = authzdCtrId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerAthzdCtrId() {
		return ownerAthzdCtrId;
	}

	public void setOwnerAthzdCtrId(String ownerAthzdCtrId) {
		this.ownerAthzdCtrId = ownerAthzdCtrId;
	}

}
