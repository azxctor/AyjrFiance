package com.hengxin.platform.escrow.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.escrow.dto.AcctBalnceInfoDto;
import com.hengxin.platform.escrow.service.EswAcctBalanceService;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EswAcctStatusEnum;
import com.hengxin.platform.security.service.UserService;

@Controller
public class EswAcctBalanceController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EswAcctBalanceController.class);

	@Autowired
	private AcctService acctService;
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private EswAcctBalanceService eswAcctBalanceService;
	@Autowired
	private FundAcctBalService fundAcctBalService;
	
	@RequiresPermissions(value = { Permissions.ESW_ACCT_BAL_QRY })
	@RequestMapping(value = UrlConstant.ESCROW_ACCT_BALANCE_MGT_URL, method = RequestMethod.GET)
	public String eswAcctBalanceView(HttpServletRequest request, Model model){
		return "escrow/acct_balance_query";
	}
	
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_BAL_QRY })
	@RequestMapping(value = "esw/acct/get/balance", method = RequestMethod.POST)
	public AcctBalnceInfoDto signup(HttpServletRequest request, Model model, @RequestParam(value="acctNo") String acctNo) {
		LOGGER.debug("获取交易账户{}的托管账户余额",acctNo);
		AcctBalnceInfoDto info = null;
		AcctPo acct = acctService.getAcctByAcctNo(acctNo);
		if(acct==null){
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"交易账号不存在");
		}
		String userId = acct.getUserId();
		User user = userService.getUserByUserId(userId);
		if(user.getEswAcctStatus()==EswAcctStatusEnum.UNDO){
			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"交易账号未开通托管账户");
		}
		info = eswAcctBalanceService.getEswAcctBalanceInfo(acctNo);
		info.setAcctBal(fundAcctBalService.getUserSubAcctsSumBalAmt(userId));
		info.setAvlAmt(fundAcctBalService.getUserCurrentAcctAvlBal(userId));
		info.setCashAbleAmt(fundAcctBalService.getUserCashableAmt(userId));
		return info;
	}
}
