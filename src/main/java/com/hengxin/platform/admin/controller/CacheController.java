package com.hengxin.platform.admin.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class CacheController {

	@RequiresPermissions(value = Permissions.SYSTEM_MANAGEMENT_CACHE_MANAGEMENT)
	@RequestMapping(value = UrlConstant.SYSTEM_MANAGEMENT_CACHE_MANAGEMENT_URL, method = RequestMethod.GET)
	public String cacheManagementHome() {
		return "admin/cache_management";
	}

	@RequiresPermissions(value = Permissions.SYSTEM_MANAGEMENT_CACHE_MANAGEMENT)
	@RequestMapping(value = "admin/cachemanagement/refreshcommon", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto refreshCommon() {
		CommonBusinessUtil.init();
		return ResultDtoFactory.toAck("刷新成功");
	}
	
	@RequiresPermissions(value = Permissions.SYSTEM_MANAGEMENT_CACHE_MANAGEMENT)
	@RequestMapping(value = "admin/cachemanagement/refreshauth", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto refreshAuth(@RequestParam String userName) {
		SecurityContext.clearAuthcCache(userName);
		SecurityContext.clearAuthzCache(userName);
		return ResultDtoFactory.toAck("刷新成功");
	}
	
	@RequiresPermissions(value = Permissions.SYSTEM_MANAGEMENT_CACHE_MANAGEMENT)
    @RequestMapping(value = "admin/cachemanagement/refreshallauthz", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto refreshAuth() {
        SecurityContext.clearAllAuthzCache();
        return ResultDtoFactory.toAck("刷新成功");
    }

}
