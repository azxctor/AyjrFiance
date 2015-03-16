/*
 * Project Name: kmfex-platform
 * File Name: MemberManageController.java
 * Class Name: MemberManageController
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hengxin.platform.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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

import com.hengxin.platform.app.dto.downstream.MemberListDownDto;
import com.hengxin.platform.app.dto.downstream.MemberRegisterInitDownDto;
import com.hengxin.platform.app.dto.upstream.MemberListUpDto;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.dto.upstream.RegisterInfoDto;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.member.controller.AgentRegisterController;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.dto.MemberSearchDto;
import com.hengxin.platform.member.dto.MemberViewDto;
import com.hengxin.platform.member.dto.FinancierDto.SubmitFinance;
import com.hengxin.platform.member.dto.FinancierDto.SubmitOrgCode;
import com.hengxin.platform.member.dto.InvestorDto.SubmitInvest;
import com.hengxin.platform.member.dto.OrganizationDto.SubmitOrg;
import com.hengxin.platform.member.dto.PersonDto.IdCardExist;
import com.hengxin.platform.member.dto.PersonDto.SubmitPerson;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberApplicationViewService;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.ServiceCenterService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.service.UserService;

/**
 * 会员管理.
 * 
 * @author yicai
 *
 */
@Controller
public class MemberManageController extends BaseController {
	@Autowired
	private SecurityContext securityContext;
	
	@Autowired
	private transient ServiceCenterService serviceCenterService;

	@Autowired
	private MemberApplicationViewService memberApplicationViewService;
	
	@Autowired
	private transient UserService userService;
	
	@Autowired
    private transient MemberMessageService messageService;
	
	@Autowired
    private transient WebUtil webUtil;
	
	// inject web controller
	private AgentRegisterController agentRegisterController;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MemberManageController.class);

	@PostConstruct
	public void init() {
		LOGGER.debug("init agentRegisterController");
		agentRegisterController = new AgentRegisterController();
		agentRegisterController.setSecurityContext(securityContext);
		agentRegisterController.setMessageService(messageService);
		agentRegisterController.setUserService(userService);
	}
	
	/**
	 * 会员管理-会员信息维护列表, post, 获取返回所有列表及过滤条件搜索
	 * 
	 * refer to search/getmemberinfo
	 * 
	 * @param MemberListUpDto
	 * @return
	 */
	@RequestMapping(value = "/members", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MEMBER_REGISTER_FOR_USER, Permissions.MEMBER_ADV_MAINT_INVST_FIN_VIEW }, logical = Logical.OR)
	public ResultDto memberList(@OnValid @RequestBody MemberListUpDto memberListUpDto) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("memberList() start: ");
		}
		MemberSearchDto searchDto = initMemberSearchDto(memberListUpDto);
		DataTablesResponseDto<MemberViewDto> members = memberApplicationViewService.getMembers(searchDto);
		List<MemberListDownDto> memberListDownDtoList = convert2TransferMarketDownDtoList(members);
		return ResultDtoFactory.toAck("获取成功", memberListDownDtoList);
	}

	private List<MemberListDownDto> convert2TransferMarketDownDtoList(
			DataTablesResponseDto<MemberViewDto> members) {
		List<MemberListDownDto> memberListDownDtoList = new ArrayList<MemberListDownDto>();
		MemberListDownDto memberListDownDto = null;
		if (members.getData() != null) {
			for (MemberViewDto memberViewDto : members.getData()) {
				memberListDownDto = new MemberListDownDto();
				memberListDownDtoList.add(memberListDownDto);
				memberListDownDto.setUserId(memberViewDto.getUserId()); // 用户编号
				memberListDownDto.setUserName(memberViewDto.getUserName());
				memberListDownDto.setAccountNo(memberViewDto.getAccountNo());
				memberListDownDto.setLastestTs(memberViewDto.getLastestTs());
				memberListDownDto.setAgentRegister(memberViewDto.getAgentRegister());
				try {
					memberListDownDto.setUserRole(memberViewDto.getUserRole().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setUserRole(StringUtils.EMPTY);
				}
				memberListDownDto.setName(memberViewDto.getName());
				try {
					memberListDownDto.setRegion(memberViewDto.getRegion().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setRegion(StringUtils.EMPTY);
				}
				try {
					memberListDownDto.setPersonJob(memberViewDto.getPersonJob().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setPersonJob(StringUtils.EMPTY);
				}
				try {
					memberListDownDto.setPersonEducation(memberViewDto.getPersonEducation().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setPersonEducation(StringUtils.EMPTY);
				}
				memberListDownDto.setAgent(memberViewDto.getAgent());
				memberListDownDto.setAgentName(memberViewDto.getAgentName());
				try {
					memberListDownDto.setInvestorStatus(memberViewDto.getInvestorStatus().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setInvestorStatus(StringUtils.EMPTY);
				}
				try {
					memberListDownDto.setFinanceStatus(memberViewDto.getFinanceStatus().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setFinanceStatus(StringUtils.EMPTY);
				}
				try {
					memberListDownDto.setOrgIndustry(memberViewDto.getOrgIndustry().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setOrgIndustry(StringUtils.EMPTY);
				}
				try {
					memberListDownDto.setOrgNature(memberViewDto.getOrgNature().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setOrgNature(StringUtils.EMPTY);
				}
				try {
					memberListDownDto.setOrgType(memberViewDto.getOrgType().getText());
				} catch (NullPointerException e) {
					memberListDownDto.setOrgType(StringUtils.EMPTY);
				}
			}
		}
		return memberListDownDtoList;
	}

	/**
	 * init MemberSearchDto from MemberListUpDto
	 * 
	 * @param memberListUpDto
	 * @return
	 */
	private MemberSearchDto initMemberSearchDto(MemberListUpDto memberListUpDto) {
		MemberSearchDto searchDto = new MemberSearchDto();
		// 模拟前端参数-字段排序列
		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
		searchDto.setSortedColumns(aiSortCol);
		searchDto.setSortDirections(asSortDir);
		searchDto.setDataProp(amDataProp);
		aiSortCol.add(0);
		asSortDir.add("desc");
		amDataProp.add("lastestTs");
		
		searchDto.setUserRole(EMemberType.NULL);
		searchDto.setUserName(StringUtils.EMPTY);
		searchDto.setUserType(EUserType.NULL);
		searchDto.setName(StringUtils.EMPTY);
		searchDto.setRegistTime(StringUtils.EMPTY);
		searchDto.setInvestorStatus(EApplicationStatus.NULL);
		searchDto.setFinanceStatus(EApplicationStatus.NULL);
		searchDto.setAgentRegister(StringUtils.EMPTY);
		searchDto.setAgentname(StringUtils.EMPTY);
		searchDto.setDisplayLength(10); // default 10 item per page
		searchDto.setDisplayStart(Integer.parseInt(memberListUpDto
				.getDisplayStart()));
		if (memberListUpDto.getDisplayLength() != 0) {
			searchDto.setDisplayLength(memberListUpDto.getDisplayLength());
		}
		if (!StringUtils.isEmpty(memberListUpDto.getUserRole()) &&
				!"NULL".equals(memberListUpDto.getUserRole())) {
			searchDto.setUserRole(EnumHelper.translate(EMemberType.class,
					memberListUpDto.getUserRole()));
		}
		if (!StringUtils.isEmpty(memberListUpDto.getUserName())) {
			searchDto.setUserName(memberListUpDto.getUserName());
		}
		if (!StringUtils.isEmpty(memberListUpDto.getUserType()) &&
				!"NULL".equals(memberListUpDto.getUserType())) {
			searchDto.setUserType(EnumHelper.translate(EUserType.class,
					memberListUpDto.getUserType()));
		}
		if (!StringUtils.isEmpty(memberListUpDto.getName())) {
			searchDto.setName(memberListUpDto.getName());
		}
		if (!StringUtils.isEmpty(memberListUpDto.getRegistTime())) {
			searchDto.setRegistTime(memberListUpDto.getRegistTime());
		}
		if (!StringUtils.isEmpty(memberListUpDto.getInvestorStatus()) &&
				!"NULL".equals(memberListUpDto.getInvestorStatus())) {
			searchDto.setInvestorStatus(EnumHelper.translate(EApplicationStatus.class,
					memberListUpDto.getInvestorStatus()));
		}
		if (!StringUtils.isEmpty(memberListUpDto.getFinanceStatus()) &&
				!"NULL".equals(memberListUpDto.getFinanceStatus())) {
			searchDto.setFinanceStatus(EnumHelper.translate(EApplicationStatus.class,
					memberListUpDto.getFinanceStatus()));
		}
		if (!StringUtils.isEmpty(memberListUpDto.getAgentRegister()) &&
				!"NULL".equals(memberListUpDto.getAgentRegister())) {
			searchDto.setAgentRegister(memberListUpDto.getAgentRegister());
		}
		if (!StringUtils.isEmpty(memberListUpDto.getAgentName())) {
			searchDto.setAgentname(memberListUpDto.getAgentName());
		}
		return searchDto;
	}
	
	/**
	 * 会员管理-会员信息维护列表-代注册, post
	 * 
	 * refer to /register/memberagent/submit
	 * 
	 */
	@RequestMapping(value = "/member/register", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_REGISTER_FOR_USER })
	public ResultDto submitMemberRegisterAgent (@OnValid({ SubmitPerson.class, SubmitOrg.class, SubmitInvest.class,
        SubmitFinance.class }) @RequestBody RegisterInfoDto registerDto, Model model) throws Exception {
		//基于基本信息修改页面，隐藏了银行信息填写，取消银行信息验证
/*		if (registerDto.getType() == EUserType.PERSON) {
            registerDto.getPerson().setUserId(registerDto.getUsername());
            validate(registerDto, new Class[] { BankExist.class, IdCardExist.class });
        } else {
            registerDto.getOrg().setUserId(registerDto.getUsername());
            validate(registerDto, new Class[] { BankExist.class, SubmitOrgCode.class });
        }
*/
		if (registerDto.getType() == EUserType.PERSON) {
            registerDto.getPerson().setUserId(registerDto.getUsername());
            validate(registerDto, new Class[] { IdCardExist.class });
        } else {
            registerDto.getOrg().setUserId(registerDto.getUsername());
            validate(registerDto, new Class[] { SubmitOrgCode.class });
        }
		
        String idCardNumber = registerDto.getType() == EUserType.PERSON ? registerDto.getPerson()
                .getPersonIdCardNumber() : registerDto.getOrg().getOrgLegalPersonIdCardNumber();

        registerDto.setPassword(idCardNumber.substring(idCardNumber.length() - 6));

        agentRegisterController.doRegisterAgent(registerDto);
        return ResultDtoFactory.toAck("注册成功");
	}	
	
	
	/**
	 * 会员管理-会员信息维护列表-代注册-授权服务中心信息获取, get
	 * 
	 * refer to /registeragent 
	 * 
	 * AgentRegisterController.java (addAllPageStateToModel)
	 * 
	 */
	@RequestMapping(value = "/member/register/info", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_REGISTER_FOR_USER })
	public ResultDto fetchRegisterAgentInfo () {
		MemberRegisterInitDownDto memberRegisterInitDownDto = new MemberRegisterInitDownDto();
        String agencyId = StringUtils.isNotBlank(securityContext.getCurrentOwnerId()) ? securityContext
                .getCurrentOwnerId() : securityContext.getCurrentUserId();
        Agency agencyPo = serviceCenterService.getAgencyById(agencyId);
        AgencyDto agency = ConverterService.convert(agencyPo, AgencyDto.class);
        ServiceCenterInfo serviceCenter = serviceCenterService.getServiceCenter(agencyId);
        if (serviceCenter != null && serviceCenter.getServiceCenterDesc() != null) {
        	agency.setDesc(serviceCenter.getServiceCenterDesc());	
		} else {
			agency.setDesc(agencyPo.getName());
		}
        memberRegisterInitDownDto.setDesc(agency.getDesc());
        memberRegisterInitDownDto.setFixedAgency(true);
        /** Display Service Center Info at edit mode. **/
        memberRegisterInitDownDto.setRegisterByAgentScenario(true);
        return ResultDtoFactory.toAck("获取成功", memberRegisterInitDownDto);
	}	
}
