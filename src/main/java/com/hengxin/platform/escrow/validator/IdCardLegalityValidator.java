package com.hengxin.platform.escrow.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hengxin.platform.common.service.validator.BaseValidator;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.escrow.service.EswIdNoCheckService;

public class IdCardLegalityValidator extends BaseValidator implements
		ConstraintValidator<IdCardLegalityCheck, Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdCardLegalityValidator.class);

	private String idCardNumber;
	private String name;
	private String messageTemplete;

	@Autowired
	private EswIdNoCheckService eswIdNoCheckService;

	@Override
	public void initialize(IdCardLegalityCheck check) {
		this.idCardNumber = check.idCardNumber();
		this.name = check.name();
		this.messageTemplete = check.message();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		// 校验开关
		if (!AppConfigUtil.isProdEnv()) {
			return true;
		}
		try {
			boolean isIdCardLegality = false;
			final String idCardNum = BeanUtils.getProperty(object, this.idCardNumber);
			final String name = BeanUtils.getProperty(object, this.name);
			if (StringUtils.isNotBlank(idCardNum) && StringUtils.isNotBlank(name)) {
				isIdCardLegality = eswIdNoCheckService.idCardCheck(idCardNum, name);
				if (!isIdCardLegality) {
					bindNode(context, this.idCardNumber, this.messageTemplete);
					return false;
				}
			}
		} catch (Exception e) {
			LOGGER.error("IdCardLegalityValidator throw error!", e);
		}
		return true;
	}
}
