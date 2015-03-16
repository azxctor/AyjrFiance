package com.hengxin.platform.fund.service;

import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 平台手工还款服务
 * 
 * @author dcliu
 * 
 */
public interface PlatformRepayByHandService {

	/**
	 * 非最后一期还款
	 * 
	 * @param args
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public void repayOnPayDate(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 最后一期还款
	 * 
	 * @param req
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public void repayOnMaturityDate(String eventId,
			List<DedicatedTransferInfo> payerList,
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
			List<TransferInfo> payerList, List<TransferInfo> payeeList,
			String bizId, String pkgId, String seqId, String currOpId, Date workDate);

}
