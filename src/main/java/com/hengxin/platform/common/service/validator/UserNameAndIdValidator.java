package com.hengxin.platform.common.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.service.UserService;

/**
 * Used for updating nickname.
 *
 */
public class UserNameAndIdValidator extends BaseValidator implements ConstraintValidator<ExistUserNameAndIdCheck, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserNameAndIdValidator.class);
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private transient SecurityContext securityContext;
    
	@Override
	public void initialize(ExistUserNameAndIdCheck constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		LOGGER.info("isValid() invoked");
		String userId = securityContext.getCurrentUserId();
		return !userService.isExistingUser(userId, value);	
	}

}
