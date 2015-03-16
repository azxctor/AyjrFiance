package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.fund.entity.converter.EAcctStatusEnumConverter;
import com.hengxin.platform.fund.entity.converter.EAcctTypeEnumConverter;
import com.hengxin.platform.fund.enums.EAcctStatus;
import com.hengxin.platform.fund.enums.EAcctType;

@Entity
@SuppressWarnings("serial")
@Table(name = "V_USER_ACCT_INFO")
public class UserAcctInfoView implements Serializable {

	@Id
    @Column(name = "ACCT_NO")
	private String acctNo;
	
    @Column(name = "USER_ID")
	private String userId;
	
    @Column(name = "USER_NAME")
	private String userName;
	
    @Column(name = "ACCT_STATUS")
    @Convert(converter = EAcctStatusEnumConverter.class)
	private EAcctStatus acctStatus;
	
    @Column(name = "ACCT_TYPE")
    @Convert(converter = EAcctTypeEnumConverter.class)
	private EAcctType acctType;
	
    @Column(name = "FRZ_CT")
	private Long frzCt;
	
    @Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
	private Date lastMntTs;
	
    @Column(name = "CURR_BAL")
	private BigDecimal currBal;
	
    @Column(name = "XWB_BAL")
	private BigDecimal xwbBal;

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

	public EAcctStatus getAcctStatus() {
		return acctStatus;
	}

	public void setAcctStatus(EAcctStatus acctStatus) {
		this.acctStatus = acctStatus;
	}

	public EAcctType getAcctType() {
		return acctType;
	}

	public void setAcctType(EAcctType acctType) {
		this.acctType = acctType;
	}

	public Long getFrzCt() {
		return frzCt;
	}

	public void setFrzCt(Long frzCt) {
		this.frzCt = frzCt;
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

	public BigDecimal getCurrBal() {
		return currBal;
	}

	public void setCurrBal(BigDecimal currBal) {
		this.currBal = currBal;
	}

	public BigDecimal getXwbBal() {
		return xwbBal;
	}

	public void setXwbBal(BigDecimal xwbBal) {
		this.xwbBal = xwbBal;
	}
	
}
