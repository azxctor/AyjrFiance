package com.hengxin.platform.escrow.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 提现时校验支付密码
 * @author chenwulou
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PayPwdForWithdrawalValidator.class)
@Documented
public @interface PayPwdForWithdrawalCheck {
	// 支付密码错误，请重新输入
	String message() default "{escrow.error.paypwd.error}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
