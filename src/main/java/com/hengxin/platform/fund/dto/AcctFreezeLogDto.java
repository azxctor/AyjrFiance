package com.hengxin.platform.fund.dto;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class AcctFreezeLogDto  extends DataTablesRequestDto {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acctNo; // 交易账号

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    

}
