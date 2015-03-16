package com.hengxin.platform.risk.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.risk.dto.QuestionReqDto;
import com.hengxin.platform.risk.dto.QuestionResDto;
import com.hengxin.platform.risk.enums.ERiskBearLevel;
import com.hengxin.platform.risk.service.InvestRiskBearService;
import com.hengxin.platform.risk.service.InvestorQuestionnaireService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class InvestorQuestionController extends BaseController {

    @Autowired
    private SecurityContext securityContext;
    
    @Autowired
    private InvestorQuestionnaireService investorQuestionnaireService;
    
    @Autowired
    private InvestRiskBearService investRiskBearService;
	
//    @ResponseBody
//    @RequestMapping(value = "risk/questionnaire/submit", method = RequestMethod.POST)
//    @RequiresPermissions(value = {Permissions.INVESTOR_MEMBER})
//    public ResultDto doQuestionnaire(HttpServletRequest request, Model mode, @RequestBody QuestionReqDto req){
//    	String currUserId = securityContext.getCurrentUserId();
//    	boolean hasEval = investorQuestionnaireService.hasQuestionnaire(currUserId);
//    	if (hasEval) {
//    		return ResultDtoFactory.toNack("已完成测评");
//    	}
//    	BigDecimal score = req.getScore();
//    	try{
//    		investorQuestionnaireService.doQuestionnaire(currUserId, score);
//    	}catch(BizServiceException ex){
//    		return ResultDtoFactory.toNack(ex.getError());
//    	}catch(Exception ex){
//    		return ResultDtoFactory.toCommonError(ex);
//    	}
//    	return ResultDtoFactory.toAck("测评提交成功");
//    }
    
    @ResponseBody
    @RequestMapping(value = "risk/questionnaire/submit", method = RequestMethod.POST)
    @RequiresPermissions(value = {Permissions.INVESTOR_MEMBER})
    public QuestionResDto doQuestionnaire(HttpServletRequest request, Model mode, @RequestBody QuestionReqDto req){
    	String currUserId = securityContext.getCurrentUserId();
    	QuestionResDto result = new QuestionResDto();
    	boolean hasEval = investorQuestionnaireService.hasQuestionnaire(currUserId);
    	if (hasEval) {
    		result.setStatus("R");
    		return result;
    	}
    	BigDecimal score = req.getScore();
    	try {
    		investorQuestionnaireService.doQuestionnaire(currUserId, score);
    		ERiskBearLevel level = investRiskBearService.getUserRiskBearLevel(currUserId);
    		result.setLevel(level.getText());
    	} catch(BizServiceException ex) {
    		result.setStatus("F");
    	} catch(Exception ex) {
    		result.setStatus("F");
    	}
    	return result;
    }
	
}
