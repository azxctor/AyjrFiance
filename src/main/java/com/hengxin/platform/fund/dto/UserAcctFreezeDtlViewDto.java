package com.hengxin.platform.fund.dto;

import java.io.Serializable;
import java.util.Date;

public class UserAcctFreezeDtlViewDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jnlNo; // 冻结流水号

    private String acctNo; // 交易账号

    private String createOpid; // 创建人

    private String userName; // 用户名

    private String trxMemo; // 冻结理由

    private Date effectDate; // 冻结时间

    private String userId; //(hide)

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

    public String getCreateOpid() {
        return createOpid;
    }

    public void setCreateOpid(String createOpid) {
        this.createOpid = createOpid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrxMemo() {
        return trxMemo;
    }

    public void setTrxMemo(String trxMemo) {
        this.trxMemo = trxMemo;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
