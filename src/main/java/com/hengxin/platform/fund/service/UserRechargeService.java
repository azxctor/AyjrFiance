package com.hengxin.platform.fund.service;

import com.hengxin.platform.account.dto.UserRechargeApplDto;
import com.hengxin.platform.account.dto.downstream.RechargeSearchDto;
import com.hengxin.platform.account.dto.downstream.UnsignedUserInfoSearchDto;
import com.hengxin.platform.account.dto.upstream.UnsignedUserInfoDto;
import com.hengxin.platform.account.dto.upstream.UserRechargeTrxJnlsDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.UserRechargeReq;
import com.hengxin.platform.fund.dto.biz.rsp.FundTransferRsp;
import com.hengxin.platform.fund.enums.EFlagType;

/**
 * 会员充值服务 Class Name: FundRechargeService.
 * 
 * @author tingwang
 * 
 */
public interface UserRechargeService {

	/**
	 * 处理签约会员在银行发起的充值请求 
	 * 
	 * @param req
	 * @throws ServiceException
	 */
	FundTransferRsp processSignedUserOnBankRecharge(UserRechargeReq req)
			throws BizServiceException;

	/**
	 * 处理签约会员在平台发起的充值请求 
	 * 
	 * @param req
	 * @throws ServiceException
	 */
	void doSignedUserOnPlatRecharge(UserRechargeReq req)
			throws BizServiceException;

	/**
	 * 处理非签约会员在银行发起的充值请求
	 * 
	 * @param req
	 * @throws ServiceException
	 */
	void doUnSignedUserOnBankRechargeAppl(UserRechargeReq req)
			throws BizServiceException;
	
	/**
	 * 非签约会员充值审批通过充值
	 * @param appId
	 * @param currentUser
	 * @param currentDate
	 * @throws BizServiceException
	 */
	void doUnSignUserRechargeApplApprove(String applId, boolean passed,
			String comments, String opratorId) throws BizServiceException;

	/**
	 * 获取非签约用户充值记录 
	 * 
	 * @param userId
	 * @return
	 */
	DataTablesResponseDto<UserRechargeTrxJnlsDto> getUserRechargeTrxJnlList(
			final RechargeSearchDto searchDto, final String userId, final EFlagType signedFlag);
	
	/**
	 * 查询
	 * @param searchDto
	 * @param userId
	 * @return
	 */
	DataTablesResponseDto<UserRechargeApplDto> getUserRechargeApplList(
			final RechargeSearchDto searchDto, final String userId);

	/**
	 * 根据平台账号获取用户绑定银行卡开户名及渠道信息
	 * @param searchDto
	 * @return
	 */
	UnsignedUserInfoDto queryUnsignedUserInfo(
			UnsignedUserInfoSearchDto searchDto);

}
