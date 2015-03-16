package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.dto.biz.req.Invs2LoanPledgeTransferAmtReq;
import com.hengxin.platform.fund.dto.biz.req.LoadnHonourAgtDepositPayReq;
import com.hengxin.platform.fund.dto.biz.req.RiskTransferFinancingAmtReq;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.InternalAcctTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.FreezeReserveDtlPo;
import com.hengxin.platform.fund.entity.Invs2LoanCashDtlPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFnrOperType;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.FreezeReserveDtlRepository;
import com.hengxin.platform.fund.repository.Invs2LoanCashDtlRepository;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.service.Invs2LoanFundService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;

@Service
@Qualifier("invs2LoanFundService")
public class Invs2LoanFundServiceImpl implements Invs2LoanFundService {

	private final static Logger LOG = LoggerFactory
			.getLogger(Invs2LoanFundServiceImpl.class);

	@Autowired
	private FreezeReserveDtlRepository freezeReserveDtlRepository;

	@Autowired
	private Invs2LoanCashDtlRepository invs2LoanCashDtlRepository;

	@Autowired
	private SubAcctRepository subAcctRepository;

	@Autowired
	private AcctRepository acctRepository;

	@Autowired
	private FundAcctService fundAcctService;

	@Override
	public BigDecimal invs2LoanRiskTransferAmt(RiskTransferFinancingAmtReq req)
			throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~投转贷业务风控部放款~~~");
			LOG.debug("~~~风控部将投转贷融得的资金直接在融资账户上解冻，并返回解冻的资金~~~");
		}

		// 解冻融资账户上的金额
		UnFreezeReq unReq = new UnFreezeReq();
		unReq.setCurrOpId(req.getCurrOpId());
		unReq.setFreezeJnlNo(req.getFrzJnlNo());
		unReq.setTrxMemo(req.getTrxMemo());
		unReq.setUserId(req.getUserId());
		unReq.setWorkDate(req.getWorkDate());
		BigDecimal unfreezeAmt = fundAcctService.unFreezeAmt(unReq);

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~投转贷业务风控部放款~~~放款金额为： " + unfreezeAmt.doubleValue());
		}

		return unfreezeAmt;
	}

	private AcctPo getUserAcctPoByUserId(String userId)
			throws AcctNotExistException {
		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = acctRepository.findByUserId(userId);
		if (acctPo == null) {
			throw new AcctNotExistException("会员综合账户不存在");
		}
		return acctPo;
	}

	@Override
	public String pledgeAmtAndTransferAmt(Invs2LoanPledgeTransferAmtReq req)
			throws BizServiceException, AvlBalNotEnoughException {

		// 事件编号
		String eventId = req.getEventId();

		// 此次投转贷放款对应的冻结明细编号
		String frzJnlNo = req.getFrzJnlNo();

		// 会员编号
		String userId = req.getUserId();

		// 资金用途
		EFundUseType useType = req.getUseType();

		// 本次投转贷贷款金额
		BigDecimal loanAmt = AmtUtils.processNegativeAmt(req.getLoanAmt(),
				BigDecimal.ZERO);

		// 本次投转贷最大提现金额
		BigDecimal maxCashAmt = AmtUtils.processNegativeAmt(
				req.getMaxCashableAmt(), BigDecimal.ZERO);

		// 做投转贷持有有效债权总和
		BigDecimal ownAmt = AmtUtils.processNegativeAmt(req.getOwnAmt(),
				BigDecimal.ZERO);

		String trxMemo = req.getTrxMemo();

		String bizId = req.getBizId();

		String currOpId = req.getCurrOpId();

		Date workDate = req.getWorkDate();

		Date currDate = new Date();

		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = this.getUserAcctPoByUserId(userId);
		String acctNo = acctPo.getAcctNo();

		// 查询放款对应的冻结明细
		FreezeReserveDtlPo fnrPo = freezeReserveDtlRepository
				.findFreezeReserveDtlByJnlNo(frzJnlNo);

		// 风控放款金额
		BigDecimal riskTransferAmt = AmtUtils.processNegativeAmt(
				fnrPo.getTrxAmt(), BigDecimal.ZERO);

		// 校验投转贷相关金额是否合法
		this.checkInvs2LoanAmt(riskTransferAmt, loanAmt, maxCashAmt, ownAmt);

		// 查询质押账户上是否存在质押金额，如果无，则表示此次投转贷为首次，否则，则为非首次
		SubAcctPo pledgeSubAcct = getSubAcctByAcctNoAndSubAcctNo(acctNo,
				ESubAcctNo.PLEDGE.getCode());

		// 质押账户上原有的冻结金额
		BigDecimal oldPledgeAmt = AmtUtils.processNegativeAmt(
				pledgeSubAcct.getFreezableAmt(), BigDecimal.ZERO);
		BigDecimal oldDebtAmt = AmtUtils.processNegativeAmt(
				pledgeSubAcct.getDebtAmt(), BigDecimal.ZERO);

		BigDecimal toCurrentAmt = BigDecimal.ZERO;
		BigDecimal toPledgeAmt = BigDecimal.ZERO;

		if (oldPledgeAmt.compareTo(BigDecimal.ZERO) <= 0) {
			// 质押账户不存在投转贷情况， 负债金额应该为 0.00
			if (oldDebtAmt.compareTo(BigDecimal.ZERO) != 0) {
				throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
						"会员" + userId + " 的质押账户数据有误，冻结金额为0.00，负债金额也应该为0.00");
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("质押账户不存在投转贷情况");
				LOG.debug("实际可提现金额全部转入活期账户");
				LOG.debug("贷款金额与to活期账户金额的差额转入质押账户");
			}

		} else {
			// 质押账户存在投转贷的情况
			if (LOG.isDebugEnabled()) {
				LOG.debug("质押账户存在投转贷情况");
				LOG.debug("to 活期金额 =  ");
				LOG.debug("贷款金额与to活期账户金额的差额转入质押账户");
			}

		}

		// 计算划转至活期账户金额
		toCurrentAmt = this.calToCurrentAmt(maxCashAmt, oldDebtAmt);
		if (toCurrentAmt.compareTo(BigDecimal.ZERO) > 0) {
			this.internalTransferAmt(eventId, userId, ESubAcctType.LOAN,
					ESubAcctType.CURRENT, toCurrentAmt, useType, trxMemo,
					bizId, currOpId, workDate);
		}

		// 计算划转至质押账户金额
		toPledgeAmt = AmtUtils.processNegativeAmt(loanAmt.subtract(toCurrentAmt),
				BigDecimal.ZERO);
		if (toPledgeAmt.compareTo(BigDecimal.ZERO) > 0) {
			this.internalTransferAmt(eventId, userId, ESubAcctType.LOAN,
					ESubAcctType.PLEDGE, toPledgeAmt, useType, trxMemo, bizId,
					currOpId, workDate);
		}

		// 质押账户上冻结贷款金额
		String freezeNo = this.freezeAmtOnPledgeAcct(acctNo, useType, loanAmt,
				toCurrentAmt, trxMemo, bizId, currOpId, workDate, currDate);

		// 记录一笔投转贷提现明细
		this.createInvs2LoanCashDtl(freezeNo, acctNo, ownAmt, loanAmt,
				maxCashAmt, toCurrentAmt, currOpId, workDate, currDate);

		return freezeNo;
	}

	/**
	 * 计算划至活期账户的金额
	 * 
	 * @param maxCashableAmt
	 * @param realCashAmt
	 * @param debtAmt
	 * @return
	 */
	private BigDecimal calToCurrentAmt(BigDecimal maxCashableAmt,
			BigDecimal debtAmt) {

		BigDecimal toCurrAmt = BigDecimal.ZERO;

		if (debtAmt.compareTo(maxCashableAmt) < 0) {
			toCurrAmt = AmtUtils.max(maxCashableAmt.subtract(debtAmt),
					BigDecimal.ZERO);
		}

		return toCurrAmt;
	}

	private void internalTransferAmt(String eventId, String userId,
			ESubAcctType fromAcctType, ESubAcctType toAcctType,
			BigDecimal trxAmt, EFundUseType useType, String trxMemo,
			String bizId, String currOpId, Date workDate)
			throws BizServiceException, AvlBalNotEnoughException {
		// 将解冻后的金额划转至活期账户
		InternalAcctTransferInfo reqInfo = new InternalAcctTransferInfo();
		reqInfo.setCurrOpId(currOpId);
		reqInfo.setEventId(eventId);
		reqInfo.setFromAcctType(fromAcctType);
		reqInfo.setBizId(bizId);
		reqInfo.setToAcctType(toAcctType);
		reqInfo.setTrxAmt(trxAmt);
		reqInfo.setTrxMemo(trxMemo);
		reqInfo.setUserId(userId);
		reqInfo.setUseType(useType);
		reqInfo.setWorkDate(workDate);
		fundAcctService.internalAcctTransferAmt(reqInfo);
	}

	private void checkInvs2LoanAmt(BigDecimal riskTransferAmt,
			BigDecimal loanAmt, BigDecimal maxCashAmt, BigDecimal ownAmt)
			throws BizServiceException {

		if (loanAmt.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
					"贷款金额必须大于0.00");
		}

		if (maxCashAmt.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
					"最大可提现金额必须大于0.00");
		}

		if (ownAmt.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
					"拥有有效债权金额必须大于0.00");
		}

		if (riskTransferAmt.compareTo(loanAmt) < 0) {
			throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
					"风控放款金额必须大于等于贷款金额");
		}

	}

	private SubAcctPo getSubAcctByAcctNoAndSubAcctNo(String acctNo,
			String subAcctNo) throws BizServiceException {

		SubAcctPo subAcctPo = subAcctRepository
				.findSubAcctByAcctNoAndSubAcctNo(acctNo, subAcctNo);
		if (subAcctPo == null) {
			ESubAcctNo text = EnumHelper.translate(ESubAcctNo.class, subAcctNo);
			throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
					text.getText() + "子账户不存在");
		}

		return subAcctPo;
	}

	private String freezeAmtOnPledgeAcct(String acctNo, EFundUseType useType,
			BigDecimal pledgeAmt, BigDecimal toCurrentAmt, String trxMemo,
			String bizId, String currOpId, Date workDate, Date currDate)
			throws BizServiceException {

		BigDecimal frzAmt = AmtUtils.processNegativeAmt(pledgeAmt,
				BigDecimal.ZERO);
		BigDecimal toCurrAmt = AmtUtils.processNegativeAmt(toCurrentAmt,
				BigDecimal.ZERO);

		SubAcctPo subAcctPo = getSubAcctByAcctNoAndSubAcctNo(acctNo,
				ESubAcctNo.PLEDGE.getCode());

		// 计算应冻结金额
		BigDecimal oldfreezeAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getFreezableAmt(), BigDecimal.ZERO);
		BigDecimal oldDebtAmt = AmtUtils.processNegativeAmt(
				subAcctPo.getDebtAmt(), BigDecimal.ZERO);

		// 更新金额
		BigDecimal newFreezeAmt = oldfreezeAmt.add(frzAmt);
		BigDecimal newDebtAmt = oldDebtAmt.add(toCurrAmt);

		// 更新子账户相关金额
		SubAcctPo newSubAcctPo = ConverterService.convert(subAcctPo,
				SubAcctPo.class);
		newSubAcctPo.setFreezableAmt(newFreezeAmt);
		newSubAcctPo.setDebtAmt(newDebtAmt);
		newSubAcctPo.setLastMntOpid(currOpId);
		newSubAcctPo.setLastMntTs(currDate);
		subAcctRepository.save(newSubAcctPo);

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~冻结金额~~~" + frzAmt.doubleValue());
			LOG.debug("~~~原应冻结金额~~~" + oldfreezeAmt.doubleValue());
			LOG.debug("~~~现应冻结金额~~~" + newFreezeAmt.doubleValue());
		}

		FreezeReserveDtlPo acFnrDtlPo = new FreezeReserveDtlPo();
		acFnrDtlPo.setAcctNo(subAcctPo.getAcctNo());
		acFnrDtlPo.setSubAcctNo(ESubAcctNo.PLEDGE.getCode());
		acFnrDtlPo.setUseType(useType);
		acFnrDtlPo.setOperType(EFnrOperType.FREEZE);
		acFnrDtlPo.setStatus(EFnrStatus.ACTIVE);
		acFnrDtlPo.setEffectDt(workDate);
		acFnrDtlPo
				.setExpireDt(DateUtils.getDate(
						DictConsts.DEFAULT_MAX_DATE_VALUE,
						DictConsts.WORK_DATE_FORMAT));
		acFnrDtlPo.setTrxAmt(frzAmt);
		acFnrDtlPo.setTrxMemo(trxMemo);
		acFnrDtlPo.setRelBizId(bizId);
		acFnrDtlPo.setCreateOpid(currOpId);
		acFnrDtlPo.setCreateTs(currDate);
		FreezeReserveDtlPo retObj = freezeReserveDtlRepository.save(acFnrDtlPo);
		// 流水号在保存的时候自动生成返回
		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			LOG.debug("~~~新增保证金冻结明细~~");
			LOG.debug("~~~更新冻结金额为~~" + frzAmt.doubleValue());
			LOG.debug("~~~返回的冻结明细流水号为~~" + retObj.getJnlNo());
		}

		return retObj.getJnlNo();
	}

	@Override
	public void payInterest2Investors(String eventId,
			List<DedicatedTransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String currOpId,
			Date workDate) throws BizServiceException, AvlBalNotEnoughException {
		fundAcctService.transferAmtFromFnr(eventId, payerList, payeeList, false,
				bizId, currOpId, workDate);
	}

	private void createInvs2LoanCashDtl(String frzJnlNo, String acctNo,
			BigDecimal ownAmt, BigDecimal loanAmt, BigDecimal maxCashAmt,
			BigDecimal realCashAmt, String currOpId, Date workDate,
			Date currDate) {

		Invs2LoanCashDtlPo dtlReq = new Invs2LoanCashDtlPo();
		dtlReq.setJnlNo(frzJnlNo);
		dtlReq.setAcctNo(acctNo);
		dtlReq.setRepayFlg(EFlagType.NO.getCode());
		dtlReq.setTrxDt(workDate);
		dtlReq.setRepayDt(DateUtils.getDate(DictConsts.DEFAULT_MAX_DATE_VALUE,
				DictConsts.WORK_DATE_FORMAT));
		dtlReq.setLoanAmt(loanAmt);
		dtlReq.setMaxCashAmt(maxCashAmt);
		dtlReq.setOwnAmt(ownAmt);
		dtlReq.setRealCashAmt(realCashAmt);
		dtlReq.setCreateOpId(currOpId);
		dtlReq.setCreateTs(currDate);
		invs2LoanCashDtlRepository.save(dtlReq);
	}

	@Override
	public String payLoadnHonourAgtDeposit(String eventId,
			LoadnHonourAgtDepositPayReq req) throws BizServiceException,
			AvlBalNotEnoughException {
		String userId = req.getUserId();
		String currOpId = req.getCurrOpId();
		String bizId = req.getBizId();
		String trxMemo = req.getTxMemo();
		Date workDate = req.getWorkDate();
		EFundUseType useType = EFundUseType.LOANDEPOSIT;
		BigDecimal trxAmt = AmtUtils.processNegativeAmt(req.getTrxAmt(),
				BigDecimal.ZERO);

		// 将要保留的借款履约保证金 划转至 活期在账户上
		this.internalTransferAmt(eventId, userId, ESubAcctType.LOAN,
				ESubAcctType.CURRENT, trxAmt, useType, trxMemo, bizId,
				currOpId, workDate);

		// 在活期子账户上将该笔划转金额进行 保留操作
		ReserveReq resvReq = new ReserveReq();
		resvReq.setCurrOpId(currOpId);
		resvReq.setBizId(bizId);
		resvReq.setTrxAmt(trxAmt);
		resvReq.setTrxMemo(trxMemo);
		resvReq.setUserId(userId);
		resvReq.setUseType(useType);
		resvReq.setWorkDate(workDate);
		return fundAcctService.specificReserveAmt(resvReq);
	}

	@Override
	public String invs2LoanPrePayInterest(ReserveReq req)
			throws AcctStatusIllegalException, BizServiceException,
			AvlBalNotEnoughException {
		return fundAcctService.reserveAmt(req);
	}

	@Override
	public void refundPrePayInterest(UnReserveReq req)
			throws BizServiceException {
		fundAcctService.unReserveAmt(req);
	}

}
