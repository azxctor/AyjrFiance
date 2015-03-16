package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.Invs2LoanPledgeTransferAmtReq;
import com.hengxin.platform.fund.dto.biz.req.LoadnHonourAgtDepositPayReq;
import com.hengxin.platform.fund.dto.biz.req.RiskTransferFinancingAmtReq;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * Invs2LoanFundService.
 */
public interface Invs2LoanFundService {

	/**
	 * 投转贷利息资金保留.
	 * 
	 * @param req
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	String invs2LoanPrePayInterest(ReserveReq req)
			throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 全额退还预支付利息.
	 * 
	 * @param req
	 * @throws BizServiceException
	 */
	void refundPrePayInterest(UnReserveReq req)
			throws BizServiceException;

	/**
	 * 投转贷付息操作.
	 * 
	 * @param eventId
	 * @param payerList
	 * @param payeeList
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	void payInterest2Investors(String eventId,
			List<DedicatedTransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String currOpId,
			Date workDate) throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 投转贷风控放款.
	 * 
	 * @return
	 * @throws BizServiceException
	 */
	BigDecimal invs2LoanRiskTransferAmt(RiskTransferFinancingAmtReq req)
			throws BizServiceException;

	/**
	 * 投转贷风控放款之后交借款履约保证金.
	 * 
	 * @param req
	 * @return
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	String payLoadnHonourAgtDeposit(String eventId,
			LoadnHonourAgtDepositPayReq req) throws AvlBalNotEnoughException,
			BizServiceException;

	/**
	 * 投转贷风控放款之后进行贷款金额质押、贷款金额转活期、贷款金额转质押、累加提现金额.
	 * 
	 * @param req
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	String pledgeAmtAndTransferAmt(Invs2LoanPledgeTransferAmtReq req)
			throws BizServiceException, AvlBalNotEnoughException;

}
