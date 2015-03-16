/*
 * Project Name: kmfex-platform
 * File Name: SigninController.java
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

package com.hengxin.platform.security.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.dto.CommonMenuDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.dto.upstream.SignInDto;
import com.hengxin.platform.common.service.NoticeService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MenuUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.common.validation.ValidateException;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.authc.SessionTimeoutAuthenticationFilter;
import com.hengxin.platform.security.service.AuthorizationService;
import com.hengxin.platform.security.service.UserService;
import com.hengxin.platform.security.util.KaptchaSupport;

/**
 * Class Name: SigninController
 * 
 * @author runchen
 */

@Controller
public class SigninController {

    private static final String GLOBAL_CONTEXT = "/web/";

    private static final Logger LOGGER = LoggerFactory.getLogger(SigninController.class);

    @Autowired
    private transient UserService userService;

    @Autowired
    private KaptchaSupport kaptchaSupport;

    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private SecurityContext secContext;

    @Autowired
    private MenuUtil roleMenuUtil;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private   NoticeService noticeService; 
    
    // TODO for performance test BEGIN
    @RequestMapping(value = "/members/signin/authc_int")
    @ResponseBody
    public ResultDto doShiroSinginInternal(@RequestParam String userName, @RequestParam String password) {
        if (!CommonBusinessUtil.isInternalLoginOpen()) {
            throw null;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doSignin() start: ");
        }
        SignInDto signInDto = new SignInDto();
        signInDto.setUserName(userName);
        signInDto.setPassword(password);
        try {
            SecurityContext.login(signInDto.getUserName(), signInDto.getPassword());
        } catch (Exception e) {
            LOGGER.warn("login failed", e);
            return ResultDtoFactory.toNack("login failed");
        }
        return ResultDtoFactory.toAck("login success");

    }

    // TODO for performance test END

    @RequestMapping(value = "/members/signin", method = RequestMethod.GET)
    public String register(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute(SessionTimeoutAuthenticationFilter.SESSION_TIMEOUT) != null) {
            model.addAttribute("session_timeout", true);
            request.getSession().removeAttribute(SessionTimeoutAuthenticationFilter.SESSION_TIMEOUT);
        }
        return "members/signin";
    }

    @RequestMapping(value = "/members/signin/authc", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto doShiroSingin(@OnValid @RequestBody SignInDto signInDto, BindingResult result,
            HttpServletRequest request, HttpSession session, Model model) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doSignin() start: ");
        }
        long start = System.currentTimeMillis();
        Session newSession = null;
        try {
            newSession = SecurityContext.login(signInDto.getUserName(), signInDto.getPassword());
        } catch (LockedAccountException e1) {
            result.rejectValue("password", "member.error.password.toomanyfailure");
        } catch (IncorrectCredentialsException e1) {
            result.rejectValue("password", "member.error.signin.fail");
        } catch (UnknownAccountException e1) {
            result.rejectValue("userName", "member.error.not_exist");
        } catch (DisabledAccountException e1) {
            result.rejectValue("userName", "member.error.username.status");
        }
        if (result.hasErrors()) {
            throw new ValidateException(result);
        }
        
        newSession.setAttribute("userNotices", noticeService.getNotices(secContext.getCurrentUserId()));
         
        newSession.setAttribute("userName", signInDto.getUserName());
        List<CommonMenuDto> sysMenu = roleMenuUtil.getSysMenu();
        if (sysMenu != null) {
        	newSession.setAttribute("menuList", sysMenu);
            newSession.setAttribute("menuMap", roleMenuUtil.getMenuMap(sysMenu));
        }
        long end = System.currentTimeMillis();
        LOGGER.debug("doShiroSingin completed for user {}, total time spent: {}ms", signInDto.getUserName(), end
                - start);
        if (!secContext.isPlatformUser()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/dashboard?login=true"));
        }else{//平台用户，统一进入消息代办
        	return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.COMMON_DEPT));
        }

        /*
        // 客服部
        if (secContext.isCustomerServiceUser() && secContext.hasPermission(Permissions.MEMBER_ADV_MAINT_INVST_FIN_VIEW)) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.MEMBER_MANAGER_INVEST_FIN_INFO_MAINT_URL));
        } else if (secContext.isCustomerServiceUser()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.QUERY_STATISTICS_MEMBER_APPROVE_QUERY_URL));
        }
        // 产品部
        else if (secContext.isProdDeptUser()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.MEMBER_MANAGER_AGENCY_INFO_MAINT_URL));
        }
        // 渠道部
        else if (secContext.isChannelDeptUser()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.MEMBER_MANAGER_AGENCY_INFO_MAINT_URL));
        }
        // 贷后管理员
        else if (secContext.isRiskControlDeptUser() && !secContext.hasPermission(Permissions.MARKETING_CREDITOR_VIEW)) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.QUERY_STATISTICS_FINANCE_FEE_QUERY_URL));
        }
        // 风控部
        else if (secContext.isRiskControlDeptUser()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.FINANCING_PACKAGE_RISK_DEPT_VIEW_URL));
        }
        // 交易部
        else if (secContext.isTransDeptUser()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.FINANCING_PACKAGE_TRANS_DEPT_VIEW_URL));
        }
        // 系统管理员
        else if (secContext.isSystemAdmin()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.SYSTEM_MANAGEMENT_ABNORMAL_WITHDRAW_URL));
        }
        // 结算部1
        else if (secContext.hasPermission(Permissions.SETTLEMENT_UNSIGNED_MEMBER_TOPUP)
                && !secContext.hasPermission(Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING)) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.MY_ACCOUNT_UNSIGNED_RECHARGE_URL));
        }
        // 结算部2
        else if (secContext.hasPermission(Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE)
                && !secContext.hasPermission(Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING)) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.MY_ACCOUNT_WITHDRAW_APPLY_APPROVE_URL));
        }
        // 结算部经理
        else if (secContext.hasPermission(Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING)) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.SETTLE_MANAGEMENT_DEFAULT_HANDLING_URL));
        } else if (secContext.hasPermission(Permissions.SETTLEMENT_DEMPARTMENT)) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.QUERY_STATISTICS_MEMBER_BASIC_INFO_QUERY_URL));
        }
        // 财务部
        else if (secContext.isFinanceDeptUser()) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.QUERY_STATISTICS_FINANCE_FEE_QUERY_URL));
        }
        //通用部门
        else if (secContext.hasPermission(Permissions.COMMON_DEPT)) {
            return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn(GLOBAL_CONTEXT
                    + UrlConstant.COMMON_DEPT));
        }
        
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/dashboard"));
        */
    }

    @RequestMapping("/captcha")
    public void show(HttpServletRequest request, HttpServletResponse response) throws Exception {
        kaptchaSupport.captcha(request, response);
    }

}
