package com.hengxin.platform.product.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.product.dto.ProductFeeDto;
import com.hengxin.platform.product.enums.EFeePayMethodType;

@Component
public class FinancingPackageFeePayMethodValidator extends BaseValidator implements
        ConstraintValidator<FinancingPackageFeePayMethodCheck, ProductFeeDto> {

    private String payMethod;
    private String messageTemplate;

    @Override
    public void initialize(FinancingPackageFeePayMethodCheck obj) {
        this.payMethod = obj.payMethod();
        this.messageTemplate = obj.message();
    }

    @Override
    public boolean isValid(ProductFeeDto productFeeDto, ConstraintValidatorContext context) {
        messageTemplate = "";
        EFeePayMethodType payMethod = productFeeDto.getPayMethod();
        if(payMethod == EFeePayMethodType.NULL){
            messageTemplate = "{package.error.feepaymethod.empty}";
        }
        if (StringUtils.isNotBlank(messageTemplate)) {
            bindNode(context, this.payMethod, this.messageTemplate);
            return false;
        }

        return true;
    }
}
