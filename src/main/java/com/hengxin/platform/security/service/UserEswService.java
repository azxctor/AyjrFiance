package com.hengxin.platform.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;
import com.hengxin.platform.security.enums.EswAcctStatusEnum;

@Service
public class UserEswService {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 修改用户开户处理状态
	 * @param userId
	 * @param eswAcctStatus
	 * @return
	 */
	public UserPo updateUserEswAcctStatus(String userId, EswAcctStatusEnum eswAcctStatus){
		UserPo userPo = userRepository.findUserByUserId(userId);
		userPo.setEswAcctStatus(eswAcctStatus);
		return userRepository.save(userPo);
	}
	
	/**
	 * 修改用户绑卡处理状态
	 * @param userId
	 * @param bindingCardStatus
	 * @return
	 */
	public UserPo updateUserBindingCardStatus(String userId, BindingCardStatusEnum bindingCardStatus){
		UserPo userPo = userRepository.findUserByUserId(userId);
		userPo.setBindingCardStatus(bindingCardStatus);
		return userRepository.save(userPo);
	}

}
