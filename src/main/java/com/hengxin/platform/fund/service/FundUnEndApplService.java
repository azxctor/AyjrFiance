package com.hengxin.platform.fund.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.repository.WithdrawDepositApplRepository;

@Service
public class FundUnEndApplService {
	
	@Autowired
	private WithdrawDepositApplRepository withdrawDepositApplRepository;
	
	/**
	 * 判断是否存在未结束的申请，存在，则抛异常
	 * @param userId
	 * @throws BizServiceException
	 */
	public void checkUnEndAppl(String userId)throws BizServiceException {
		
		Integer count = Integer.valueOf(0);
		
		List<Object[]> countList = withdrawDepositApplRepository.countUnEndAppls(userId);
		
		Object[] counts = countList.get(0);
		
		count = Integer.valueOf(String.valueOf(counts[0]));
		if(count.intValue()>0){
			throw new BizServiceException(EErrorCode.EXISTS_UN_END_RECHARGE_APPL);
		}
		count = Integer.valueOf(String.valueOf(counts[1]));
		if(count.intValue()>0){
			throw new BizServiceException(EErrorCode.EXISTS_UN_END_WDRW_APPL);
		}
		count = Integer.valueOf(String.valueOf(counts[2]));
		if(count.intValue()>0){
			throw new BizServiceException(EErrorCode.EXISTS_UN_END_TRSF_APPL);
		}
		count = Integer.valueOf(String.valueOf(counts[3]));
		if(count.intValue()>0){
			throw new BizServiceException(EErrorCode.EXISTS_UN_END_RVS_APPL);
		}
		
	}
	
}
