package com.hengxin.platform.product.dto;

import java.math.BigDecimal;

public class AccountDtoForAccountNo {
    private String acctNo;

    private String userId;

    private String acctStatus;
    
    private String cashPool;
    
    private BigDecimal reservedAsset;

    private Long frzCt;

    private String acctType;

    /**
     * @return return the value of the var acctNo
     */
    
    public String getAcctNo() {
        return acctNo;
    }

    /**
     * @param acctNo Set acctNo value
     */
    
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    /**
     * @return return the value of the var userId
     */
    
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId Set userId value
     */
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return return the value of the var acctStatus
     */
    
    public String getAcctStatus() {
        return acctStatus;
    }

    /**
     * @param acctStatus Set acctStatus value
     */
    
    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    /**
     * @return return the value of the var cashPool
     */
    
    public String getCashPool() {
        return cashPool;
    }

    /**
     * @param cashPool Set cashPool value
     */
    
    public void setCashPool(String cashPool) {
        this.cashPool = cashPool;
    }

    /**
     * @return return the value of the var reservedAsset
     */
    
    public BigDecimal getReservedAsset() {
        return reservedAsset;
    }

    /**
     * @param reservedAsset Set reservedAsset value
     */
    
    public void setReservedAsset(BigDecimal reservedAsset) {
        this.reservedAsset = reservedAsset;
    }

    /**
     * @return return the value of the var frzCt
     */
    
    public Long getFrzCt() {
        return frzCt;
    }

    /**
     * @param frzCt Set frzCt value
     */
    
    public void setFrzCt(Long frzCt) {
        this.frzCt = frzCt;
    }

    /**
     * @return return the value of the var acctType
     */
    
    public String getAcctType() {
        return acctType;
    }

    /**
     * @param acctType Set acctType value
     */
    
    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }
    
    
    
}
