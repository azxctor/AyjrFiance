package com.hengxin.platform.common.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberService;

public class OrganizationCodeUniqueValidator extends BaseValidator implements
		ConstraintValidator<OrganizationCodeUniqueCheck, Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationCodeUniqueValidator.class);
	
    private String orgId;
    private String orgCode;
    private String type;
    private String messageTemplete;
    
	@Autowired
	private MemberService memberService;
	
	@Override
	public void initialize(OrganizationCodeUniqueCheck constraintAnnotation) {
		this.orgId = constraintAnnotation.orgId();
        this.orgCode = constraintAnnotation.orgCode();
        this.type = constraintAnnotation.type();
        this.messageTemplete = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		try {
			final String id = BeanUtils.getProperty(object, this.orgId);
	        final String code = BeanUtils.getProperty(object, this.orgCode);
	        if (code == null || code.isEmpty()) {
	        	bindNode(context, this.orgCode, "{member.error.organization.code.null}");
	        	return false;
			}
	        boolean exist;
	        if (this.type.equalsIgnoreCase(EUserType.PERSON.getCode())) {
	        	exist = memberService.isExistingOrgCodeForIndividual(id, code);
	        	if (!exist) {
	        		exist = memberService.isExistingOrgCodeForOrganization(id, code);
				}
			} else if (this.type.equalsIgnoreCase(EUserType.ORGANIZATION.getCode())) {
				exist = memberService.isExistingOrgCodeForOrganization(id, code);
				if (!exist) {
					exist = memberService.isExistingOrgCodeForIndividual(id, code);	
				}
			} else {
				throw new RuntimeException("Null type for Anno OrganizationCodeUniqueCheck");
			}
	        LOGGER.info("exist {}", exist);
			if (exist) {
				bindNode(context, this.orgCode, this.messageTemplete);	
			}
	        return !exist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
