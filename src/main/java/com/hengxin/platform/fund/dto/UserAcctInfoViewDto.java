package com.hengxin.platform.fund.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.fund.enums.EAcctStatus;

public class UserAcctInfoViewDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String acctNo; // 交易账号

    private String userName; // 用户名

    private EAcctStatus acctStatus; // 用户状态

    private BigDecimal currBal; // 活期可用余额

    private BigDecimal xwbBal; // 小微宝余额

    private Long frzCt; // 冻结次数

    private String userId; // 用户Id (hide)

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

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public Long getFrzCt() {
        return frzCt;
    }

    public void setFrzCt(Long frzCt) {
        this.frzCt = frzCt;
    }

}
