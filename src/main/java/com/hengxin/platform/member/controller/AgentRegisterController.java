/*
 * Project Name: kmfex-platform
 * File Name: RegisterController.java
 * Class Name: RegisterController
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

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.hengxin.platform.common.dto.upstream.RegisterInfoDto;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.member.converter.FinancierApplicationConverter;
import com.hengxin.platform.member.converter.InvestorApplicationConverter;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.FinancierDto.SubmitFinance;
import com.hengxin.platform.member.dto.FinancierDto.SubmitOrgCode;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.InvestorDto.SubmitInvest;
import com.hengxin.platform.member.dto.OrganizationDto.ContactIdcardExist;
import com.hengxin.platform.member.dto.OrganizationDto.ContactMobileExist;
import com.hengxin.platform.member.dto.OrganizationDto.SubmitOrg;
import com.hengxin.platform.member.dto.PersonDto.IdCardExist;
import com.hengxin.platform.member.dto.PersonDto.PersionMobileExist;
import com.hengxin.platform.member.dto.PersonDto.SubmitPerson;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.ServiceCenterService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: AgentRegisterController
 * 
 * @author runchen
 */

@Controller
public class AgentRegisterController extends BaseController {

    @Autowired
    private transient UserService userService;

    @Autowired
    private transient ServiceCenterService serviceCenterService;

    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private transient SecurityContext securityContext;
    
    @Autowired
    private transient MemberMessageService messageService;
    
//    @Autowired
//    private transient MemberService memberService;

    @RequestMapping(value = UrlConstant.MEMBER_MANAGEMENT_REGISTER_FOR_USER_URL, 
    		method = RequestMethod.GET)
    @RequiresPermissions({ Permissions.MEMBER_REGISTER_FOR_USER})
    public String registeragent(HttpServletRequest request, Model model) {
        addAllPageStateToModel(model);
        this.addAllBasicOptionsToModel(model);
        this.addAllFinancierOptionsToModel(model);
        return "members/registeragent";
    } 
     
    @RequestMapping(value = "members/register/memberagent/submit", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_REGISTER_FOR_USER })
    public ResultDto submitMemberRegisterAgent(@OnValid({ SubmitPerson.class, SubmitOrg.class, SubmitInvest.class,
            SubmitFinance.class }) @RequestBody RegisterInfoDto registerDto, Model model) throws Exception {
    	
    	//基于基本信息修改页面，隐藏了银行信息填写，取消银行信息验证    	
/*        if (registerDto.getType() == EUserType.PERSON) {
            registerDto.getPerson().setUserId(registerDto.getUsername());
            validate(registerDto, new Class[] { BankExist.class, IdCardExist.class });
        } else {
            registerDto.getOrg().setUserId(registerDto.getUsername());
            validate(registerDto, new Class[] { BankExist.class, SubmitOrgCode.class });
        }
*/	
        if (registerDto.getType() == EUserType.PERSON) {
            registerDto.getPerson().setUserId(registerDto.getUsername());
            validate(registerDto, new Class[] { IdCardExist.class, PersionMobileExist.class });
        } else {
            registerDto.getOrg().setUserId(registerDto.getUsername());
            validate(registerDto, new Class[] { SubmitOrgCode.class, ContactMobileExist.class, ContactIdcardExist.class });
        }
    	
        String idCardNumber = registerDto.getType() == EUserType.PERSON ? registerDto.getPerson()
                .getPersonIdCardNumber() : registerDto.getOrg().getOrgLegalPersonIdCardNumber();

        registerDto.setPassword(idCardNumber.substring(idCardNumber.length() - 6));

        this.doRegisterAgent(registerDto);
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/search/agent"));
    }

    public void doRegisterAgent(RegisterInfoDto registerDto) throws Exception {

        User user = ConverterService.convert(registerDto, User.class);
        String currentUserId = securityContext.getCurrentUserId();
        String ownerId = securityContext.getCurrentOwnerId();
        String creator = StringUtils.isNotBlank(ownerId) ? ownerId : currentUserId;
        user.setCreator(creator);
        user.setLastMntOpid(currentUserId);

        MemberApplication member = null;
        if (registerDto.getType() == EUserType.PERSON) {
            member = ConverterService.convert(registerDto.getPerson(), MemberApplication.class);
            user.setMobile(registerDto.getPerson().getPersonMobile());
        } else {
            member = ConverterService.convert(registerDto.getOrg(), MemberApplication.class);
            user.setMobile(registerDto.getOrg().getContactMobile());
        }
        member.setStatus(EApplicationStatus.PENDDING);
        member.setUserType(registerDto.getType());
        member.setCreator(creator);
        member.setLastMntOpid(currentUserId);

        InvestorApplication investor = null;
        if (registerDto.getInvestor() != null) {
            investor = ConverterService.convert(registerDto.getInvestor(), InvestorApplication.class,
                    InvestorApplicationConverter.class);
            investor.setCreator(creator);
            investor.setLastMntOpid(currentUserId);
            investor.setAuthCenterId(creator);
            investor.setStatus(EApplicationStatus.PENDDING);
            if (registerDto.getInvestor().getAgent() == null || registerDto.getInvestor().getAgent().isEmpty()) {
            	investor.setAgent(securityContext.getCurrentUserName());
//            	Agency agency = memberService.getAgencyByUserId(securityContext.getCurrentUserId());
//            	if (agency != null && agency.getOrgLegalPerson() != null) {
//            		investor.setAgent(agency.getOrgLegalPerson());
//				}
			}
        }

        FinancierApplication financier = null;
        if (registerDto.getFinancier() != null) {
            financier = ConverterService.convert(registerDto.getFinancier(), FinancierApplication.class,
                    FinancierApplicationConverter.class);
            financier.setCreator(creator);
            financier.setLastMntOpid(currentUserId);
            financier.setAuthCenterId(creator);
            financier.setStatus(EApplicationStatus.PENDDING);
            if (registerDto.getFinancier().getAgent() == null || registerDto.getFinancier().getAgent().isEmpty()) {
            	financier.setAgent(securityContext.getCurrentUserName());
//            	Agency agency = memberService.getAgencyByUserId(securityContext.getCurrentUserId());
//            	if (agency != null && agency.getOrgLegalPerson() != null) {
//            		financier.setAgent(agency.getOrgLegalPerson());
//				}
			}
        }

        userService.createUserByAgency(user, member, investor, financier);
        
        /** Send message to Client Service Dept. **/
        String messageKey = "member.audit.message";
    	String content = registerDto.getUsername();
    	String content2 = EUserType.PERSON == registerDto.getType() ? "#indiv" : "#org";
    	List<EBizRole> roles = new ArrayList<EBizRole>();
    	roles.add(EBizRole.PLATFORM_CLIENT_SERVICE_MANAGER);
    	roles.add(EBizRole.PLATFORM_MEMBER_MEMBERINFO_ASSOCIATE);
    	messageService.sendMessage(EMessageType.TODO, messageKey, roles, content, content2);
        
    }

    /** Private methods */
    private void addAllPageStateToModel(Model model) {
        InvestorDto investor = new InvestorDto();
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

        investor.setAuthCenter(agency);
//        investor.setAgent(agency.getOrgLegalPerson());
        investor.setAgent(securityContext.getCurrentUserName());
        investor.setFixedAgency(true);
        /** Display Service Center Info at edit mode. **/
        investor.setRegisterByAgentScenario(true);
        /** Fixed AgentName field when current user is not service center admin. **/
        /** Cancel AgentName field fixed status due to requirement change. **/
//        if (SecurityContext.getInstance().isAuthServiceCenter()) {
//        	if (SecurityContext.getInstance().isAuthServiceCenterAgent() || SecurityContext.getInstance().isAuthServiceCenterGeneral()) {
//        		investor.getFixedStatus().setAgentNameFixed(true);
//			}
//        }
        
        model.addAttribute("investorInfo", investor);

        FinancierDto financier = new FinancierDto();
        financier.setShowServiceCenter(true);
        financier.setFixedAgency(true);
        financier.setAuthCenter(agency);
//        financier.setAgent(agency.getOrgLegalPerson());
        financier.setAgent(securityContext.getCurrentUserName());
        model.addAttribute("financierinfo", financier);
        // set prefix attributes
        model.addAttribute(ApplicationConstant.VM_HASPREFIX, ApplicationConstant.VM_HASPREFIX);
        model.addAttribute(ApplicationConstant.VM_PREFIX_INVESTOR, "investor.");
        model.addAttribute(ApplicationConstant.VM_PREFIX_FINANCIER, "financier.");
        model.addAttribute(ApplicationConstant.VM_PREFIX_PERSON, "person.");
        model.addAttribute(ApplicationConstant.VM_PREFIX_ORG, "org.");
    }

    private void addAllBasicOptionsToModel(Model model) {
        model.addAttribute("genderList", getStaticOptions(EGender.class, false));
        model.addAttribute("bankList", SystemDictUtil.getRootDictList(EOptionCategory.BANK, true));
        model.addAttribute("provinceList", SystemDictUtil.getRootDictList(EOptionCategory.REGION, true));
        model.addAttribute("educationList", SystemDictUtil.getRootDictList(EOptionCategory.EDUCATION, true));
        model.addAttribute("industryList", SystemDictUtil.getRootDictList(EOptionCategory.ORG_INDUSTRY, true));
        model.addAttribute("natureList", SystemDictUtil.getRootDictList(EOptionCategory.ORG_NATURE, true));
        model.addAttribute("orgTypeList", SystemDictUtil.getRootDictList(EOptionCategory.ORG_TYPE, true));
        model.addAttribute("jobList", getDynamicOptions(EOptionCategory.JOB, true));

//        List<ServiceCenterDto> serviceCenterDtoList = new ArrayList<ServiceCenterDto>();
//        List<ServiceCenterInfo> allServiceCenters = serviceCenterService.getAllServiceCenters();
//        serviceCenterDtoList.add(new ServiceCenterDto("", ""));
//        for (ServiceCenterInfo domain : allServiceCenters) {
//            serviceCenterDtoList.add(ConverterService.convert(domain, ServiceCenterDto.class));
//        }
//        model.addAttribute("serviceCenterList", serviceCenterDtoList);
    }

    private void addAllFinancierOptionsToModel(Model model) {
        model.addAttribute("creditHistList", getDynamicOptions(EOptionCategory.CREDIT_HIST, true));
        model.addAttribute("residenceList", getDynamicOptions(EOptionCategory.RESIDENCE, true));
        model.addAttribute("familyStatustList", getDynamicOptions(EOptionCategory.FAMILY_STATUS, true));
        model.addAttribute("orgAgeList", getDynamicOptions(EOptionCategory.ORG_AGE, true));
        model.addAttribute("totalAssetsList", getDynamicOptions(EOptionCategory.TOTAL_ASSETS, true));
        model.addAttribute("debtRatioList", getDynamicOptions(EOptionCategory.DEBT_RATIO, true));
        model.addAttribute("incomeDebtRatioList", getDynamicOptions(EOptionCategory.INCOME_DEBT_RATIO, true));
        model.addAttribute("concentraList", getDynamicOptions(EOptionCategory.CONCENTRATION_RATIO, true));
        model.addAttribute("profitRatioList", getDynamicOptions(EOptionCategory.PROFIT_RATIO, true));
        model.addAttribute("growthList", getDynamicOptions(EOptionCategory.REVENUE_GROWTH, true));
        model.addAttribute("customerDistList", getDynamicOptions(EOptionCategory.CUSTOMER_DIST, true));
        model.addAttribute("socialStatusList", getDynamicOptions(EOptionCategory.SOCIAL_STATUS, true));
    }

    /* getter && setter */
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ServiceCenterService getServiceCenterService() {
		return serviceCenterService;
	}

	public void setServiceCenterService(ServiceCenterService serviceCenterService) {
		this.serviceCenterService = serviceCenterService;
	}

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public MemberMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MemberMessageService messageService) {
		this.messageService = messageService;
	}

}
