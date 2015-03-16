package com.hengxin.platform.fund.service;

import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 融资会员撤单资金相关服务
 * 
 * @author dcliu
 * 
 */
public interface FinancierRevokeFundService {

	/**
	 * 融资会员撤单,付违约金(委托发布履约保证金)
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
	public void compensateInvestorsAndExchangeForRevoke(String eventId,
			List<DedicatedTransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String pkgId, 
			String seqId, String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 申购预支付资金退还
	 * 
	 * @param req
	 * @throws ServiceException
	 */
	public void refundSubsPrePayAmt(UnReserveReq req)
			throws BizServiceException;
	
	
	/**
	 * 申购预支付费用退还
	 * 
	 * @param req
	 * @throws ServiceException
	 */
	public void refundSubsFeePrePayAmt(UnReserveReq req)
			throws BizServiceException;

}
