/**
 *
 */
package com.hengxin.platform.product.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Ryan
 *
 */
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductAppliedQuotaValidator.class)
@Documented
public @interface ProductAppliedQuota {
    String message() default "{product.error.appliedQuota.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
