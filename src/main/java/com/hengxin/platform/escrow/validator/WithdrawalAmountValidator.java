package com.hengxin.platform.escrow.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.escrow.service.EswWithdrawalService;
import com.hengxin.platform.security.SecurityContext;

/**
 * 提现时校验提现金额是否超过实际可提现金额
 * 
 * @author chenwulou
 *
 */
@Component
public class WithdrawalAmountValidator extends BaseValidator implements ConstraintValidator<WithdrawalAmountCheck, BigDecimal>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawalAmountValidator.class);
	
	@Autowired
	private SecurityContext securityContext;
	
	@Autowired
	private EswWithdrawalService ebcWithdrawalService; 

	@Override
	public void initialize(WithdrawalAmountCheck constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(BigDecimal amount, ConstraintValidatorContext context) {
		LOGGER.info("WithdrawalAmountValidator  isValid() invoked");
		final String userId = securityContext.getCurrentUserId();
		return ebcWithdrawalService.isBalEnough(amount, userId);
	}

}
