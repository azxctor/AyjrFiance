package com.hengxin.platform.market.dto.upstream;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class FinancingMarketSearchDto extends DataTablesRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean topurchase;//待申购

	private  boolean prerelease;//预发布

	private boolean hasexpired;//已过期

	private Date createDate;

	public boolean isTopurchase() {
		return topurchase;
	}

	public void setTopurchase(boolean topurchase) {
		this.topurchase = topurchase;
	}

	public boolean isPrerelease() {
		return prerelease;
	}

	public void setPrerelease(boolean prerelease) {
		this.prerelease = prerelease;
	}

	public boolean isHasexpired() {
		return hasexpired;
	}

	public void setHasexpired(boolean hasexpired) {
		this.hasexpired = hasexpired;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


}
