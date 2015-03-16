package com.hengxin.platform.product.validator;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.market.utils.SubscribeUtils;

@Component
public class ProductAppliedQuotaValidator extends BaseValidator implements
        ConstraintValidator<ProductAppliedQuota, BigDecimal> {

    @Override
    public void initialize(ProductAppliedQuota obj) {
    }

    @Override
    public boolean isValid(BigDecimal appliedQuota, ConstraintValidatorContext context) {
    	BigDecimal unitFaceValue = SubscribeUtils.getUnitFaceValue();
        if (appliedQuota.remainder(unitFaceValue).compareTo(BigDecimal.ZERO)==0) {
            return true;
        }
        return false;
    }
}
