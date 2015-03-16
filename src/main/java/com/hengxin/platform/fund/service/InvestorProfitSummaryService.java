package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.repository.InvestorTradeSumRepository;
import com.hengxin.platform.fund.util.AmtUtils;

@Service
@Transactional(readOnly = true)
public class InvestorProfitSummaryService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InvestorProfitSummaryService.class);

	@Autowired
	private InvestorTradeSumRepository investorTradeSumRepository;

	/**
	 * 会员预期年化总收益率
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal getExpectTotalProfitRate(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		try {
			BigDecimal totalInvestmentAmt = getTotalInvestmentAmt(userId);
			if (totalInvestmentAmt.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal expectTotalReceivedProfit = getInvsExceptTotalProfitForCal(userId);
				expectTotalReceivedProfit = expectTotalReceivedProfit.multiply(BigDecimal.valueOf(100));
				value = expectTotalReceivedProfit.divide(totalInvestmentAmt, 2,
						RoundingMode.HALF_UP);
			}
		} catch (Exception ex) {
			LOGGER.error("计算预期总收益率异常", ex);
		}
		return value;
	}

	/**
	 * 会员已实现的总收益率
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal getRealizedTotalProfitRate(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		try {
			BigDecimal totalInvestmentAmt = getTotalInvestmentAmt(userId);
			if (totalInvestmentAmt.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal expectTotalReceivedProfit = getInvsRealizedRecvProfitFroCal(userId);
				expectTotalReceivedProfit = expectTotalReceivedProfit.multiply(BigDecimal.valueOf(100));
				value = expectTotalReceivedProfit.divide(totalInvestmentAmt, 2,
						RoundingMode.HALF_UP);
			}
		} catch (Exception ex) {
			LOGGER.error("计算已实现的总收益率异常", ex);
		}
		return value;
	}

	/**
	 * 会员预期总收益
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal getInvestorExpectTotalReceivedProfit(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		try {
			value = value.add(getInvestorRealizedReceivedProfit(userId));
			value = value.add(getUnReceivedProfit(userId));
		} catch (Exception ex) {
			LOGGER.error("计算预期总收益异常", ex);
		}
		return value;
	}

	/**
	 * 投资人已实现总收益
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal getInvestorRealizedReceivedProfit(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		try {
			value = value.add(getInvsReceivedProfit(userId));
			//value = value.add(getInvsCrTrsfDiffAmt(userId));
		} catch (Exception ex) {
			LOGGER.error("计算已实现总收益异常", ex);
		}
		return value;
	}

	/**
	 * 投资人未实现总收益
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal getUnReceivedProfit(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		try {
			BigDecimal deductRt = CommonBusinessUtil
					.getPaymentInterestDeductRate();
			deductRt = AmtUtils.processNegativeAmt(deductRt, BigDecimal.ONE);
			value = investorTradeSumRepository.getUnReceivedProfit(userId,
					deductRt);
			value = AmtUtils.processNullAmt(value, BigDecimal.ZERO);
		} catch (Exception ex) {
			LOGGER.error("计算未实现总收益异常", ex);
		}
		return value;
	}

	/**
	 * 投资人总投资额
	 * 
	 * @param userId
	 * @return
	 */
	private BigDecimal getTotalInvestmentAmt(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		value = investorTradeSumRepository.getTotalInvestmentAmt(userId);
		value = AmtUtils.processNullAmt(value, BigDecimal.ZERO);
		return value;
	}

	private BigDecimal getInvsReceivedProfit(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		value = investorTradeSumRepository.getInvsReceivedProfit(userId);
		value = AmtUtils.processNullAmt(value, BigDecimal.ZERO);
		return value;
	}

	@SuppressWarnings("unused")
	private BigDecimal getInvsCrTrsfDiffAmt(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		value = investorTradeSumRepository.getInvsCrTrsfDiffAmt(userId);
		value = AmtUtils.processNullAmt(value, BigDecimal.ZERO);
		return value;
	}

	private BigDecimal getInvsExceptTotalProfitForCal(String userId) {
		BigDecimal value = BigDecimal.ZERO;
		BigDecimal deductRt = CommonBusinessUtil.getPaymentInterestDeductRate();
		deductRt = AmtUtils.processNegativeAmt(deductRt, BigDecimal.ONE);
		value = investorTradeSumRepository.getExpectTotalProfitForCal(userId,
				deductRt);
		value = AmtUtils.processNullAmt(value, BigDecimal.ZERO);
		return value;
	}
	
	private BigDecimal getInvsRealizedRecvProfitFroCal(String userId){
		BigDecimal value = BigDecimal.ZERO;
		value = investorTradeSumRepository.getRealizedRecvProfitForCal(userId);
		value = AmtUtils.processNullAmt(value, BigDecimal.ZERO);
		return value;
	}
}
