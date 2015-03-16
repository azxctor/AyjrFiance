package com.hengxin.platform.account.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.hengxin.platform.account.dto.TransactionJournalDto;
import com.hengxin.platform.account.dto.upstream.TransactionJournalSearchDto;
import com.hengxin.platform.account.service.TranjourExcel;
import com.hengxin.platform.account.service.TransactionJournalService;
import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.Permissions;

/**
 * @author qimingzou
 *
 */
@Controller
public class TransactionJournalController extends BaseController {
	@Autowired
	private TransactionJournalService transactionJournalService;
	@Autowired
	private TranjourExcel tranjourExcel;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransactionJournalController.class);

	@RequestMapping(value = UrlConstant.ACCOUNT_TRANSACTION_JOURNAL, method = RequestMethod.GET)
	@RequiresPermissions(value = Permissions.INVSTR_TRANSACTION_JOURNAL_QUERY)
	public String home(Model model) {
		LOGGER.info("home() invoked");
		List<EnumOption> options = new ArrayList<EnumOption>();
		for (EFundUseType type : EFundUseType.getInvstrUseTypes()) {
			options.add(new EnumOption(type.name(), type.getText()));
		}
		model.addAttribute("useTypeList", options);
		return "packet/member_stream_detail";
	}

	@RequestMapping(value = UrlConstant.ACCOUNT_TRANSACTION_JOURNAL_SEARCH)
	@ResponseBody
	@RequiresPermissions(value = Permissions.INVSTR_TRANSACTION_JOURNAL_QUERY)
	public DataTablesResponseDto<TransactionJournalDto> search(
			@RequestBody TransactionJournalSearchDto request) {
		LOGGER.info("search() invoked");   
		List<String> dataProps = request.getDataProps();
        Integer sortDirection = request.getSortedColumns().get(0);
        if(6==sortDirection){
        	dataProps.set(6, "trxAmt");
        } 	
        if(7==sortDirection){
        	dataProps.set(7, "trxAmt");
        } 	
		DataTablesResponseDto<TransactionJournalDto> resp = transactionJournalService
				.getTransactionJournalItems(request);
		resp.setEcho(request.getEcho()); 
		return resp;
	}
	
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequestMapping(value = UrlConstant.ACCOUNT_TRANSACTION_JOURNAL_EX_SEARCH,method = RequestMethod.POST) 
	@RequiresPermissions(value = Permissions.INVSTR_TRANSACTION_JOURNAL_EX_QUERY)
	public ModelAndView searchEx(HttpServletRequest request, Model model,
            @RequestParam("keyword") String keyword, 
            @RequestParam("useType") String useType,
            @RequestParam("beginDate") String  beginDate, 
            @RequestParam("endDate") String endDate,
            @RequestParam("agent") String agent, 
            @RequestParam("agentName") String agentName) {
		 try {
			    LOGGER.info("search() invoked");  
			    TransactionJournalSearchDto dto=new TransactionJournalSearchDto(); 
	            if (!StringUtils.isBlank(beginDate)) {
	            	dto.setBeginDate(DateUtils.getDate(beginDate, LiteralConstant.YYYY_MM_DD));
	            }
	            if (!StringUtils.isBlank(endDate)) {
	            	dto.setEndDate(DateUtils.getDate(endDate, LiteralConstant.YYYY_MM_DD));
	            }
	            if (!StringUtils.isBlank(useType)) {
	            	dto.setUseType(EFundUseType.valueOf(useType));
	            } 
	            if (!StringUtils.isBlank(keyword)) { 
	            	keyword = new String(keyword.getBytes("ISO8859-1"),"UTF-8");
	            } 
	            if (!StringUtils.isBlank(agent)) { 
	            	agent = new String(agent.getBytes("ISO8859-1"),"UTF-8");
	            } 
	            if (!StringUtils.isBlank(agentName)) { 
	            	agentName = new String(agentName.getBytes("ISO8859-1"),"UTF-8");
	            } 
	             
			    dto.setAgent(agent);
			    dto.setAgentName(agentName); 
			    dto.setKeyword(keyword); 
				List<TransactionJournalDto> resp = transactionJournalService
						.getTransactionJournalExItems(dto); 
		        String fileName = "会员资金流水明细.xls";
		        String tempPath = AppConfigUtil.getTransactionJournalExcelTemplatePath();
		        Map<String, Object> map = new HashMap<String, Object>();
		        map.put("detailList", resp); 
		        map.put("fileName", fileName);
		        map.put("tempPath", tempPath);
		
		        return new ModelAndView(tranjourExcel, map);
		    } catch (Exception e) {
		        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
		    }
	}
	
	@RequestMapping(value = UrlConstant.ACCOUNT_TRANSACTION_JOURNAL_SEARCH_SUM)
	@ResponseBody
	@RequiresPermissions(value = Permissions.INVSTR_TRANSACTION_JOURNAL_QUERY)
	public Map<String,BigDecimal> search_sum(@RequestBody TransactionJournalSearchDto request) {
		LOGGER.info("search_sum() invoked");
		Map<String,BigDecimal> summarymap = transactionJournalService.getTransactionJournalItemsSum(request);
		return summarymap; 
	}
}
