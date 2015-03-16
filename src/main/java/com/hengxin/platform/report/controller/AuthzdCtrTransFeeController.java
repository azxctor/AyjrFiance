package com.hengxin.platform.report.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.PdfService;
import com.hengxin.platform.report.EReportOutputType;
import com.hengxin.platform.report.EReportType;
import com.hengxin.platform.report.ReportContext;
import com.hengxin.platform.report.ReportCriteria;
import com.hengxin.platform.report.dto.AuthzdCtrTransFeeDto;
import com.hengxin.platform.report.dto.AuthzdCtrTransFeeSearchDto;
import com.hengxin.platform.report.service.AuthzdCtrTransFeeService;
import com.hengxin.platform.report.service.ReportingService;
import com.hengxin.platform.security.Permissions;

@Controller
public class AuthzdCtrTransFeeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthzdCtrTransFeeController.class);

	@Autowired
	private AuthzdCtrTransFeeService authzdCtrTransFeeService;
	
	@Autowired
	private ReportingService reportingService;
	
	@Autowired
	private PdfService pdfService;

	@RequestMapping(value = UrlConstant.REPORT_AUTHZD_CTR_TRANSFER_FEE, method = RequestMethod.GET)
	@RequiresPermissions(value = Permissions.REPORT_AUTHZD_CTR_TRANS_FEE)
	public String home(Model model) {
		LOGGER.info("home() invoked");
		return "report/authzd_ctr_transfer_fee";
	}

	@RequestMapping(value = UrlConstant.REPORT_AUTHZD_CTR_TRANSFER_FEE_SEARCH)
	@ResponseBody
	@RequiresPermissions(value = Permissions.REPORT_AUTHZD_CTR_TRANS_FEE)
	public DataTablesResponseDto<AuthzdCtrTransFeeDto> search(
			@RequestBody AuthzdCtrTransFeeSearchDto request) {
		LOGGER.info("search() invoked");
		DataTablesResponseDto<AuthzdCtrTransFeeDto> resp = authzdCtrTransFeeService
				.getAuthzdCtrTransFeeItems(request);
		resp.setEcho(request.getEcho());

		return resp;
	}

	@RequestMapping(value = UrlConstant.REPORT_AUTHZD_CTR_TRANSFER_FEE_SEARCH_TOTAL)
	@ResponseBody
	@RequiresPermissions(value = Permissions.REPORT_AUTHZD_CTR_TRANS_FEE)
	public BigDecimal getTotalFee(
			@RequestBody AuthzdCtrTransFeeSearchDto request) {
		LOGGER.info("getTotalFee() invoked");
		return authzdCtrTransFeeService.getTotalFee(request);
	}

	@RequestMapping(value = UrlConstant.REPORT_AUTHZD_CTR_TRANSFER_FEE_DOWNLOAD, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequiresPermissions(value = Permissions.REPORT_AUTHZD_CTR_TRANS_FEE)
	public ModelAndView download(HttpServletRequest request,
			@RequestParam("beginDate") String beginDate,
			@RequestParam("endDate") String endDate,
			@RequestParam("keyword") String keyword) throws Exception {
		LOGGER.info("download() invoked");
		
		// 解决中文乱码问题
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		
		try {
			AuthzdCtrTransFeeSearchDto searchDto = new AuthzdCtrTransFeeSearchDto();
			searchDto.setBeginDate(beginDate);
			searchDto.setEndDate(endDate);
			searchDto.setKeyword(keyword);
			
			ReportCriteria criteria = new ReportCriteria();
			criteria.setOutputType(EReportOutputType.PDF);
			criteria.setReportType(EReportType.AuthzdCtrTransFee);
			criteria.setSearchDto(searchDto);
			
			ReportContext ctx = new ReportContext();
			ctx.setCriteria(criteria);
			ctx.setBusinessService(authzdCtrTransFeeService);
			
			reportingService.createReport(ctx);
			
			Map<String, Object> map = new HashMap<String, Object>();
	        map.put("reportContext", ctx);
	        return new ModelAndView(pdfService, map);
	        
		} catch (Exception e) {
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "Unexpected Error", e);
		}
	}

}
