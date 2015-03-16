package com.hengxin.platform.escrow.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 提现时校验提现金额是否超过实际可提现金额
 * 
 * @author chenwulou
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = WithdrawalAmountValidator.class)
public @interface WithdrawalAmountCheck {
	
	// 余额不足
	String message() default "{escrow.error.amount.notenough}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
