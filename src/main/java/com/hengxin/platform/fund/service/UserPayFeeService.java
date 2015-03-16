package com.hengxin.platform.fund.service;

import java.util.Date;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.UserPayFeeReq;
import com.hengxin.platform.fund.entity.FeePayStatePo;
import com.hengxin.platform.fund.enums.EFeeType;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;

/**
 * 会员缴费相关服务.
 * 
 * @author dcliu
 * 
 */
public interface UserPayFeeService {

	boolean hasPayFeeOfSeat(String userId, Date workDate);

	boolean hasPayFee(String userId, EFeeType feeType, Date workDate);

	void payFee(UserPayFeeReq req) throws BizServiceException,
			AvlBalNotEnoughException;
	
	FeePayStatePo getFeePayState(String userId, EFeeType feeType) throws BizServiceException;

}
