package com.hengxin.platform.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.app.dto.downstream.AssetDto;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: AppServices
 * 
 * @author Tom
 * 
 */
@Service
@SuppressWarnings("unused")
public class AppService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AppService.class);

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private UserRepository userRepository;

	public AssetDto accountAsset() {
		AssetDto assetdto = new AssetDto();
		// 设置用户信息
		User user = securityContext.getCurrentUser();
		UserPo userPo = userRepository.findOne(user.getUserId());

		return assetdto;
	}

}
