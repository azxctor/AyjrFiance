package com.hengxin.platform.escrow.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.member.service.UserMobileCheckService;

@Component
public class ExceptSelfMobileValidator extends BaseValidator implements
		ConstraintValidator<ExceptSelfMobileCheck, Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptSelfMobileValidator.class);

	private String userId;
	private String mobile;
	private String messageTemplete;

	@Autowired
	private UserMobileCheckService userMobileCheckService;

	@Override
	public void initialize(ExceptSelfMobileCheck check) {
		this.userId = check.userId();
		this.mobile = check.mobile();
		this.messageTemplete = check.message();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		try {
			boolean bool = false;
			final String mobile = BeanUtils.getProperty(object, this.mobile);
			final String userId = BeanUtils.getProperty(object, this.userId);
			if (StringUtils.isNotBlank(mobile)) {
				bool = userMobileCheckService.mobileExceptSelfCheck(mobile, userId);
				if (!bool) {
					bindNode(context, this.mobile, this.messageTemplete);
					return false;
				}
			}
		} catch (Exception e) {
			LOGGER.error("IdCardValidator throw error!", e);
		}
		return true;
	}

}
