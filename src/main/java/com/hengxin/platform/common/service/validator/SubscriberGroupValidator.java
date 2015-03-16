package com.hengxin.platform.common.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hengxin.platform.member.dto.SubscribeGroupDto;
import com.hengxin.platform.member.service.SubscribeGroupService;

public class SubscriberGroupValidator extends BaseValidator implements
		ConstraintValidator<SubscriberGroupCheck, SubscribeGroupDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoSubscribeValidator.class);

	private String groupIdMessage      = "{member.error.subscribergroup.groupId}";
	private String groupIdDupMessage   = "{member.error.subscribergroup.groupId.dup}";
	private String usersMessage        = "{member.error.subscribergroup.users}";
	
	@Autowired
	private SubscribeGroupService subscribeGroupService;
    
	@Override
	public void initialize(SubscriberGroupCheck constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(SubscribeGroupDto object, ConstraintValidatorContext context) {
		LOGGER.info("SubscriberGroupValidator isValid() invoked");
		boolean result = true;
		if (object.getGroupName() == null || object.getGroupName().trim().isEmpty()) {
			bindNode(context, "groupName", groupIdMessage);
			result = false;
		} else {
			if (object.getGroupName().trim().length() > 100) {
				bindNode(context, "groupName", groupIdMessage);
				result = false;	
			}
		}
		if (object.getUsers() == null || object.getUsers().isEmpty()) {
			if (object.getServiceCenterIds().isEmpty()) {
				bindNode(context, "renderUser", usersMessage);
				result = false;
			}
		}
		if (result) {
			boolean exist = subscribeGroupService.isExistingGroup(object.getGroupId(), object.getGroupName());
			LOGGER.info("exist {}", exist);
			bindNode(context, "groupName", groupIdDupMessage);
			return !exist;
		}
		return result;
	}
}
