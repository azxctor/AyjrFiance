package com.hengxin.platform.escrow.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hengxin.platform.common.service.validator.BaseValidator;

/**
 * 校验支付密码
 * 
 * @author chenwulou
 * 
 */

@Component
public class PayPwdValidator extends BaseValidator implements ConstraintValidator<PayPwdCheck, Object> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PayPwdValidator.class);
	
	private String payPwd;
	private String confirmPayPwd;
	private String messageTemplete;

	@Override
	public void initialize(PayPwdCheck check) {
		this.payPwd = check.payPwd();
		this.confirmPayPwd = check.confirmPayPwd();
		this.messageTemplete = check.message();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		LOGGER.info("PayPwdValidator  isValid() invoked");
		try {
			final String payPwd = BeanUtils.getProperty(object, this.payPwd);
			final String confirmPayPwd = BeanUtils.getProperty(object, this.confirmPayPwd);
			// should input same
			if (StringUtils.equals(payPwd, confirmPayPwd)) {
				return true;
			} else {
				bindNode(context, this.confirmPayPwd, this.messageTemplete);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
