package com.hengxin.platform.fund.service.impl;

import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.FundAcctReverseService;
import com.hengxin.platform.fund.service.atom.FundAcctService;

@Service
@Qualifier("fundAcctReverseService")
public class FundAcctReverseServiceImpl implements FundAcctReverseService {

	private final static Logger LOG = LoggerFactory
			.getLogger(FundAcctReverseServiceImpl.class);

	@Autowired
	private FundAcctService fundAcctService;

	@Override
	public void reverseRechargeAmt(String eventId, TransferInfo payerInfo,
			String bizId, String currOpId, Date workDate)
			throws BizServiceException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("会员账户充值金额冲正交易.....");
			LOG.debug("冲正会员为--->" + payerInfo.getUserId());
			LOG.debug("冲正金额为--->" + payerInfo.getTrxAmt());
		}

		payerInfo.setRelZQ(false);
		payerInfo.setUseType(EFundUseType.RECHARGE_REVERSE);
		fundAcctService.transferAmtIgnoreFrzAmt(eventId,
				Arrays.asList(payerInfo), null, false, bizId, currOpId,
				workDate);
	}

	@Override
	public void reverseWithdrawAmt(String eventId, TransferInfo payeeInfo,
			String bizId, String currOpId, Date workDate)
			throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("会员账户提现金额冲正交易.....");
			LOG.debug("冲正会员为--->" + payeeInfo.getUserId());
			LOG.debug("冲正金额为--->" + payeeInfo.getTrxAmt());
		}

		payeeInfo.setRelZQ(false);
		payeeInfo.setUseType(EFundUseType.CASH_REVERSE);
		fundAcctService.transferAmtIgnoreFrzAmt(eventId, null,
				Arrays.asList(payeeInfo), false, bizId, currOpId, workDate);
	}

}
