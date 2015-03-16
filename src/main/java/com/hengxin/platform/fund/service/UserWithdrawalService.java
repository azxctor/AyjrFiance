package com.hengxin.platform.fund.service;

import java.util.Date;
import java.util.List;

import com.hengxin.platform.account.dto.WithdrawalApplSumAmtDto;
import com.hengxin.platform.account.dto.downstream.WithdConfirmDto;
import com.hengxin.platform.account.dto.downstream.WithdDepApplListSearchDto;
import com.hengxin.platform.account.dto.upstream.WithdrawalApplDetailDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.UserWithdrawalReq;
import com.hengxin.platform.fund.dto.biz.rsp.FundTransferRsp;
import com.hengxin.platform.fund.entity.WithdrawDepositApplPo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;

/**
 * 会员提现服务 Class Name: FundWithdrawalService.
 * 
 * @author tingwang
 * 
 */
public interface UserWithdrawalService {

	/**
	 * 处理签约会员在平台发起的提现请求 Description: TODO
	 * 
	 * @param req
	 * @throws ServiceException
	 */
	void doSignUserWithdrawalOnPlat(UserWithdrawalReq req)
			throws BizServiceException;

	/**
	 * 处理非签约会员在平台发起的提现请求 Description: TODO
	 * 
	 * @param req
	 * @throws ServiceException
	 */
	void doUnSignUserWithdrawalOnPlat(UserWithdrawalReq req)
			throws BizServiceException;

	/**
	 * 处理非签约用户提现审批通过后的提现操作 Description: TODO
	 * 
	 * @param appId
	 * @param currentUser
	 * @param currentDate
	 * @throws ServiceException
	 */
	void doUnSignUserWithdrawalAfterApprove(String appId,
			String currentUser, Date currentDate) throws BizServiceException;

	/**
	 * 处理签约用户在银行的提现操作 Description: TODO
	 * 
	 * @param appl
	 * @throws ServiceException
	 */
	FundTransferRsp processSignUserWithdrawalOnBank(UserWithdrawalReq req)
			throws BizServiceException;

	/**
	 * 获取提现申请表列表 Description: TODO
	 * 
	 * @param searchDto
	 * @return
	 * @throws ServiceException
	 */
	DataTablesResponseDto<WithdrawalApplDetailDto> getWithdrawDepositApplList(
			final String userId, final WithdDepApplListSearchDto searchDto,
			boolean approve, boolean confirm) throws BizServiceException;

	/**
	 * 提现申请表审批 Description: TODO
	 * 
	 * @param applId
	 * @param passed
	 * @param comments
	 * @param opratorId
	 * @param workDate
	 * @throws ServiceException
	 */
	void approvalWithdrawDepositAppl(String applId, boolean passed,
			String comments, String opratorId, Date workDate)
			throws BizServiceException;

	/**
	 * 根据申请表NO获取申请表 Description: TODO
	 * 
	 * @param applNo
	 * @return
	 */
	WithdrawDepositApplPo getWithdrawDepositApplByApplNo(String applNo);
	
	/**
	 * 根据日期和渠道获取对应的提现申请
	 * @param searchDate
	 * @param bnkCd
	 * @return
	 */
	List<WithdrawDepositApplPo> getWithdrawDepositApplByCashPoolAndStatus(
			EFundApplStatus applStatus, EFundDealStatus dealStatus, ECashPool cashPool);
	
	
	/**
	 * 处理非签约用户批量提现审批操作 Description: TODO
	 * 
	 * @param appId
	 * @param currentUser
	 * @param currentDate
	 * @throws ServiceException
	 */
	void doBatchUnSignUserWithdrawalApprove(List<WithdrawDepositApplPo> list,
			String currentUser, Date currentDate) throws BizServiceException;
	
	
	/**
	 * 处理非签约用户批量提现确认操作 Description: TODO
	 * 
	 * @param appId
	 * @param currentUser
	 * @param currentDate
	 * @throws ServiceException
	 */
	void doBatchUnSignUserWithdrawalConfirm(List<WithdrawDepositApplPo> list,
			String currentUser, Date currentDate) throws BizServiceException;
	
	/**
	 *  获取提现申请汇总金额
	 * @param userId
	 * @param searchDto
	 * @param approve
	 * @param confirm
	 * @return
	 * @throws BizServiceException
	 */
	WithdrawalApplSumAmtDto getWithdrawApplSumAmtDto(
			final String userId, final WithdDepApplListSearchDto searchDto,
			boolean approve, final boolean confirm) throws BizServiceException;

    
    /**
    * Description: TODO
    *
    * @param dto
    * @param userId
    * @param workDate
    */
    	
    void doUnSignUserWithdrawalReject(WithdConfirmDto dto, String userId, Date workDate);

	List<WithdrawalApplDetailDto> getWithdrawDepositApplExList(String userId,
			WithdDepApplListSearchDto searchDto, boolean approve,
			boolean confirm) throws BizServiceException; 

}
