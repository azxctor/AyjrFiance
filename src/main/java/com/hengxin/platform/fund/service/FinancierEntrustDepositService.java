package com.hengxin.platform.fund.service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 融资人委托保证金接口服务
 * 
 * @author dcliu
 * 
 */
public interface FinancierEntrustDepositService {

	/**
	 * 交委托发布履约保证金(保证金冻结)
	 * 
	 * @return 保证金冻结编号
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public String payDeposit(ReserveReq req) throws BizServiceException,
			AcctStatusIllegalException, AvlBalNotEnoughException;
	
	
	/**
	 * 交委托发布履约保证金(保证金冻结)
	 * 
	 * @return 保证金冻结编号
	 * @throws AvlBalNotEnoughException
	 * @throws BizServiceException
	 */
	public String payDepositAgain(ReserveReq req) throws BizServiceException,
			AcctStatusIllegalException, AvlBalNotEnoughException;

}
