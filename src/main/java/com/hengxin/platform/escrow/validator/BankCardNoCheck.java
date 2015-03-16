package com.hengxin.platform.escrow.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 检验银行卡号是否已被绑定
 * @author juhuahuang
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BankCardNoValidator.class)
@Documented
public @interface BankCardNoCheck {
	
	String message() default "{escrow.error.bankcardno.exist}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
