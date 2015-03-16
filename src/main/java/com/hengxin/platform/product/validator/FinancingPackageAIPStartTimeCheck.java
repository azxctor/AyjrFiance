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
 * @author yingchangwang
 *
 */
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FinancingPackageAIPStartTimeValidator.class)
@Documented
public @interface FinancingPackageAIPStartTimeCheck {
    String message() default "{package.error.aipstarttime.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String aipStartTime();
}
