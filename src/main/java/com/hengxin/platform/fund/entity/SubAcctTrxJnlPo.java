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
import com.hengxin.platform.fund.entity.converter.ECashPoolEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundPayRecvFlagEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundUseTypeEnumConverter;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * Class Name: SubAcctTrxJnlPo
 * 
 * @author jishen
 * 会员综合账户子账户交易日志表
 */
@Entity
@Table(name="AC_TRX_JNL")
@SuppressWarnings("serial")
@EntityListeners(IdInjectionEntityListener.class)
public class SubAcctTrxJnlPo implements Serializable {

	@Id
	@Column(name = "JNL_NO")
	private String jnlNo;
	
	@Column(name = "EVENT_ID")
	private String eventId;
	
	@Column(name = "ACCT_NO")
	private String acctNo;
	
	@Column(name = "SUB_ACCT_NO")
	private String subAcctNo;
	
	@Column(name = "USE_TYPE")
	@Convert(converter = FundUseTypeEnumConverter.class)
	private EFundUseType useType;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDt;
	
	@Column(name = "PAY_RECV_FLG")
	@Convert(converter = FundPayRecvFlagEnumConverter.class)
	private EFundPayRecvFlag payRecvFlg;
	
	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;
    
    @Column(name = "CASH_POOL")
    @Convert(converter = ECashPoolEnumConverter.class)
    private ECashPool cashPool;
    
    @Column(name = "BAL")
    private BigDecimal bal;
	
	@Column(name = "TRX_MEMO")
	private String trxMemo;
	
	@Column(name = "REL_BIZ_ID")
	private String relBizId;
	
	@Column(name = "AUTHZD_CTR_ID")
	//@Transient
	private String authzdCtrId;
	
	@Column(name = "CREATE_OPID")
	private String createOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getSubAcctNo() {
		return subAcctNo;
	}

	public void setSubAcctNo(String subAcctNo) {
		this.subAcctNo = subAcctNo;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public String getRelBizId() {
		return relBizId;
	}

	public void setRelBizId(String relBizId) {
		this.relBizId = relBizId;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
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

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}

	public String getAuthzdCtrId() {
		return authzdCtrId;
	}

	public void setAuthzdCtrId(String authzdCtrId) {
		this.authzdCtrId = authzdCtrId;
	}
	
}
