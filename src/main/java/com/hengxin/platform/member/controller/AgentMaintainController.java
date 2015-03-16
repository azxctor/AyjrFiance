/*
 * Project Name: kmfex-platform
 * File Name: AgentMaintainController.java
 * Class Name: AgentMaintainController
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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EFormMode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.member.converter.ApplicationConverter;
import com.hengxin.platform.member.converter.FinancierApplicationConverter;
import com.hengxin.platform.member.converter.FinancierInfoConverter;
import com.hengxin.platform.member.converter.InvestorApplicationConverter;
import com.hengxin.platform.member.converter.InvestorInfoConverter;
import com.hengxin.platform.member.converter.MemberInfoConverter;
import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.domain.FinancierInfo;
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.FinancierDto.SubmitFinance;
import com.hengxin.platform.member.dto.FinancierDto.SubmitOrgCode;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.InvestorDto.SubmitInvest;
import com.hengxin.platform.member.dto.MemberDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.OrganizationDto.ContactIdcardExist;
import com.hengxin.platform.member.dto.OrganizationDto.SubmitOrg;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.member.dto.PersonDto.IdCardExist;
import com.hengxin.platform.member.dto.PersonDto.SubmitPerson;
import com.hengxin.platform.member.dto.downstream.AccountDto;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.dto.upstream.ServiceCenterDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.member.service.ServiceCenterService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: AgentMaintainController.
 * 
 * @author runchen
 */

@Controller
@RequestMapping(value = "/members/details")
public class AgentMaintainController extends BaseController {

    @Autowired
    private transient AcctService acctService;

    @Autowired
    private transient MemberService memberService;

    @Autowired
    private transient ServiceCenterService serviceCenterService;

    @Autowired
    private transient SecurityContext securityContext;
    
    @Autowired
    private MemberMessageService messageService;
    
    @Autowired
    private UserService userService;
    
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE })
    public String memberDetails(@PathVariable("userId") String userId, HttpServletRequest request,
            HttpServletResponse rep, Model model) {
        MemberDto basicInfo = getBasicInfo(userId);
        // rep.setHeader("status", "206");
    	/** Set Edit mode to hide application form fields. **/
    	basicInfo.setFormMode(EFormMode.EDIT);
        model.addAttribute("member", basicInfo);
        AgencyDto agency = getCurrentAgency();
        InvestorDto investor = getInvestorInfo(userId);
        investor.setAuthCenter(agency);
        FinancierDto financier = getFinancierInfo(userId);
        financier.setAuthCenter(agency);
        model.addAttribute("investorInfo", investor);
        model.addAttribute("financierInfo", financier);
        addAllBasicOptionsToModel(model);
        addAllFinancierOptionsToModel(model);

        return "members/detail_userinfo";
    }

    /**
     * Ajax methods.
     * @param userId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{userId}/basicinfo", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE })
    public MemberDto basicDetail(@PathVariable("userId") String userId, HttpServletRequest request, Model model) {
    	return getBasicInfo(userId);
    }

    @RequestMapping(value = "/{userId}/basicinfo/person", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE })
    public ResultDto submitPersonDetail(@OnValid(SubmitPerson.class) @RequestBody PersonDto person, @PathVariable("userId") String userId,
            HttpServletRequest request, Model model) {

        saveBasicInfo(person, userId, EUserType.PERSON);
        return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE);
    }

    @RequestMapping(value = "/{userId}/basicinfo/org", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE })
    public ResultDto submitOrgDetail(@OnValid(SubmitOrg.class) @RequestBody OrganizationDto org,
            @PathVariable("userId") String userId, HttpServletRequest request, Model model) {

        saveBasicInfo(org, userId, EUserType.ORGANIZATION);
        return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE);
    }

    @RequestMapping(value = "/{userId}/investorinfo", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INVST_FIN_VIEW })
    public InvestorDto investorDetail(@PathVariable("userId") String userId, HttpServletRequest request, Model model) {
        InvestorDto investorInfo = getInvestorInfo(userId);
        investorInfo.setAuthCenter(getCurrentAgency());
        investorInfo.setFixedAgency(true);
        return investorInfo;
    }

    @RequestMapping(value = "/{userId}/investorinfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE })
    public ResultDto submitInvestorDetail(@OnValid(SubmitInvest.class) @RequestBody InvestorDto investor,
            @PathVariable("userId") String userId, HttpServletRequest request, Model model)  {
        investor.setUserId(userId);
        investor.setStatus(EApplicationStatus.PENDDING);
        investor.setLastMntOpid(securityContext.getCurrentUserId());
        InvestorApplication investApplication = memberService.getLatestInvestorWithoutReject(userId);
        ActionType actionType = ActionType.AGENTREGISTER;
        if (investApplication != null) {
            investor.setCreator(investApplication.getCreator());
            investor.setAuthCenter(getCurrentAgency());
            investor.setAgent(investApplication.getAgent());
            investor.setApplicationImg1(investApplication.getApplicationImg1());
            investor.setApplicationImg2(investApplication.getApplicationImg2());
            actionType = ActionType.MAINTAIN;
        } else {
        	/** After register by agent, at first time submit investor info by Maintenance function.... **/
        	String agencyId = StringUtils.isNotBlank(securityContext.getCurrentOwnerId()) ? securityContext
                    .getCurrentOwnerId() : securityContext.getCurrentUserId();
            investor.setCreator(agencyId);
            investor.setAuthCenter(getCurrentAgency());
            if (investor.getAgent() == null || investor.getAgent().isEmpty()) {
        		investor.setAgent(securityContext.getCurrentUserName());
			}
        }
        InvestorApplication temp = ConverterService.convert(investor, InvestorApplication.class, InvestorApplicationConverter.class);
        memberService.saveInvestorInfo(temp, actionType, investApplication);
        
        /** Send message to Client Service Dept. **/
    	User user = userService.getUserByUserId(userId);
    	this.processTodo(user.getUsername(), user.getType());
        return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE);
    }

    @RequestMapping(value = "/{userId}/financierinfo", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INVST_FIN_VIEW })
    public FinancierDto financierDetail(@PathVariable("userId") String userId, HttpServletRequest request, Model model) {
        FinancierDto financierInfo = getFinancierInfo(userId);
        financierInfo.setAuthCenter(getCurrentAgency());
        financierInfo.setFixedAgency(true);
        return financierInfo;
    }

    @RequestMapping(value = "/{userId}/financierinfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE })
    public ResultDto submitFinancierDetail(@OnValid(SubmitFinance.class) @RequestBody FinancierDto financier,
            @PathVariable("userId") String userId, HttpServletRequest request, Model model) {
    	User user = userService.getUserByUserId(userId);
    	if (EUserType.ORGANIZATION == user.getType()) {
    		validate(financier, new Class[] { SubmitOrgCode.class });
    	}
    	financier.setUserId(userId);
        financier.setStatus(EApplicationStatus.PENDDING);
        financier.setLastMntOpid(securityContext.getCurrentUserId());
        FinancierApplication financeApplication = memberService.getLatestFinancierWithoutReject(userId);
        ActionType actionType = ActionType.AGENTREGISTER;
        if (financeApplication != null) {
            financier.setCreator(financeApplication.getCreator());
            financier.setAuthCenter(getCurrentAgency());
            financier.setAgent(financeApplication.getAgent());
            actionType = ActionType.MAINTAIN;
        } else {
        	/** After register by agent, at first time submit investor info by Maintenance function.... **/
        	String agencyId = StringUtils.isNotBlank(securityContext.getCurrentOwnerId()) ? securityContext
                    .getCurrentOwnerId() : securityContext.getCurrentUserId();
        	financier.setCreator(agencyId);
            financier.setAuthCenter(getCurrentAgency());
            /** correct agent. **/
            if (financier.getAgent() == null || financier.getAgent().isEmpty()) {
        		financier.setAgent(securityContext.getCurrentUserName());
			}
        }
        FinancierApplication temp = ConverterService.convert(financier, FinancierApplication.class, FinancierApplicationConverter.class); 
        memberService.saveFinancierInfo(temp, actionType, financeApplication);
        /** Send message to Client Service Dept. **/
    	this.processTodo(user.getUsername(), user.getType());
        return ResultDtoFactory.toAck(ApplicationConstant.SUBMIT_SUCCESS_MESSAGE);
    }

    /** Private methods */
    private void saveBasicInfo(MemberDto member, String userId, EUserType type) {
        member.setUserId(userId);
        member.setUserType(type);
      //基于基本信息修改页面，隐藏了银行信息填写，取消银行信息验证
/*        if (type == EUserType.PERSON) {
            validate(member, new Class[] { BankExist.class, IdCardExist.class });
        } else {
            validate(member, new Class[] { BankExist.class });
        }
*/
        if (type == EUserType.PERSON) {
            validate(member, new Class[] { IdCardExist.class});
        }else{
            validate(member, new Class[] { ContactIdcardExist.class });
        }
//        else {
//            validate(member, new Class[] { });
//        }
            
        boolean isFirstReject = false;
        MemberApplication memberApplication = memberService.getMemberWithLatestStatus(userId);
        if (EApplicationStatus.REJECT == memberApplication.getStatus()) {
        	Member oldMember = memberService.getMemberByUserId(userId);
        	if (oldMember == null) {
				isFirstReject = true;
			}
        }
        if (!isFirstReject) {
            // 银行信息不能修改，全部set成原信息
            member.setBankAccount(memberApplication.getBankAccount());
            member.setBankAccountName(memberApplication.getBankAccountName());
            member.setBankShortName(SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.BANK,
                    memberApplication.getBankShortName()));
            member.setBankFullName(memberApplication.getBankFullName());
            member.setBankCardFrontImg(memberApplication.getBankCardFrontImg());
            DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION,
                    memberApplication.getBankOpenProvince());
            member.setBankOpenProvince(option);
            DynamicOption option2 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION,
                    memberApplication.getBankOpenCity());
            member.setBankOpenCity(option2);
            member.setBankOpenBranch(memberApplication.getBankOpenBranch());
            member.setCreator(memberApplication.getCreator());
        }
        //TODO judge role, check whether it need to override original info.

        member.setStatus(EApplicationStatus.PENDDING);
        //TODO what is it?
        User user = new User();
        user.setUserId(userId);
        user.setType(type);
        member.setCreator(securityContext.getCurrentUserId());
        member.setLastMntOpid(securityContext.getCurrentUserId());
        memberService.saveBasicInfo(
                ConverterService.convert(member, MemberApplication.class, ApplicationConverter.class), user,
                ActionType.MAINTAIN, memberApplication);
        
        /** Send message to Client Service Dept. **/
        User user2 = userService.getUserByUserId(userId);
    	this.processTodo(user2.getUsername(), type);
    }

    private MemberDto getBasicInfo(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            MemberApplication memberPo = memberService.getMemberWithLatestStatus(userId);
            if (memberPo != null) {
                MemberDto memberDto = new MemberDto();
                EApplicationStatus status = memberPo.getStatus();
                Class<? extends MemberDto> clazz = memberPo.getUserType() == EUserType.PERSON ? PersonDto.class
                        : OrganizationDto.class;
                // 拒绝状态下，有原信息取原信息
                boolean isFirstReject = false;
                Member oldMember = memberService.getMemberByUserId(userId);
                if (status == EApplicationStatus.REJECT) {
                    if (oldMember != null) {
                        memberDto = ConverterService.convert(oldMember, clazz, MemberInfoConverter.class);
                        memberDto.setUserType(memberPo.getUserType());
                        // 标示下是否是第一次被拒绝
                        memberDto.setOldMember(new MemberDto());
                    } else {
                        memberDto = ConverterService.convert(memberPo, clazz);
                        memberDto.setFixedInput(false);
                        isFirstReject = true;
                    }
                    memberDto.setStatus(memberPo.getStatus());
                    memberDto.setRejectLastTime(true);
                    memberDto.setComments(memberPo.getComments());
                } else {
                    memberDto = ConverterService.convert(memberPo, clazz);
                }
                /** 尚未审核通过. **/
                if (oldMember == null) {
                	memberDto.setInCanViewPage(true);
				}

                DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, memberPo.getBankOpenProvince());
                memberDto.setBankOpenProvince(option);
                DynamicOption option2 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, memberPo.getBankOpenCity());
                memberDto.setBankOpenCity(option2);
                
                AcctPo acctPo = acctService.getAcctByUserId(userId);
                if (acctPo != null) {
                    memberDto.setAccount(ConverterService.convert(acctPo, AccountDto.class));
                }

                boolean finalStatus = status == EApplicationStatus.ACCEPT || status == EApplicationStatus.ACTIVE
                        || status == EApplicationStatus.REJECT;
                memberDto.setCanEdit(securityContext.canEditMemberBasicInfo() && finalStatus);
                /** process mask & lock fields **/
                if (!isFirstReject) {
                	if (SecurityContext.getInstance().cannotViewRealIdCardNo(userId, false)) {
                    	memberDto.getFixedStatus().setIdCardFixed(true);
                	}
                	if (SecurityContext.getInstance().cannotViewRealBankCardNo(userId, false)) {
                    	memberDto.getFixedStatus().setBankCardFixed(true);
                	}
                	if (SecurityContext.getInstance().cannotViewRealPhoneNo(userId, false)) {
                    	memberDto.getFixedStatus().setMobileFixed(true);
                	}
				}
                return memberDto;
            }
        }
        return new MemberDto();
    }

    private InvestorDto getInvestorInfo(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            InvestorApplication investor = memberService.getInvestorWithLatestStatus(userId);
            if (investor != null) {
            	boolean isFirstReject = false;
                InvestorDto investorDto = new InvestorDto();
                EApplicationStatus status = investor.getStatus();
                // 拒绝状态下，有原信息取原信息
                if (status == EApplicationStatus.REJECT) {
                    InvestorInfo oldInvestor = memberService.getInvestorById(userId);
                    if (oldInvestor != null) {
                        investorDto = ConverterService.convert(oldInvestor, InvestorDto.class,
                                InvestorInfoConverter.class);
                    } else {
                        investorDto = ConverterService.convert(investor, InvestorDto.class,
                                InvestorApplicationConverter.class);
                        isFirstReject = true;
                    }
                    investorDto.setComments(investor.getComments());
                    investorDto.setRejectLastTime(true);
                    investorDto.setStatus(status);
                } else {
                    investorDto = ConverterService.convert(investor, InvestorDto.class,
                            InvestorApplicationConverter.class);
                }
                investorDto.setRegisterByAgentScenario(true);
                boolean finalStatus = status == EApplicationStatus.ACCEPT || status == EApplicationStatus.ACTIVE
                        || status == EApplicationStatus.REJECT;
                investorDto.setCanEdit(securityContext.canEditMemberInvestorInfo() && finalStatus);
                displayApplicationForm(userId, investorDto, isFirstReject);
                return investorDto;
            }
        }
        InvestorDto investorDto = new InvestorDto();
        investorDto.setRegisterByAgentScenario(true);
        investorDto.setCanEdit(securityContext.canEditMemberInvestorInfo());
//        displayApplicationForm(userId, investorDto);
        return investorDto;
    }

    private FinancierDto getFinancierInfo(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            FinancierApplication financier = memberService.getFinancierWithLatestStatus(userId);
            if (financier != null) {
            	boolean isFirstReject = false;
                FinancierDto financierDto = new FinancierDto();
                EApplicationStatus status = financier.getStatus();
                // 拒绝状态下，有原信息取原信息
                if (status == EApplicationStatus.REJECT) {
                    FinancierInfo oldFinancier = memberService.getFinancierById(userId);
                    if (oldFinancier != null) {
                        financierDto = ConverterService.convert(oldFinancier, FinancierDto.class,
                                FinancierInfoConverter.class);
                    } else {
                        financierDto = ConverterService.convert(financier, FinancierDto.class,
                                FinancierApplicationConverter.class);
                        isFirstReject = true;
                    }
                    financierDto.setComments(financier.getComments());
                    financierDto.setRejectLastTime(true);
                    financierDto.setStatus(status);
                } else {
                    financierDto = ConverterService.convert(financier, FinancierDto.class,
                            FinancierApplicationConverter.class);
                }

                boolean finalStatus = status == EApplicationStatus.ACCEPT || status == EApplicationStatus.ACTIVE
                        || status == EApplicationStatus.REJECT;
                financierDto.setCanEdit(securityContext.canEditMemberInvestorInfo() && finalStatus);
                displayApplicationForm(userId, financierDto, isFirstReject);
                return financierDto;
            }
        }
        FinancierDto financierDto = new FinancierDto();
        financierDto.setCanEdit(securityContext.canEditMemberInvestorInfo());
        return financierDto;
    }

    /**
     * check whether display application form or not for auth service center. 
     * @param userId
     * @param financierDto
     */
	private void displayApplicationForm(String userId, InvestorDto investorDto, boolean isFirstReject) {
		if (SecurityContext.getInstance().isAuthServiceCenter()) {
			/** display App form if it has not been approved yet first time. **/
			InvestorInfo info = memberService.getInvestorById(userId);
            if (info == null) {
            	investorDto.setInCanViewPage(true);	
			}
    		if (!SecurityContext.getInstance().cannotViewRealName(userId, isFirstReject)
    				&& !SecurityContext.getInstance().cannotViewRealIdCardNo(userId, isFirstReject)
    				&& !SecurityContext.getInstance().cannotViewRealPhoneNo(userId, isFirstReject)
    				&& !SecurityContext.getInstance().cannotViewRealBankCardNo(userId, isFirstReject)) {
    			investorDto.getFixedStatus().setApplicationFormFixed(false);	
			} else {
				investorDto.getFixedStatus().setApplicationFormFixed(true);
			}
    		/** IF NOT isFirstReject, Allow all roles belong to service center to update agent name(介绍人). **/
    		if (!isFirstReject) {
    			if (SecurityContext.getInstance().isAuthServiceCenterAgent() || SecurityContext.getInstance().isAuthServiceCenterGeneral()) {
	    			investorDto.getFixedStatus().setAgentNameFixed(true);
				}
			}
    		/**能否编辑介绍人字段**/
    		if(securityContext.canEditAgentName()){
    			// 能编辑
    			investorDto.getFixedStatus().setAgentNameFixed(false);
    		} else {
    			// 不能编辑
    			investorDto.getFixedStatus().setAgentNameFixed(true);
    		}
    	}
	}
	
    /**
     * check whether display application form or not for auth service center. 
     * @param userId
     * @param financierDto
     */
	private void displayApplicationForm(String userId, FinancierDto financierDto, boolean isFirstReject) {
		if (SecurityContext.getInstance().isAuthServiceCenter()) {
			/** display App form if it has not been approved yet first time. **/
            FinancierInfo info = memberService.getFinancierById(userId);
            if (info == null) {
            	financierDto.setInCanViewPage(true);
			}
    		if (!SecurityContext.getInstance().cannotViewRealName(userId, isFirstReject)
    				&& !SecurityContext.getInstance().cannotViewRealIdCardNo(userId, isFirstReject)
    				&& !SecurityContext.getInstance().cannotViewRealPhoneNo(userId, isFirstReject)
    				&& !SecurityContext.getInstance().cannotViewRealBankCardNo(userId, isFirstReject)) {
    			financierDto.getFixedStatus().setApplicationFormFixed(false);	
			} else {
				financierDto.getFixedStatus().setApplicationFormFixed(true);
			}
    	}
	}

    private AgencyDto getCurrentAgency() {
        String agencyId = StringUtils.isNotBlank(securityContext.getCurrentOwnerId()) ? securityContext
                .getCurrentOwnerId() : securityContext.getCurrentUserId();
        AgencyDto agency = ConverterService.convert(serviceCenterService.getAgencyById(agencyId), AgencyDto.class);
        /** add service center desc. **/
        ServiceCenterInfo serviceCenter = serviceCenterService.getServiceCenter(agencyId);
        if (serviceCenter != null && serviceCenter.getServiceCenterDesc() != null) {
        	agency.setDesc(serviceCenter.getServiceCenterDesc());	
		} else {
			agency.setDesc(agency.getName());
		}
        return agency;
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

        addSerivceCenterListToMode(model);
    }

    private void addSerivceCenterListToMode(Model model) {
        List<ServiceCenterDto> serviceCenterDtoList = new ArrayList<ServiceCenterDto>();
        List<ServiceCenterInfo> allServiceCenters = serviceCenterService.getAllServiceCenters();
        for (ServiceCenterInfo domain : allServiceCenters) {
            serviceCenterDtoList.add(ConverterService.convert(domain, ServiceCenterDto.class));
        }
        model.addAttribute("serviceCenterList", serviceCenterDtoList);
        model.addAttribute("investorLevelList", getDynamicOptions(EOptionCategory.INVESTOR_LEVEL, true));
    }

    private void addAllFinancierOptionsToModel(Model model) {
        addSerivceCenterListToMode(model);
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
        model.addAttribute("financierLevelList", getDynamicOptions(EOptionCategory.FINANCIER_LEVEL, true));
    }

    private void processTodo(String content, EUserType type) {
        /** Send message to Client Service Dept. **/
    	String messageKey = "member.audit.message";
    	String content2 = EUserType.PERSON == type ? "#indiv" : "#org";
    	List<EBizRole> roles = new ArrayList<EBizRole>();
    	roles.add(EBizRole.PLATFORM_CLIENT_SERVICE_MANAGER);
    	roles.add(EBizRole.PLATFORM_MEMBER_MEMBERINFO_ASSOCIATE);
    	messageService.sendMessage(EMessageType.TODO, messageKey, roles, content, content2);
    }
    
}
