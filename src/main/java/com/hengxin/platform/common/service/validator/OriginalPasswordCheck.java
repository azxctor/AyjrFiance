package com.hengxin.platform.common.service.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OriginalPasswordValidator.class)
@Documented
public @interface OriginalPasswordCheck {

    String message() default "{member.error.password.notmatch}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
