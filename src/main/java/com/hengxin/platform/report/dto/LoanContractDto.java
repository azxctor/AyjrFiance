package com.hengxin.platform.report.dto;

import java.util.List;

public class LoanContractDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> autorizationList; // 授权服务中心list

    public List<String> getAutorizationList() {
        return autorizationList;
    }

    public void setAutorizationList(List<String> autorizationList) {
        this.autorizationList = autorizationList;
    }

}
