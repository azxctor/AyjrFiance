package com.hengxin.platform.escrow.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.escrow.service.EswAccountService;

/**
 * 校验账户是否已经激活
 * 
 * @author chenwulou
 *
 */
@Component
public class UserIdValidator extends BaseValidator implements ConstraintValidator<UserIdCheck, String>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserIdValidator.class);

	@Autowired
	private EswAccountService ebcAccountService;
	
	@Override
	public void initialize(UserIdCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(String userId, ConstraintValidatorContext context) {
		LOGGER.info("MobileValidator  isValid() invoked");
		return ebcAccountService.isExistingUser(userId);
	}

}
