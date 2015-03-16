package com.hengxin.platform.fund.service;

import java.math.BigDecimal;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

public interface AddMaxCashableAmtService {

	/**
	 * 调整最大可提现金额
	 * 
	 * @param userId
	 * @param trxAmt
	 * @param currOpId
	 */
	public void addMaxCashableAmt(String userId, BigDecimal trxAmt,
			String currOpId) throws BizServiceException,
			AvlBalNotEnoughException;
}
