package com.hengxin.platform.escrow.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.ebc.dto.EbcMobileVerifyRequest;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.enums.EEbcSmsModel;
import com.hengxin.platform.escrow.utils.EswUtils;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

@Service
public class EswMobileService {

	@Autowired
	private EswAcctService eswAcctService;
	@Autowired
	private UserService userService;

	/**
	 * 获取验证码
	 * 
	 * @param userId
	 */
	public void getMobileVerifyCode(String userId, String newMobile) {
		EbcMobileVerifyRequest request = buildEbcMobileVerifyRequest();
		EswAcctPo eswAcctPo = eswAcctService.getEscrowAccountByUserId(userId);
		request.setUserNo(eswAcctPo.getEswUserNo());
		request.setMediumNo(eswAcctPo.getEswAcctNo());
		User user = userService.getUserByUserId(userId);
		String mobile = user.getMobile();
		Boolean changeMobile = StringUtils.isNotBlank(newMobile);
		if (changeMobile) {
			request.setMobile(newMobile);
		} else {
			request.setMobile(mobile);
		}
		request.execute();
	}

	private EbcMobileVerifyRequest buildEbcMobileVerifyRequest() {
		EbcMobileVerifyRequest request = new EbcMobileVerifyRequest();
		request.setMerchNo(EswUtils.getEswMerChNo());
		request.setOrderSn(IdUtil.produce());
		request.setOwnerId(EswUtils.getEswServProv());
		request.setSmsModel(EEbcSmsModel.M.getCode());
		return request;
	}

}
