package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.LoadnHonourAgtDepositPayReq;
import com.hengxin.platform.fund.dto.biz.req.RiskTransferFinancingAmtReq;
import com.hengxin.platform.fund.dto.biz.req.atom.InternalAcctTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.FinancierReceiveFundSerive;
import com.hengxin.platform.fund.service.atom.FundAcctService;

@Service
@Qualifier("financierReceiveFundSerive")
public class FinancierReceiveFundSeriveImpl implements
		FinancierReceiveFundSerive {

	private final static Logger LOG = LoggerFactory
			.getLogger(FinancierReceiveFundSeriveImpl.class);

	@Autowired
	private FundAcctService fundAcctService;

	@Override
	public BigDecimal riskTransferAmt(RiskTransferFinancingAmtReq req)
			throws BizServiceException, AvlBalNotEnoughException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~风控部放款~~~");
			LOG.debug("~~~会员编号:" + req.getUserId() + "~~~");
			LOG.debug("~~~冻结日志流水号:" + req.getFrzJnlNo() + "~~~");
			LOG.debug("~~~融资包编号:" + req.getBizId() + "~~~");
			LOG.debug("~~~备 注:" + req.getTrxMemo() + "~~~");
			LOG.debug("~~~当前操作用户:" + req.getCurrOpId() + "~~~");
			LOG.debug("~~~当前工作日期:" + req.getWorkDate() + "~~~");
		}

		// 解保留融资账户上的金额
		UnReserveReq unReq = new UnReserveReq();
		unReq.setCurrOpId(req.getCurrOpId());
		unReq.setReserveJnlNo(req.getFrzJnlNo());
		unReq.setTrxMemo(req.getTrxMemo());
		unReq.setUserId(req.getUserId());
		unReq.setWorkDate(req.getWorkDate());
		BigDecimal unResvAmt = fundAcctService.unReserveAmt(unReq);

		// 将解冻后的金额划转至活期账户
		InternalAcctTransferInfo reqInfo = new InternalAcctTransferInfo();
		reqInfo.setCurrOpId(req.getCurrOpId());
		reqInfo.setEventId(req.getEventId());
		reqInfo.setFromAcctType(ESubAcctType.LOAN);
		reqInfo.setBizId(req.getBizId());
		reqInfo.setToAcctType(ESubAcctType.CURRENT);
		reqInfo.setTrxAmt(unResvAmt);
		reqInfo.setTrxMemo(req.getTrxMemo());
		reqInfo.setUserId(req.getUserId());
		reqInfo.setUseType(EFundUseType.INTERNAL);
		reqInfo.setWorkDate(req.getWorkDate());
		fundAcctService.internalAcctTransferAmt(reqInfo);

		return unResvAmt;
	}

	@Override
	public String payLoadnHonourAgtDeposit(LoadnHonourAgtDepositPayReq req)
			throws BizServiceException, AvlBalNotEnoughException {
		ReserveReq resvReq = new ReserveReq();
		resvReq.setCurrOpId(req.getCurrOpId());
		resvReq.setBizId(req.getBizId());
		resvReq.setTrxAmt(req.getTrxAmt());
		resvReq.setTrxMemo(req.getTxMemo());
		resvReq.setUserId(req.getUserId());
		resvReq.setUseType(EFundUseType.LOANDEPOSIT);
		resvReq.setWorkDate(req.getWorkDate());
		return fundAcctService.specificReserveAmt(resvReq);
	}

}
