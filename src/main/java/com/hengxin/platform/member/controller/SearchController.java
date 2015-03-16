/*
 * Project Name: kmfex-platform
 * File Name: SearchController.java
 * Class Name: SearchController
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.member.domain.AgencyApplication;
import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.domain.ProductProviderApplication;
import com.hengxin.platform.member.domain.ServiceCenterApplication;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.dto.ActionHistoryDto;
import com.hengxin.platform.member.dto.AuditLogDto;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.MemberInfoDto;
import com.hengxin.platform.member.dto.MemberInfoSearchDto;
import com.hengxin.platform.member.dto.MemberSearchDto;
import com.hengxin.platform.member.dto.MemberUpdateBankDto;
import com.hengxin.platform.member.dto.MemberUpdateBankDto.SubmitPerson;
import com.hengxin.platform.member.dto.MemberViewDto;
import com.hengxin.platform.member.dto.upstream.AgencyApplicationSearchDto;
import com.hengxin.platform.member.dto.upstream.MemberApplicationAuditDto;
import com.hengxin.platform.member.dto.upstream.ProductProviderDto;
import com.hengxin.platform.member.dto.upstream.ServiceCenterDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.AgencyApplicationViewService;
import com.hengxin.platform.member.service.MemberApplicationService;
import com.hengxin.platform.member.service.MemberApplicationViewService;
import com.hengxin.platform.member.service.MemberInfoExcelService;
import com.hengxin.platform.member.service.MemberInfoSearchService;
import com.hengxin.platform.member.service.MemberLessViewService;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.member.service.ServiceCenterService;
import com.hengxin.platform.member.service.SubscribeGroupService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: SearchController
 * 
 * @author shengzhou
 */

@Controller
public class SearchController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private transient MemberService memberService;

	@Autowired
	private MemberApplicationService memberApplicationService;

	@Autowired
	private transient WebUtil webUtil;

	@Autowired
	private MemberApplicationViewService memberApplicationViewService;

	@Autowired
	private MemberLessViewService memberLessViewService;

	@Autowired
	private AgencyApplicationViewService agencyApplicationViewService;

	@Autowired
	private ServiceCenterService serviceCenterService;

	@Autowired
	private UserService userService;

	@Autowired
	private SubscribeGroupService subscribeGroupService;

	@Autowired
	private BankAcctService bankService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private MemberMessageService messageService;

	@Autowired
	private MemberInfoSearchService memberInfoSearchSearch;
	
	@Autowired
	private MemberInfoExcelService memberInfoExcelService;

	/** Get page methods */

	/**
	 * Render investor and financier list page
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MEMBER_MANAGER_INVEST_FIN_INFO_MAINT_URL)
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_INVST_FIN_VIEW,
			Permissions.CUSTOMER_SERVICE_DEPT })
	public String renderMemberInfoList(HttpServletRequest request, HttpSession session, Model model) {
		return "members/search_member";
	}

	/**
	 * list for member's password reset page
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MEMBER_MANAGEMENT_PASSWORD_RESET_URL)
	@RequiresPermissions(Permissions.MEMBER_ADV_MAINT_PASSWORD_RESET)
	public String renderMemberInfoListForPR(HttpServletRequest request, HttpSession session, Model model) {
		model.addAttribute("shiro", securityContext);
		return "members/search_pwreset";
	}

	/**
	 * Render ServiceCenter and Product Service list page
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MEMBER_MANAGER_AGENCY_INFO_MAINT_URL)
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_VIEW,
			Permissions.MEMBER_ADV_MAINT_PROD_SERV_VIEW }, logical = Logical.OR)
	public String renderAgencyInfoList(HttpServletRequest request, HttpSession session, Model model) {
		return "members/search_agency";
	}

	@RequestMapping(value = UrlConstant.MEMBER_MANAGER_AGENT_INVEST_FIN_INFO_MAINT_URL)
	@RequiresPermissions(value = { Permissions.MEMBER_REGISTER_FOR_USER, Permissions.ATHD_CENTER_MEMBER }, logical = Logical.OR)
	public String renderAgentInfoList(HttpServletRequest request, Model model) {
		return "members/search_agent";
	}

	/**
	 * Get investor or financier details
	 * 
	 * @param request
	 * @param model
	 * @param userId
	 * @param userType
	 * @return
	 */
	@RequestMapping(value = "search/getmemberinfo/{userId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_INVST_FIN_VIEW,
			Permissions.ATHD_CENTER_MEMBER }, logical = Logical.OR)
	public String getMemberInfoDetails(HttpServletRequest request, Model model,
			@PathVariable(value = "userId") String userId, @RequestParam(value = "userType") String userType) {
		String returnURL = "";
		MemberApplicationAuditDto dto = memberApplicationService.getMemberInfoDetails(userId, userType);
		if (EUserType.PERSON == EUserType.valueOf(userType)) {
			dto.getPerson().setInCanViewPage(securityContext.canUpdateInvstFinInfo());
			returnURL = "members/searchinfo_person";
		}
		if (EUserType.ORGANIZATION == EUserType.valueOf(userType)) {
			dto.getOrganization().setInCanViewPage(securityContext.canUpdateInvstFinInfo());
			returnURL = "members/searchinfo_org";
		}
		dto.setAccount(memberApplicationService.getAccountDetails(userId));
		dto.setUser(userService.getUserByUserId(userId));

		if (dto.getInvestorInfo() != null) {
			dto.getInvestorInfo().setInCanViewPage(securityContext.canUpdateInvstFinInfo());
			if (securityContext.canEditAgentName()) {// 判断能否编辑介绍人
				// 能编辑
				dto.getInvestorInfo().getFixedStatus().setAgentNameFixed(false);
			} else {
				// 不能编辑
				dto.getInvestorInfo().getFixedStatus().setAgentNameFixed(true);
			}
		}

		if (dto.getFinancierInfo() != null) {
			dto.getFinancierInfo().setInCanViewPage(securityContext.canUpdateInvstFinInfo());
		}

		List<ServiceCenterDto> serviceCenterDtoList = new ArrayList<ServiceCenterDto>();
		List<ServiceCenterInfo> allServiceCenters = serviceCenterService.getAllServiceCenters();
		for (ServiceCenterInfo domain : allServiceCenters) {
			serviceCenterDtoList.add(ConverterService.convert(domain, ServiceCenterDto.class));
		}

		model.addAttribute("person", dto.getPerson());
		model.addAttribute("organization", dto.getOrganization());
		model.addAttribute("investorInfo", dto.getInvestorInfo());
		model.addAttribute("financierInfo", dto.getFinancierInfo());
		model.addAttribute("account", dto.getAccount());
		model.addAttribute("user", dto.getUser());
		model.addAttribute("bankList", getDynamicOptions(EOptionCategory.BANK, true));
		model.addAttribute("provinceList", getDynamicOptions(EOptionCategory.REGION, true));
		model.addAttribute("agencyList", serviceCenterDtoList);
		model.addAttribute("investorLevelList",
				SystemDictUtil.getRootDictList(EOptionCategory.INVESTOR_LEVEL, false));
		model.addAttribute("financierLevelList",
				SystemDictUtil.getRootDictList(EOptionCategory.FINANCIER_LEVEL, false));
		return returnURL;
	}

	@RequestMapping(value = "search/getmemberdtoinfo/{userId}", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions({ Permissions.MEMBER_PROFILE_INVESTOR_FINANCIER_APPROVE })
	public MemberApplicationAuditDto getMemberDtoInfoDetails(HttpServletRequest request, Model model,
			@PathVariable(value = "userId") String userId, @RequestParam(value = "userType") String userType) {
		MemberApplicationAuditDto dto = memberApplicationService.getMemberInfoDetails(userId, userType);
		dto.setAccount(memberApplicationService.getAccountDetails(userId));
		dto.setUser(userService.getUserByUserId(userId));
		return dto;
	}

	/**
	 * Get Service center or product service details
	 * 
	 * @param request
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "search/getagencyinfo/{userId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_VIEW,
			Permissions.MEMBER_ADV_MAINT_PROD_SERV_VIEW }, logical = Logical.OR)
	public String getMemberAgencyInfoDetails(HttpServletRequest request, Model model,
			@PathVariable(value = "userId") String userId) {
		AgencyApplicationSearchDto dto = memberService.getMemberAgencyDetails(userId);
		model.addAttribute("account", dto.getAccount());
		model.addAttribute("agency", dto.getAgency());
		model.addAttribute("productProvider", dto.getProductProvider());
		model.addAttribute("serviceCenter", dto.getServiceCenter());
		model.addAttribute("provinceList", getDynamicOptions(EOptionCategory.REGION, true));
		model.addAttribute("bankList", getDynamicOptions(EOptionCategory.BANK, true));
		model.addAttribute("serviceCenterLevelList",
				SystemDictUtil.getRootDictList(EOptionCategory.SERVICE_CENTER_LEVEL, false));
		model.addAttribute("prodServLevelList",
				SystemDictUtil.getRootDictList(EOptionCategory.PRODUCT_SERVICE_LEVEL, false));

		return "members/searchinfo_agency";
	}

	@RequestMapping(value = "search/getagencydtoinfo/{userId}", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_VIEW,
			Permissions.MEMBER_ADV_MAINT_PROD_SERV_VIEW }, logical = Logical.OR)
	public AgencyApplicationSearchDto getMemberAgencyInfoDtoDetails(HttpServletRequest request, Model model,
			@PathVariable(value = "userId") String userId) {
		return memberService.getMemberAgencyDetails(userId);
	}

	@RequestMapping("search/getmemberinfo")
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_REGISTER_FOR_USER,
			Permissions.MEMBER_ADV_MAINT_INVST_FIN_VIEW }, logical = Logical.OR)
	public DataTablesResponseDto<MemberViewDto> getMemberInfoList(HttpServletRequest request, Model model,
			@RequestBody MemberSearchDto memberSearch) {
		DataTablesResponseDto<MemberViewDto> members = memberApplicationViewService.getMembers(memberSearch);
		return members;
	}

	@RequestMapping("search/getmemberinfoforpwreset")
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_PASSWORD_RESET })
	public DataTablesResponseDto<MemberViewDto> getMemberInfoListForPR(HttpServletRequest request,
			Model model, @RequestBody MemberSearchDto memberSearch) {
		memberSearch.setAuditStatus(EApplicationStatus.AUDITED);
		DataTablesResponseDto<MemberViewDto> members = memberLessViewService
				.getMembersForPasswordReset(memberSearch);
		return members;
	}

	@RequestMapping("search/getagencyinfo")
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_VIEW,
			Permissions.MEMBER_ADV_MAINT_PROD_SERV_VIEW }, logical = Logical.OR)
	public DataTablesResponseDto<MemberViewDto> getAgencyInfoList(HttpServletRequest request, Model model,
			@RequestBody MemberSearchDto memberSearch) {
		DataTablesResponseDto<MemberViewDto> agencies = agencyApplicationViewService
				.getAgencies(memberSearch);
		return agencies;
	}

	/**
	 * 
	 * @param request
	 * @param auditLog
	 * @return
	 */
	@RequestMapping(value = "search/getlogs")
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_INVST_FIN_VIEW,
			Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_VIEW, Permissions.MEMBER_ADV_MAINT_PROD_SERV_VIEW,
			Permissions.MEMBER_PROFILE_INVESTOR_FINANCIER_APPROVE,
			Permissions.MEMBER_PROFILE_PROD_SERV_APPROVE, Permissions.MEMBER_PROFILE_ATHD_CNTL_APPROVE,
			Permissions.MEMBER_ADV_MAINT_PASSWORD_RESET }, logical = Logical.OR)
	public DataTablesResponseDto<ActionHistoryDto> getLog(HttpServletRequest request,
			@RequestBody AuditLogDto auditLog) {
		return serviceCenterService.getLog(auditLog, securityContext.canUpdateInvstFinInfo());
	}

	/**
	 * Description: TODO
	 * 
	 * @param request
	 * @param model
	 * @param personDto
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "search/updatebasicinfo/{userId}", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE })
	public ResultDto updateMemberBasicInfo(
			HttpServletRequest request,
			Model model,
			@OnValid(value = { SubmitPerson.class,
					com.hengxin.platform.member.dto.PersonDto.SubmitPerson.class }) @RequestBody MemberUpdateBankDto bankDto,
			@PathVariable(value = "userId") String userId) {
		LOGGER.debug("update member : {}", bankDto);
		MemberApplication member = memberService.getLatestMemberApplWithoutReject(userId);
		MemberApplication oldInfo = member.clone();
		member.setBankAccount(bankDto.getBankAccount());
		member.setBankAccountName(bankDto.getBankAccountName());
		member.setBankCardFrontImg(bankDto.getBankCardFrontImg());
		member.setBankFullName(bankDto.getBankFullName());
		member.setBankOpenBranch(bankDto.getBankOpenBranch());
		member.setBankOpenCity(bankDto.getBankOpenCity().getCode());
		member.setBankOpenProvince(bankDto.getBankOpenProvince().getCode());
		member.setBankShortName(bankDto.getBankShortName().getCode());
		BankAcct bankAcct = getBankAcct(bankDto, userId);
		try {
			memberApplicationService.updateBasicInfo(member, bankAcct, oldInfo);
			/** send SMS when bank info update. **/
			if (!bankDto.getBankAccount().equals(oldInfo.getBankAccount())) {
				String messageKey = "member.bankaccount.change.message";
				String content = bankDto.getBankAccount();
				String name = member.getPersonName() == null ? member.getOrgName() : member.getPersonName();
				messageService.sendMessage(EMessageType.SMS, messageKey, userId, content, name);
				messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, name);
			}
			return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE, bankDto);
		} catch (BizServiceException e) {
			return ResultDtoFactory.toNack(MessageUtil.getMessage(e.getError().getDisplayMsg(), e.getError()
					.getArgs()));
		}
	}

	/**
	 * Description: TODO
	 * 
	 * @param request
	 * @param model
	 * @param investorDto
	 * @param userId
	 * @return
	 */

	@RequestMapping(value = "search/updateinvestorinfo/{userId}", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE })
	public ResultDto updateMemberInvestorInfo(HttpServletRequest request, Model model,
			@RequestBody InvestorDto investorDto, @PathVariable(value = "userId") String userId) {
		LOGGER.debug("update member : {}", investorDto);
		InvestorApplication investorInfo = memberApplicationService
				.getInvestorDetailsWithLatestStatus(userId);
		InvestorApplication oldInfo = investorInfo.clone();
		if (investorDto.getAuthCenter() != null) {
			investorInfo.setAuthCenterId(investorDto.getAuthCenter().getUserId());
		}
		investorInfo.setAgent(investorDto.getAgent());
		if (investorDto.getAgentName() != null) {// 新增介绍人字段
			investorInfo.setAgentName(investorDto.getAgentName());
		}
		if (investorDto.getInvestorLevel() != null) {
			investorInfo.setInvestorLevel(investorDto.getInvestorLevel().getCode());
		}
		memberApplicationService.updateInvestorInfo(investorInfo, oldInfo);
		FinancierApplication financierInfo = memberApplicationService
				.getFinancierDetailsWithLatestStatus(userId);
		if (financierInfo != null) {
			financierInfo.setAgent(investorDto.getAgent());
			if (investorDto.getAuthCenter() != null) {
				financierInfo.setAuthCenterId(investorDto.getAuthCenter().getUserId());
			}
			memberApplicationService.updateFinancierInfo(financierInfo, null, false);
		}
		return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE, investorDto);
	}

	/**
	 * Description: TODO
	 * 
	 * @param request
	 * @param model
	 * @param financierDto
	 * @param userId
	 * @return
	 */

	@RequestMapping(value = "search/updatefinancierinfo/{userId}", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE })
	public ResultDto updateMemberFinancierInfo(HttpServletRequest request, Model model,
			@RequestBody FinancierDto financierDto, @PathVariable(value = "userId") String userId) {
		LOGGER.debug("update member : {}", financierDto);
		FinancierApplication financierInfo = memberApplicationService
				.getFinancierDetailsWithLatestStatus(userId);
		FinancierApplication oldInfo = financierInfo.clone();
		financierInfo.setFinancierLevel(financierDto.getFinancierLevel().getCode());
		memberApplicationService.updateFinancierInfo(financierInfo, oldInfo, true);
		return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE, financierDto);
	}

	@RequestMapping(value = "search/updateAgencyBasicInfo/{userId}", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_UPDATE,
			Permissions.MEMBER_ADV_MAINT_PROD_SERV_UPDATE }, logical = Logical.OR)
	public ResultDto updateAgencyBasicInfo(
			HttpServletRequest request,
			Model model,
			@OnValid(value = { SubmitPerson.class,
					com.hengxin.platform.member.dto.PersonDto.SubmitPerson.class }) @RequestBody MemberUpdateBankDto agencyDto,
			@PathVariable(value = "userId") String userId) {
		LOGGER.debug("update member : {}", agencyDto);
		AgencyApplication agencyInfo = memberService.getAgencyApplicationWithLatestStatus(userId);
		AgencyApplication oldInfo = agencyInfo.clone();
		agencyInfo.setBankShortName(agencyDto.getBankShortName().getCode());
		agencyInfo.setBankFullName(agencyDto.getBankFullName());
		agencyInfo.setBankAccount(agencyDto.getBankAccount());
		agencyInfo.setBankAccountName(agencyDto.getBankAccountName());
		agencyInfo.setBankCardFrontImg(agencyDto.getBankCardFrontImg());
		agencyInfo.setBankOpenBranch(agencyDto.getBankOpenBranch());
		agencyInfo.setBankOpenCity(agencyDto.getBankOpenCity().getCode());
		agencyInfo.setBankOpenProvince(agencyDto.getBankOpenProvince().getCode());
		BankAcct bankAcct = getBankAcct(agencyDto, userId);
		memberApplicationService.updateAgencyInfo(agencyInfo, bankAcct, oldInfo);
		/** send SMS when bank info update. **/
		if (!agencyInfo.getBankAccount().equals(oldInfo.getBankAccount())) {
			String messageKey = "member.bankaccount.change.message";
			String content = agencyInfo.getBankAccount();
			String name = agencyInfo.getName();
			messageService.sendMessage(EMessageType.SMS, messageKey, userId, content, name);
			messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, name);
		}
		return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE, agencyDto);
	}

	@RequestMapping(value = "search/updateServiceCenterInfo/{userId}", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_UPDATE,
			Permissions.MEMBER_ADV_MAINT_PROD_SERV_UPDATE }, logical = Logical.OR)
	public ResultDto updateServiceCenterInfo(HttpServletRequest request, Model model,
			@RequestBody ServiceCenterDto serverDto, @PathVariable(value = "userId") String userId) {
		LOGGER.debug("update member : {}", serverDto);
		ServiceCenterApplication serviceCenterInfo = memberService.getServiceCenterWithLatestStatus(userId);
		ServiceCenterApplication oldInfo = serviceCenterInfo.clone();
		serviceCenterInfo.setLevel(serverDto.getLevel().getCode());
		serviceCenterInfo.setAgent(serverDto.getAgent());
		/** version 20150317   新增 编辑 授权服务中心的显示名称  **/
		serviceCenterInfo.setServiceCenterDesc(serverDto.getServiceCenterDesc());
		memberApplicationService.updateServiceCenterInfo(serviceCenterInfo, oldInfo);
		return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE, serverDto);
	}

	@RequestMapping(value = "search/updateProductProviderInfo/{userId}", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_UPDATE,
			Permissions.MEMBER_ADV_MAINT_PROD_SERV_UPDATE }, logical = Logical.OR)
	public ResultDto updateProductProviderInfo(HttpServletRequest request, Model model,
			@RequestBody ProductProviderDto productDto, @PathVariable(value = "userId") String userId) {
		LOGGER.debug("update member : {}", productDto);
		ProductProviderApplication productProviderInfo = memberService
				.getProductProviderWithLatestStatus(userId);
		ProductProviderApplication oldInfo = productProviderInfo.clone();
		productProviderInfo.setProSeverLevel(productDto.getLevel().getCode());
		productProviderInfo.setAgent(productDto.getAgent());
		productProviderInfo.setWrtrCreditFile(productDto.getWrtrCreditFile());
		memberApplicationService.updateProductProviderInfo(productProviderInfo, oldInfo);
		return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE, productDto);
	}

	private BankAcct getBankAcct(MemberUpdateBankDto dto, String userId) {
		BankAcct bankAcct = new BankAcct();
		List<BankAcct> bankAcctList = bankService.findBankAcctByUserIdWihoutCheck(userId);
		if (!bankAcctList.isEmpty()) {
			bankAcct = bankAcctList.get(0);
			bankAcct.setOldBnkAcctNo(bankAcct.getBnkAcctNo());
		} else {
			bankAcct.setOldBnkAcctNo("");
			bankAcct.setCreateTs(new Date());
			bankAcct.setCreateOpid(securityContext.getCurrentUserId());
		}
		bankAcct.setUserId(userId);
		bankAcct.setBnkAcctNo(dto.getBankAccount());
		bankAcct.setBnkAcctName(dto.getBankAccountName());
		bankAcct.setBnkBrch(dto.getBankOpenBranch());
		bankAcct.setBnkCardImg(dto.getBankCardFrontImg());
		bankAcct.setBnkCd(dto.getBankShortName().getCode());
		bankAcct.setBnkName(dto.getBankFullName());
		bankAcct.setBnkOpenCity(dto.getBankOpenCity().getCode());
		bankAcct.setBnkOpenProv(dto.getBankOpenProvince().getCode());
		bankAcct.setLastMntTs(new Date());
		bankAcct.setLastMntOpid(securityContext.getCurrentUserId());
		return bankAcct;
	}

	/**
	 * 会员信息查询跳转 会员管理--游客信息查询（version:20150306）
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequestMapping(value = UrlConstant.MEMBER_MANAGER_INFO_SEARCH_URL)
	@RequiresPermissions(value = { Permissions.CUSTOMER_SERVICE_MANAGER })
	public String renderMemberList(HttpServletRequest request, HttpSession session, Model model) {
		model.addAttribute("eMemberType", getMemberTypeItems());
		return "members/guest_search";
	}

	/**
	 * 会员信息查询 会员管理--游客信息查询（version:20150306）
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@RequestMapping("search/memberinfo")
	@ResponseBody
    @RequiresPermissions(value = { Permissions.CUSTOMER_SERVICE_MANAGER })
	public DataTablesResponseDto<MemberInfoDto> getMemberInfoList(HttpServletRequest request, Model model,
			@RequestBody MemberInfoSearchDto memberInfoSearchDto) {
		/** 默认游客 **/
		memberInfoSearchDto.setRoleName(EBizRole.TOURIST.getCode());
		DataTablesResponseDto<MemberInfoDto> members = memberInfoSearchSearch
				.getMemberInfoList(memberInfoSearchDto);
		return members;
	}
	
    /**
     * 导出会员信息 EXCEL（version:20150306）
     * 
     * @param request
     * @param model
     * @param fromDate
     * @param toDate
     * @param userType
     * @return
     */
    @RequestMapping(value = "search/memberinfoExcel", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequiresPermissions(value = { Permissions.CUSTOMER_SERVICE_MANAGER })
	public ModelAndView getMemberInfoExcel(HttpServletRequest request, Model model,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,
			@RequestParam("userType") EUserType userType )  {
	    MemberInfoSearchDto memberInfoSearchDto = new MemberInfoSearchDto();
	    memberInfoSearchDto.setRoleName(EBizRole.TOURIST.getCode());
	    memberInfoSearchDto.setFromDate(fromDate);
	    memberInfoSearchDto.setToDate(toDate);
	    memberInfoSearchDto.setUserType(userType);
	    List<MemberInfoDto> memberInfoList = memberInfoSearchSearch.getMemberInfoExcel(memberInfoSearchDto);
	    String fileName = "游客信息.xls";
	    String tempPath = AppConfigUtil.getPersonMemberInfoExcelTemplatePath();
	    if(EUserType.ORGANIZATION.equals(userType)){
	    	tempPath = AppConfigUtil.getOrganizationMemberInfoExcelTemplatePath();
	    }
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("applList", memberInfoList);
	    map.put("fileName", fileName);
	    map.put("tempPath", tempPath);
	    return new ModelAndView(memberInfoExcelService, map);       
	}

	private List<EnumOption> getMemberTypeItems() {
		List<EnumOption> options = new ArrayList<EnumOption>();
		/** 游客 **/
		options.add(new EnumOption(EBizRole.TOURIST.name(), EBizRole.TOURIST.getName()));
		/** 投资会员 **/
		options.add(new EnumOption(EBizRole.INVESTER.name(), EBizRole.INVESTER.getName()));
		/** 融资会员 **/
		options.add(new EnumOption(EBizRole.FINANCIER.name(), EBizRole.FINANCIER.getName()));
		/** 全部 **/
		options.add(new EnumOption("ALL", "全部"));
		return options;
	}
}
