package com.hengxin.platform.market.dto;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

/**
 * Search package.
 */
public class AutoSubscribeSearchDto extends DataTablesRequestDto {

	private static final long serialVersionUID = 1L;

	private String packageId;
	
	private String accountId;
	
	private boolean includeUser = true;

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public boolean isIncludeUser() {
		return includeUser;
	}

	public void setIncludeUser(boolean includeUser) {
		this.includeUser = includeUser;
	}

}
