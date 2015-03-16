package com.hengxin.platform.fund.service.atom;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.InternalAcctTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

public interface FundAcctService {

	/**
	 * 会员活期账户资金冻结
	 * 
	 * @param req
	 * @return
	 * @throws BizServiceException
	 */
	String freezeAmt(FreezeReq req) throws BizServiceException;

	/**
	 * 会员资金解冻
	 * 
	 * @param req
	 * @return
	 * @throws BizServiceException
	 */
	BigDecimal unFreezeAmt(UnFreezeReq req) throws BizServiceException;

	/**
	 * 会员综合账户只收不付冻结
	 * 
	 * @param userId
	 * @return
	 * @throws BizServiceException
	 */
	String freezeAcct(FreezeReq req, boolean isBizFreeze)
			throws BizServiceException;

	/**
	 * 会员综合账户只收不付解冻
	 * 
	 * @param userId
	 * @param freezeNo
	 * @throws BizServiceException
	 */
	BigDecimal unFreezeAcct(UnFreezeReq req, boolean isBizFreeze)
			throws BizServiceException;

	/**
	 * 获取会员综合账户冻结次数
	 * 
	 * @param userId
	 * @return
	 * @throws BizServiceException
	 */
	Long getAcctFrzCt(String userId) throws BizServiceException;

	/**
	 * 会员活期子账户资金保留
	 * 
	 * @param req
	 * @return
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	String reserveAmt(ReserveReq req) throws AcctStatusIllegalException,
			BizServiceException, AvlBalNotEnoughException;

	/**
	 * 特殊保留接口
	 * 
	 * @param req
	 * @return
	 * @throws BizServiceException
	 * @throws AvlBalNotEnoughException
	 */
	String specificReserveAmt(ReserveReq req)
			throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 会员资金解保留
	 * 
	 * @param req
	 * @throws BizServiceException
	 */
	BigDecimal unReserveAmt(UnReserveReq req) throws BizServiceException;

	/**
	 * 资金划转(对已保留或者已冻结金额进行划转操作)
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
	void transferAmtFromFnr(String eventId,
			List<DedicatedTransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId,
			String currOpId, Date workDate) throws BizServiceException,
			AvlBalNotEnoughException;

	/**
	 * 资金划转
	 * 
	 * @param eventId
	 * @param payerList
	 * @param payeeList
	 * @param useType
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	void transferAmt(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId,
			String currOpId, Date workDate) throws BizServiceException,
			AvlBalNotEnoughException;

	/**
	 * 融资资金划转(资金入收款人融资子账户并冻结)
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
	String transferAmtForFinancing(String eventId,
			List<DedicatedTransferInfo> payerList, TransferInfo payeeInfo,
			String bizId, String currOpId, Date workDate)
			throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 综合账户子账户内部转账接口
	 * 
	 * @param req
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	void internalAcctTransferAmt(InternalAcctTransferInfo req)
			throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 资金划转(可用余额忽略冻结金额，适用于违约清分还款支付、会员账户冲正等操作)
	 * 
	 * @param eventId
	 * @param payerList
	 * @param payeeList
	 * @param isAddXwbPay
	 *            余额不足的时候，是否算上小微宝的 可用余额
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	void transferAmtIgnoreFrzAmt(String eventId,
			List<TransferInfo> payerList, List<TransferInfo> payeeList,
			boolean isAddXwbPay, String bizId, String currOpId, Date workDate)
			throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 查询会员指定账户的可用余额
	 * 
	 * @param userId
	 * @param subAcctType
	 * @return
	 */
	BigDecimal getUserSubAcctAvlAmt(String userId,
			ESubAcctType subAcctType);

	/**
	 * 查询会员活期子账户与余额宝子账户的可用余额之和
	 * 
	 * @param userId
	 * @return
	 */
	BigDecimal getUserCurrentAndXwbSubAcctAvlAmt(String userId);

	/**
	 * 查询会员子账户可用余额(忽略冻结金额,适用于违约还款场景)
	 * 
	 * @param userId
	 * @param subAcctType
	 * @return
	 */
	BigDecimal getUserSubAcctAvlBalIgnoreFrzAmt(String userId,
			ESubAcctType subAcctType);

	/**
	 * 查询会员活期子账户与余额宝子账户的可用余额之和(忽略冻结金额,适用于违约还款场景)
	 * 
	 * @param userId
	 * @return
	 */
	BigDecimal getUserCurrentAndXwbSubAcctAvlAmtIgnoreFrzAmt(
			String userId);

	/**
	 * 查询综合账户可提现金额
	 * 
	 * @param acctNo
	 * @return
	 * @throws BizServiceException
	 */
	BigDecimal getSubAcctCashableAmt(String acctNo, ESubAcctNo subAcctNo)
			throws BizServiceException;

	/**
	 * 调整最大可提现金额
	 * 
	 * @param userId
	 * @param trxAmt
	 * @param currOpId
	 */
	void addMaxCashableAmt(String userId, BigDecimal trxAmt,
			String currOpId) throws AvlBalNotEnoughException,
			BizServiceException;

	/**
	 * 会员资金池变更，记录会员从资金池A划转当前总余额至资金池B
	 * 
	 * @throws BizServiceException
	 */
	void treasurerAcctTotalBal(String eventId, String acctNo, String fromPool,
			String toPool, String trxMemo, String bizId, String currOpId, Date workDate)
			throws BizServiceException;
	
	/**
	 * 会员资金子账户汇总余额
	 * @param userId
	 * @return
	 * @throws BizServiceException
	 */
	BigDecimal getUserSubAcctsSumBalAmt(String userId)throws BizServiceException;

}
