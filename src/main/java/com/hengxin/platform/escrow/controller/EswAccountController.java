package com.hengxin.platform.escrow.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.escrow.dto.EswMobileDto;
import com.hengxin.platform.escrow.dto.EswMobileDto.UpdateMobile;
import com.hengxin.platform.escrow.dto.EswPayPwdDto;
import com.hengxin.platform.escrow.dto.EswPayPwdDto.UpdatePayPwd;
import com.hengxin.platform.escrow.dto.EswSignupDto;
import com.hengxin.platform.escrow.dto.EswSignupDto.SubmitSignup;
import com.hengxin.platform.escrow.service.EswAccountService;
import com.hengxin.platform.escrow.service.EswMobileService;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.service.UserService;

/**
 * 第三方托管账户操作跳转控制
 * 
 * @author chenwulou
 * 
 */
@Controller
public class EswAccountController extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(EswAccountController.class);

	@Autowired
	private transient SecurityContext securityContext;

	@Autowired
	private EswAccountService eswAccountService;
	
	@Autowired
	private EswMobileService eswMobileService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AcctService acctService;
	
	@Autowired
	private MemberMessageService memberMessageService;

	/**
	 * 获取短信验证码
	 * 
	 * @param request
	 * @param model
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "esw/account/sendmessage", method = RequestMethod.GET)
	public ResultDto sendMessage(HttpServletRequest request, Model model, String mobile){
		LOGGER.debug("发送验证码...");
		eswMobileService.getMobileVerifyCode(securityContext.getCurrentUserId(), mobile);
		return ResultDtoFactory.toAck("发送成功");
	}

	/**
	 * 激活账户
	 * @param acct
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = "esw/account/signup", method = RequestMethod.POST)
	public ResultDto signup(HttpServletRequest request, Model model, @OnValid @RequestBody EswSignupDto eswSignupDto) {
		LOGGER.debug("signup() start: ");
		validate(eswSignupDto, new Class[] { SubmitSignup.class });
		String userId = securityContext.getCurrentUserId();
		eswSignupDto.setUserId(userId);
		if(acctService.getAcctByUserId(userId) == null){
			return ResultDtoFactory.toNack("无此账户");
		}
		eswAccountService.signup(eswSignupDto);
		return ResultDtoFactory.toAck("账户激活成功");
	}

	/**
	 * 修改账户信息view
	 * @param request
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = "esw/account/editeview", method = RequestMethod.GET)
	public String renderEditAccount(HttpServletRequest request, Model model, String userId) {
		String isSignup = userService.getUserByUserId(userId).getEswAcctStatus().getCode();
		model.addAttribute("userId", userId);
		model.addAttribute("isSignup", isSignup);
 		return "escrow/update_password";
	}
	
	/**
	 * 
	 * 修改支付密码
	 * @param request
	 * @param model
	 * @param eswPayPwdDto
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = "esw/account/updatepaypwd", method = RequestMethod.POST)
	public ResultDto updatePayPwd(HttpServletRequest request, Model model, @OnValid @RequestBody EswPayPwdDto eswPayPwdDto){
		validate(eswPayPwdDto, new Class[] { UpdatePayPwd.class });
		String userId = securityContext.getCurrentUserId();
		eswPayPwdDto.setUserId(userId);
		eswAccountService.updatePayPwd(eswPayPwdDto);
		return ResultDtoFactory.toAck("支付密码修改成功");
	}
	
	/**
	 * 重置支付密码
	 * 
	 * @param request
	 * @param model
	 * @param eswPayPwdDto
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = "esw/account/resetpaypwd", method = RequestMethod.POST)
	public ResultDto resetPayPwd(HttpServletRequest request, Model model, @RequestBody EswPayPwdDto eswPayPwdDto){
		String userId = securityContext.getCurrentUserId();
		eswPayPwdDto.setUserId(userId);
		String newPayPwd = eswAccountService.resetPayPwd(eswPayPwdDto);
		try {
			String messageKey = "escrow.resetpaypwd.message";
			memberMessageService.sendMessage(EMessageType.SMS, messageKey,
					userId, newPayPwd);
		} catch (Exception e) {
			LOGGER.error("Ex {}", e);
		}
		return ResultDtoFactory.toAck("支付密码重置成功");
	}

	/**
	 * 
	 * 修改手机号
	 * 
	 * @param request
	 * @param model
	 * @param eswMobileDto
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = "esw/account/updatemobile", method = RequestMethod.POST)
	public ResultDto updateMobile(HttpServletRequest request, Model model, @OnValid @RequestBody EswMobileDto  eswMobileDto){
		validate(eswMobileDto, new Class[] { UpdateMobile.class });
		String userId = securityContext.getCurrentUserId();
		eswMobileDto.setUserId(userId);
		eswAccountService.updateMobile(eswMobileDto);
		return ResultDtoFactory.toAck("手机号修改成功");
	}

}