package com.hengxin.platform.common.dto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hengxin.platform.common.enums.EOptionCategory;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessName {

    String value() default "";

    EOptionCategory optionCategory() default EOptionCategory.NULL;

    boolean ignore() default false;
}
