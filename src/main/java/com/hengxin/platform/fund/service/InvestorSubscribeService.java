package com.hengxin.platform.fund.service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 投资人申购资金付款服务
 * 
 * @author dcliu
 * 
 */
public interface InvestorSubscribeService {

	/**
	 * 投资人申购产品预付款(申购产品时，资金预支付，资金做保留)
	 * 
	 * @param args
	 * @return 资金保留编号
	 * @throws AvlBalNotEnoughException
	 * @throws AcctStatusIllegalException
	 * @throws BizServiceException
	 */
	public String subsPkgPrePayAmt(ReserveReq req) throws BizServiceException,
			AcctStatusIllegalException, AvlBalNotEnoughException;
	
	/**
	 * 投资人申购费用预付款保留
	 * @param req
	 * @return
	 * @throws BizServiceException
	 * @throws AcctStatusIllegalException
	 * @throws AvlBalNotEnoughException
	 */
	public String subsFeePrePayAmt(ReserveReq req) throws BizServiceException,
		AcctStatusIllegalException, AvlBalNotEnoughException;

}
