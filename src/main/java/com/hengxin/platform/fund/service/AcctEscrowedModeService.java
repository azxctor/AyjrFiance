package com.hengxin.platform.fund.service;

import com.hengxin.platform.fund.enums.ECashPool;

public interface AcctEscrowedModeService {

	/**
	 * 通过银行卡所属银行与签约信息获取资金池
	 * 此方法只能在 新建综合账户 与 新建银行卡的时候用，
	 * 其他地方可能不准确
	 * @param signedFlag
	 * @param bnkCode
	 * @return
	 */
	public ECashPool getCashPool(String signedFlag, String bnkCode);
	
	/**
	 * 是否托管模式
	 * @return
	 */
	public boolean isEscrowedMode();

}
