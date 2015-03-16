package com.hengxin.platform.escrow.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OriginalPayPwdValidator.class)
@Documented
public @interface OriginalPayPwdCheck {
	// 原支付密码错误，请重新输入
	String message() default "{escrow.error.paypwd.notmatch}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
