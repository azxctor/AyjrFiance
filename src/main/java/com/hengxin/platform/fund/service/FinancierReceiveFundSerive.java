package com.hengxin.platform.fund.service;

import java.math.BigDecimal;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.LoadnHonourAgtDepositPayReq;
import com.hengxin.platform.fund.dto.biz.req.RiskTransferFinancingAmtReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 融资人接收融资款
 * 
 * @author dcliu
 * 
 */
public interface FinancierReceiveFundSerive {

	/**
	 * 风控部对融资人放款
	 * 
	 * @param args
	 * @return 放款金额
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public BigDecimal riskTransferAmt(RiskTransferFinancingAmtReq req)
			throws BizServiceException, AvlBalNotEnoughException;

	/**
	 * 融资人交借款履约保证金(保证金冻结，此处保证金只能用冻结) 先做放款，再做保证金冻结
	 * 
	 * @param args
	 * @return 保证金冻结编号
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public String payLoadnHonourAgtDeposit(LoadnHonourAgtDepositPayReq req)
			throws BizServiceException, AvlBalNotEnoughException;

}
