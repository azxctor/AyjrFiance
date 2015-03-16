/*
 * Project Name: kmfex-platform
 * File Name: MemberDetailController.java
 * Class Name: MemberDetailController
 *
 * Copyright 2014 KMFEX Inc
 *
 *
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.member.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EFormMode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.member.converter.ApplicationConverter;
import com.hengxin.platform.member.converter.MemberInfoConverter;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.dto.MemberDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.OrganizationDto.ContactIdcardExist;
import com.hengxin.platform.member.dto.OrganizationDto.ContactMobileExist;
import com.hengxin.platform.member.dto.OrganizationDto.SubmitOrg;
import com.hengxin.platform.member.dto.PasswordDto;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.member.dto.PersonDto.IdCardExist;
import com.hengxin.platform.member.dto.PersonDto.PersionMobileExist;
import com.hengxin.platform.member.dto.PersonDto.SubmitPerson;
import com.hengxin.platform.member.dto.downstream.AccountDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.domain.User.NickNameGroup;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: MemberDetailController
 * 
 * @author runchen
 */

@Controller
public class MemberController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private transient MemberService memberService;

	@Autowired
	private transient UserService userService;

	@Autowired
	private transient WebUtil webUtil;

	@Autowired
	private transient SecurityContext securityContext;

	@Autowired
	private transient AcctService acctService;

	@Autowired
	private MemberMessageService messageService;

	/** Get Page methods */
	@RequestMapping(value = UrlConstant.MEMBER_BASIC_INFO_URL, method = RequestMethod.GET)
	@RequiresPermissions({ Permissions.MEMBER_BASIC_INFO_VIEW })
	public String basicInfoFill(HttpServletRequest request, HttpSession session, Model model) {

		String mode = request.getParameter("mode");
		String userId = securityContext.getCurrentUserId();
		MemberApplication memberPo = memberService.getMemberWithLatestStatus(userId);

		MemberDto memberDto = new MemberDto();
		if (memberPo != null) {
			Class<? extends MemberDto> clazz = memberPo.getUserType() == EUserType.PERSON ? PersonDto.class
					: OrganizationDto.class;

			// 拒绝状态下取原信息，原信息为空，表示第一次提交，需要将拒绝信息pop到add页面
			if (memberPo.getStatus() == EApplicationStatus.REJECT) {
				Member oldMember = memberService.getMemberByUserId(userId);
				if (oldMember != null) {
					memberDto = ConverterService.convert(oldMember, clazz, MemberInfoConverter.class);
					memberDto.setUserType(securityContext.getCurrentUser().getType());
					memberDto.setStatus(EApplicationStatus.ACCEPT);
				} else {
					memberDto = ConverterService.convert(memberPo, clazz);
					memberDto.setStatus(EApplicationStatus.UNCOMMITTED);
				}
				memberDto.setRejectLastTime(true);
				memberDto.setComments(memberPo.getComments());
			} else {
				memberDto = ConverterService.convert(memberPo, clazz);
			}

			// 修改的pending状态需要看到原信息
			if (memberPo.getStatus() == EApplicationStatus.PENDDING) {
				Member oldMember = memberService.getMemberByUserId(userId);
				if (oldMember != null) {
					memberDto.setOldMember(ConverterService.convert(oldMember, clazz,
							MemberInfoConverter.class));
				}
			}
			// 取账号信息
			AcctPo acctPo = acctService.getAcctByUserId(userId);
			if (acctPo != null) {
				AccountDto accountDto = ConverterService.convert(acctPo, AccountDto.class);
				memberDto.setAccount(accountDto);
				// 账号信息set到原信息中
				if (memberDto.getOldMember() != null) {
					memberDto.getOldMember().setAccount(accountDto);
				}
			}

		} else {
			// 第一次补全时email和mobile应该是注册时填入的
			User user = securityContext.getCurrentUser();
			memberDto.setEmail(user.getEmail());
			memberDto.setMobile(user.getMobile());
		}

		EApplicationStatus status = memberDto.getStatus();
		if (StringUtils.isNotBlank(mode)) {
			// 如果有mode参数，页面显示根据mode
			EFormMode formMode = getFormMode(mode);
			memberDto.setFormMode(formMode);
			memberDto
					.setFixedInput(formMode != EFormMode.EDIT || !securityContext.canAdvancedBasicInfoEdit());
		} else {
			// 如果没有mode参数，除保存未提交状态其他状态全部为View
			if (status == null || status == EApplicationStatus.NULL
					|| status == EApplicationStatus.UNCOMMITTED) {
				memberDto.setFormMode(EFormMode.NULL);
				memberDto.setFixedInput(false);
			}
		}
		boolean finalStatus = status == EApplicationStatus.ACCEPT || status == EApplicationStatus.ACTIVE
				|| status == EApplicationStatus.REJECT;
		memberDto.setCanEdit(securityContext.canEditMemberBasicInfo() && finalStatus);
		if (securityContext.isInvestor()) {
			if (securityContext.isFinancier()) {
				memberDto.setMemberType(EMemberType.INVESTOR_FINANCER);
			} else {
				memberDto.setMemberType(EMemberType.INVESTOR);
			}
		} else if (securityContext.isFinancier()) {
			memberDto.setMemberType(EMemberType.FINANCER);
		}
		memberDto.setFromPage(request.getParameter("fromPage"));
		model.addAttribute("member", memberDto);

		// get all selection options
		addAllBasicOptionsToModel(model);
		return "members/basicinfo";
	}

	/** Submit methods */

	@RequestMapping(value = "members/basicinfo/person", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MEMBER_BASIC_INFO_ADD, Permissions.MEMBER_BASIC_INFO_UPDATE })
	public ResultDto doBasicInfoFillPerson(
			@OnValid({ SubmitPerson.class }) @RequestBody PersonDto person,
			HttpServletRequest request, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doBasicInfoFillPerson() start: ");
		}

		saveBasicInfo(person, EUserType.PERSON, EApplicationStatus.PENDDING);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doBasicInfoFillPerson() succeed: ");
		}

		String fromPage = request.getParameter("from");
		String returnURL = "/web/members/basicinfo";
		if ("invest".equals(fromPage)) {
			returnURL = "/web/members/investorinfo";
		} else if ("finance".equals(fromPage)) {
			returnURL = "/web/members/financierinfo";
		}

		return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(returnURL));
	}

	@RequestMapping(value = "members/basicinfo/organization", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MEMBER_BASIC_INFO_ADD, Permissions.MEMBER_BASIC_INFO_UPDATE })
	public ResultDto doBasicInfoFillOrganization(
			@OnValid({ SubmitOrg.class }) @RequestBody OrganizationDto organization,
			HttpServletRequest request, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doBasicInfoFillOrganization() start: ");
		}

		saveBasicInfo(organization, EUserType.ORGANIZATION, EApplicationStatus.PENDDING);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doBasicInfoFillOrganization() succeed: ");
		}

		String fromPage = request.getParameter("from");
		String returnURL = "/web/members/basicinfo";
		if ("invest".equals(fromPage)) {
			returnURL = "/web/members/investorinfo";
		} else if ("finance".equals(fromPage)) {
			returnURL = "/web/members/financierinfo";
		}

		return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(returnURL));
	}

	/** Save methods */

	@RequestMapping(value = "members/basicinfo/person/save", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MEMBER_BASIC_INFO_ADD, Permissions.MEMBER_BASIC_INFO_UPDATE })
	public ResultDto saveBasicInfoFillPerson(@OnValid @RequestBody PersonDto person,
			HttpServletRequest request, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saveBasicInfoFillPerson() start: ");
		}

		saveBasicInfo(person, EUserType.PERSON, EApplicationStatus.UNCOMMITTED);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saveBasicInfoFillPerson() succeed: ");
		}

		return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
	}

	@RequestMapping(value = "members/basicinfo/organization/save", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MEMBER_BASIC_INFO_ADD, Permissions.MEMBER_BASIC_INFO_UPDATE })
	public ResultDto saveBasicInfoFillOrganization(@OnValid @RequestBody OrganizationDto organization,
			HttpServletRequest request, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saveBasicInfoFillOrganization() start: ");
		}


		saveBasicInfo(organization, EUserType.ORGANIZATION, EApplicationStatus.UNCOMMITTED);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saveBasicInfoFillOrganization() succeed: ");
		}

		return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
	}

	@RequestMapping(value = UrlConstant.MEMBER_MY_SETTINGS_CHANGE_PASSWORD_URL, method = RequestMethod.GET)
	@RequiresPermissions({ Permissions.MY_SETTINGS_PASSWORD_SETTINGS })
	public String toPasswordUpdate(HttpServletRequest request, Model model) {
		LOGGER.info("passwordUpdate() start: ");
		String userId = securityContext.getCurrentUserId();
		model.addAttribute("userId", userId);
		return "members/password_update";
	}

	@RequestMapping(value = "members/password/update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MY_SETTINGS_PASSWORD_SETTINGS })
	public ResultDto passwordUpdate(@OnValid @RequestBody PasswordDto passwordDto,
			HttpServletRequest request, Model model) {
		LOGGER.info("passwordUpdate() start: ");
		String userName = securityContext.getCurrentUserName();
		passwordDto.setUserName(userName);
		this.userService.updatePassword(passwordDto);
		return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
	}

	@RequestMapping(value = "members/password/reset/do", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_PASSWORD_RESET })
	public ResultDto passwordReset(@RequestBody PasswordDto passwordDto, HttpServletRequest request,
			Model model) throws Exception {
		LOGGER.info("passwordReset() start: ");
		String userName = securityContext.getCurrentUserName();
		passwordDto.setUserName(userName);
		this.userService.resetPassword(passwordDto);
		return ResultDtoFactory.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
	}

	@RequestMapping(value = "members/nickname/update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MY_SETTINGS_PASSWORD_SETTINGS })
	public ResultDto nicknameUpdate(@OnValid(NickNameGroup.class) @RequestBody User user,
			HttpServletRequest request, Model model) {
		LOGGER.info("nicknameUpdate() start: ");
		User oldUser = securityContext.getCurrentUser();
		this.userService.updateUserName(oldUser.getUserId(), user.getUsername(), oldUser.getUsername());
		return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
	}

	private void saveBasicInfo(MemberDto member, EUserType type, EApplicationStatus status) {
		String username = securityContext.getCurrentUserId();
		member.setUserId(username);
		member.setUserType(type);
		// 基于基本信息修改页面，隐藏了银行信息填写，取消银行信息验证
		/*
		 * if (type == EUserType.PERSON) { validate(member, new Class[] {
		 * BankExist.class, IdCardExist.class }); } else { validate(member, new
		 * Class[] { BankExist.class }); }
		 */

		if (type == EUserType.PERSON) {
			validate(member, new Class[] { IdCardExist.class, PersionMobileExist.class });
		} else {
			validate(member, new Class[] { ContactIdcardExist.class, ContactMobileExist.class });
		}

		ActionType action = ActionType.NEW;
		MemberApplication memberApplication = memberService.getLatestMemberApplWithoutReject(username);
		if (memberApplication != null) {
			if (memberApplication.getStatus() == EApplicationStatus.UNCOMMITTED) {
				member.setAppId(memberApplication.getAppId());
			}
			member.setCreator(memberApplication.getCreator());
			action = memberApplication.getStatus() == EApplicationStatus.UNCOMMITTED ? ActionType.NEW
					: ActionType.EDIT;
		} else {
			/** reset creator id, operator id. **/
			MemberApplication originalMember = memberService.getLatestMemberAppl(username);
			if (originalMember != null) {
				member.setCreator(originalMember.getCreator());
			}
			member.setLastMntOpid(securityContext.getCurrentUserId());
		}

		User user = securityContext.getCurrentUser();
		member.setStatus(status);
		memberService.saveBasicInfo(
				ConverterService.convert(member, MemberApplication.class, ApplicationConverter.class), user,
				action, memberApplication);
		/** Send message to Client Service Dept. **/
		String messageKey = "member.audit.message";
		String content = securityContext.getCurrentUserName();
		String content2 = EUserType.PERSON == type ? "#indiv" : "#org";
		List<EBizRole> roles = new ArrayList<EBizRole>();
		roles.add(EBizRole.PLATFORM_CLIENT_SERVICE_MANAGER);
		roles.add(EBizRole.PLATFORM_MEMBER_MEMBERINFO_ASSOCIATE);
		messageService.sendMessage(EMessageType.TODO, messageKey, roles, content, content2);
	}

	/** Ajax methods */

	/** Private methods */

	private void addAllBasicOptionsToModel(Model model) {
		// Enum
		model.addAttribute("genderList", getStaticOptions(EGender.class, false));
		model.addAttribute("jobList", getDynamicOptions(EOptionCategory.JOB, true));
		// DynamicOption
		model.addAttribute("bankList", getDynamicOptions(EOptionCategory.BANK, true));
		model.addAttribute("provinceList", getDynamicOptions(EOptionCategory.REGION, true));
		model.addAttribute("educationList", getDynamicOptions(EOptionCategory.EDUCATION, true));
		model.addAttribute("industryList", getDynamicOptions(EOptionCategory.ORG_INDUSTRY, true));
		model.addAttribute("orgTypeList", getDynamicOptions(EOptionCategory.ORG_TYPE, true));
	}

}
