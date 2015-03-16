package com.hengxin.platform.fund.service;

import java.math.BigDecimal;

import com.hengxin.platform.common.exception.BizServiceException;

public interface FundAcctBalService {

	/**
	 * 获取会员活期账户可用余额
	 * 
	 * @param userId
	 * @return
	 * @throws BizServiceException
	 */
	public BigDecimal getUserCurrentAcctAvlBal(String userId)
			throws BizServiceException;

	/**
	 * 获取会员活期账户可用余额
	 * 
	 * @param userId
	 * @return
	 * @throws BizServiceException
	 */
	public BigDecimal getUserCurrentAcctAvlBal(String userId, boolean isAddXwb)
			throws BizServiceException;

	/**
	 * 获取会员活期账户可提现金额
	 * 
	 * @param userId
	 * @return
	 * @throws BizServiceException
	 */
	public BigDecimal getUserCashableAmt(String userId)
			throws BizServiceException;
	
	
	/**
	 * 获取会员所有子账户余额
	 * @param userId
	 * @return
	 * @throws BizServiceException
	 */
	public BigDecimal getUserSubAcctsSumBalAmt(String userId)
			throws BizServiceException;

}
