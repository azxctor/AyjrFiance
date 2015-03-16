package com.hengxin.platform.escrow.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.escrow.service.EswAccountService;
import com.hengxin.platform.security.SecurityContext;

/**
 * @author chenwulou
 *
 */

@Component
public class OriginalPayPwdValidator extends BaseValidator implements ConstraintValidator<OriginalPayPwdCheck, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OriginalPayPwdValidator.class);

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private EswAccountService ebcAccountService;

	@Override
	public void initialize(OriginalPayPwdCheck constraintAnnotation) {
	}

	@Override
	public boolean isValid(String payPwd, ConstraintValidatorContext context) {
		LOGGER.info("OriginalPayPwdValidator  isValid() invoked");
		final String userId = securityContext.getCurrentUserId();
		return ebcAccountService.isValidPayPwd(payPwd, userId);
	}

}
