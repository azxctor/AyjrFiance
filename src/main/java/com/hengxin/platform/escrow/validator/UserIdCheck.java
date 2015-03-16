package com.hengxin.platform.escrow.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * 验证改用户是否已经激活第三方支付平台账号
 * 
 * @author chenwulou
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class)
@Documented
public @interface UserIdCheck {
	// 账户已激活
	String message() default "{escrow.error.acct.exist}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}