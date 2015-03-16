package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.fund.entity.converter.EFnrOperTypeEnumConverter;
import com.hengxin.platform.fund.entity.converter.EFnrStatusEnumConverter;
import com.hengxin.platform.fund.enums.EFnrOperType;
import com.hengxin.platform.fund.enums.EFnrStatus;

@Entity
@SuppressWarnings("serial")
@Table(name = "V_USER_ACCT_FRZ_DTL")
public class UserAcctFreezeDtlView implements Serializable {
	
	@Id
    @Column(name = "JNL_NO")
	private String jnlNo;
	
    @Column(name = "ACCT_NO")
	private String acctNo;
	
    @Column(name = "USER_ID")
	private String userId;
	
    @Column(name = "USER_NAME")
	private String userName;
    
    @Column(name = "OPER_TYPE")
    @Convert(converter = EFnrOperTypeEnumConverter.class)
    private EFnrOperType operType;
	
    @Temporal(TemporalType.DATE)
    @Column(name = "EFFECT_DT")
	private Date effectDate;
	
    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRE_DT")
	private Date expireDate;
	
    @Column(name = "STATUS")
    @Convert(converter = EFnrStatusEnumConverter.class)
	private EFnrStatus status;
	
    @Column(name = "TRX_MEMO")
	private String trxMemo;
	
    @Column(name = "CREATE_OPID")
	private String createOpid;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
	private Date createTs;
	
	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
	private Date lastMntTs;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public EFnrStatus getStatus() {
		return status;
	}

	public void setStatus(EFnrStatus status) {
		this.status = status;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
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

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public EFnrOperType getOperType() {
		return operType;
	}

	public void setOperType(EFnrOperType operType) {
		this.operType = operType;
	}
	
}
