package com.hengxin.platform.escrow.controller;

import static com.hengxin.platform.common.constant.UrlConstant.SYSTEM_MANAGEMENT_ESWMSGLOG_SEARCH_URL;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.ebc.enums.EEbcCommandType;
import com.hengxin.platform.ebc.enums.EEbcMsgType;
import com.hengxin.platform.escrow.dto.EswMsgLogInfoDto;
import com.hengxin.platform.escrow.dto.EswMsgLogInfoSearchDto;
import com.hengxin.platform.escrow.service.EswMsgLogInfoService;
import com.hengxin.platform.security.Permissions;

/**
 * 与第三方资金托管交互日志
 * 
 * @author chenwulou
 *
 */
@Controller
public class EswMsgLogController extends BaseController{
	
	@Autowired
	private EswMsgLogInfoService eswMsgLogInfoService;
	
	/**
	 *  version 20150317 提供页面，展示与银盈通所有交互的日志 页面跳转
	 *  
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SYSTEM_MANAGEMENT_ESWMSGLOG_SEARCH_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = Permissions.SYSTEM_MANAGEMENT_ESWMSGLOG_QUERY)
	public String renderEswMsgLogInfoList(HttpServletRequest request, HttpSession session, Model model){
		model.addAttribute("eTrxTypeItems", getTrxTypeItems());
		model.addAttribute("eMsgTypeItems", getMsgTypeItems());
		return "escrow/esw_msg_log";
	}
	
	/**
	 *  version 20150317 提供页面，展示与银盈通所有交互的日志 页面跳转
	 *  
	 *  交互日志分页查询
	 * 
	 * @param request
	 * @param model
	 * @param eswMsgLogInfoSearchDto
	 * @return
	 */
	@RequestMapping(value = "esw/eswmsgloglist")
	@ResponseBody
	@RequiresPermissions(value = Permissions.SYSTEM_MANAGEMENT_ESWMSGLOG_QUERY)
	public DataTablesResponseDto<EswMsgLogInfoDto> getEswMsgLogInfoList(HttpServletRequest request, Model model,
			@RequestBody EswMsgLogInfoSearchDto eswMsgLogInfoSearchDto){
		DataTablesResponseDto<EswMsgLogInfoDto> eswMsgLogInfo = eswMsgLogInfoService.getEswMsgLogInfoList(eswMsgLogInfoSearchDto);
		return eswMsgLogInfo;
	}
	
	/**
	 * 交易类型list
	 * 
	 * @return
	 */
	private List<EnumOption> getTrxTypeItems() {
		List<EnumOption> options = new ArrayList<EnumOption>();
		options.add(new EnumOption(EEbcCommandType.NULL.getText(), EEbcCommandType.NULL.getText()));
		options.add(new EnumOption(EEbcCommandType.SIGNUP.getText(), EEbcCommandType.SIGNUP.getText()));
		options.add(new EnumOption(EEbcCommandType.MOBILE_VERIFY.getText(), EEbcCommandType.MOBILE_VERIFY.getText()));
		options.add(new EnumOption(EEbcCommandType.BINDING_BANK_CARD.getText(), EEbcCommandType.BINDING_BANK_CARD.getText()));
		options.add(new EnumOption(EEbcCommandType.GET_BANK_LIST.getText(), EEbcCommandType.GET_BANK_LIST.getText()));
		options.add(new EnumOption(EEbcCommandType.RECHARGE.getText(), EEbcCommandType.RECHARGE.getText()));
		options.add(new EnumOption(EEbcCommandType.WITHDRAWAL.getText(), EEbcCommandType.WITHDRAWAL.getText()));
		options.add(new EnumOption(EEbcCommandType.BINDING_RESULT_QUERY.getText(), EEbcCommandType.BINDING_RESULT_QUERY.getText()));
		options.add(new EnumOption(EEbcCommandType.EDIT_BINDING_BANK_CARD.getText(), EEbcCommandType.EDIT_BINDING_BANK_CARD.getText()));
		options.add(new EnumOption(EEbcCommandType.GET_RECHARGE_BANK_LIST.getText(), EEbcCommandType.GET_RECHARGE_BANK_LIST.getText()));
		options.add(new EnumOption(EEbcCommandType.EDIT_ACCOUNT.getText(), EEbcCommandType.EDIT_ACCOUNT.getText()));
		options.add(new EnumOption(EEbcCommandType.TRANSFER.getText(), EEbcCommandType.TRANSFER.getText()));
		options.add(new EnumOption(EEbcCommandType.ACCOUNT_BALANCE_QUERY.getText(), EEbcCommandType.ACCOUNT_BALANCE_QUERY.getText()));
		options.add(new EnumOption(EEbcCommandType.WITHDRAWAL_RESULT_QUERY.getText(), EEbcCommandType.WITHDRAWAL_RESULT_QUERY.getText()));
		options.add(new EnumOption(EEbcCommandType.ID_CHECK.getText(), EEbcCommandType.ID_CHECK.getText()));
		return options;
	}
	
	/**
	 * 报文类型list
	 * 
	 * @return
	 */
	private List<EnumOption> getMsgTypeItems() {
		List<EnumOption> options = new ArrayList<EnumOption>();
		options.add(new EnumOption(EEbcMsgType.NULL.getText(), EEbcMsgType.NULL.getText()));
		options.add(new EnumOption(EEbcMsgType.REQ.getText(), EEbcMsgType.REQ.getText()));
		options.add(new EnumOption(EEbcMsgType.RESP.getText(), EEbcMsgType.RESP.getText()));
		return options;
	}
}
