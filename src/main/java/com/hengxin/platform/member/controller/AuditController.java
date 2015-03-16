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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.ServiceException;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.AgencyApplicationView;
import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.dto.ActionHistoryDto;
import com.hengxin.platform.member.dto.AuditDto;
import com.hengxin.platform.member.dto.AuditLogDto;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.MemberDto;
import com.hengxin.platform.member.dto.MemberSearchDto;
import com.hengxin.platform.member.dto.MemberViewDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.member.dto.downstream.ProductProviderApplicationDto;
import com.hengxin.platform.member.dto.downstream.ServiceCenterApplicationDto;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.dto.upstream.MemberApplicationAuditDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.AuditService;
import com.hengxin.platform.member.service.MemberApplicationService;
import com.hengxin.platform.member.service.MemberApplicationViewService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.member.service.ProductProviderService;
import com.hengxin.platform.member.service.ServiceCenterService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;

/**
 * Class Name: AuditController
 *
 * @author yingchangwang
 *
 */
@Controller
public class AuditController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditController.class);

    @Autowired
    private MemberApplicationService memberApplicationService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private MemberApplicationViewService memberApplicationViewService;

    @Autowired
    private ServiceCenterService serviceCenterService;

    @Autowired
    private ProductProviderService productProviderApplicationAuditService;

    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private BankAcctService bankAcctService;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value=UrlConstant.MEMBER_MANAGEMENT_INVEST_FIN_APPLY_APPROVE_URL)
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_INVESTOR_FINANCIER_APPROVE })
    public String renderMemberInfoList(HttpServletRequest request, HttpSession session, Model model) {
        return "audit/audit";
    }

    /**
     *
     * @param request
     * @param model
     * @param memberSearch
     * @return
     */
    @RequestMapping("audit/getmemberinfo")
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_INVESTOR_FINANCIER_APPROVE })
    public DataTablesResponseDto<MemberViewDto> getMemberInfoList(HttpServletRequest request, Model model,
            @RequestBody MemberSearchDto memberSearch) {
        DataTablesResponseDto<MemberViewDto> personalMembers = memberApplicationViewService.getMembers(memberSearch);

        // exclusive
        if (memberSearch.getAuditStatus() == EApplicationStatus.AUDITED) {
            List<MemberViewDto> data = personalMembers.getData();
            for (MemberViewDto memberViewDto : data) {
                if (memberViewDto.getBasicStatus() != null
                        && memberViewDto.getBasicStatus() == EApplicationStatus.PENDDING) {
                    memberViewDto.setBasicStatus(null);
                }
                if (memberViewDto.getInvestorStatus() != null
                        && memberViewDto.getInvestorStatus() == EApplicationStatus.PENDDING) {
                    memberViewDto.setInvestorStatus(null);
                }
                if (memberViewDto.getFinanceStatus() != null
                        && memberViewDto.getFinanceStatus() == EApplicationStatus.PENDDING) {
                    memberViewDto.setFinanceStatus(null);
                }
            }
        }

        return personalMembers;
    }

    @RequestMapping(value = "audit/userinfo/{userId}", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_INVESTOR_FINANCIER_APPROVE })
    public MemberApplicationAuditDto getMemberInfoDetails(HttpServletRequest request, Model model,
            @PathVariable(value = "userId") String userId, @RequestParam(value = "userType") String userType,
            @RequestParam(value = "queryStatus") String queryStatus) {
        MemberApplicationAuditDto dto = new MemberApplicationAuditDto();
        MemberApplication member = memberApplicationService.getLastCreateMember(userId);
        if (EUserType.PERSON == EUserType.valueOf(userType)) {
            dto.setPerson(ConverterService.convert(member, PersonDto.class));
            dto.getPerson().setInCanViewPage(true);
            this.setBankRegionInfo(dto.getPerson(), member.getBankOpenProvince(), member.getBankOpenCity());
        }
        if (EUserType.ORGANIZATION == EUserType.valueOf(userType)) {
            dto.setOrganization(ConverterService.convert(member, OrganizationDto.class));
            dto.getOrganization().setInCanViewPage(true);
            this.setBankRegionInfo(dto.getOrganization(), member.getBankOpenProvince(), member.getBankOpenCity());
        }

        InvestorApplication investorDetails = memberApplicationService.getLastCreateInvestorDetails(userId);
        InvestorDto investorDto = ConverterService.convert(investorDetails, InvestorDto.class);
        if (investorDetails != null && StringUtils.isNotBlank(investorDetails.getAuthCenterId())) {
            Agency agency = serviceCenterService.getAgencyById(investorDetails.getAuthCenterId());
            investorDto.setAuthCenter(ConverterService.convert(agency, AgencyDto.class));
            investorDto.setInCanViewPage(true);
        }
        dto.setInvestorInfo(investorDto);

        FinancierApplication financierDetails = memberApplicationService.getLastCreateFinancierDetails(userId);
        FinancierDto financierDto = ConverterService.convert(financierDetails, FinancierDto.class);
        if (financierDetails != null && StringUtils.isNotBlank(financierDetails.getAuthCenterId())) {
            Agency agency = serviceCenterService.getAgencyById(financierDetails.getAuthCenterId());
            financierDto.setAuthCenter(ConverterService.convert(agency, AgencyDto.class));
            financierDto.setInCanViewPage(true);
        }
        dto.setFinancierInfo(financierDto);

        dto.setAccount(memberApplicationService.getAccountDetails(userId));

        boolean isPermitted = securityContext.hasPermission(Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE);

        EApplicationStatus selectedStatus = EApplicationStatus.valueOf(queryStatus);
        if (EApplicationStatus.AUDITED == selectedStatus) {
            if (member != null && member.getStatus() == EApplicationStatus.PENDDING) {
                dto.setPerson(null);
                dto.setOrganization(null);
            }
            if (investorDetails != null && investorDetails.getStatus() == EApplicationStatus.PENDDING) {
                dto.setInvestorInfo(null);
            }
            if (financierDetails != null && financierDetails.getStatus() == EApplicationStatus.PENDDING) {
                dto.setFinancierInfo(null);
            }
        }
        if (isPermitted && EApplicationStatus.PENDDING == selectedStatus) {
            if (investorDetails != null && investorDetails.getStatus() == EApplicationStatus.PENDDING) {
                dto.setChangeInvestorLevel(true);
            }
            if (financierDetails != null && financierDetails.getStatus() == EApplicationStatus.PENDDING) {
                dto.setChangeFinancierLevel(true);
            }
        }
        Member existMember = memberService.getMemberByUserId(userId);
        if(existMember!=null){
            dto.setBasicApproved(true);
        }
        dto.setInvestorLevelList(SystemDictUtil.getRootDictList(EOptionCategory.INVESTOR_LEVEL, false));
        dto.setFinancierLevelList(SystemDictUtil.getRootDictList(EOptionCategory.FINANCIER_LEVEL, false));

        return dto;
    }
    
	private void setBankRegionInfo(MemberDto member, String provinceCode, String cityCode) {
        DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, provinceCode);
        member.setBankOpenProvince(option);
        DynamicOption option2 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, cityCode);
        member.setBankOpenCity(option2);
	}

    @RequestMapping(value = "audit/userinfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_INVESTOR_FINANCIER_APPROVE })
    public ResultDto auditMemberInfo(HttpServletRequest request, Model model, @OnValid @RequestBody AuditDto audit,
            HttpSession session) {
        LOGGER.debug("audit member : {}", audit);
        User currentUser = securityContext.getCurrentUser();
        auditService.auditMembers(audit, currentUser.getUserId());
        return ResultDtoFactory.toAck(MessageUtil.getMessage("userinfo.approve.success"), audit);
    }

    @RequestMapping(value = "audit/members/{userId}", method = RequestMethod.GET)
    public Object getAuditLogs(HttpServletRequest request, @PathVariable(value = "userId") String userId) {
        return null;
    }

    @RequestMapping(value=UrlConstant.MEMBER_MANAGEMENT_SERVICE_CENTER_APPLY_APPROVE_URL)
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_ATHD_CNTL_APPROVE })
    public String renderServiceCenterList(HttpServletRequest request, HttpSession session, Model model) {
        return "audit/service_audit";
    }

    /**
     *
     * @param request
     * @param model
     * @param auditSearch
     * @return
     */
    @RequestMapping("audit/getservicecenterapp")
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_ATHD_CNTL_APPROVE })
    public DataTablesResponseDto<ServiceCenterApplicationDto> getServiceCenterList(HttpServletRequest request,
            Model model, @RequestBody MemberSearchDto auditSearch) {
        DataTablesResponseDto<ServiceCenterApplicationDto> serviceCenters = serviceCenterService
                .getServiceCenters(auditSearch);
        return serviceCenters;
    }

    @RequestMapping(value = "audit/servicecenterapp/{userId}", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_ATHD_CNTL_APPROVE })
    public ServiceCenterApplicationDto getServiceCenterDetails(HttpServletRequest request, HttpSession session,
            @PathVariable(value = "userId") String userId) {
        ServiceCenterApplicationDto serviceCenterApplication = serviceCenterService.getServiceCenterApplication(userId);
        serviceCenterApplication.setMemberLevelList(SystemDictUtil.getRootDictList(EOptionCategory.SERVICE_CENTER_LEVEL, false));
        return serviceCenterApplication;
    }

    @RequestMapping(value = "audit/getlogs")
    @ResponseBody
    public DataTablesResponseDto<ActionHistoryDto> getLog(HttpServletRequest request, @RequestBody AuditLogDto auditLog) {
        return serviceCenterService.getLog(auditLog, true);
    }

    @RequestMapping(value = "audit/servicecenter", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_ATHD_CNTL_APPROVE })
    public ResultDto auditServiceCenterDetails(HttpServletRequest request, String serviceCenterId,
            @OnValid @RequestBody AuditDto audit, HttpSession session) throws ServiceException {
        LOGGER.debug("Audit ServiceCenter:", audit);
        User currentUser = securityContext.getCurrentUser();
        auditService.auditServiceCenter(audit, currentUser.getUserId());
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/audit/servicecenterapps"));
    }

    /**
     *
     * Description: forward to provider_audit
     *
     * @param audit
     */
    @RequestMapping(value=UrlConstant.MEMBER_MANAGEMENT_PRODUCT_SERVICE_APPLY_APPROVE_URL)
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_PROD_SERV_APPROVE })
    public String renderProductProviderList(HttpServletRequest request, HttpSession session, Model model) {
        return "audit/provider_audit";
    }

    /**
     *
     * Description: getAllProductProviderInfo and searchProductProviderInfo
     *
     * @param audit
     */
    @RequestMapping("audit/getproviderinfo")
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_PROD_SERV_APPROVE })
    public DataTablesResponseDto<ProductProviderApplicationDto> getProductProviderInfoList(HttpServletRequest request,
            ModelMap model, @RequestBody MemberSearchDto memberSearch) {

        return productProviderApplicationAuditService.getProductProviderApplicationMembers(memberSearch);
    }

    /**
     *
     * Description: getProductProviderDetails
     *
     * @param audit
     */
    @RequestMapping(value = "audit/providerinfo/{providerId}", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_PROD_SERV_APPROVE })
    public ProductProviderApplicationDto getProductProviderDetails(HttpServletRequest request,
            @PathVariable(value = "providerId") String providerId) {

        AgencyApplicationView agencyApplicationView = productProviderApplicationAuditService
                .getAgencyApplicationView(providerId);

        if (agencyApplicationView == null) {
            return null;
        }
        ProductProviderApplicationDto ppa = ConverterService.convert(agencyApplicationView,
                ProductProviderApplicationDto.class);

        String userId = agencyApplicationView.getUserId();
        List<BankAcct> bankAcct = bankAcctService.findBankAcctByUserIdWihoutCheck(userId);
        if (bankAcct != null && !bankAcct.isEmpty()) {

            ppa.setBank(bankAcct.get(0));
        }
        ppa.setAccount(memberApplicationService.getAccountDetails(userId));
        ppa.setMemberLevelList(SystemDictUtil.getRootDictList(EOptionCategory.PRODUCT_SERVICE_LEVEL, false));
        return ppa;
    }

    /**
     *
     * Description: auditProductProviderDetails
     *
     * @param audit
     */

    @RequestMapping(value = "audit/providerinfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions({ Permissions.MEMBER_PROFILE_PROD_SERV_APPROVE })
    public ResultDto auditProductProviderInfo(HttpServletRequest request, Model model,
            @OnValid @RequestBody AuditDto audit, HttpSession session) throws ServiceException {
        LOGGER.debug("audit member : {}", audit);
        User currentUser = securityContext.getCurrentUser();
        auditService.auditProductProviderApplicationMember(audit, currentUser.getUserId());
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/audit/productprovider"));
    }
}
