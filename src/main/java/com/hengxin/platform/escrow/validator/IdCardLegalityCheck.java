package com.hengxin.platform.escrow.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdCardLegalityValidator.class)
@Documented
public @interface IdCardLegalityCheck {
	// 身份证号不合法，请输入正确的身份证
	String message() default "{escrow.error.idcard.legality}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String idCardNumber();
	
	String name();
}
