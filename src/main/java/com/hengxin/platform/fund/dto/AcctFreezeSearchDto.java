package com.hengxin.platform.fund.dto;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class AcctFreezeSearchDto extends DataTablesRequestDto {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acctNo; // 交易账号
    private String userName; // 用户名

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
