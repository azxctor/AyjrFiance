package com.hengxin.platform.escrow.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 校验支付密码
 * 
 * @author chenwulou
 * 
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PayPwdValidator.class)
@Documented
public @interface PayPwdCheck {
	// 两次输入密码不一致，请重新输入
	String message() default "{member.error.password.confirm}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String payPwd();

	String confirmPayPwd();
}
