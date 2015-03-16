package com.hengxin.platform.common.controller.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hengxin.platform.common.dto.MenuDto;
import com.hengxin.platform.common.enums.EMenuConstant;

public class MenuInterceptor implements HandlerInterceptor {

    private static final String WEB_CONTEXT = "/web/";
    public static final String MEMBER_PERM_AUTO_SUBSCRIBE_EDIT_URL = "autosubscribe/edit";
    public static final String MEMBER_PERM_AUTO_SUBSCRIBE_RELIEVE_URL = "autosubscribe/msg/relieve";
    public static final String MEMBER_PERM_AUTO_SUBSCRIBE_MSG_URL = "autosubscribe/msg";

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // do nothing
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // do nothing
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String url = request.getRequestURI();
        Map<String, MenuDto> menuMap = (Map<String, MenuDto>) session.getAttribute("menuMap");
        if (menuMap == null) {
            return true;
        }
        String contextPath = request.getContextPath();
        String context = "";
        if (StringUtils.isBlank(contextPath)) {
            context = WEB_CONTEXT;
        } else {
            context = contextPath + WEB_CONTEXT;
        }
        if (StringUtils.isNotBlank(url) && url.startsWith(context)) {
            url = url.substring(context.length());// remove '/web/'
        }

        // 代注册处理
        /***代注册已经修改为二级菜单
         if (StringUtils.isNotBlank(url) && UrlConstant.MEMBER_MANAGEMENT_REGISTER_FOR_USER_URL.equals(url)) {
          
            request.setAttribute("mainMenuCode", EMenuConstant.MEMBER_MANAGEMENT.toString());
            request.setAttribute("subMenuCode", EMenuConstant.MEMBER_MANAGER_AGENT_INVEST_FIN_INFO_MAINT.toString());
            return true;
        }**/ 
        
        // 自动申购编辑
         if (StringUtils.isNotBlank(url)
                && (MEMBER_PERM_AUTO_SUBSCRIBE_EDIT_URL.equals(url)
                        || MEMBER_PERM_AUTO_SUBSCRIBE_RELIEVE_URL.equals(url) || MEMBER_PERM_AUTO_SUBSCRIBE_MSG_URL
                            .equals(url))) {
            request.setAttribute("mainMenuCode", EMenuConstant.MEMBER_MY_SETTINGS.toString());
            request.setAttribute("subMenuCode", EMenuConstant.MEMBER_PERM_AUTO_SUBSCRIBE.toString());
            return true;
        }

        if (menuMap.get(url) == null) {
            return true;
        }

        if (menuMap.get(url) != null) {
            MenuDto menuDto = menuMap.get(url);
            String code = menuDto.getCode();
            if (StringUtils.isNotBlank(menuDto.getParentUrl())) {
                String parentCode = menuMap.get(menuDto.getParentUrl()).getCode();
                request.setAttribute("mainMenuCode", parentCode);
                request.setAttribute("subMenuCode", code);
            } else {
                request.setAttribute("subMenuCode", null);
                request.setAttribute("mainMenuCode", menuDto.getCode());
            }
        }
        return true;
    }

}
