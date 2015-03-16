package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Class Name: AcctBal
 * 
 * @author liudc
 * 
 */
@SuppressWarnings("serial")
public class Acct implements Serializable {

    /**
     * 综合账户编号
     */
    private String acctNo;
    /**
     * 会员编号
     */
    private String userId;
    /**
     * 账户状态
     */
    private String acctStatus;
    
    /**
     * 当前资金池
     */
    private String cashPool;
    
    /**
     * 保留资产
     */
    private BigDecimal reservedAsset;
    
    /**
     * 只收不付冻结次数
     */
    private long frzCt;
    
    /**
     * 账户类型
     */
    private String acctType;
    /**
     * 创建用户
     */
    private String createOpid;
    /**
     * 创建时间
     */
    private String createTs;
    /**
     * 更新用户
     */
    private String lastMntOpid;
    /**
     * 更新时间
     */
    private Date lastMntTs;

    /**
     * 获得综合账户编号
     * 
     * @return
     */
    public String getAcctNo() {
        return acctNo;
    }

    /**
     * 设置综合账户编号
     * 
     * @param acctNo
     */
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    /**
     * 获取会员编号
     * 
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 获得会员编号
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取账户状态
     * 
     * @return
     */
    public String getAcctStatus() {
        return acctStatus;
    }

    /**
     * 设置账户状态
     * 
     * @param acctStatus
     */
    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    /**
     * 获取创建用户
     * 
     * @return
     */
    public String getCreateOpid() {
        return createOpid;
    }

    /**
     * 设置创建用户
     * 
     * @param createOpid
     */
    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    /**
     * 获取创建时间
     * 
     * @return
     */
    public String getCreateTs() {
        return createTs;
    }

    /**
     * 设置创建时间
     * 
     * @param createTs
     */
    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

    /**
     * 获取更新用户
     * 
     * @return
     */
    public String getLastMntOpid() {
        return lastMntOpid;
    }

    /**
     * 设置更新用户
     * 
     * @param lastMntOpid
     */
    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    /**
     * 获取更新时间
     * 
     * @return
     */
    public Date getLastMntTs() {
        return lastMntTs;
    }

    /**
     * 设置更新时间
     * 
     * @param lastMntTs
     */
    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    /**
     * 获取账户类型
     * 
     * @return
     */
    public String getAcctType() {
        return acctType;
    }

    /**
     * 设置账户类型
     * 
     * @param lastMntTs
     */
    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

	public long getFrzCt() {
		return frzCt;
	}

	public void setFrzCt(long frzCt) {
		this.frzCt = frzCt;
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
