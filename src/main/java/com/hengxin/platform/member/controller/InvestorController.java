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
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.hengxin.platform.member.converter.InvestorApplicationConverter;
import com.hengxin.platform.member.converter.InvestorInfoConverter;
import com.hengxin.platform.member.converter.MemberInfoConverter;
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.MemberDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.member.dto.InvestorDto.SubmitInvest;
import com.hengxin.platform.member.dto.downstream.AccountDto;
import com.hengxin.platform.member.dto.upstream.ServiceCenterDto;
import com.hengxin.platform.member.dto.upstream.SkinnyServiceCenterDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
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
 * Class Name: MemberDetailController
 *
 * @author runchen
 */

@Controller
public class InvestorController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestorController.class);

    @Autowired
    private transient UserService userService;
    
    @Autowired
    private transient MemberService memberService;

    @Autowired
    private transient ServiceCenterService serviceCenterService;

    @Autowired
    private transient AcctService acctService;

    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private transient SecurityContext securityContext;
    
    @Autowired
    private MemberMessageService messageService;

    /** Get page methods */

    @RequestMapping(value = UrlConstant.MEMBER_PERM_APPLY_INVERSTER_URL, method = RequestMethod.GET)
    @RequiresPermissions({ Permissions.MEMBER_PERM_APPLY_INVERSTER_VIEW })
    public String investorInfoFill(HttpServletRequest request, HttpSession session, Model model) {

        String userId = securityContext.getCurrentUserId();
        String mode = request.getParameter("mode");
        InvestorApplication investor = memberService.getInvestorWithLatestStatus(userId);
        InvestorDto investorDto = new InvestorDto();
        if (investor != null) {
        	// 拒绝状态下取原信息
        	if(investor.getAgentName()!=null){
        		investorDto.setAgentName(investor.getAgentName());// 介绍人
        	}
            if (investor.getStatus() == EApplicationStatus.REJECT) {
                InvestorInfo oldInvestor = memberService.getInvestorById(userId);
                if (oldInvestor != null) {
                    investorDto = ConverterService.convert(oldInvestor, InvestorDto.class, InvestorInfoConverter.class);
                    investorDto.setStatus(EApplicationStatus.ACCEPT);
                } else {
                    investorDto = ConverterService.convert(investor, InvestorDto.class,
                            InvestorApplicationConverter.class);
                    investorDto.setStatus(EApplicationStatus.UNCOMMITTED);
                }
                investorDto.setComments(investor.getComments());
                investorDto.setRejectLastTime(true);
            } else {
                investorDto = ConverterService.convert(investor, InvestorDto.class, InvestorApplicationConverter.class);
            }

            // 修改的pending状态需要看到原信息
            if (investor.getStatus() == EApplicationStatus.PENDDING) {
                InvestorInfo oldInvestor = memberService.getInvestorById(userId);
                if (oldInvestor != null) {
                    investorDto.setOldInvestor(ConverterService.convert(oldInvestor, InvestorDto.class,
                            InvestorInfoConverter.class));
                }
            }

        }

        EApplicationStatus status = investorDto.getStatus();
        if (StringUtils.isNotBlank(mode)) {
            // 如果有mode参数，页面显示根据mode
            EFormMode formMode = getFormMode(mode);
            investorDto.setFormMode(formMode);
            investorDto.setFixedAgency(formMode != EFormMode.EDIT || !securityContext.canEditAgencyAndServerCenter());
            investorDto.setFixedLevel(formMode != EFormMode.EDIT || !securityContext.canEditInvestorLevel());
        } else {
            // 如果没有mode参数，除保存未提交状态其他状态全部为View
            if (status == EApplicationStatus.NULL || status == EApplicationStatus.UNCOMMITTED) {
                investorDto.setFormMode(EFormMode.NULL);
                investorDto.setFixedAgency(false);
            }
        }

        // 如果融资的授权服务中心已有，投资不可以更改
        FinancierDto financier = ConverterService.convert(
                memberService.getFinancierById(securityContext.getCurrentUserId()), FinancierDto.class);
        if (financier != null && financier.getAuthCenter() != null) {
            investorDto.setFixedAgency(true);
            investorDto.setAuthCenter(financier.getAuthCenter());
            investorDto.setAgent(financier.getAgent());
        }

        initMemberInfoForInvestor(investorDto, model);
        addAllInvestorOptionsToModel(model);
        boolean finalStatus = status == EApplicationStatus.ACCEPT
        		|| status == EApplicationStatus.ACTIVE
                || status == EApplicationStatus.REJECT;
        /** When individual user, it does not need to display edit button due to there is not any filed can be updated.. **/
        if (finalStatus) {
        	User user = this.userService.getUserByUserId(userId);
        	if (EUserType.PERSON == user.getType()) {
        		finalStatus = true;	
			}
		}
        investorDto.setCanEdit(securityContext.canEditMemberInvestorInfo() && finalStatus);

        model.addAttribute("investorInfo", investorDto);
        return "members/investorinfo";
    }

    /**
     * Submit methods. Used for apply and update info. 
     * @param investor
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "members/investorinfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MEMBER_PERM_APPLY_INVERSTER, Permissions.MEMBER_PROFILE_INVESTER_UPDATE }, logical = Logical.OR)
    public ResultDto doInvestorInfoFill(@OnValid(SubmitInvest.class) @RequestBody InvestorDto investor,
            HttpServletRequest request, Model model) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInvestorInfoFill() start: ");
        }

        saveInvestorInfo(investor, EApplicationStatus.PENDDING);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInvestorInfoFill() succeed: ");
        }

        String content2 = "";
        MemberApplication member = memberService.getMemberWithLatestStatus(investor.getUserId());
        if (member  != null) {
        	content2 = EUserType.PERSON == member.getUserType() ? "#indiv" : "#org";	
		}

        /** Send message to Client Service Dept. **/
    	String messageKey = "member.audit.message";
    	String content = securityContext.getCurrentUserName();
    	List<EBizRole> roles = new ArrayList<EBizRole>();
    	roles.add(EBizRole.PLATFORM_CLIENT_SERVICE_MANAGER);
    	roles.add(EBizRole.PLATFORM_MEMBER_MEMBERINFO_ASSOCIATE);
    	messageService.sendMessage(EMessageType.TODO, messageKey, roles, content, content2);
        
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/members/investorinfo"));
    }

    /** Save methods */
    @RequestMapping(value = "members/investorinfo/save", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PERM_APPLY_INVERSTER })
    public ResultDto saveInvestorInfoFill(@OnValid @RequestBody InvestorDto investor, HttpServletRequest request,
            Model model) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("saveInvestorInfoFill() start: ");
        }
        saveInvestorInfo(investor, EApplicationStatus.UNCOMMITTED);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("saveInvestorInfoFill() succeed: ");
        }

        return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
    }

    /**
     * Search service centers.
     * @param name
     */
    @Deprecated
    @RequestMapping(value = "search/getServiceCenters/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<SkinnyServiceCenterDto> getServiceCenters(@RequestBody @PathVariable(value = "name") String name) {
    	if (name == null) {
			return Collections.emptyList();
		}
        List<ServiceCenterInfo> list = this.serviceCenterService.getServiceCenters(name.trim());
        List<SkinnyServiceCenterDto> sc = new ArrayList<SkinnyServiceCenterDto>();
        for (ServiceCenterInfo s : list) {
        	SkinnyServiceCenterDto temp = new SkinnyServiceCenterDto();
        	temp.setId(s.getAgencyApplication().getUserId());
        	temp.setShortName(s.getAgencyApplication().getName());
        	temp.setDesc(s.getServiceCenterDesc());
        	sc.add(temp);
        }
    	return sc;
    }
    
    /** Ajax methods */
    private void saveInvestorInfo(InvestorDto investor, EApplicationStatus status) {
        String username = securityContext.getCurrentUserId();
        InvestorApplication investApplication = memberService.getLatestInvestorWithoutReject(username);
        if (investApplication != null) {
            if (investApplication.getStatus() == EApplicationStatus.UNCOMMITTED) {
                investor.setAppId(investApplication.getAppId());
            }
            investor.setCreator(investApplication.getCreator());
//          investor.setAgentName(investApplication.getAgentName());
        } else {
        	/** reset creator id, agent name. **/
        	InvestorApplication originalInvestor = memberService.getInvestorWithLatestStatus(username);
        	if (originalInvestor != null) {
				investor.setCreator(originalInvestor.getCreator());
//				investor.setAgentName(originalInvestor.getAgentName());
			}
        	investor.setLastMntOpid(securityContext.getCurrentUserId());
        }
        investor.setUserId(username);
        investor.setStatus(status);
        ActionType action = ActionType.NEW;
        InvestorApplication investorPo = ConverterService.convert(investor, InvestorApplication.class,
                InvestorApplicationConverter.class);
        /** Reset auth center user Id & agent due to it is disable on page. **/
        /** Which scenario is auth center disabled? **/
        if (investApplication != null && investApplication.getStatus() != EApplicationStatus.UNCOMMITTED) {
        	investorPo.setAgent(investApplication.getAgent());
            investorPo.setAuthCenterId(investApplication.getAuthCenterId());
            investorPo.setAgentName(investor.getAgentName());
            action = investorPo.getStatus() == EApplicationStatus.UNCOMMITTED ? ActionType.NEW : ActionType.EDIT;
		}
        memberService.saveInvestorInfo(investorPo, action, investApplication);
    }

    private void initMemberInfoForInvestor(InvestorDto dto, Model model) {
        String userId = securityContext.getCurrentUserId();
        User user = this.userService.getUserByUserId(userId);
        Class<? extends MemberDto> clazz = null;
        if (EUserType.PERSON == user.getType()) {
        	clazz = PersonDto.class;
		} else if (EUserType.ORGANIZATION == user.getType()) {
			clazz = OrganizationDto.class;
		}

        MemberDto memberDto = new MemberDto();
        Member oldMember = memberService.getMemberByUserId(userId);
        if (oldMember == null) {
            MemberApplication member = memberService.getLatestMemberApplWithoutReject(userId);
            if (member == null || member.getStatus() == EApplicationStatus.UNCOMMITTED) {
                dto.setHasMemberInfo(false);
            } else {
                memberDto = ConverterService.convert(member, clazz);
            }
        } else {
            memberDto = ConverterService.convert(oldMember, clazz, MemberInfoConverter.class);
        }

        if (StringUtils.isNotBlank(memberDto.getUserId())) {
            AcctPo acctPo = acctService.getAcctByUserId(memberDto.getUserId());
            if (acctPo != null) {
                memberDto.setAccount(ConverterService.convert(acctPo, AccountDto.class));
            }
        }

        memberDto.setUserType(user.getType());
        model.addAttribute("member", memberDto);
    }

    private void addAllInvestorOptionsToModel(Model model) {
        List<ServiceCenterDto> serviceCenterDtoList = new ArrayList<ServiceCenterDto>();
        List<ServiceCenterInfo> allServiceCenters = serviceCenterService.getAllServiceCenters();
        for (ServiceCenterInfo domain : allServiceCenters) {
            serviceCenterDtoList.add(ConverterService.convert(domain, ServiceCenterDto.class));
        }
        model.addAttribute("serviceCenterList", serviceCenterDtoList);
        model.addAttribute("investorLevelList", getDynamicOptions(EOptionCategory.INVESTOR_LEVEL, true));
    }

}
