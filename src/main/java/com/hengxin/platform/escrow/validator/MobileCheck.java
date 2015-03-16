package com.hengxin.platform.escrow.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 手机号校验是否存在
 * @author chenwulou
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class)
@Documented
public @interface MobileCheck {
	// 该手机号已存在，请更换其他手机号(跟自己之前设置的手机号相同也是存在)
	String message() default "{escrow.error.mobile.exist}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
