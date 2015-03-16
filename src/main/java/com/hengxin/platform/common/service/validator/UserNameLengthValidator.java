package com.hengxin.platform.common.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNameLengthValidator extends BaseValidator implements
		ConstraintValidator<UserNameLengthCheck, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoSubscribeValidator.class);
	
	@Override
	public void initialize(UserNameLengthCheck constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		LOGGER.info("UserNameLengthValidator isValid() invoked");
		char[] ch = value.toCharArray();
		int length = 0;
	    for (int i = 0; i < ch.length; i++) {
	        char c = ch[i];
	        if (isChinese(c)) {
	        	length = length + 2;
			} else {
				length = length + 1;
			}
	    }
		if (length >= 4 && length <= 15) {
			return true;
		}
		LOGGER.error("{} length is  {}", value, length);
		return false;
	}

	private boolean isChinese(char c) {
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
	        return true;
	    }
	    return false;
	}
}
