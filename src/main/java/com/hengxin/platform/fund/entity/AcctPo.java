package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.security.entity.SimpleUserPo;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: AcctBalPo 会员综合账户表
 * 
 * @author jishen
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AC_ACCT")
@EntityListeners(IdInjectionEntityListener.class)
public class AcctPo implements Serializable {

    @Id
    @Column(name = "ACCT_NO")
    private String acctNo;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ACCT_STATUS")
    private String acctStatus;
    
    @Column(name = "CASH_POOL")
    private String cashPool;
    
    @Column(name = "RESERVED_ASSET")
    private BigDecimal reservedAsset;

    @Column(name = "FRZ_CT")
    private Long frzCt;

    @Column(name = "ACCT_TYPE")
    private String acctType;

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

    @Version
    @Column(name = "VERSION_CT")
    private Long versionCt;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "acctNo", fetch = FetchType.LAZY)
    private List<SubAcctPo> subAccounts;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private SimpleUserPo user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserPo userPo;

    public Long getVersionCt() {
        return versionCt;
    }

    public void setVersionCt(Long versionCt) {
        this.versionCt = versionCt;
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

    public String getAcctStatus() {
        return acctStatus;
    }

    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
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

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public Long getFrzCt() {
        return frzCt;
    }

    public void setFrzCt(Long frzCt) {
        this.frzCt = frzCt;
    }

    public List<SubAcctPo> getSubAccounts() {
        return subAccounts;
    }

    public void setSubAccounts(List<SubAcctPo> subAccounts) {
        this.subAccounts = subAccounts;
    }

    public SimpleUserPo getUser() {
        return user;
    }

    public void setUser(SimpleUserPo user) {
        this.user = user;
    }

    public UserPo getUserPo() {
        return userPo;
    }

    public void setUserPo(UserPo userPo) {
        this.userPo = userPo;
    }

	public String getCashPool() {
		return cashPool;
	}

	public void setCashPool(String cashPool) {
		this.cashPool = cashPool;
	}

	public BigDecimal getReservedAsset() {
		return reservedAsset;
	}

	public void setReservedAsset(BigDecimal reservedAsset) {
		this.reservedAsset = reservedAsset;
	}

}
