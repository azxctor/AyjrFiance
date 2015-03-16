package com.hengxin.platform.fund.dto;

import java.io.Serializable;

public class AcctFreezeOrUnDto implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId; // 用户Id

    private String trxMemo; //冻结或者解冻理由
    
    private String freezeJnlNo; // 冻结流水号

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrxMemo() {
        return trxMemo;
    }

    public void setTrxMemo(String trxMemo) {
        this.trxMemo = trxMemo;
    }

    public String getFreezeJnlNo() {
        return freezeJnlNo;
    }

    public void setFreezeJnlNo(String freezeJnlNo) {
        this.freezeJnlNo = freezeJnlNo;
    }

}
