package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 提前还款
 * 
 * @author dcliu
 * 
 */
public interface PrepaymentService {
	
	/**
	 * 保证金解保留
	 * @param req
	 * @return
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	public BigDecimal unReserveMargin(UnReserveReq req) throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 提前还款操作
	 * 
	 * @param req
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public void prepayment(String eventId,
			List<TransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException;
	
	/**
	 * 投资人付利息、罚金收益部分金额给平台
	 * @param eventId
	 * @param payerList
	 * @param exchangePayee
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 */
	public void payInvsIntrProfitToExchange(String eventId, 
			List<TransferInfo> payerList, List<TransferInfo> payeeList,	String bizId, 
			String pkgId, String seqId,	String currOpId, Date workDate)throws BizServiceException;
	
	
	public void refundOncePaidFee(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId, 
			String pkgId, String seqId, String currOpId, Date workDate)throws BizServiceException;

}
