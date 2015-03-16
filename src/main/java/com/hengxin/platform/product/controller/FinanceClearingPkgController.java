package com.hengxin.platform.product.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.dto.PkgClearingPaymentAmtDto;
import com.hengxin.platform.product.dto.PkgPaymentClearDto;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.EPkgPaymentClearType;
import com.hengxin.platform.product.service.ClearingPkgWithRulesService;
import com.hengxin.platform.product.service.FinancingPackageDefaultService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.product.enums.ENumberOperator;

@Controller
public class FinanceClearingPkgController extends BaseController {

    private final static Logger LOG = LoggerFactory.getLogger(FinanceClearingPkgController.class);

    @Autowired
    private SecurityContext securityContext;
    
    @Autowired
    private ClearingPkgWithRulesService clearingPkgWithRulesService;

    @Autowired
    private FinancingPackageDefaultService financingPackageDefaultService;

    private static final String CLEARE_SUCCESS = "清分成功";
    
    /**
     * 
     * 违约处理列表页面加载
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.SETTLE_MANAGEMENT_DEFAULT_HANDLING_URL)
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING })
    public String renderClearingPackageList(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("cleared", false);
        List<EnumOption> clearTypeList = getStaticOptions(EPkgPaymentClearType.class, true);
        model.addAttribute("clearTypeList", clearTypeList);
        List<EnumOption> operationList = getStaticOptions(ENumberOperator.class, true);
        model.addAttribute("operationList", operationList);
        return "product/clearing_pkg_schd_list";
    }
    
    @ResponseBody
    @RequestMapping(value = "/payment/clear/{packageId}/{period}/rules")
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING })
    public ResultDto paymentScheduleClear(HttpServletRequest request, @RequestBody PkgPaymentClearDto clearDto){
        String operatorId = securityContext.getCurrentUserId();
    	try{
    		//check params
    		checkClearingParams(clearDto);
            PaymentSchedule schedule = financingPackageDefaultService.getPaymentScheduleByPackageIdAndPeriod(clearDto.getPackageId(), clearDto.getPeriod());
            if (schedule != null) {
	            if (EPayStatus.OVERDUE == schedule.getStatus()) {
	            	clearingPkgWithRulesService.clearingProductPackageInOverdue(clearDto, operatorId);
	            } else if (EPayStatus.COMPENSATING == schedule.getStatus()) {
	            	clearingPkgWithRulesService.clearingProductPackageInCompensating(clearDto, operatorId);
	            } else if (EPayStatus.COMPENSATORY == schedule.getStatus()) {
	            	clearingPkgWithRulesService.clearingProductPackageInCompensatory(clearDto, operatorId);
	            }
            }
    	}catch(BizServiceException ex){
    		LOG.error("error in clearing {} ",ex);
    		throw ex;
    	}
    	return ResultDtoFactory.toAck(CLEARE_SUCCESS);
    }
    
    private void checkClearingParams(PkgPaymentClearDto clearDto){	
    	 if(clearDto.getClearAmt().compareTo(BigDecimal.ZERO) <= 0){
    		 throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "清分金额应该大于0.00");
    	 }
    }
    
    @ResponseBody
    @RequestMapping(value = "/payment/clear/amt/info/{packageId}/{period}")
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING })
    public PkgClearingPaymentAmtDto getClearingPkgPaymentAmtInfo(
    		HttpServletRequest request, 
    		@PathVariable String packageId,
            @PathVariable Integer period){
        PaymentSchedule schedule = financingPackageDefaultService.getPaymentScheduleByPackageIdAndPeriod(packageId, period);
    	return clearingPkgWithRulesService.getClearingPkgAmtInfo(schedule);
    }
}
