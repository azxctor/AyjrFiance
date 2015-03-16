package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.TradeFeePayReq;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 融资人签约服务
 * 
 * @author dcliu
 * 
 */
public interface FinancierSigningService {

	/**
	 * 融资会员交易签约，向交易所支付费用
	 * 
	 * @param args
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public void payTradeFeeToExchange(String eventId, String userId,
			List<TradeFeePayReq> feeReqList, String bizId, String pkgId, String currOpId,
			Date workDate) throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 退委托发布履约保证金(保证金解冻)
	 * 
	 * @throws BizServiceException
	 * 
	 * @throws Exception
	 */
	public BigDecimal refundAllEntrustDeposit(UnReserveReq req)
			throws BizServiceException;

	/**
	 * 签约申购资金划转
	 * 
	 * @param eventId
	 * @param payerList
	 * @param payeeList
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public String signingTransferAmt(String eventId,
			List<DedicatedTransferInfo> payerList, TransferInfo payeeInfo,
			String bizId, String pkgId, String currOpId, Date workDate)
			throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 申购费用划转
	 * 
	 * @param eventId
	 * @param payerList
	 * @param payeeInfo
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	public void subsFeeTransferAmt(String eventId,
			List<DedicatedTransferInfo> payerList, TransferInfo payeeInfo,
			String bizId, String pkgId, String currOpId, Date workDate)
			throws BizServiceException, AvlBalNotEnoughException;

}
