package com.hengxin.platform.escrow.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.escrow.dto.EswWithdrawalDto;
import com.hengxin.platform.escrow.dto.EswWithdrawalDto.SubmitWithdrawal;
import com.hengxin.platform.escrow.dto.EswWithdrawalInfoDto;
import com.hengxin.platform.escrow.dto.EswWithdrawalSearchDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.service.EswAcctService;
import com.hengxin.platform.escrow.service.EswWithdrawalService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * 第三方托管账户提现
 * 
 * @author chenwulou
 * 
 */
@Controller
public class EswWithdrawalController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswWithdrawalController.class);

	@Autowired
	private transient SecurityContext securityContext;

	@Autowired
	private EswWithdrawalService eswWithdrawalService;

	@Autowired
	private EswAcctService eswAcctService;

	/**
	 * 跳转到提现查询页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	// 这边需要传状态对象，是否平台用户
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequiresPermissions(value = { Permissions.ESW_ACCT_CASH })
	@RequestMapping(value = "esw/withdrawal/my/list", method = RequestMethod.GET)
	public String renderMaywithdrawalList(HttpServletRequest request,
			Model model) {
		Boolean isPlatformUser = securityContext.isPlatformUser();// 是否平台用户
		model.addAttribute("isPlatformUser", isPlatformUser);
		model.addAttribute("eOrderStatusEnum", getCashPoolItems());
		return "escrow/withdraw_list";
	}

	@RequiresPermissions(value = { Permissions.ESW_ACCT_CASH })
	@RequestMapping(value = UrlConstant.ESCROW_WITHDRAWAL_LIST_URL, method = RequestMethod.GET)
	public String renderwithdrawalList(HttpServletRequest request, Model model) {
		Boolean isPlatformUser = securityContext.isPlatformUser();// 是否平台用户
		model.addAttribute("isPlatformUser", isPlatformUser);
		model.addAttribute("isMenuEnter", true);
		model.addAttribute("eOrderStatusEnum", getCashPoolItems());
		return "escrow/withdraw_list";
	}

	private List<EnumOption> getCashPoolItems() {
		List<EnumOption> options = new ArrayList<EnumOption>();
		options.add(new EnumOption(EOrderStatusEnum.ALL.name(),
				EOrderStatusEnum.ALL.getText()));
		options.add(new EnumOption(EOrderStatusEnum.WAITING.name(),
				EOrderStatusEnum.WAITING.getText()));
		// options.add(new EnumOption(EOrderStatusEnum.PROCESS.name(),
		// EOrderStatusEnum.PROCESS.getText()));
		options.add(new EnumOption(EOrderStatusEnum.SUCCESS.name(),
				EOrderStatusEnum.SUCCESS.getText()));
		options.add(new EnumOption(EOrderStatusEnum.FAILED.name(),
				EOrderStatusEnum.FAILED.getText()));
		// options.add(new EnumOption(EOrderStatusEnum.UNKNOW.name(),
		// EOrderStatusEnum.UNKNOW.getText()));
		options.add(new EnumOption(EOrderStatusEnum.ABANDON.name(),
				EOrderStatusEnum.ABANDON.getText()));
		return options;
	}

	/**
	 * 获取提现默认信息（银行卡号，持卡人姓名）
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "esw/getbankinfo", method = RequestMethod.GET)
	public EswWithdrawalDto getBankInfo(HttpServletRequest request, Model model) {
		EswWithdrawalDto eswWithdrawalDto = new EswWithdrawalDto();
		String userId = securityContext.getCurrentUserId();
		EswAcctPo eswAcctPo = eswAcctService.getEscrowAccountByUserId(userId);
		String bankCardNo = eswAcctPo.getBankCardNo();
		String bankCardName = eswAcctPo.getBankCardName();
		eswWithdrawalDto.setBankCardName(bankCardName);
		eswWithdrawalDto.setBankCardNo(bankCardNo);
		return eswWithdrawalDto;
	}

	/**
	 * 提现
	 * 
	 * @param request
	 * @param model
	 * @param eswWithdrawalDto
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_CASH })
	@RequestMapping(value = "esw/withdrawal", method = RequestMethod.POST)
	public ResultDto withdrawal(HttpServletRequest request, Model model,
			@OnValid @RequestBody EswWithdrawalDto eswWithdrawalDto) {
		LOGGER.debug("withdrawal() start: ");
		validate(eswWithdrawalDto, new Class[] { SubmitWithdrawal.class });
		String userId = securityContext.getCurrentUserId();
		eswWithdrawalService.withdraw(userId, eswWithdrawalDto);
		return ResultDtoFactory.toAck("已提交银行处理");
	}

	/**
	 * 提现信息查询(分页)
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "esw/withdrawalinfolist")
	public DataTablesResponseDto<EswWithdrawalInfoDto> getWithdrawalInfoDtoList(
			HttpServletRequest request, Model model,
			@RequestBody EswWithdrawalSearchDto searchDto) {
		String userId = securityContext.getCurrentUserId();
		return eswWithdrawalService.getEswWithdrawalInfoList(userId, searchDto);
	}

	/**
	 * 即时提现状态查询
	 * 
	 * @param request
	 * @param model
	 * @param orderId
	 * @param cashId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "esw/getwithdrawalstate/{orderId}/{cashId}")
	public EswWithdrawalInfoDto getWithdrawalState(HttpServletRequest request,
			Model model, @PathVariable(value = "orderId") String orderId,
			@PathVariable(value = "cashId") String cashId) {
		String userId = securityContext.getCurrentUserId();
		return eswWithdrawalService.getWithdrawalState(userId, orderId, cashId);
	}
}
