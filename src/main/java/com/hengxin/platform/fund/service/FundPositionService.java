package com.hengxin.platform.fund.service;

import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.ManualPaymentReq;
import com.hengxin.platform.fund.dto.biz.req.PrePayMentReq;
import com.hengxin.platform.fund.dto.biz.req.SubscribeCRReq;
import com.hengxin.platform.fund.dto.biz.req.TransferCRReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * FundPositionService.
 *
 */
public interface FundPositionService {

	/**
	 * 债权转让.
	 * 
	 * @return
	 * @throws AmtParamInvalidException
	 */
	String transferCR(TransferCRReq req) throws BizServiceException;

	/**
	 * 债权转让资金划转.
	 * 
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	void transferCRPayAmt(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId, String pkgId, String seqId,
			String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 申购债权.
	 * 
	 * @return
	 * @throws BizServiceException
	 */
	@Deprecated
	String subscribeCR(SubscribeCRReq req) throws BizServiceException;

	/**
	 * 提前还款.
	 * 
	 * @throws BizServiceException
	 */
	void prepayment(PrePayMentReq req) throws BizServiceException;
	
	
	/**
	 * 手工还款.
	 * @param manualPaymentReq
	 */
	void manualPayment(ManualPaymentReq manualPaymentReq);
}
