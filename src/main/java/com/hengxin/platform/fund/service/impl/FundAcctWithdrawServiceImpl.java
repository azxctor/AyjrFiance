package com.hengxin.platform.fund.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.dto.biz.req.FundAcctWithDrawReq;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankTrxJnlPo;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.BankTrxJnlRepository;
import com.hengxin.platform.fund.service.FundAcctWithdrawService;
import com.hengxin.platform.fund.service.atom.FundAcctService;

@Service
@Qualifier("fundAcctWithdrawService")
public class FundAcctWithdrawServiceImpl implements FundAcctWithdrawService {

	private final static Logger LOG = LoggerFactory
			.getLogger(FundAcctWithdrawServiceImpl.class);

	@Autowired
	private AcctRepository acctRepository;

	@Autowired
	private BankTrxJnlRepository bankTrxJnlRepository;

	@Autowired
	private FundAcctService fundAcctService;

	@Override
	public String reserveWithdrawAmt(ReserveReq req)
			throws BizServiceException, AcctStatusIllegalException,
			AvlBalNotEnoughException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~提现金额保留~~~");
			LOG.debug("~~~会员编号:" + req.getUserId() + "~~~");
			LOG.debug("~~~保留金额:" + req.getTrxAmt() + "~~~");
			LOG.debug("~~~备 注:" + req.getTrxMemo() + "~~~");
			LOG.debug("~~~当前操作会员:" + req.getCurrOpId() + "~~~");
			LOG.debug("~~~当前工作日期:" + req.getWorkDate() + "~~~");
			LOG.debug("~~~提现申请编号:" + req.getBizId() + "~~~");
		}
		req.setUseType(EFundUseType.CASH);
		return fundAcctService.reserveAmt(req);
	}

	@Override
	public String signedAcctWithdrawAmt(FundAcctWithDrawReq req)
			throws BizServiceException, AvlBalNotEnoughException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~资金账户提现扣款~~~");
			LOG.debug("~~~会员编号:" + req.getUserId() + "~~~");
			LOG.debug("~~~会员名:" + req.getUserName() + "~~~");
			LOG.debug("~~~扣款金额:" + req.getTrxAmt() + "~~~");
			LOG.debug("~~~备 注:" + req.getTrxMemo() + "~~~");
			LOG.debug("~~~保留日志流水:" + req.getBizId() + "~~~");
			LOG.debug("~~~当前操作用户:" + req.getCurrOpid() + "~~~");
			LOG.debug("~~~当前工作日期:" + req.getWorkDate() + "~~~");
			LOG.debug("~~~是否签约会员:" + req.getSignedFlg() + "~~~");
			LOG.debug("~~~银行交易流水:" + req.getRelBnkId() + "~~~");
		}

		// 通过会员编号获取综合账户
		AcctPo acctPo = acctRepository.findByUserId(req.getUserId());

		String eventId = req.getEventId();
		String pool = req.getCashPool();
		if(StringUtils.isBlank(pool)){
			pool = acctPo.getCashPool();
		}
		ECashPool cashPool = EnumHelper.translate(ECashPool.class, pool);
		List<TransferInfo> payerList = new ArrayList<TransferInfo>();
		TransferInfo withDrawInfo = new TransferInfo();
		withDrawInfo.setRelZQ(false);
		withDrawInfo.setTrxAmt(req.getTrxAmt());
		withDrawInfo.setTrxMemo(req.getTrxMemo());
		withDrawInfo.setUserId(req.getUserId());
		withDrawInfo.setCashPool(cashPool);
		withDrawInfo.setUseType(EFundUseType.CASH);
		payerList.add(withDrawInfo);

		// 添加会员提现日志
		BankTrxJnlPo trxJnlPo = new BankTrxJnlPo();
		trxJnlPo.setBankCode(EnumHelper.translate(EBankType.class, req.getBankCode()));
		trxJnlPo.setCashPool(cashPool);
		trxJnlPo.setUserId(req.getUserId());
		trxJnlPo.setUserName(req.getUserName());
		trxJnlPo.setAcctNo(acctPo.getAcctNo());
		trxJnlPo.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
		trxJnlPo.setRechargeWithdrawFlag(ERechargeWithdrawFlag.WITHDRAW);
		trxJnlPo.setSignedFlg(EnumHelper.translate(EFlagType.class, req.getSignedFlg()));
		trxJnlPo.setTrxDt(req.getWorkDate());
		trxJnlPo.setTrxAmt(req.getTrxAmt());
		trxJnlPo.setTrxMemo(req.getTrxMemo());
		trxJnlPo.setRelBnkId(req.getRelBnkId());
		trxJnlPo.setCreateOpid(req.getCurrOpid());
		trxJnlPo.setCreateTs(new Date());
		trxJnlPo.setEventId(eventId);
		trxJnlPo.setTrxStatus(EBnkTrxStatus.NORMAL);
		trxJnlPo.setRvsFlg(EFlagType.NO);
		trxJnlPo.setBnkAcctNo(req.getBnkAcctNo());
		trxJnlPo.setBnkAcctName(req.getBnkAcctName());
		BankTrxJnlPo retAcRechargeTrxJnlPo = bankTrxJnlRepository
				.save(trxJnlPo);
		String jnlNo = retAcRechargeTrxJnlPo.getJnlNo();

		fundAcctService.transferAmt(eventId, payerList, null, false, jnlNo,
				req.getCurrOpid(), req.getWorkDate());

		return jnlNo;
	}

	@Override
	public String unReserveWithdrawAmt(UnReserveReq req)
			throws BizServiceException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~提现金额解保留~~~");
			LOG.debug("~~~会员编号:" + req.getUserId() + "~~~");
			LOG.debug("~~~备 注:" + req.getTrxMemo() + "~~~");
			LOG.debug("~~~当前工作日期:" + req.getWorkDate() + "~~~");
			LOG.debug("~~~当前操作用户:" + req.getCurrOpId() + "~~~");
			LOG.debug("~~~保留日志流水:" + req.getReserveJnlNo() + "~~~");
		}
		fundAcctService.unReserveAmt(req);
		return req.getReserveJnlNo();
	}

	@Override
	public String unSignedAcctWithdrawAmt(FundAcctWithDrawReq req)
			throws BizServiceException, AvlBalNotEnoughException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~资金账户提现扣款~~~");
			LOG.debug("~~~会员编号:" + req.getUserId() + "~~~");
			LOG.debug("~~~会员名:" + req.getUserName() + "~~~");
			LOG.debug("~~~扣款金额:" + req.getTrxAmt() + "~~~");
			LOG.debug("~~~备 注:" + req.getTrxMemo() + "~~~");
			LOG.debug("~~~保留日志流水:" + req.getBizId() + "~~~");
			LOG.debug("~~~当前操作用户:" + req.getCurrOpid() + "~~~");
			LOG.debug("~~~当前工作日期:" + req.getWorkDate() + "~~~");
			LOG.debug("~~~是否签约会员:" + req.getSignedFlg() + "~~~");
			LOG.debug("~~~银行交易流水:" + req.getRelBnkId() + "~~~");
		}

		// 通过会员编号获取综合账户
		AcctPo acctPo = acctRepository.findByUserId(req.getUserId());

		String eventId = req.getEventId();
		String pool = req.getCashPool();
		if(StringUtils.isBlank(pool)){
			pool = acctPo.getCashPool();
		}
		ECashPool cashPool = EnumHelper.translate(ECashPool.class, pool);
		List<DedicatedTransferInfo> payerList = new ArrayList<DedicatedTransferInfo>();
		DedicatedTransferInfo withDrawInfo = new DedicatedTransferInfo();
		withDrawInfo.setRelZQ(false);
		withDrawInfo.setTrxAmt(req.getTrxAmt());
		withDrawInfo.setTrxMemo(req.getTrxMemo());
		withDrawInfo.setUserId(req.getUserId());
		withDrawInfo.setCashPool(cashPool);
		withDrawInfo.setFnrJnlNo(req.getResvJnlNo());
		withDrawInfo.setUseType(EFundUseType.CASH);
		payerList.add(withDrawInfo);

		// 添加会员提现日志
		BankTrxJnlPo trxJnlPo = new BankTrxJnlPo();
		trxJnlPo.setUserId(req.getUserId());
		trxJnlPo.setUserName(req.getUserName());
		trxJnlPo.setAcctNo(acctPo.getAcctNo());
		trxJnlPo.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
		trxJnlPo.setRechargeWithdrawFlag(ERechargeWithdrawFlag.WITHDRAW);
		trxJnlPo.setSignedFlg(EnumHelper.translate(EFlagType.class, req.getSignedFlg()));
		trxJnlPo.setTrxDt(req.getWorkDate());
		trxJnlPo.setTrxAmt(req.getTrxAmt());
		trxJnlPo.setTrxMemo(req.getTrxMemo());
		trxJnlPo.setRelBnkId(req.getRelBnkId());
		trxJnlPo.setCreateOpid(req.getCurrOpid());
		trxJnlPo.setCreateTs(new Date());
		trxJnlPo.setEventId(eventId);
		trxJnlPo.setTrxStatus(EBnkTrxStatus.NORMAL);
		trxJnlPo.setRvsFlg(EFlagType.NO);
		trxJnlPo.setCashPool(cashPool);
		trxJnlPo.setBnkAcctNo(req.getBnkAcctNo());
		trxJnlPo.setBnkAcctName(req.getBnkAcctName());
		trxJnlPo.setBankCode(EnumHelper.translate(EBankType.class, req.getBankCode()));
		BankTrxJnlPo retAcRechargeTrxJnlPo = bankTrxJnlRepository
				.save(trxJnlPo);
		String jnlNo = retAcRechargeTrxJnlPo.getJnlNo();

		fundAcctService.transferAmtFromFnr(eventId, payerList, null, false, jnlNo,
				req.getCurrOpid(), req.getWorkDate());

		return jnlNo;
	}

}
