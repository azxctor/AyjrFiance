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
import com.hengxin.platform.member.converter.FinancierApplicationConverter;
import com.hengxin.platform.member.converter.FinancierInfoConverter;
import com.hengxin.platform.member.converter.MemberInfoConverter;
import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.domain.FinancierInfo;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.MemberDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.member.dto.FinancierDto.SubmitFinance;
import com.hengxin.platform.member.dto.FinancierDto.SubmitOrgCode;
import com.hengxin.platform.member.dto.FinancierDto.SubmitOrgFinance;
import com.hengxin.platform.member.dto.downstream.AccountDto;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.UserService;

@Controller
public class FinancierController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancierController.class);

    @Autowired
    private transient UserService userService;
    
    @Autowired
    private transient MemberService memberService;

    @Autowired
    private transient AcctService acctService;

    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private transient SecurityContext securityContext;
    
    @Autowired
    private MemberMessageService messageService;

    /** Get page methods */

    @RequestMapping(value = UrlConstant.MEMBER_PERM_APPLY_FINANCIER_URL, method = RequestMethod.GET)
    @RequiresPermissions({ Permissions.MEMBER_PERM_APPLY_FINANCIER_VIEW })
    public String financierInfoFill(HttpServletRequest request, HttpSession session, Model model) {

        String userId = securityContext.getCurrentUserId();
        String mode = request.getParameter("mode");

        FinancierApplication financier = memberService.getFinancierWithLatestStatus(userId);
        FinancierDto financierDto = new FinancierDto();

        if (financier != null) {
            // 拒绝状态下取原信息
            if (financier.getStatus() == EApplicationStatus.REJECT) {
                FinancierInfo oldFinancier = memberService.getFinancierById(userId);
                if (oldFinancier != null) {
                    financierDto = ConverterService.convert(oldFinancier, FinancierDto.class,
                            FinancierInfoConverter.class);
                    financierDto.setStatus(EApplicationStatus.ACCEPT);
                } else {
                    financierDto = ConverterService.convert(financier, FinancierDto.class,
                            FinancierApplicationConverter.class);
                    financierDto.setStatus(EApplicationStatus.UNCOMMITTED);
                }
                financierDto.setComments(financier.getComments());
                financierDto.setRejectLastTime(true);
            } else {
                financierDto = ConverterService.convert(financier, FinancierDto.class,
                        FinancierApplicationConverter.class);
            }

            // 修改的pending状态需要看到原信息
            if (financier.getStatus() == EApplicationStatus.PENDDING) {
                FinancierInfo oldFinancier = memberService.getFinancierById(userId);
                if (oldFinancier != null) {
                    financierDto.setOldFinancier(ConverterService.convert(oldFinancier, FinancierDto.class,
                            FinancierInfoConverter.class));
                }
            }
        }
        /** Disable Auth Center & Agent if current user is created by Agent. **/
        if (financierDto.getAuthCenter() != null) {
			if (financierDto.getAuthCenter().getUserId().equals(financierDto.getCreator())) {
				financierDto.setFixedAgency(true);
			}
		}

        EApplicationStatus status = financierDto.getStatus();
        if (StringUtils.isNotBlank(mode)) {
            // 如果有mode参数，页面显示根据mode
            EFormMode formMode = getFormMode(mode);
            financierDto.setFormMode(formMode);
            financierDto.setFixedLevel(formMode != EFormMode.EDIT || !securityContext.canEditFinancierLevel());
        } else {
            // 如果没有mode参数，除保存未提交状态其他状态全部为View
            if (status == EApplicationStatus.NULL || status == EApplicationStatus.UNCOMMITTED) {
                financierDto.setFormMode(EFormMode.NULL);
            }
        }

        addAllFinancierOptionsToModel(model);
        initMemberInfoForFinancier(model, financierDto);
        boolean finalStatus = status == EApplicationStatus.ACCEPT || status == EApplicationStatus.ACTIVE
                || status == EApplicationStatus.REJECT;
        financierDto.setCanEdit(securityContext.canEditMemberFinancierInfo() && finalStatus);
        /** Auth-Center may be null. **/
        financierDto.setShowServiceCenter(financierDto.getCreator() != null
                && financierDto.getAuthCenter() != null && financierDto.getCreator().equals(financierDto.getAuthCenter().getUserId()));

        model.addAttribute("financierInfo", financierDto);
        return "members/financierinfo";
    }

    /**
     * Submit methods. Used for apply and update info. 
     * @param financier
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "members/financierinfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MEMBER_PERM_APPLY_FINANCIER, Permissions.MEMBER_PROFILE_FINANCIER_UPDATE }, logical = Logical.OR)
    public ResultDto doFinancierInfoFill(@RequestBody FinancierDto financier, HttpServletRequest request, Model model)
            throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doFinancierInfoFill() start: ");
        }

        // 机构的验证字段不同，需要手动验证
        if (securityContext.getCurrentUser().getType() == EUserType.ORGANIZATION) {
            validate(financier, new Class[] { SubmitOrgFinance.class, SubmitOrgCode.class });
        } else {
            validate(financier, new Class[] { SubmitFinance.class });
        }

        saveFinancierInfo(financier, EApplicationStatus.PENDDING);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doFinancierInfoFill() succeed: ");
        }

        String content2 = "";
        MemberApplication member = memberService.getMemberWithLatestStatus(financier.getUserId());
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
        
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/members/financierinfo"));
    }

    /** Save methods */

    @RequestMapping(value = "members/financierinfo/save", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PERM_APPLY_FINANCIER })
    public ResultDto saveFinancierInfoFill(@OnValid @RequestBody FinancierDto financier, HttpServletRequest request,
            Model model) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("saveFinancierInfoFill() start: ");
        }

        saveFinancierInfo(financier, EApplicationStatus.UNCOMMITTED);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("saveFinancierInfoFill() succeed: ");
        }

        return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
    }

    /** Ajax methods */

    /** Private methods */
    private void saveFinancierInfo(FinancierDto financier, EApplicationStatus status) {
        String username = securityContext.getCurrentUserId();
        FinancierApplication financeApplication = memberService.getLatestFinancierWithoutReject(username);
        if (financeApplication != null) {
            if (financeApplication.getStatus() == EApplicationStatus.UNCOMMITTED) {
                financier.setAppId(financeApplication.getAppId());
            }
            financier.setCreator(financeApplication.getCreator());
        } else {
         	/** reset creator id, auth center id. **/
        	FinancierApplication originalfinancier = memberService.getFinancierWithLatestStatus(username);
         	if (originalfinancier != null) {
         		financier.setCreator(originalfinancier.getCreator());
         		AgencyDto agency = new AgencyDto();
         		agency.setUserId(originalfinancier.getAuthCenterId());
         		financier.setAuthCenter(agency);
 			}
         	financier.setLastMntOpid(securityContext.getCurrentUserId());
         }
        financier.setUserId(username);
        financier.setStatus(status);
        ActionType action = ActionType.NEW;
        FinancierApplication financierPo = ConverterService.convert(financier, FinancierApplication.class,
                FinancierApplicationConverter.class);
        /** Reset auth center user Id & agent due to it is disable on page. **/
        if (financeApplication != null) {
        	financierPo.setAgent(financeApplication.getAgent());
            financierPo.setAuthCenterId(financeApplication.getAuthCenterId());
            action = financierPo.getStatus() == EApplicationStatus.UNCOMMITTED ? ActionType.NEW : ActionType.EDIT;
		}
        memberService.saveFinancierInfo(financierPo, action, financeApplication);
    }

    private void initMemberInfoForFinancier(Model model, FinancierDto dto) {
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
        model.addAttribute("financierLevelList", getDynamicOptions(EOptionCategory.FINANCIER_LEVEL, true));
    }
}
