package com.hengxin.platform.account.dto.upstream;

import java.io.Serializable;
import java.util.List;

import com.hengxin.platform.account.dto.UnsignedRechargeInfoDto;


/**
 * 
 * @author jishen
 *
 */
public class UnsignedUserInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UnsignedRechargeInfoDto> list;

	public List<UnsignedRechargeInfoDto> getList() {
		return list;
	}

	public void setList(List<UnsignedRechargeInfoDto> list) {
		this.list = list;
	}

}
