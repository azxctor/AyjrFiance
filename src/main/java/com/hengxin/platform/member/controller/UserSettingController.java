package com.hengxin.platform.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.member.dto.UserIconDto;
import com.hengxin.platform.member.service.UserSettingService;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;

@Controller
public class UserSettingController {

	@Autowired
	private transient SecurityContext securityContext;

	@Autowired
	private transient UserSettingService userSettingService;

	@ResponseBody
	@RequestMapping(value = "user/setting", method = RequestMethod.POST)
	public ResultDto updateSetting(
			@RequestParam(value = "showBulletin", required = false) Boolean showBulletin) {
		User user = new User();
		String userId = securityContext.getCurrentUserId();
		user.setUserId(userId);
		user.setLastMntOpid(userId);
		user.setShowBulletin(showBulletin);
		userSettingService.updateUserSetting(user);
		SecurityContext.resetUser(userId);
		return new ResultDto();
	}

	/**
	 * 修改头像
	 * 
	 * @param iconFileId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "user/setting/icon/update", method = RequestMethod.POST)
	public ResultDto updateUserIcon(@RequestBody UserIconDto iconDto) {
		String userId = securityContext.getCurrentUserId();
		userSettingService.updateIconFileId(userId, iconDto.getIconFileId(), userId);
		return ResultDtoFactory
				.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
	}
}
