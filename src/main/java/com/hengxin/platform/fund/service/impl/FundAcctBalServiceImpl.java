package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.atom.PositionService;
import com.hengxin.platform.fund.service.support.AcctUtils;
import com.hengxin.platform.fund.util.AmtUtils;

/**
 * 
 * @author dcliu 资金账户的服务方法,资金财务模块内容实用的方法
 */
@Service
@Qualifier("fundAcctBalService")
public class FundAcctBalServiceImpl implements FundAcctBalService {

	private final static Logger LOG = LoggerFactory
			.getLogger(FundAcctBalServiceImpl.class);
	
	@Autowired
	private AcctService acctService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private FundAcctService fundAcctService;
	@Autowired
	private SubAcctRepository subAcctRepository;

	@Override
	public BigDecimal getUserCurrentAcctAvlBal(String userId)throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~判断可用余额~~~会员编号为:" + userId);
		}

		BigDecimal avlAmt = fundAcctService.getUserSubAcctAvlAmt(userId,
				ESubAcctType.CURRENT);

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~可用余额为:" + avlAmt.doubleValue());
		}

		return avlAmt;
	}

	@Override
	public BigDecimal getUserCashableAmt(String userId)throws BizServiceException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~获取会员可提现金额~~~会员编号为:" + userId);
		}

		// 通过 会员编号获取 会员综合账户
		AcctPo acctPo = acctService.getAcctByUserId(userId);

		// 检查是否只收不付状态
		boolean canNotPay = AcctUtils.isCanNotPayStatus(acctPo);

		// 可提现金额
		BigDecimal cashAbleAmt = fundAcctService
				.getSubAcctCashableAmt(acctPo.getAcctNo(), ESubAcctNo.CURRENT);
		
		if(canNotPay){
			cashAbleAmt = BigDecimal.ZERO;
		}else{
			// 综合账户上保留资产
			BigDecimal resvAsset = AmtUtils.processNegativeAmt(acctPo.getReservedAsset(), BigDecimal.ZERO);
			
			if(resvAsset.compareTo(BigDecimal.ZERO)>0){
				
				BigDecimal totalBal = subAcctRepository.getAcctTotalBalByAcctNo(acctPo.getAcctNo());
				
				BigDecimal crAmt = positionService.getUserReceivableTotalPrincipal(userId);
				
				BigDecimal totalAssets = totalBal.add(crAmt);
				
				BigDecimal minAmt = AmtUtils.min(totalAssets.subtract(resvAsset), cashAbleAmt);
				
				cashAbleAmt = AmtUtils.max(minAmt, BigDecimal.ZERO);
			}
		}
		

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~获取会员可提现金额~~~:" + cashAbleAmt.doubleValue());
		}

		return cashAbleAmt;
	}

	@Override
	public BigDecimal getUserCurrentAcctAvlBal(String userId, boolean isAddXwb)
			throws BizServiceException {
		BigDecimal avlAmt = BigDecimal.ZERO;
		if (isAddXwb) {
			avlAmt = fundAcctService.getUserCurrentAndXwbSubAcctAvlAmt(userId);
		} else {
			avlAmt = fundAcctService.getUserSubAcctAvlAmt(userId,
					ESubAcctType.CURRENT);
		}
		return avlAmt;
	}

	@Override
	public BigDecimal getUserSubAcctsSumBalAmt(String userId)
			throws BizServiceException {
		return fundAcctService.getUserSubAcctsSumBalAmt(userId);
	}

}
