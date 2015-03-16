package com.hengxin.platform.fund.dto;

import java.io.Serializable;

import com.hengxin.platform.fund.enums.EFnrOperType;

public class AcctFreezeLogListDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jnlNo; // 交易流水号

    private String trxMemo; // 冻结理由

    private String effectDate; // 冻结时间

    private String expireDate; // 解冻时间

    private EFnrOperType operType; // 操作类型

    public String getJnlNo() {
        return jnlNo;
    }

    public void setJnlNo(String jnlNo) {
        this.jnlNo = jnlNo;
    }

    public String getTrxMemo() {
        return trxMemo;
    }

    public void setTrxMemo(String trxMemo) {
        this.trxMemo = trxMemo;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public EFnrOperType getOperType() {
        return operType;
    }

    public void setOperType(EFnrOperType operType) {
        this.operType = operType;
    }

}
