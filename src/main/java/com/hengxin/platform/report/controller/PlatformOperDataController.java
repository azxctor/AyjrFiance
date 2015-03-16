package com.hengxin.platform.report.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.report.dto.PlatformOperDataDto;
import com.hengxin.platform.report.service.PlatformOperDataService;
import com.hengxin.platform.security.SecurityContext;

/**
 * 平台季度运营数据统计
 * 
 * @author juhuahuang
 *
 */
@Controller
public class PlatformOperDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlatformOperDataController.class);

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private PlatformOperDataService platOperService;

	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	// @RequiresPermissions(value = "")
	@RequestMapping(value = UrlConstant.QUERY_STATISTICS_PLATFORM_QUARTER_OPER_DATA_URL, method = RequestMethod.GET)
	public String quarterOperDataView(HttpServletRequest request, Model model) {
		Boolean isPlatformUser = securityContext.isPlatformUser();
		model.addAttribute("isPlatformUser", isPlatformUser);
		return "platform/quarter_oper_data";
	}

	
	@ResponseBody
	//@RequiresPermissions(value="")
	@RequestMapping(value = UrlConstant.QUERY_STATISTICS_PLATFORM_QUARTER_OPER_DATA_LIST_URL)
	public PlatformOperDataDto getQuarterOperData(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String searchDate) {
		LOGGER.info("季度数据表");
		PlatformOperDataDto pageData = platOperService.getQuarterOperData(searchDate);	
		return pageData;
	}

}
