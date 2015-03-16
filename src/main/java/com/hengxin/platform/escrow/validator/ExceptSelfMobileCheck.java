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
@Constraint(validatedBy = ExceptSelfMobileValidator.class)
@Documented
public @interface ExceptSelfMobileCheck {
	
	String message() default "{escrow.error.mobile.exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String mobile();

    String userId();
}
