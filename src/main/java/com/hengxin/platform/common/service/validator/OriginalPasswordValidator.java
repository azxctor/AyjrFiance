package com.hengxin.platform.common.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.service.UserService;

public class OriginalPasswordValidator extends BaseValidator implements ConstraintValidator<OriginalPasswordCheck, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OriginalPasswordValidator.class);
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private SecurityContext securityContext;

    @Override
    public void initialize(OriginalPasswordCheck check) {
    }

    /**
     * TODO encrypt password in the future.
     * 
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
    	LOGGER.info("OriginalPasswordValidator  isValid() invoked");
    	final String userId = securityContext.getCurrentUserId();
    	return userService.isValidPassward(password, userId);
    }
    
}
