package com.hengxin.platform.market.dto.upstream;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.market.enums.EHistoryPeriod;

public class FinancingMarketHistorySearchDto extends DataTablesRequestDto
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private EHistoryPeriod period = EHistoryPeriod.ONEWEEK;

	public EHistoryPeriod getPeriod() {
		return period;
	}

	public void setPeriod(EHistoryPeriod period) {
		this.period = period;
	}

}
