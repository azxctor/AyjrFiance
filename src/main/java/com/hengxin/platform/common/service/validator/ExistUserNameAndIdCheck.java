package com.hengxin.platform.common.service.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Used for updating nickname.
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserNameAndIdValidator.class)
@Documented
public @interface ExistUserNameAndIdCheck {

    String message() default "{member.error.username.exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
