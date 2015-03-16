package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

public interface WarrantCmpnsAmtService {

	/**
	 * 担保方代偿，支付代偿款
	 * @param eventId
	 * @param warrantPayerList
	 * @param fncrPayeeList
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	public void payCmpnsAmtOnFinalPayment(String eventId, DedicatedTransferInfo warrantPayer,
			TransferInfo fncrPayee, String bizId, String pkgId, String seqId, String currOpId,
			Date workDate)throws BizServiceException, AvlBalNotEnoughException;
	
	/**
	 * 担保方代偿，支付代偿款
	 * @param eventId
	 * @param warrantPayerList
	 * @param fncrPayeeList
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	public void payCmpnsAmtOnPayment(String eventId, TransferInfo warrantPayer,
			TransferInfo fncrPayee, String bizId, String pkgId, String seqId, String currOpId,
			Date workDate)throws BizServiceException, AvlBalNotEnoughException;
	
	/**
	 * 融资人偿还担保方支付的代偿款
	 * @param eventId
	 * @param fncrPayerList
	 * @param warrantPayeeList
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	public void repaymentCmpnsAmt(String eventId, TransferInfo fncrPayer,
			TransferInfo warrantPayee, String bizId,  String pkgId, String seqId, 
			String currOpId, Date workDate)throws BizServiceException, AvlBalNotEnoughException;
	
	/**
	 * 会员账户可用余额查询
	 * @param userId
	 * @param isAddXwb
	 * @return
	 * @throws BizServiceException
	 */
	public BigDecimal getUserCurrentAcctAvlBal(String userId, boolean isAddXwb)throws BizServiceException;
	
}
