package com.hengxin.platform.member.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.constant.ResultCode;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.CommonMenuDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.Company;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.SaveAgency;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.SubmitAgency;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.UNAndPW;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EFormMode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.MenuUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.common.validation.ValidateException;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.AgencyApplication;
import com.hengxin.platform.member.domain.AgencyFeeConstractPo;
import com.hengxin.platform.member.domain.AuthzdCtrAgencyFeeContractInfoView;
import com.hengxin.platform.member.domain.ProductProviderApplication;
import com.hengxin.platform.member.domain.ProductProviderInfo;
import com.hengxin.platform.member.domain.ServiceCenterApplication;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.domain.UserCompanyView;
import com.hengxin.platform.member.dto.AgencyFeeConstractDto;
import com.hengxin.platform.member.dto.AgencyFeeConstractDto.AgencyFeeContractSave;
import com.hengxin.platform.member.dto.AuthzdCtrAgencyFeeContractInfoViewDto;
import com.hengxin.platform.member.dto.AuthzdCtrSearchDto;
import com.hengxin.platform.member.dto.FinancierDto.SubmitOrgCode;
import com.hengxin.platform.member.dto.downstream.AccountDto;
import com.hengxin.platform.member.dto.upstream.AgencyApplicationCommonDto;
import com.hengxin.platform.member.dto.upstream.ProductProviderDto;
import com.hengxin.platform.member.dto.upstream.ServiceCenterDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.AgencyFeeConstractManageService;
import com.hengxin.platform.member.service.CompanyService;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.member.service.UserCompanyViewService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * AgencyController.
 *
 */
@Controller
public class AgencyController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgencyController.class);

    @Autowired
    private transient MemberService memberService;

    @Autowired
    private transient AcctService acctService;

    @Autowired
    private BankAcctService bankAcctService;

    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private transient SecurityContext securityContext;

    @Autowired
    private MenuUtil roleMenuUtil;
    
    @Autowired
    private MemberMessageService messageService;
    
    @Autowired
    private CompanyService companyService;
    
    @Autowired
    private UserCompanyViewService userCompanyViewService;

    @Autowired
    private AgencyFeeConstractManageService authzdCtrAgencyFeeConstractManageService;    
    
    @RequestMapping(value = "agency/register", method = RequestMethod.GET)
    public String registerAgency(HttpServletRequest request, HttpSession session, Model model) {
        LOGGER.info("registerAgency() invoked");
        this.addAllPageStateToModel(model);
        this.addAllBasicOptionsToModel(model);
        this.addAllVMPrefixToModel(model);
        return "members/register_agency";
    }

    @RequestMapping(value = UrlConstant.MEMBER_PERM_APPLY_PROD_SERV_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.MEMBER_PERM_APPLY_PROD_SERV_VIEW})
    public String getProductProviderInfo(HttpServletRequest request, HttpSession session, Model model) {
        LOGGER.info("getProductProviderInfo() invoked");
        String username = securityContext.getCurrentUserId();
        return this.loadProductProviderInfo(request, model, username);
    }

    @RequestMapping(value = UrlConstant.MEMBER_PERM_APPLY_ATHD_CNTL_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.MEMBER_PERM_APPLY_ATHD_CNTL_VIEW})
    public String getServiceCenterInfo(HttpServletRequest request, HttpSession session, Model model) {
        LOGGER.info("getServiceCenterInfo() invoked");
        String username = securityContext.getCurrentUserId();
        return this.loadServiceCenterInfo(request, model, username);
    }

    /** Get Page methods */
    @RequestMapping(value = "agency/productproviderinfo/view/{username}", method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.MEMBER_PERM_APPLY_PROD_SERV_VIEW,
            Permissions.MEMBER_PERM_APPLY_PROD_SERV }, logical = Logical.OR)
    public String getProductProviderInfo(@PathVariable("username") String username, HttpServletRequest request,
            HttpSession session, Model model) {
        LOGGER.info("getProductProviderInfo() invoked, {}", username);
        return this.loadProductProviderInfo(request, model, username);
    }

    /** Get Page methods */
    @RequestMapping(value = "agency/servicecentorinfo/view/{username}", method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.MEMBER_PERM_APPLY_ATHD_CNTL_VIEW,
            Permissions.MEMBER_PERM_APPLY_ATHD_CNTL }, logical = Logical.OR)
    public String getServiceCenterInfo(@PathVariable("username") String username, HttpServletRequest request,
            HttpSession session, Model model) {
        LOGGER.info("getServiceCenterInfo() invoked, {}", username);
        return this.loadServiceCenterInfo(request, model, username);
    }

    private String loadProductProviderInfo(HttpServletRequest request, Model model, String userId) {
        String edit = request.getParameter("basicMode");
        boolean editable = edit != null ? true : false;
        String status = EApplicationStatus.UNCOMMITTED.getCode();
        EFormMode mode = EFormMode.VIEW;
        ProductProviderApplication productProviderApplication = memberService
                .getProductProviderWithLatestStatus(userId);
        AgencyRegisterInfoDto dto = new AgencyRegisterInfoDto();
        dto.setType(EMemberType.PRODUCTSERVICE);
        if (productProviderApplication != null) {
            ProductProviderInfo productProviderInfo = memberService.getProductProviderInfoById(userId);
            ProductProviderDto product = new ProductProviderDto();
          //如果不是第一次拒绝，曾经审核通过过，INFO表中有数据，读取被审核过的数据
            if (EApplicationStatus.REJECT == productProviderApplication.getStatus() && productProviderInfo != null) {
                product = ConverterService.convert(productProviderInfo, ProductProviderDto.class);
                if (productProviderInfo.getProSeverLevel() != null) {
                    product.setLevel(loadInfo(EOptionCategory.PRODUCT_SERVICE_LEVEL,
                            productProviderInfo.getProSeverLevel()));
                }
            } else {
                product = ConverterService.convert(productProviderApplication, ProductProviderDto.class);
                if (productProviderApplication.getProSeverLevel() != null) {
                    product.setLevel(loadInfo(EOptionCategory.PRODUCT_SERVICE_LEVEL,
                            productProviderApplication.getProSeverLevel()));
                }
            }
            dto.setProductProvider(product);
            if (EApplicationStatus.PENDDING == productProviderApplication.getStatus()) {
                dto.setProviderPendding(true);
            }
            /**add reject comments**/
            if (EApplicationStatus.REJECT == productProviderApplication.getStatus()) {
                dto.setComments(productProviderApplication.getComments());
                dto.setProductReject(true);
            }
        }
        AgencyApplication agencyApplication = memberService.getAgencyApplicationWithLatestStatus(userId);
        Agency oldAgency = memberService.getAgencyByUserId(userId);
        AgencyApplicationCommonDto agency = new AgencyApplicationCommonDto();
        if (agencyApplication != null) {
            //被拒绝，用已审核数据
            if (EApplicationStatus.REJECT == agencyApplication.getStatus() && oldAgency != null) {
                agency = ConverterService.convert(oldAgency, AgencyApplicationCommonDto.class);
            	this.loadFormalBankInfo(userId, agency);
            } else {
                agency = ConverterService.convert(agencyApplication, AgencyApplicationCommonDto.class);
                this.setBankRegionInfo(agency, agencyApplication.getBankOpenProvince(), agencyApplication.getBankOpenCity());
            }
            if (EApplicationStatus.UNCOMMITTED == agencyApplication.getStatus()) {
                status = EApplicationStatus.UNCOMMITTED.getCode();
                mode = EFormMode.EDIT;
            } else if (EApplicationStatus.PENDDING == agencyApplication.getStatus()) {
                status = EApplicationStatus.PENDDING.getCode();
                mode = EFormMode.VIEW;
            } else {
                status = agencyApplication.getStatus().getCode();
                if (editable) {
                    mode = EFormMode.EDIT;
                } else {
                    mode = EFormMode.VIEW;
                }
            }
        }
        dto.setCommon(agency);
        dto.setFormMode(mode);
        /** get member type. **/
        if (securityContext.isAuthServiceCenter()) {
            if (securityContext.isProdServ()) {
            	dto.setMemberType(EMemberType.PRODSERV_AUTHZD);
            } else {
            	dto.setMemberType(EMemberType.AUTHZDCENTER);
            }
        } else if (securityContext.isProdServ()) {
        	dto.setMemberType(EMemberType.PRODUCTSERVICE);
        }
        LOGGER.debug("Member Type {}", dto.getMemberType().getText());
        /** get accoutn into. **/
        AcctPo acctPo = acctService.getAcctByUserId(userId);
        if (acctPo != null) {
        	dto.setAccount(ConverterService.convert(acctPo, AccountDto.class));
        	LOGGER.debug("Account No {}", dto.getAccount().getAcctNo());
        } else {
        	LOGGER.debug("Account is null");
        }
        /**hide bank field, not first reject or not uncommitted status**/

        /** simplify logic..... **/
        if (EApplicationStatus.ACCEPT == agencyApplication.getStatus()) {
        	dto.setFixedInput(true);
		} else if (EApplicationStatus.UNCOMMITTED == agencyApplication.getStatus()
				|| EApplicationStatus.REJECT == agencyApplication.getStatus()) {
			dto.setFixedInput(false);
		} else {
			if (securityContext.canAdvancedBasicInfoEdit()) {
				dto.setFixedInput(false);
			} else {
				dto.setFixedInput(true);
			}
		}
        //if first reject, set mode to edit mode
        boolean firstReject = false;
        if (oldAgency == null && EApplicationStatus.REJECT == agencyApplication.getStatus()) {
            mode = EFormMode.EDIT;
            editable = true;
            dto.setShowSaveButton(true);
            /** hide level. **/
            firstReject = true;
        }
        //fix agent if user is auth center
        if (EMemberType.PRODUCTSERVICE == dto.getMemberType() || EMemberType.PRODSERV_AUTHZD == dto.getMemberType()) {
			if (!firstReject && EFormMode.EDIT == mode) {
				/** disable field agent. **/
				dto.setProductFixedAgent(true);
				dto.setFixedInput(true);
			}
		}
        
        //取分公司
        UserCompanyView uc = userCompanyViewService.getUserCompany(userId);
        if(null!=uc){
        	dto.setCompanyId(uc.getCompany_id());
        }else{
        	dto.setCompanyId("1");
        }

        // FIXME model attribute must be in a object
        model.addAttribute("userType", EUserType.ORGANIZATION);
        model.addAttribute("editable", editable);
        model.addAttribute("status", status);
        model.addAttribute("mode", mode);
        model.addAttribute("dto", dto);
        model.addAttribute("username", userId);
        model.addAttribute("type", EMemberType.PRODUCTSERVICE.getCode());
        LOGGER.info("Status {}, editable {}", status, editable);
        addAllVMPrefixToModel(model);
        addAllBasicOptionsToModel(model);
        addAllPageStateToModel(model);
        return "members/agency_info";
    }

    private String loadServiceCenterInfo(HttpServletRequest request, Model model, String userId) {
        String edit = request.getParameter("basicMode");
        boolean editable = edit != null ? true : false;
        String status = EApplicationStatus.UNCOMMITTED.getCode();
        EFormMode mode = EFormMode.VIEW;
        ServiceCenterApplication serviceInfoApplication = memberService.getServiceCenterWithLatestStatus(userId);
        AgencyRegisterInfoDto dto = new AgencyRegisterInfoDto();
        dto.setType(EMemberType.AUTHZDCENTER);
        if (serviceInfoApplication != null) {
            ServiceCenterInfo serviceCenterInfo = memberService.getServiceCenterInfoById(userId);
            ServiceCenterDto service = new ServiceCenterDto();
            //如果不是第一次拒绝，曾经审核通过过，INFO表中有数据，读取被审核过的数据
            if (EApplicationStatus.REJECT == serviceInfoApplication.getStatus() && serviceCenterInfo != null) {
                service = ConverterService.convert(serviceCenterInfo, ServiceCenterDto.class);
                if (serviceCenterInfo.getLevel() != null) {
                    service.setLevel(loadInfo(EOptionCategory.SERVICE_CENTER_LEVEL, serviceCenterInfo.getLevel()));
                }
            } else {
                service = ConverterService.convert(serviceInfoApplication, ServiceCenterDto.class);
                if (serviceInfoApplication.getLevel() != null) {
                    service.setLevel(loadInfo(EOptionCategory.SERVICE_CENTER_LEVEL, serviceInfoApplication.getLevel()));
                }
            }
            dto.setServiceCenter(service);
            if (EApplicationStatus.PENDDING == serviceInfoApplication.getStatus()) {
                dto.setServicePendding(true);
            }
            /**add reject comments**/
            if (EApplicationStatus.REJECT == serviceInfoApplication.getStatus()) {
                dto.setComments(serviceInfoApplication.getComments());
                dto.setServiceReject(true);
            }
        }
        AgencyApplication agencyApplication = memberService.getAgencyApplicationWithLatestStatus(userId);
        Agency oldAgency = memberService.getAgencyByUserId(userId);
        AgencyApplicationCommonDto agency = new AgencyApplicationCommonDto();
        if (agencyApplication != null) {
            //被拒绝，用已审核数据
            if (EApplicationStatus.REJECT == agencyApplication.getStatus() && oldAgency != null) {
                agency = ConverterService.convert(oldAgency, AgencyApplicationCommonDto.class);
                this.loadFormalBankInfo(userId, agency);
            } else {
                agency = ConverterService.convert(agencyApplication, AgencyApplicationCommonDto.class);
                this.setBankRegionInfo(agency, agencyApplication.getBankOpenProvince(), agencyApplication.getBankOpenCity());
            }
            if (EApplicationStatus.UNCOMMITTED == agencyApplication.getStatus()) {
                status = EApplicationStatus.UNCOMMITTED.getCode();
                mode = EFormMode.EDIT;
            } else if (EApplicationStatus.PENDDING == agencyApplication.getStatus()) {
                status = EApplicationStatus.PENDDING.getCode();
                mode = EFormMode.VIEW;
            } else {
                status = agencyApplication.getStatus().getCode();
                if (editable) {
                    mode = EFormMode.EDIT;
                } else {
                    mode = EFormMode.VIEW;
                }
            }
        }
        dto.setCommon(agency);
        dto.setFormMode(mode);
        /** get member type. **/
        if (securityContext.isAuthServiceCenter()) {
            if (securityContext.isProdServ()) {
            	dto.setMemberType(EMemberType.PRODSERV_AUTHZD);
            } else {
            	dto.setMemberType(EMemberType.AUTHZDCENTER);
            }
        } else if (securityContext.isProdServ()) {
        	dto.setMemberType(EMemberType.PRODUCTSERVICE);
        }
        LOGGER.debug("Member Type {}", dto.getMemberType().getText());
        /** get account into. **/
        AcctPo acctPo = acctService.getAcctByUserId(userId);
        if (acctPo != null) {
        	dto.setAccount(ConverterService.convert(acctPo, AccountDto.class));
        	LOGGER.info("Account No {}", dto.getAccount().getAcctNo());
        } else {
        	LOGGER.warn("Account is null");
        }
        /**hide bank field, not first reject or not uncommitted status**/

        /** simplify logic..... **/
        if (EApplicationStatus.ACCEPT == agencyApplication.getStatus()) {
        	dto.setFixedInput(true);
		} else if (EApplicationStatus.UNCOMMITTED == agencyApplication.getStatus()
				|| EApplicationStatus.REJECT == agencyApplication.getStatus()) {
			dto.setFixedInput(false);
		} else {
			if (securityContext.canAdvancedBasicInfoEdit()) {
				dto.setFixedInput(false);
			} else {
				dto.setFixedInput(true);
			}
		}
      //if first reject, set mode to edit mode
        boolean firstReject = false;
        if (oldAgency == null && EApplicationStatus.REJECT == agencyApplication.getStatus()) {
            mode = EFormMode.EDIT;
            editable = true;
            dto.setShowSaveButton(true);
            /** hide level. **/
            firstReject = true;
        }
        //fix agent if user is auth center
        if (EMemberType.AUTHZDCENTER == dto.getMemberType() || EMemberType.PRODSERV_AUTHZD == dto.getMemberType()) {
			if (!firstReject && EFormMode.EDIT == mode) {
				/** disable field agent. **/
				dto.setServiceFixedAgent(true);
				dto.setFixedInput(true);
			}
		}
        
        //取分公司
        UserCompanyView uc = userCompanyViewService.getUserCompany(userId);
        if(null!=uc){
        	dto.setCompanyId(uc.getCompany_id());
        }else{
        	dto.setCompanyId("1");
        }

        // FIXME model attribute must be in a object
        // FIXME duplicate logic as loadProductProviderInfo
        model.addAttribute("userType", EUserType.ORGANIZATION);
        model.addAttribute("editable", editable);
        model.addAttribute("status", status);
        model.addAttribute("mode", mode);
        model.addAttribute("dto", dto);
        model.addAttribute("username", userId);
        model.addAttribute("type", EMemberType.AUTHZDCENTER.getCode());
        LOGGER.info("Status {}, editable {}", status, editable);
        addAllVMPrefixToModel(model);
        addAllBasicOptionsToModel(model);
        addAllPageStateToModel(model);
        return "members/agency_info";
    }

    /**
     * load bank info.
     * @param userId
     * @param agency
     */
	private void loadFormalBankInfo(String userId, AgencyApplicationCommonDto agency) {
		List<BankAcct> bankAccounts = this.bankAcctService.findBankAcctByUserIdWihoutCheck(userId);
		if (!bankAccounts.isEmpty()) {
			BankAcct ba = bankAccounts.get(0);
			DynamicOption option = new DynamicOption();
			option.setText(ba.getBnkName());
			option.setCode(ba.getBnkCd());
			agency.setBankShortName(option);
			agency.setBankFullName(ba.getBnkName());
			agency.setBankAccount(ba.getBnkAcctNo());
			agency.setBankAccountName(ba.getBnkAcctName());
			agency.setBankCardFrontImg(ba.getBnkCardImg());
			this.setBankRegionInfo(agency, ba.getBnkOpenProv(), ba.getBnkOpenCity());
			agency.setBankOpenBranch(ba.getBnkBrch());
		}
	}
	
	private void setBankRegionInfo(AgencyApplicationCommonDto agency, String provinceCode, String cityCode) {
        DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, provinceCode);
        agency.setBankOpenProvince(option);
        DynamicOption option2 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, cityCode);
        agency.setBankOpenCity(option2);
	}

    /** Submit methods */
    @RequestMapping(value = "agency/register/save/", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto insertAgencyInfoForSave(
            @OnValid({ SaveAgency.class, UNAndPW.class, Company.class }) @RequestBody AgencyRegisterInfoDto agencyRegisterInfo,
            BindingResult result, HttpSession session, HttpServletRequest request, Model model) {
        String userId = agencyRegisterInfo.getUsername();
        fillAgencyInfo(agencyRegisterInfo, EApplicationStatus.UNCOMMITTED, userId);
        Session newSession = null;
        try {
            newSession = SecurityContext.login(agencyRegisterInfo.getUsername(), agencyRegisterInfo.getPassword());
            List<CommonMenuDto> sysMenu = roleMenuUtil.getSysMenu();
            if(sysMenu!=null){
            	newSession.setAttribute("menuList", sysMenu);
                newSession.setAttribute("menuMap", roleMenuUtil.getMenuMap(sysMenu));
            }
        } catch (LockedAccountException e1) {
            result.rejectValue(LiteralConstant.PASSWORD, "member.error.password.toomanyfailure");
        } catch (IncorrectCredentialsException e1) {
            result.rejectValue(LiteralConstant.PASSWORD, "member.error.signin.fail");
        }
        if(result.hasErrors()) {
            throw new ValidateException(result);
        }

        if(agencyRegisterInfo.getType()==EMemberType.AUTHZDCENTER){
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/agency/servicecentorinfo/view/"));
        }else{
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/agency/productproviderinfo/view/"));
        }
    }

    @RequestMapping(value = "agency/register/submit/", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto insertAgencyInfoForSubmit(@RequestBody AgencyRegisterInfoDto agencyRegisterInfo,
            BindingResult result,HttpSession session, HttpServletRequest request, Model model) {
        if (agencyRegisterInfo.getType() == null || EMemberType.NULL == agencyRegisterInfo.getType()) {
            agencyRegisterInfo.setProductProvider(null);
            agencyRegisterInfo.setServiceCenter(null);
        } else if (EMemberType.AUTHZDCENTER == agencyRegisterInfo.getType()) {
            agencyRegisterInfo.setProductProvider(null);
        } else if (EMemberType.PRODUCTSERVICE == agencyRegisterInfo.getType()) {
            agencyRegisterInfo.setServiceCenter(null);
        }
        // please do the manually validate if you have the inner method call.
        validate(agencyRegisterInfo, new Class<?>[] { SubmitAgency.class, UNAndPW.class, SubmitOrgCode.class });

        String userId = agencyRegisterInfo.getUsername();
        fillAgencyInfo(agencyRegisterInfo, EApplicationStatus.PENDDING, userId);
        
        this.processTodo(agencyRegisterInfo, true);

        Session newSession;
        try {
            newSession = SecurityContext.login(agencyRegisterInfo.getUsername(), agencyRegisterInfo.getPassword());
            List<CommonMenuDto> sysMenu = roleMenuUtil.getSysMenu();
            if(sysMenu!=null){
            	newSession.setAttribute("menuList", sysMenu);
                newSession.setAttribute("menuMap", roleMenuUtil.getMenuMap(sysMenu));
            }
        } catch (LockedAccountException e1) {
            result.rejectValue(LiteralConstant.PASSWORD, "member.error.password.toomanyfailure");
        } catch (IncorrectCredentialsException e1) {
            result.rejectValue(LiteralConstant.PASSWORD, "member.error.signin.fail");
        }
        if(result.hasErrors()) {
            throw new ValidateException(result);
        }
        if(agencyRegisterInfo.getType()==EMemberType.AUTHZDCENTER){
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/agency/servicecentorinfo/view/"));
        }else{
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/agency/productproviderinfo/view/"));
        }
    }

    private void fillAgencyInfo(AgencyRegisterInfoDto agencyRegisterInfo, EApplicationStatus status, String userId) {
        LOGGER.debug("fillAgencyInfo() invoked");
        User user = ConverterService.convert(agencyRegisterInfo, User.class);
        user.setType(EUserType.ORGANIZATION);
        user.setMobile(agencyRegisterInfo.getCommon().getContactMobile());
        AgencyApplication agency = ConverterService.convert(agencyRegisterInfo.getCommon(), AgencyApplication.class);
        agency.setStatus(status);
        ServiceCenterApplication serviceCenter = null;
        if (EMemberType.AUTHZDCENTER == agencyRegisterInfo.getType()) {
            serviceCenter = ConverterService.convert(agencyRegisterInfo.getServiceCenter(),
                    ServiceCenterApplication.class);
            serviceCenter.setStatus(status);
        }
        ProductProviderApplication productProvider = null;
        if (EMemberType.PRODUCTSERVICE == agencyRegisterInfo.getType()) {
            productProvider = ConverterService.convert(agencyRegisterInfo.getProductProvider(),
                    ProductProviderApplication.class);
            productProvider.setStatus(status);
        }
        memberService.saveAgency(user, agency, serviceCenter, productProvider, userId);
    }

    @RequestMapping(value = "agency/update/save/", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MEMBER_PROFILE_ATHD_CNTL_UPDATE, Permissions.MEMBER_PERM_APPLY_ATHD_CNTL }, logical = Logical.OR)
    public ResultDto updateAgencyInfoForSave(@OnValid({ Company.class })@RequestBody AgencyRegisterInfoDto agencyRegisterInfo, HttpServletRequest request, Model model) throws Exception {
        LOGGER.debug("updateAgencyInfoForSave() invoked {} ", agencyRegisterInfo);
        String userId = securityContext.getCurrentUserId();
        LOGGER.info("Shadow Type {}, type {}", agencyRegisterInfo.getShadowType(), agencyRegisterInfo.getType());
        if (EMemberType.AUTHZDCENTER.getCode().equalsIgnoreCase(agencyRegisterInfo.getShadowType())) {
            agencyRegisterInfo.setType(EMemberType.AUTHZDCENTER);
        } else if (EMemberType.PRODUCTSERVICE.getCode().equalsIgnoreCase(agencyRegisterInfo.getShadowType())) {
            agencyRegisterInfo.setType(EMemberType.PRODUCTSERVICE);
        } else {
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,"Unmatched member type = " + agencyRegisterInfo.getShadowType());
        }
        memberService.updateAgency(agencyRegisterInfo, userId, EApplicationStatus.UNCOMMITTED);
        if (EMemberType.PRODUCTSERVICE == agencyRegisterInfo.getType()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/agency/productproviderinfo/view/" + userId));
        } else {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/agency/servicecentorinfo/view/" + userId));
        }
    }

    @RequestMapping(value = "agency/update/submit/", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MEMBER_PROFILE_ATHD_CNTL_UPDATE, Permissions.MEMBER_PERM_APPLY_ATHD_CNTL }, logical = Logical.OR)
    public ResultDto updateAgencyInfoForSubmit(@RequestBody AgencyRegisterInfoDto agencyRegisterInfo, HttpServletRequest request, Model model) {
        LOGGER.debug("updateAgencyInfoForSubmit() invoked, {}", agencyRegisterInfo);
        String userId = securityContext.getCurrentUserId();
        agencyRegisterInfo.getCommon().setUserId(userId);
        validate(agencyRegisterInfo, new Class<?>[] { SubmitAgency.class, SubmitOrgCode.class});
        LOGGER.info("Shadow Type {}, type {}", agencyRegisterInfo.getShadowType(), agencyRegisterInfo.getType());
        if (EMemberType.AUTHZDCENTER.getCode().equalsIgnoreCase(agencyRegisterInfo.getShadowType())) {
            agencyRegisterInfo.setType(EMemberType.AUTHZDCENTER);
        } else if (EMemberType.PRODUCTSERVICE.getCode().equalsIgnoreCase(agencyRegisterInfo.getShadowType())) {
            agencyRegisterInfo.setType(EMemberType.PRODUCTSERVICE);
        } else {
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,"Unmatched member type = " + agencyRegisterInfo.getShadowType());
        }
        memberService.updateAgency(agencyRegisterInfo, userId, EApplicationStatus.PENDDING);
        
        this.processTodo(agencyRegisterInfo, false);

        if (EMemberType.PRODUCTSERVICE == agencyRegisterInfo.getType()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/agency/productproviderinfo/view/" + userId));
        } else {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/agency/servicecentorinfo/view/" + userId));
        }
    }

    /**
     * Send message to Channel or Production Dept.
     * @param agencyRegisterInfo
     */
	private void processTodo(AgencyRegisterInfoDto agencyRegisterInfo, boolean insert) {
		String content = null;
		if (insert) {
			content = agencyRegisterInfo.getUsername();
		} else {
			content = securityContext.getCurrentUserName();
		}
		if (EMemberType.PRODUCTSERVICE == agencyRegisterInfo.getType()) {
        	String messageKey = "productprovider.audit.message";
        	messageService.sendMessage(EMessageType.TODO, messageKey, EBizRole.PLATFORM_PRODUCTION_DEPT, content);
        } else {
        	String messageKey = "servicecenter.audit.message";
        	messageService.sendMessage(EMessageType.TODO, messageKey, EBizRole.PLATFORM_CHANNEL_DEPT, content);
        }
	}

    /** Private methods */
    private void addAllPageStateToModel(Model model) {
        // set prefix attributes
        model.addAttribute("agencyPrefix", "common.");
        model.addAttribute("serviceCentorPrefix", "serviceCenter.");
        model.addAttribute("productProviderPrefix", "productProvider.");
    }

    private void addAllBasicOptionsToModel(Model model) {
        // DynamicOption
        model.addAttribute("bankList", getDynamicOptions(EOptionCategory.BANK, true));
        model.addAttribute("provinceList", getDynamicOptions(EOptionCategory.REGION, true));
        model.addAttribute("industryList", getDynamicOptions(EOptionCategory.ORG_INDUSTRY, true));
        model.addAttribute("companyList",companyService.findAll());
    }

    private void addAllVMPrefixToModel(Model model) {
        model.addAttribute("hasPrefix", ApplicationConstant.VM_HASPREFIX);
        model.addAttribute(ApplicationConstant.VM_PREFIX_ORG, "agencyApplication.");
    }

    private DynamicOption loadInfo(EOptionCategory category, String code) {
        List<DynamicOption> list = getDynamicOptions(category, true);
        for (DynamicOption dynamicOption : list) {
            if (dynamicOption.getCode().equals(code)) {
                return dynamicOption;
            }
        }
        return new DynamicOption();
    }

    /**
     * version 20150317 代理费合同维护
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_URL)
    @RequiresPermissions(Permissions.TRANS_AGENCY_FEE_CONTRACT_MANAGE)
    public String authzdCtrListPage(HttpServletRequest request, Model model){
    	return "members/agencyfee_authzdctr_list";
    }
    
    /**
     * version 20150317 代理费合同维护  机构列表
     * @param query
     * @return
     */
    @RequestMapping(value = UrlConstant.TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_AUTHZDCTR_LIST_URL)
    @ResponseBody
    @RequiresPermissions(Permissions.TRANS_AGENCY_FEE_CONTRACT_MANAGE)
    public DataTablesResponseDto<AuthzdCtrAgencyFeeContractInfoViewDto> getAuthzdCtrList(@RequestBody AuthzdCtrSearchDto query){
    	LOGGER.info("getAuthzdCtrList() invoked");
		DataTablesResponseDto<AuthzdCtrAgencyFeeContractInfoViewDto> result = new DataTablesResponseDto<AuthzdCtrAgencyFeeContractInfoViewDto>();
		Page<AuthzdCtrAgencyFeeContractInfoView> overdueList = this.authzdCtrAgencyFeeConstractManageService.getAuthzdCtrs(query);
		List<AuthzdCtrAgencyFeeContractInfoViewDto> content= new ArrayList<AuthzdCtrAgencyFeeContractInfoViewDto>();
		for(AuthzdCtrAgencyFeeContractInfoView po : overdueList.getContent()){
			AuthzdCtrAgencyFeeContractInfoViewDto dto = ConverterService.convert(po,AuthzdCtrAgencyFeeContractInfoViewDto.class);
			if(po.getStartDt() != null) dto.setStartDt(DateUtils.formatDate(po.getStartDt(), "yyyy-MM-dd"));
    		if(po.getEndDt() != null) dto.setEndDt(DateUtils.formatDate(po.getEndDt(), "yyyy-MM-dd"));
    		content.add(dto);
		}
		result.setTotalDisplayRecords(overdueList.getTotalElements());
		result.setTotalRecords(overdueList.getTotalElements());
		result.setData(content);
		result.setEcho(query.getEcho());
    	return result;
    }
    
    /**
     * version 20150317 代理费合同维护 所属机构合同列表
     * @param query
     * @return
     */
    @RequestMapping(value = UrlConstant.TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_CONSTRACT_LIST_URL)
    @ResponseBody
    @RequiresPermissions(Permissions.TRANS_AGENCY_FEE_CONTRACT_MANAGE)
    public DataTablesResponseDto<AgencyFeeConstractDto> getConstractList(@RequestBody AuthzdCtrSearchDto query){
    	LOGGER.info("getAuthzdCtrList() invoked");
    	DataTablesResponseDto<AgencyFeeConstractDto> result = new DataTablesResponseDto<AgencyFeeConstractDto>();
    	Page<AgencyFeeConstractPo> overdueList = this.authzdCtrAgencyFeeConstractManageService.getConstracts(query);
    	List<AgencyFeeConstractDto> content= new ArrayList<AgencyFeeConstractDto>();
    	for(AgencyFeeConstractPo po : overdueList.getContent()){
    		AgencyFeeConstractDto dto = ConverterService.convert(po,AgencyFeeConstractDto.class);
    		dto.setStartDt(DateUtils.formatDate(po.getStartDt(), "yyyy-MM-dd"));
    		dto.setEndDt(DateUtils.formatDate(po.getEndDt(), "yyyy-MM-dd"));
    		content.add(dto);
    	}
    	result.setTotalDisplayRecords(overdueList.getTotalElements());
    	result.setTotalRecords(overdueList.getTotalElements());
    	result.setData(content);
    	result.setEcho(query.getEcho());
    	return result;
    }
    
    /**
     * version 20150317 代理费合同维护 获取合同信息
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_CONSTRACT_INFO_URL)
    @ResponseBody
    @RequiresPermissions(value={Permissions.TRANS_AGENCY_FEE_CONTRACT_MANAGE,Permissions.TRANS_AGENCY_FEE_CONTRACT_MANAGE},logical=Logical.OR)
    public AgencyFeeConstractDto getConstractInfo(@PathVariable("contractId") String id, HttpServletRequest request,
            HttpSession session, Model model){
    	LOGGER.info("getConstractInfo() invoked");
    	if(!NumberUtil.isNumeric(id) || Integer.parseInt(id) == 0) return new AgencyFeeConstractDto();
    	AgencyFeeConstractPo object = this.authzdCtrAgencyFeeConstractManageService.getConstract(id);
    	AgencyFeeConstractDto result = ConverterService.convert(object,AgencyFeeConstractDto.class);
    	result.setAcctNo(object.getAcctPo().getAcctNo());
    	result.setFullName(object.getAgency().getServiceCenterDesc());
    	result.setStartDt(DateUtils.formatDate(object.getStartDt(), "yyyy-MM-dd"));
		result.setEndDt(DateUtils.formatDate(object.getEndDt(), "yyyy-MM-dd"));
    	return result;
    }
    
    /**
     * version 20150317 代理费合同维护 保存合同信息
     * @param agencyFeeConstract
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_CONSTRACT_SAVE_URL, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(Permissions.TRANS_AGENCY_FEE_CONTRACT_MANAGE)
    public ResultDto saveConstractInfo(@OnValid(AgencyFeeContractSave.class) @RequestBody AgencyFeeConstractDto agencyFeeConstract,HttpServletRequest request, Model model) {
    	this.authzdCtrAgencyFeeConstractManageService.save(agencyFeeConstract);
        ResultDto dto = new ResultDto();
        dto.setCode(ResultCode.ACK);
        dto.setMessage("保存成功");
        return dto;
    }
    
}
