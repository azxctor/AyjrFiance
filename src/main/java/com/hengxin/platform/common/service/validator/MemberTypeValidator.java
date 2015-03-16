package com.hengxin.platform.common.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.hengxin.platform.member.enums.EMemberType;

public class MemberTypeValidator extends BaseValidator implements ConstraintValidator<MemberTypeNullCheck, EMemberType> {

    @Override
    public void initialize(MemberTypeNullCheck check) {
    }

    @Override
    public boolean isValid(EMemberType value, ConstraintValidatorContext context) {
        if (EMemberType.NULL == value) {
            return false;
        }
        return true;
    }
    
}
