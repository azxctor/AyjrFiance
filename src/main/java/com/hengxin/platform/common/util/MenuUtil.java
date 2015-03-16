/*
 * Project Name: kmfex-platform
 * File Name: FinanceProductController.java
 * Class Name: FinanceProductController
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
package com.hengxin.platform.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.hengxin.platform.common.dto.CommonMenuDto;
import com.hengxin.platform.common.dto.MenuDto;
import com.hengxin.platform.common.enums.EMenuConstant;
import com.hengxin.platform.security.SecurityContext;

/**
 * @author yingchangwang
 * 
 */
@Component
public class MenuUtil {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    public List<EMenuConstant> getDashboardLink() {
        List<EMenuConstant> result = new ArrayList<EMenuConstant>();

        if (securityContext.isFinancier() && securityContext.isInvestor()) {
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVERVIEW);
            result.add(EMenuConstant.DASHBOARD_MY_CREDITORS);
            result.add(EMenuConstant.DASHBOARD_FINANCING_PRODUCT_MANAGEMENRT);
            result.add(EMenuConstant.DASHBOARD_CREDITOR_MARKETING);
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVER_MORE);
        } else if (securityContext.isInvestor()) {
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVERVIEW);
            result.add(EMenuConstant.DASHBOARD_MY_SETTINGS);
            result.add(EMenuConstant.DASHBOARD_MY_CREDITORS);
            result.add(EMenuConstant.DASHBOARD_CREDITOR_MARKETING);
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVER_MORE);
        } else if (securityContext.isFinancier()) {
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVERVIEW);
            result.add(EMenuConstant.DASHBOARD_MY_SETTINGS);
            result.add(EMenuConstant.DASHBOARD_FINANCING_PRODUCT_MANAGEMENRT);
            result.add(EMenuConstant.DASHBOARD_MY_PAYMENT_SHCEDULE_TABLE);
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVER_MORE);
        } else if (securityContext.isInvestorFinancierTourist()) {
            result.add(EMenuConstant.DASHBOARD_CREDITOR_MARKETING);
            result.add(EMenuConstant.DASHBOARD_MEMBER_BASIC_INFO);
            result.add(EMenuConstant.DASHBOARD_MEMBER_INVESTOR_APPLY);
            result.add(EMenuConstant.DASHBOARD_MEMBER_FINANCIER_APPLY);
            result.add(EMenuConstant.DASHBOARD_MARKETING_MORE);
        } else if (securityContext.isAuthServiceCenter() && securityContext.isProdServ()) {
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVERVIEW);
            result.add(EMenuConstant.DASHBOARD_MY_SETTINGS);
            result.add(EMenuConstant.DASHBOARD_FINANCING_PRODUCT_APPLY);
            result.add(EMenuConstant.DASHBOARD_QUERY_STATISTICS);
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVER_MORE);
        } else if (securityContext.isAuthServiceCenter()) {
        	/** check agent or general. **/
        	if (securityContext.isAuthServiceCenterGeneral()) {
        		result.add(EMenuConstant.DASHBOARD_MEMBER_INFO_MAINTAIN);
        		result.add(EMenuConstant.DASHBOARD_CREDITOR_MARKETING);
        		result.add(EMenuConstant.DASHBOARD_EMPTY);
        		result.add(EMenuConstant.DASHBOARD_EMPTY);
        		result.add(EMenuConstant.DASHBOARD_MEMBER_INFO_MAINTAIN);
			} else if (securityContext.isAuthServiceCenterAgent()) {
				result.add(EMenuConstant.DASHBOARD_MEMBER_INFO_MAINTAIN);
				result.add(EMenuConstant.DASHBOARD_CREDITOR_MARKETING);
				result.add(EMenuConstant.DASHBOARD_EMPTY);
				result.add(EMenuConstant.DASHBOARD_EMPTY);
				result.add(EMenuConstant.DASHBOARD_MEMBER_INFO_MAINTAIN);
			} else {
				result.add(EMenuConstant.DASHBOARD_CREDITOR_MARKETING);
	            result.add(EMenuConstant.DASHBOARD_MY_SETTINGS);
	            result.add(EMenuConstant.DASHBOARD_MEMBER_INFO_MAINTAIN);
	            result.add(EMenuConstant.DASHBOARD_QUERY_STATISTICS);
	            result.add(EMenuConstant.DASHBOARD_CREDITOR_MARKETING);
			}
        } else if (securityContext.isProdServ()) {
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVERVIEW);
            result.add(EMenuConstant.DASHBOARD_MY_SETTINGS);
            result.add(EMenuConstant.DASHBOARD_FINANCING_PRODUCT_APPLY);
            result.add(EMenuConstant.DASHBOARD_FINANCING_PACKAGE_MANAGEMENT);
            result.add(EMenuConstant.DASHBOARD_ACCOUNT_OVER_MORE);
        } else if (securityContext.isProdServAthdCenterTourist()) {
            result.add(EMenuConstant.DASHBOARD_MY_SETTINGS);
            result.add(EMenuConstant.DASHBOARD_MEMBER_PROD_SERV_APPLY);
            result.add(EMenuConstant.DASHBOARD_MEMBER_ATHD_SERV_APPLY);
            result.add(EMenuConstant.DASHBOARD_CREDITOR_MARKETING);
            result.add(EMenuConstant.DASHBOARD_MARKETING_MORE);
        }
        return result;
    }

    /**
     * 
     * Description: 获取用户菜单
     * 
     * @return
     */
    public List<CommonMenuDto> getSysMenu() {

        List<CommonMenuDto> sysMenuList = new ArrayList<CommonMenuDto>();

        Map<String, EMenuConstant> urlInfoMappings = new HashMap<String, EMenuConstant>();
        EMenuConstant[] values = EMenuConstant.values();
        if (values == null || values.length == 0) {
            return null;
        }
        for (EMenuConstant menu : values) {
            if (StringUtils.isNotBlank(menu.getCode()) && StringUtils.isNotBlank(menu.getMenuUrl())) {
                urlInfoMappings.put(menu.getMenuUrl(), menu);
            }
        }

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        if (handlerMethods == null) {
            return null;
        }
        Map<EMenuConstant, List<CommonMenuDto>> tempMenuMap = new HashMap<EMenuConstant, List<CommonMenuDto>>();
        List<CommonMenuDto> childMenuList = null;
        for (Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            RequiresPermissions requiresPermissions = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
            RequestMapping requestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
            if (requiresPermissions != null && requestMapping != null) {
                String[] permissions = requiresPermissions.value();
                Logical logical = requiresPermissions.logical();
                String[] urls = requestMapping.value();
                RequestMethod[] requestMethods = requestMapping.method();
                if (permissions == null || permissions.length == 0 || urls == null || urls.length == 0) {
                    continue;
                }

                // if current url is not menu
                String currentUrl = urls[0];
                RequestMethod requestMethod = null;
                if (requestMethods != null && requestMethods.length > 0) {
                    requestMethod = requestMethods[0];
                }
                EMenuConstant eMenuConstant = urlInfoMappings.get(currentUrl);
                if (eMenuConstant == null) {
                    continue;
                }
                if ((logical == Logical.AND && securityContext.hasAllPermissions(permissions) 
                		|| logical == Logical.OR
                        && SecurityContext.hasAnyPermission(permissions))
                        && (requestMethod == null || requestMethod == RequestMethod.GET)) {
                    EMenuConstant parentMenu = urlInfoMappings.get(eMenuConstant.getParentUrl());
                    if (tempMenuMap.get(parentMenu) == null) {
                        childMenuList = new ArrayList<CommonMenuDto>();
                        childMenuList.add(new CommonMenuDto(eMenuConstant.getCode(), eMenuConstant.name(),
                                eMenuConstant.getMenuName(), eMenuConstant.getMenuIcon(), eMenuConstant.getMenuUrl(),
                                eMenuConstant.getParentUrl()));
                        tempMenuMap.put(parentMenu, childMenuList);
                    } else {
                        tempMenuMap.get(parentMenu).add(
                                new CommonMenuDto(eMenuConstant.getCode(), eMenuConstant.name(), eMenuConstant
                                        .getMenuName(), eMenuConstant.getMenuIcon(), eMenuConstant.getMenuUrl(),
                                        eMenuConstant.getParentUrl()));
                    }

                }

            }
        }

        if (tempMenuMap == null || tempMenuMap.isEmpty()) {
            return null;
        }
        CommonMenuDto firstLevel = null;
        for (Entry<EMenuConstant, List<CommonMenuDto>> entry : tempMenuMap.entrySet()) {
            EMenuConstant key = entry.getKey();
            firstLevel = new CommonMenuDto(key.getCode(), key.name(), key.getMenuName(), key.getMenuIcon(),
                    key.getMenuUrl(), key.getParentUrl());
            firstLevel.setSubItems(entry.getValue());
            Collections.sort(firstLevel.getSubItems(), new CommonMenuComparator());
            sysMenuList.add(firstLevel);
        }
        Collections.sort(sysMenuList, new CommonMenuComparator());
        return sysMenuList;
    }

    /**
     * 
     * Description: 当前用户所有菜单map
     * 
     * @param sysMenuList
     * @return
     */
    public Map<String, MenuDto> getMenuMap(List<CommonMenuDto> sysMenuList) {
        Map<String, MenuDto> menuMap = new HashMap<String, MenuDto>();
        MenuDto parentMenuDto = null;
        for (CommonMenuDto sysMenu : sysMenuList) {
            parentMenuDto = new MenuDto();
            String parentMenuUrl = sysMenu.getLink();
            if (menuMap.get(parentMenuUrl) == null) {
                parentMenuDto.setCode(sysMenu.getCode());
                parentMenuDto.setUrl(parentMenuUrl);
                parentMenuDto.setName(sysMenu.getLabel());
                menuMap.put(parentMenuUrl, parentMenuDto);
            }
            MenuDto subMenuDto = null;
            if (sysMenu.getSubItems() != null && !sysMenu.getSubItems().isEmpty()) {
                for (CommonMenuDto subSysMenu : sysMenu.getSubItems()) {
                    subMenuDto = new MenuDto();
                    String subMenuUrl = subSysMenu.getLink();
                    if (menuMap.get(subMenuUrl) == null) {
                        subMenuDto.setUrl(subMenuUrl);
                        subMenuDto.setCode(subSysMenu.getCode());
                        subMenuDto.setName(subSysMenu.getLabel());
                        subMenuDto.setParentUrl(parentMenuUrl);
                        menuMap.put(subMenuUrl, subMenuDto);
                    }
                }
            }
        }
        return menuMap;
    }

}
