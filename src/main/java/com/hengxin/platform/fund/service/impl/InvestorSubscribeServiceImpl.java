package com.hengxin.platform.fund.service.impl;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.InvestorSubscribeService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.support.AcctUtils;

/**
 * Class Name: InvestorSubscribeServiceImpl
 * 
 * @author jishen
 * 
 *         投资人申购资金付款服务
 */
@Service
@Qualifier("investorSubscribeService")
public class InvestorSubscribeServiceImpl implements InvestorSubscribeService {

	@Autowired
	private FundAcctService fundAcctService;
	@Autowired
	private AcctService acctService;

	/**
	 * 申购资金保留
	 * 
	 * @param req
	 * @return fnrJnlNo
	 * @throws AvlBalNotEnoughException
	 * @throws SubAcctNotExistException
	 * @throws AcctStatusIllegalException
	 * @throws AcctNotExistException
	 * @throws AmtParamInvalidException
	 * @throws ParseException
	 * @throws Exception
	 */
	@Override
	public String subsPkgPrePayAmt(ReserveReq req) throws BizServiceException,
			AcctStatusIllegalException, AvlBalNotEnoughException {
		AcctPo acct = acctService.getAcctByUserId(req.getUserId());
		AcctUtils.checkAcctNoPayStatus(acct);
		req.setUseType(EFundUseType.SUBSCRIBE);
		return fundAcctService.reserveAmt(req);
	}

	@Override
	public String subsFeePrePayAmt(ReserveReq req) throws BizServiceException,
			AcctStatusIllegalException, AvlBalNotEnoughException {
		AcctPo acct = acctService.getAcctByUserId(req.getUserId());
		AcctUtils.checkAcctNoPayStatus(acct);
		req.setUseType(EFundUseType.SUBSCRIBEFEE);
		return fundAcctService.reserveAmt(req);
	}

}
