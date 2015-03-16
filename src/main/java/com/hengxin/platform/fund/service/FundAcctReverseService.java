package com.hengxin.platform.fund.service;

import java.util.Date;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;

/**
 * 资金账户冲正
 * @author dcliu
 */
public interface FundAcctReverseService {

	/**
	 * 会员资金账户充值金额冲正
	 * 
	 * @param eventId
	 * @param payerInfo
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws BizServiceException
	 */
	public void reverseRechargeAmt(String eventId, TransferInfo payerInfo, String bizId,
			String currOpId, Date workDate) throws BizServiceException;
	
	
	/**
	 * 会员资金账户提现金额冲正
	 * @param eventId
	 * @param payerInfo
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws BizServiceException
	 */
	public void reverseWithdrawAmt(String eventId, TransferInfo payerInfo, String bizId,
			String currOpId, Date workDate) throws BizServiceException;

}
