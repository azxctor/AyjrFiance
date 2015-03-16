package com.hengxin.platform.common.service.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.dto.AutoSubscribeParamDto;

@SuppressWarnings("unused")
public class AutoSubscribeValidator extends BaseValidator implements
		ConstraintValidator<AutoSubscribeCheck, AutoSubscribeParamDto> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AutoSubscribeValidator.class);

	private String nullMessage      					= "{member.error.autosubscribe.null}";
	private String riskMessage      					= "{member.error.autosubscribe.risk}";
	private String repayMessage     					= "{member.error.autosubscribe.repayment}";
	private String guaranteeMessage 					= "{member.error.autosubscribe.guarantee}";
	private String minBalanceMessage 					= "{member.error.autosubscribe.minBalance}";
	private String maxSubscribeMessage 					= "{member.error.autosubscribe.maxSubscribe}";

	/** 三者共存亡. **/
	private String aprMessage       					= "{member.error.autosubscribe.apr}";
	/** 数值大于0. **/
	private String aprDateMessage       				= "{member.error.autosubscribe.apr.date}";
	/** 利率大于0. **/
	private String aprDateRateMessage           		= "{member.error.autosubscribe.apr.date.rate}";
	/** 利率最小值0.1  **/
	private String aprDateRateMessageCompare    		= "{member.error.autosubscribe.apr.date.rate.compare}";
	/** 时间范围. **/	
	private String aprDateRangeMessage  				= "{member.error.autosubscribe.apr.daterange}";
	/** 数值大于0. **/
	private String aprMonthMessage       				= "{member.error.autosubscribe.apr.month}";
	/** 利率大于0. **/
	private String aprMonthRateMessage           		= "{member.error.autosubscribe.apr.month.rate}";
	/** 利率最小值0.1  **/
	private String aprMonthRateMessageCompare    		= "{member.error.autosubscribe.apr.month.rate.compare}";
	/** 时间范围. **/	
	private String aprMonthRangeMessage  				= "{member.error.autosubscribe.apr.monthrange}";
	/** 单笔申购最大金额 >= faceValue & 整数. **/
	private String maxSubscribeCompareThousandMessage 	= "{member.error.autosubscribe.maxsubscribe.thousand}";
	/** 单笔申购最大金额必须 > 资金账户保留额. **/
	private String maxSubscribeCompareMessage 			= "{member.error.autosubscribe.compare}";
	/** 账户保留额 < 0. **/
	private String balanceMessageCompareZeroMessage   	= "{member.error.autosubscribe.balance.zero}";
	/** 单笔申购最大金额 < 0. **/
	private String maxSubscribeCompareZeroMessage     	= "{member.error.autosubscribe.maxSubscribe.zero}";
	/** 以日计只能选择 到期一次还本付息. **/
	private String repayIfDateRangeMessage 				= "{member.error.autosubscribe.repayment.dateapr}";
	
	@Override
	public void initialize(AutoSubscribeCheck constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(AutoSubscribeParamDto object, ConstraintValidatorContext context) {
		//manually invoke bindNode method in super class.
		LOGGER.info("AutoSubscribeValidator isValid() invoked");
		boolean atLeast = false;
		boolean result = true;
		if (object == null) {
			return false;
		}
		BigDecimal unitFaceValue = SubscribeUtils.getUnitFaceValue();
		/** check risk. **/
		if (!object.isRiskA() && !object.isRiskB()
				&& !object.isRiskC() && !object.isRiskD()) {
			bindNode(context, "riskMessage", riskMessage);
			result = false;
		} else {
			atLeast = true;
		}
		/** check repayment. **/
		if (!object.isRepaymentA() && !object.isRepaymentB()
				&& !object.isRepaymentC() && !object.isRepaymentD()) {
			bindNode(context, "repayMessage", repayMessage);
			result = false;
		} else {
			atLeast = true;
		}
		/** check guarantee. **/
		if (!object.isGuaranteeA() && !object.isGuaranteeB()
				&& !object.isGuaranteeC()) {
			bindNode(context, "guaranteeMessage", guaranteeMessage);
			result = false;
		} else {
			atLeast = true;
		}
		/** check APR. **/
		int accumulatorD = 0;
		if (object.getMinDateRange() != 0) {
			++accumulatorD;
			atLeast = true;
		}
		if (object.getMaxDateRange() != 0) {
			++accumulatorD;
			atLeast = true;
		}
		if (object.getMinAPRForDate() != null && object.getMinAPRForDate().doubleValue() != 0) {
			++accumulatorD;
			atLeast = true;
		}
		if (accumulatorD != 0 && accumulatorD != 3) {
			if (object.getMinAPRForDate() == null || object.getMinAPRForDate().doubleValue() == 0) {
				bindNode(context, "aprMessageD1", this.aprDateRateMessage);
				result = false;
			} else {
				bindNode(context, "aprMessageD1", aprMessage);
				result = false;	
			}
		}
		boolean chooseDate = false;
		if (accumulatorD == 3) {
			if (object.getMinDateRange() <= 0 || object.getMaxDateRange() <= 0) {
				bindNode(context, "aprMessageD1", this.aprDateMessage);
				result = false;
			}
			if (object.getMinDateRange() > object.getMaxDateRange()) {
				bindNode(context, "aprMessageD1", this.aprDateRangeMessage);
				result = false;
			}
			if (object.getMinAPRForDate() == null || object.getMinAPRForDate().doubleValue() == 0) {
				bindNode(context, "aprMessageD2", this.aprDateRateMessage);
				result = false;
			} else {
				if (object.getMinAPRForDate().compareTo(new BigDecimal(0.1).setScale(0, RoundingMode.DOWN)) < 0) {
					bindNode(context, "aprMessageD2", this.aprDateRateMessageCompare);
					result = false;
				}
			}
			if (result) {
				chooseDate = true;
			}
		}
		int accumulatorM = 0;
		if (object.getMinMonthRange() != 0) {
			++accumulatorM;
			atLeast = true;
		}
		if (object.getMaxMonthRange() != 0) {
			++accumulatorM;
			atLeast = true;
		}
		if (object.getMinAPRForMonth() != null && object.getMinAPRForMonth().doubleValue() != 0) {
			++accumulatorM;
			atLeast = true;
		}
		if (accumulatorM != 0 && accumulatorM != 3) {
			if (object.getMinAPRForMonth() == null || object.getMinAPRForMonth().doubleValue() == 0) {
				bindNode(context, "aprMessageM1", this.aprMonthRateMessage);
				result = false;
			} else {
				bindNode(context, "aprMessageM1", aprMessage);
				result = false;
			}
		}
		if (accumulatorM == 3) {
			if (object.getMinMonthRange() <= 0 || object.getMaxMonthRange() <= 0) {
				bindNode(context, "aprMessageM1", this.aprMonthMessage);
				result = false;
			}
			if (object.getMinMonthRange() > object.getMaxMonthRange()) {
				bindNode(context, "aprMessageM1", this.aprMonthRangeMessage);
				result = false;
			}
			if (object.getMinAPRForMonth() == null || object.getMinAPRForMonth().doubleValue() == 0) {
				bindNode(context, "aprMessageM2", this.aprMonthRateMessage);
				result = false;
			} else {
				if (object.getMinAPRForMonth().compareTo(new BigDecimal(0.1).setScale(0, RoundingMode.DOWN)) < 0) {
					bindNode(context, "aprMessageM2", this.aprMonthRateMessageCompare);
					result = false;
				}
			}
		}
		if (chooseDate && !object.isRepaymentD()) {
			bindNode(context, "repayMessage", repayIfDateRangeMessage);
			result = false;
		}
		if (object.getMinBalance() != null) {
			atLeast = true;
			if (object.getMinBalance().compareTo(BigDecimal.ZERO) < 0) {
				bindNode(context, "minBalance", balanceMessageCompareZeroMessage);
				result = false;
			}
		}
		if (object.getMaxSubscribeAmount() != null) {
			atLeast = true;
			if (object.getMaxSubscribeAmount().compareTo(BigDecimal.ZERO) < 0) {
				bindNode(context, "maxSubscribeRate", maxSubscribeCompareZeroMessage);
				result = false;
			}
			if (object.getMaxSubscribeAmount().compareTo(unitFaceValue) < 0) {
				bindNode(context, "maxSubscribeRate", maxSubscribeCompareThousandMessage);
				result = false;
			}
			if (object.getMaxSubscribeAmount().remainder(unitFaceValue).compareTo(BigDecimal.ZERO) > 0) {
				bindNode(context, "maxSubscribeRate", maxSubscribeCompareThousandMessage);
				result = false;
			}
		}
		if (!atLeast) {
			bindNode(context, "riskMessage", nullMessage);
			result = false;
		}
		return result;
	}
    
}