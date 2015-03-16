package com.hengxin.platform.escrow.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.escrow.service.EswAccountService;

@Component
public class BankCardNoValidator extends BaseValidator implements ConstraintValidator<BankCardNoCheck, String> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BankCardNoValidator.class);

	@Autowired
	private EswAccountService ebcAccountService;

	@Override
	public void initialize(BankCardNoCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(String bankCardNo, ConstraintValidatorContext context) {
		LOGGER.info("BankCardNoValidator  isValid() invoked");
		return ebcAccountService.isExistingBankCardNo(bankCardNo);
	}
}
