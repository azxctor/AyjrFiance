package com.hengxin.platform.fund.service;

import java.util.Date;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.enums.ECashPool;

/**
 * 资金账户变更资金池
 * 
 * @author dcliu
 * 
 */
public interface FundAcctCashPoolChangeService {

	/**
	 * 会员资金账户所属资金池变更，资金调拨处理
	 * 
	 * @param eventId
	 * @param acctNo
	 * @param fromPool
	 * @param toPool
	 * @param trxMemo
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws BizServiceException
	 */
	public void changePoolTreasurerAcctBal(String eventId, String acctNo,
			ECashPool fromPool, ECashPool toPool, String trxMemo, String bizId,
			String currOpId, Date workDate) throws BizServiceException;
}
