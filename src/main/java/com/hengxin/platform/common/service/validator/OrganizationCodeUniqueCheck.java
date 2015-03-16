package com.hengxin.platform.common.service.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrganizationCodeUniqueValidator.class)
@Documented
public @interface OrganizationCodeUniqueCheck {

    String message() default "{member.error.organization.code.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    String orgId();

    String orgCode();
    
    /**
     * It must be either P or O.
     * @return
     */
    String type();
    
}
