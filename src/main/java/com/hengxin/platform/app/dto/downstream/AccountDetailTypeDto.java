package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;
import java.util.List;

import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * AccountDetailTypeDto.
 *
 */
public class AccountDetailTypeDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<EFundUseType> detailType;

	public List<EFundUseType> getDetailType() {
		return detailType;
	}

	public void setDetailType(List<EFundUseType> detailType) {
		this.detailType = detailType;
	}

}
