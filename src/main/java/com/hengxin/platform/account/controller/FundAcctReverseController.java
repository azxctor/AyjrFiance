package com.hengxin.platform.account.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.account.dto.BankTrxDtlDto;
import com.hengxin.platform.account.dto.downstream.FundApplApproveDto;
import com.hengxin.platform.account.dto.downstream.ReverseApplSearchDto;
import com.hengxin.platform.account.dto.downstream.ReverseReqDto;
import com.hengxin.platform.account.dto.downstream.ReverseSearchDto;
import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.biz.req.UserReverseReq;
import com.hengxin.platform.fund.entity.ReverseApplPo;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;
import com.hengxin.platform.fund.service.UserFundAcctReverseService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * 资金账户冲正
 * 
 * @author dcliu
 * 
 */
@Controller
public class FundAcctReverseController extends BaseController {

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private UserFundAcctReverseService userFundAcctReverseService;

	@RequiresPermissions(value = { Permissions.SETTLEMENT_FUND_ACCT_REVERSE })
	@RequestMapping(value = UrlConstant.SETTLEMENT_REVERSE_LIST_URL, method = RequestMethod.GET)
	public String renderToReverseListView(HttpServletRequest request, Model model) {
		List<EnumOption> rechargeWithdrawFlagItems = new ArrayList<EnumOption>();
		List<ERechargeWithdrawFlag> rwFlagItems = Arrays.asList(
				ERechargeWithdrawFlag.ALL, ERechargeWithdrawFlag.RECHARGE,
				ERechargeWithdrawFlag.WITHDRAW);
		for (ERechargeWithdrawFlag rwFlag : rwFlagItems) {
			rechargeWithdrawFlagItems.add(new EnumOption(rwFlag.name(), rwFlag
					.getText()));
		}
		model.addAttribute("rwItems", rechargeWithdrawFlagItems);
		List<EnumOption> trxStatusItems = new ArrayList<EnumOption>();
		List<EBnkTrxStatus> statusItems = Arrays.asList(
				EBnkTrxStatus.ALL,EBnkTrxStatus.NORMAL,
				EBnkTrxStatus.DEALING,EBnkTrxStatus.REVERSED);
		for (EBnkTrxStatus st : statusItems) {
			trxStatusItems.add(new EnumOption(st.name(), st
					.getText()));
		}
		model.addAttribute("tsItems", trxStatusItems);
		List<EnumOption> rvsFlgItems = new ArrayList<EnumOption>();
		List<EFlagType> rvsFlgList = Arrays.asList(EFlagType.ALL,EFlagType.YES,EFlagType.NO);
		for(EFlagType flg:rvsFlgList){
			rvsFlgItems.add(new EnumOption(flg.name(), flg
					.getText()));
		}
		model.addAttribute("rvsFlgItems", rvsFlgItems);
		model.addAttribute("workDate", CommonBusinessUtil.getCurrentWorkDate());
		return "myaccount/fund_acct_reverse_list";
	}

	@ResponseBody
	@RequestMapping(value = "get/bank/trx/jnl/reverse/list")
	@RequiresPermissions(value = { Permissions.SETTLEMENT_FUND_ACCT_REVERSE ,
			Permissions.SETTLEMENT_FUND_ACCT_REVERSE_APPL }, logical = Logical.OR)
	public DataTablesResponseDto<BankTrxDtlDto> getBnkTrxReverseDtlList(
			HttpServletRequest request, HttpSession session, Model model,
			@RequestBody ReverseSearchDto searchDto) {
		return userFundAcctReverseService.getBnkTrxDtlList(searchDto);
	}
	
	@RequiresPermissions(value = { Permissions.SETTLEMENT_FUND_ACCT_REVERSE_APPL })
	@RequestMapping(value = UrlConstant.SETTLEMENT_REVERSE_APPL_LIST_URL, method = RequestMethod.GET)
	public String renderToReverseApplListView(HttpServletRequest request, Model model) {
		List<EnumOption> rechargeWithdrawFlagItems = new ArrayList<EnumOption>();
		List<ERechargeWithdrawFlag> rwFlagItems = Arrays.asList(
				ERechargeWithdrawFlag.ALL, ERechargeWithdrawFlag.RECHARGE,
				ERechargeWithdrawFlag.WITHDRAW);
		for (ERechargeWithdrawFlag rwFlag : rwFlagItems) {
			rechargeWithdrawFlagItems.add(new EnumOption(rwFlag.name(), rwFlag
					.getText()));
		}
		model.addAttribute("rwItems", rechargeWithdrawFlagItems);
		List<EnumOption> trxStatusItems = new ArrayList<EnumOption>();
		List<EBnkTrxStatus> statusItems = Arrays.asList(
				EBnkTrxStatus.ALL,EBnkTrxStatus.NORMAL,
				EBnkTrxStatus.DEALING,EBnkTrxStatus.REVERSED);
		for (EBnkTrxStatus st : statusItems) {
			trxStatusItems.add(new EnumOption(st.name(), st
					.getText()));
		}
		model.addAttribute("tsItems", trxStatusItems);
		List<EnumOption> rvsFlgItems = new ArrayList<EnumOption>();
		List<EFlagType> rvsFlgList = Arrays.asList(EFlagType.ALL,EFlagType.YES,EFlagType.NO);
		for(EFlagType flg:rvsFlgList){
			rvsFlgItems.add(new EnumOption(flg.name(), flg
					.getText()));
		}
		model.addAttribute("rvsFlgItems", rvsFlgItems);
		model.addAttribute("workDate", CommonBusinessUtil.getCurrentWorkDate());
		return "myaccount/fund_acct_reverse_appl_list";
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.SETTLEMENT_FUND_ACCT_REVERSE_APPL })
	@RequestMapping(value = "bank/trx/jnl/reverse/appl/create", method = RequestMethod.POST)
	public ResultDto doReverseAppl(HttpServletRequest request, Model mode,
			@RequestBody ReverseReqDto reverseDto) {
		try {
			UserReverseReq req = new UserReverseReq();
			req.setCurrOpid(securityContext.getCurrentUserId());
			req.setRvsBnkJnlNo(reverseDto.getRvsJnlNo());
			req.setTrxMemo(reverseDto.getTrxMemo());
			req.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
			userFundAcctReverseService.doReverseAppl(req);
		} catch (BizServiceException ex) {
			return ResultDtoFactory.toNack(ex.getError());
		}
		return ResultDtoFactory.toAck("提交成功");
	}
	
	@ResponseBody
	@RequestMapping(value = "get/reverse/appl/list", method = RequestMethod.POST)
	@RequiresPermissions(value = { Permissions.SETTLEMENT_FUND_ACCT_REVERSE ,
			Permissions.SETTLEMENT_FUND_ACCT_REVERSE_APPL }, logical = Logical.OR)
	public DataTablesResponseDto<ReverseApplPo> getReverseApplList(HttpServletRequest request, Model mode,
			@RequestBody ReverseApplSearchDto searchDto) {
		return userFundAcctReverseService.getReverseApplListByRvsJnlNo(searchDto);
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.SETTLEMENT_FUND_ACCT_REVERSE })
	@RequestMapping(value = "bank/trx/jnl/reverse/appl/approve", method = RequestMethod.POST)
	public ResultDto doReverseApprove(HttpServletRequest request, Model mode,
			@RequestBody FundApplApproveDto dto) {
		try {
			userFundAcctReverseService.doReverseApprove(dto, securityContext.getCurrentUserId());
		} catch (BizServiceException ex) {
			return ResultDtoFactory.toNack(ex.getError());
		}
		return ResultDtoFactory
				.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
	}

}
