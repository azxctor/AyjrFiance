package com.hengxin.platform.member.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.entity.UserPo;

@Service
public class UserSettingService {

	@Autowired
	private transient UserRepository userRepository;

	@Transactional
	public User updateUserSetting(User user) {
		UserPo po = userRepository.findOne(user.getUserId());
		if (user.getShowBulletin() != null) {
			po.setShowBulletin(user.getShowBulletin());
		}
		if (user.getLastMntOpid() != null) {
			po.setLastMntOpid(user.getLastMntOpid());
		}
		po.setLastMntTs(new Date());
		po = userRepository.save(po);
		return ConverterService.convert(po, User.class);
	}

	/**
	 * 修改用户头像
	 * 
	 * @param userId
	 * @param userIconFileId
	 * @param currOpId
	 */
	@Transactional
	public UserPo updateIconFileId(String userId, String userIconFileId,
			String currOpId) {
		UserPo user = userRepository.findOne(userId);
		user.setIconFileId(userIconFileId);
		user.setLastMntOpid(currOpId);
		user.setLastMntTs(new Date());
		userRepository.save(user);
		return user;
	}

}
