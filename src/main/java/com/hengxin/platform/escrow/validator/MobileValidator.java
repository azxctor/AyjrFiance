package com.hengxin.platform.escrow.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.member.service.UserMobileCheckService;

@Component
public class MobileValidator extends BaseValidator implements ConstraintValidator<MobileCheck, String> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MobileValidator.class);

	@Autowired
	private UserMobileCheckService mobileCheckService;

	@Override
	public void initialize(MobileCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(String mobile, ConstraintValidatorContext context) {
		LOGGER.info("MobileValidator  isValid() invoked");
		return mobileCheckService.isCheckMobileUnique(mobile);
	}
}
