package com.hengxin.platform.fund.service;

import java.math.BigDecimal;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

public interface WarrantDepositService {

	/**
	 * 担保方交付保证金(保证金保留)
	 * 
	 * @param req
	 * @return 保留编号
	 * @throws ServiceException
	 * @throws AvlBalNotEnoughException
	 * @throws SubAcctNotExistException
	 * @throws AcctStatusIllegalException
	 * @throws AcctNotExistException
	 * @throws AmtParamInvalidException
	 */
	public String payDeposit(ReserveReq req) throws BizServiceException,
			AvlBalNotEnoughException;
	
	/**
	 * 担保方交付保证金(保证金保留)
	 * 
	 * @param req
	 * @return 保留编号
	 * @throws ServiceException
	 * @throws AvlBalNotEnoughException
	 * @throws SubAcctNotExistException
	 * @throws AcctStatusIllegalException
	 * @throws AcctNotExistException
	 * @throws AmtParamInvalidException
	 */
	public String payDepositAgain(ReserveReq req) throws BizServiceException,
			AvlBalNotEnoughException;

	/**
	 * 担保方保证金全额退还
	 * 
	 * @param req
	 * @throws BizServiceException
	 */
	public BigDecimal refundWarrantDeposit(UnReserveReq req)
			throws BizServiceException;

}
