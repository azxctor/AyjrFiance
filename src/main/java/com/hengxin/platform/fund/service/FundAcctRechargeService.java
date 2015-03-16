package com.hengxin.platform.fund.service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.FundAcctRechargeReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

public interface FundAcctRechargeService {

	/**
	 * 会员充值
	 * 
	 * @param req
	 * @return
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public String rechargeAmt(FundAcctRechargeReq req)
			throws AvlBalNotEnoughException, BizServiceException;

}
