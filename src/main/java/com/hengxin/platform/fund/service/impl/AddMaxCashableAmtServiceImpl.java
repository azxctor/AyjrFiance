package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.AddMaxCashableAmtService;
import com.hengxin.platform.fund.service.atom.FundAcctService;

@Service
@Qualifier("addMaxCashableAmtService")
public class AddMaxCashableAmtServiceImpl implements AddMaxCashableAmtService {

	@Autowired
	private FundAcctService fundAcctService;

	@Override
	public void addMaxCashableAmt(String userId, BigDecimal trxAmt,
			String currOpId) throws AvlBalNotEnoughException,
			BizServiceException {
		fundAcctService.addMaxCashableAmt(userId, trxAmt, currOpId);
	}

}
