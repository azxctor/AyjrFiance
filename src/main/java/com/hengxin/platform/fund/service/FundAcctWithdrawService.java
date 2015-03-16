package com.hengxin.platform.fund.service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.FundAcctWithDrawReq;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 会员提现相关服务
 * 
 * @author dcliu
 * 
 */
public interface FundAcctWithdrawService {

	/**
	 * 提现金额保留
	 * 
	 * @param args
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public String reserveWithdrawAmt(ReserveReq req)
			throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 提现金额解保留(当提现操作失败，将资金进行解保留)
	 * 
	 * @param args
	 * @return
	 * @throws BizServiceException
	 */
	public String unReserveWithdrawAmt(UnReserveReq req)
			throws BizServiceException;

	/**
	 * 签约会员资金账户提现扣款
	 * 
	 * @param args
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public String signedAcctWithdrawAmt(FundAcctWithDrawReq req)
			throws BizServiceException, AvlBalNotEnoughException;
	
	/**
	 * 非签约资金账户提现扣款
	 * 
	 * @param args
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public String unSignedAcctWithdrawAmt(FundAcctWithDrawReq req)
			throws BizServiceException, AvlBalNotEnoughException;

}
