package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 *  FinancierBreachFundClearingService.
 *
 */
public interface FinancierBreachFundClearingService {

	/**
	 * 查询会员子账户可用余额(忽略冻结金额,适用于违约还款场景).
	 * 
	 * @param userId
	 * @param subAcctType
	 * @return
	 * @throws BizServiceException
	 */
	BigDecimal getUserCurrentAvlAmtIgnoreFrzAmt(String userId, boolean isAddXwb) throws BizServiceException;
	
	/**
	 * 退还借款履约保证金.
	 * @param req
	 * @return
	 * @throws BizServiceException
	 */
	BigDecimal refundLoadnHonourAgtDeposit(UnReserveReq req)throws BizServiceException;
	
	
	/**
	 * 还款违约资金解冻.
	 * @param req
	 * @return
	 * @throws BizServiceException
	 */
	BigDecimal unFreezeLateFee(UnFreezeReq req)throws BizServiceException;
	
	/**
	 * 冻结欠款金额.
	 * @param req
	 * @return
	 * @throws BizServiceException
	 */
	String freezeArrearageAmt(FreezeReq req)throws BizServiceException;
	
	/**
	 * 违约还款，资金清分.
	 * 
	 * @param eventId
	 * @param payerList
	 * @param payeeList
	 * @param isAddXwbPay
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	void breachRepaymentFundClearing(String eventId,
			List<TransferInfo> payerList, List<TransferInfo> payeeList,
			boolean isAddXwbPay, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate)throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 投资人付利息、罚金收益部分金额给平台.
	 * @param eventId
	 * @param payerList
	 * @param exchangePayee
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 */
	void payInvsIntrProfitToExchange(String eventId, List<TransferInfo> payerList, List<TransferInfo> payeeList,
			String bizId, String pkgId, String seqId, String currOpId, Date workDate);

}
