package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.WarrantDepositService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.util.DateUtils;

@Service
@Qualifier("warrantDepositService")
public class WarrantDepositServiceImpl implements WarrantDepositService {

	private final static Logger LOG = LoggerFactory
			.getLogger(WarrantDepositServiceImpl.class);

	@Autowired
	private FundAcctService fundAcctService;

	@Override
	public String payDeposit(ReserveReq req) throws BizServiceException,
			AvlBalNotEnoughException {
		String userId = req.getUserId();
		String bizId = req.getBizId();
		String bizMemo = req.getTrxMemo();
		Date workDate = req.getWorkDate();
		String workDateStr = DateUtils.formatDate(workDate,
				DictConsts.WORK_DATE_FORMAT);
		String currOpId = req.getCurrOpId();
		BigDecimal trxAmt = req.getTrxAmt();

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~担保方保证金保留~~~");
			LOG.debug("~~~会员编号~~~" + userId);
			LOG.debug("~~~业务流水编号~~~" + bizId);
			LOG.debug("~~~当前工作日期~~~" + workDateStr);
			LOG.debug("~~~当前操作员编号~~~" + currOpId);
			LOG.debug("~~~待交保证金金额~~~" + trxAmt.doubleValue());
			LOG.debug("~~~备注信息~~~" + bizMemo);
		}
		req.setUseType(EFundUseType.WARRANTDEPOSIT);
		return fundAcctService.reserveAmt(req);
	}

	@Override
	public BigDecimal refundWarrantDeposit(UnReserveReq req)
			throws BizServiceException {
		return fundAcctService.unReserveAmt(req);
	}

	@Override
	public String payDepositAgain(ReserveReq req) throws BizServiceException,
			AvlBalNotEnoughException {
		String userId = req.getUserId();
		String bizId = req.getBizId();
		String bizMemo = req.getTrxMemo();
		Date workDate = req.getWorkDate();
		String workDateStr = DateUtils.formatDate(workDate,
				DictConsts.WORK_DATE_FORMAT);
		String currOpId = req.getCurrOpId();
		BigDecimal trxAmt = req.getTrxAmt();

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~担保方保证金保留~~~");
			LOG.debug("~~~会员编号~~~" + userId);
			LOG.debug("~~~业务流水编号~~~" + bizId);
			LOG.debug("~~~当前工作日期~~~" + workDateStr);
			LOG.debug("~~~当前操作员编号~~~" + currOpId);
			LOG.debug("~~~待交保证金金额~~~" + trxAmt.doubleValue());
			LOG.debug("~~~备注信息~~~" + bizMemo);
		}
		req.setUseType(EFundUseType.WARRANTDEPOSIT);
		return fundAcctService.specificReserveAmt(req);
	}

}
