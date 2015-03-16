package com.hengxin.platform.fund.service;

import com.hengxin.platform.fund.dto.biz.req.FundAcctCreateReq;

public interface FundAcctManageService {

	/**
	 * 创建会员综合账户
	 * 
	 * @param req
	 * @return
	 * @throws ServiceException
	 */
	public String createUserAcct(FundAcctCreateReq req);

	/**
	 * 创建综合交易所账户
	 * 
	 * @param req
	 * @return
	 * @throws ServiceException
	 */
	public String createExchangeAcct(FundAcctCreateReq req);

	/**
	 * 通过usrId判断账户是否存在
	 * 
	 * @param userId
	 * @return false 不存在；true存在
	 * @throws ServiceException
	 */
	public boolean existsAcct(String userId);

}
