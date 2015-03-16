package com.hengxin.platform.member.service;

import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;

@Service
public class UserMobileCheckService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserMobileCheckService.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SecurityContext securityContext;

	/**
	 * 校验除了自己以外是否有相同的手机号
	 * 
	 * @param mobile
	 * @param userId
	 * @return
	 */
	public boolean mobileExceptSelfCheck(String mobile, String userId) {
		LOGGER.debug(userId, mobile);
		List<UserPo> users = userRepository.findUserByMobile(mobile);
		int size = users.size();
		if (size == 0) {
			// 当表中没有该手机号记录，返回true
			return true;
		}
		if (StringUtils.isBlank(userId)) {
			userId = securityContext.getCurrentUserId();
		}
		// 当size>1肯定有重复，直接false,当size==1,只有当userId和mobile对应时才返回true,即该手机号是自己原先设置的
		return size == 1 && StringUtils.equals(users.get(0).getUserId(), userId);
	}

	/**
	 * 手机号码唯一性校验
	 * 
	 * @param mobile
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isCheckMobileUnique(String mobile) {
		LOGGER.debug(mobile);
		List<UserPo> users = userRepository.findUserByMobile(mobile);
		return users.size() == 0;
	}

}
