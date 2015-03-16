package com.hengxin.platform.member.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.enums.EFormMode;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.member.domain.AutoSubscribeParam;
import com.hengxin.platform.member.dto.AutoSubscribeParamDto;
import com.hengxin.platform.member.service.AutoSubscribeParamService;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * AutoSubscribeController.
 *
 */
@Controller
public class AutoSubscribeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoSubscribeController.class);

    @Autowired
    private transient AutoSubscribeParamService autoSubscriberService;

    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private transient SecurityContext securityContext;

    @RequestMapping(value=UrlConstant.MEMBER_PERM_AUTO_SUBSCRIBE_URL, method = RequestMethod.GET)
    @RequiresPermissions({ Permissions.MY_SETTINGS_AUTOSUBS_SETTINGS })
    public String view(HttpServletRequest request, Model model) {
        LOGGER.debug("view() invoked");
        this.loadParam(model, false);
        return "members/auto_subscribe";
    }
    
    @RequestMapping(value="autosubscribe/msg",method = RequestMethod.GET)
    @RequiresPermissions({ Permissions.MY_SETTINGS_AUTOSUBS_SETTINGS })
    public String viewSuccessed(HttpServletRequest request, Model model) {
        LOGGER.debug("viewSuccessed() invoked");
        this.loadParam(model, false);
        model.addAttribute("renderMessage", true);
//        String message = MessageUtil.getMessage(new String("member.autosubscribe.subscribe.success"));
        model.addAttribute("message", ApplicationConstant.SUBSCRIBE_SUCCESS_MESSAGE);
        return "members/auto_subscribe";
    }
    
    @RequestMapping(value="autosubscribe/msg/relieve",method = RequestMethod.GET)
    @RequiresPermissions({ Permissions.MY_SETTINGS_AUTOSUBS_SETTINGS })
    public String viewRelieveed(HttpServletRequest request, Model model) {
        LOGGER.debug("viewRelieveed() invoked");
    	this.loadParam(model, false);
        model.addAttribute("renderMessage", true);
        model.addAttribute("message", ApplicationConstant.RELIEVE_SUCCESS_MESSAGE);
        return "members/auto_subscribe";
    }

    @RequestMapping(value = "autosubscribe/edit", method = RequestMethod.GET)
    @RequiresPermissions({ Permissions.MY_SETTINGS_AUTOSUBS_SETTINGS })
    public String edit(HttpServletRequest request, Model model) {
    	this.loadParam(model, true);
    	return "members/auto_subscribe";
    }

    private void loadParam(Model model, boolean editable) {
        String userId = securityContext.getCurrentUserId();
        List<AutoSubscribeParam> params = autoSubscriberService.getAutoSubscriberParam(userId);
        if (params.isEmpty()) {
			/** never subscribe. **/
        	AutoSubscribeParamDto dto = new AutoSubscribeParamDto();
        	dto.setFirstTime(true);
        	dto.setPending(true);
        	model.addAttribute("pendingParam", dto);
        	model.addAttribute("pendingExist", true);
        	model.addAttribute("formalExist", false);
        	model.addAttribute("tab", false);
        	model.addAttribute("editable", true);
        	model.addAttribute("mode", EFormMode.EDIT);
		} else {
			boolean pendingExist = false;
			boolean formalExist = false;
			boolean existPending = false;
			if (params.size() == 2) {
				existPending = true;
			}
			/** at most two params. **/
			for (AutoSubscribeParam po : params) {
				AutoSubscribeParamDto dto = new AutoSubscribeParamDto();
				boolean isRelieved = false;
				if ("N".equals(po.getActiveFlag()) && "N".equals(po.getTerminationFlag())) {
					dto.setPending(true);
				} else if ("N".equals(po.getActiveFlag()) && "Y".equals(po.getTerminationFlag())) {
					dto.setPending(false);
					isRelieved = true;
				}
				
				/** IF NOT isRelieved,  Clear all param. **/
				if (!isRelieved) {
					dto.renderRiskParam(po.getRiskParam());
					dto.renderRepaymentParam(po.getRepayment());
					dto.renderGuarantee(po.getRiskGuarantee());

					dto.setMaxDateRange(po.getMaxDateRange() == null ? 0 : po.getMaxDateRange().intValue());
					dto.setMinDateRange(po.getMinDateRange() == null ? 0 : po.getMinDateRange());
					dto.setMinAPRForDate(po.getMinAPRForDate() == null ? BigDecimal.ZERO
						: po.getMinAPRForDate().multiply(NumberUtil.getHundred()).setScale(1, RoundingMode.HALF_UP));
					dto.setMaxMonthRange(po.getMaxMonthRange() == null ? 0 : po.getMaxMonthRange());
					dto.setMinMonthRange(po.getMinMonthRange() == null ? 0 : po.getMinMonthRange());
					dto.setMinAPRForMonth(po.getMinAPRForMonth() == null ? BigDecimal.ZERO
						: po.getMinAPRForMonth().multiply(NumberUtil.getHundred()).setScale(1, RoundingMode.HALF_UP));
					
					/** format rate. **/
					dto.setMinAPRForDate(FormatRate.formatRate(dto.getMinAPRForDate()));
					dto.setMinAPRForMonth(FormatRate.formatRate(dto.getMinAPRForMonth()));
					
					dto.setMinBalance(po.getMinBalance());
					dto.setMaxSubscribeAmount(po.getMaxSubscribeAmount());

					dto.setRelieve(po.getTerminationFlag());
				}
				
				if (dto.isPending()) {
					model.addAttribute("pendingParam", dto);
					pendingExist = true;
				} else {
					model.addAttribute("formalParam", dto);
					formalExist = true;
					dto.setExistPending(existPending);
				}
				if (ApplicationConstant.PENDING_CODE.equals(dto.getRelieve())) {
			        model.addAttribute("renderMessage", true);
			        String remindMessage = MessageUtil.getMessage("member.warning.autosubscribe.relieve");
			        model.addAttribute("message", remindMessage);
				}
			}
			model.addAttribute("pendingExist", pendingExist);
			model.addAttribute("formalExist", formalExist);
			if (pendingExist && formalExist) {
				model.addAttribute("tab", true);
			} else {
				model.addAttribute("tab", false);
			}
			model.addAttribute("editable", editable);
			model.addAttribute("mode", editable ? EFormMode.EDIT : EFormMode.VIEW);
		}
    }

    /** Submit methods */
    @ResponseBody
    @RequestMapping(value = "autosubscribe/save", method = RequestMethod.POST)
    @RequiresPermissions({ Permissions.MY_SETTINGS_AUTOSUBS_SETTINGS })
    public ResultDto saveAutoSubscriberInfo(@OnValid @RequestBody AutoSubscribeParamDto autoSubscriberParam,
    		HttpServletRequest request, Model model) {
        LOGGER.debug("saveAutoSubscriberInfo() invoked");
        String userId = securityContext.getCurrentUserId();
        AutoSubscribeParam param = this.autoSubscriberService.getAutoSubscriberParam(userId, "N");
        if (param == null) {
        	param = new AutoSubscribeParam();
        	param.setActiveFlag("N");
        	param.setScore(BigDecimal.ZERO);
        }
        if (autoSubscriberParam.getMinAPRForDate() != null) {
        	BigDecimal dateARP = autoSubscriberParam.getMinAPRForDate();
            dateARP = dateARP.divide(NumberUtil.getHundred()).setScale(3, RoundingMode.HALF_UP);
        	param.setMinAPRForDate(dateARP);
		} else {
			param.setMinAPRForDate(BigDecimal.ZERO);
		}
    	param.setMinDateRange(autoSubscriberParam.getMinDateRange());
    	param.setMaxDateRange(autoSubscriberParam.getMaxDateRange());
    	if (autoSubscriberParam.getMinAPRForMonth() != null) {
    		BigDecimal monthARP = autoSubscriberParam.getMinAPRForMonth();
        	monthARP = monthARP.divide(NumberUtil.getHundred()).setScale(3, RoundingMode.HALF_UP);
        	param.setMinAPRForMonth(monthARP);
		} else {
			param.setMinAPRForMonth(BigDecimal.ZERO);
		}
    	param.setMinMonthRange(autoSubscriberParam.getMinMonthRange());
    	param.setMaxMonthRange(autoSubscriberParam.getMaxMonthRange());
        StringBuilder riskParam = new StringBuilder();
        riskParam.append(autoSubscriberParam.isRiskA() ? ERiskLevel.HIGH_QUALITY.getCode() + "," : "");
        riskParam.append(autoSubscriberParam.isRiskB() ? ERiskLevel.MEDIUM.getCode() + "," : "");
        riskParam.append(autoSubscriberParam.isRiskC() ? ERiskLevel.ACCEPTABLE.getCode() + "," : "");
        riskParam.append(autoSubscriberParam.isRiskD() ? ERiskLevel.HIGH_RISK.getCode() + "," : "");
        if (riskParam.length() > 0 ) {
        	param.setRiskParam(riskParam.substring(0, riskParam.length() - 1));
        } else {
        	param.setRiskParam(riskParam.toString());
		}
        StringBuilder repaymentParam = new StringBuilder();
        repaymentParam.append(autoSubscriberParam.isRepaymentA() ? EPayMethodType.MONTH_AVERAGE_INTEREST.getCode() + "," : "");
        repaymentParam.append(autoSubscriberParam.isRepaymentB() ? EPayMethodType.MONTH_PRINCIPAL_INTEREST.getCode() + "," : "");
        repaymentParam.append(autoSubscriberParam.isRepaymentC() ? EPayMethodType.MONTH_INTEREST.getCode() + "," : "");
        repaymentParam.append(autoSubscriberParam.isRepaymentD() ? EPayMethodType.ONCE_FOR_ALL.getCode() + "," : "");
        if (repaymentParam.length() > 0 ) {
        	param.setRepayment(repaymentParam.substring(0, repaymentParam.length() - 1));
        } else {
        	param.setRepayment(repaymentParam.toString());
        }
        StringBuilder guaranteeParam = new StringBuilder();
        guaranteeParam.append(autoSubscriberParam.isGuaranteeA() ? EWarrantyType.PRINCIPAL.getCode() + "," : "");
        guaranteeParam.append(autoSubscriberParam.isGuaranteeB() ? EWarrantyType.PRINCIPALINTEREST.getCode() + "," : "");
        guaranteeParam.append(autoSubscriberParam.isGuaranteeC() ? EWarrantyType.MONITORASSETS.getCode() + "," : "");
        guaranteeParam.append(autoSubscriberParam.isGuaranteeD() ? EWarrantyType.NOTHING.getCode() + "," : "");
        if (guaranteeParam.length() > 0 ) {
        	param.setRiskGuarantee(guaranteeParam.substring(0, guaranteeParam.length() - 1));
        } else {
        	param.setRiskGuarantee(guaranteeParam.toString());
        }
        if (autoSubscriberParam.getMinBalance() != null) {
        	BigDecimal balance = autoSubscriberParam.getMinBalance();
            balance = balance.setScale(0, RoundingMode.HALF_UP);
            param.setMinBalance(balance);
		} else {
			param.setMinBalance(BigDecimal.ZERO);
		}
        if (autoSubscriberParam.getMaxSubscribeAmount() != null) {
        	BigDecimal maxSubscribeAmount = autoSubscriberParam.getMaxSubscribeAmount();
            maxSubscribeAmount = maxSubscribeAmount.setScale(0, RoundingMode.HALF_UP);
            param.setMaxSubscribeAmount(maxSubscribeAmount);
		} else {
			param.setMaxSubscribeAmount(BigDecimal.ZERO);
		}

        this.autoSubscriberService.saveAutoSubscriberParam(param, userId);
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/autosubscribe/msg"));
    }

    @ResponseBody
    @RequestMapping(value = "autosubscribe/relieve", method = RequestMethod.POST)
    @RequiresPermissions({ Permissions.MY_SETTINGS_AUTOSUBS_SETTINGS })
    public ResultDto relieveAutoSubscriberInfo(HttpServletRequest request, Model model) {
        LOGGER.info("relieveAutoSubscriberInfo() invoked");
        String userId = securityContext.getCurrentUserId();
        this.autoSubscriberService.relieveParamStatus(userId);
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/autosubscribe/msg/relieve"));
    }

    /**
     * FIXME remove it when T+1 work as schedule.
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "autosubscribe/launch", method = RequestMethod.POST)
    public ResultDto launchAutoSubscriberInfo(HttpServletRequest request, Model model) {
    	LOGGER.debug("launchAutoSubscriberInfo() invoked");
        this.autoSubscriberService.launchPendingSubscribeParams();
        return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
    }

}
