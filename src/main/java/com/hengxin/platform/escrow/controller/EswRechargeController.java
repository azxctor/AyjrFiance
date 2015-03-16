package com.hengxin.platform.escrow.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.ebc.dto.bank.RechargeBank;
import com.hengxin.platform.escrow.dto.EswChargeDto;
import com.hengxin.platform.escrow.dto.EswRechargeRequestDto;
import com.hengxin.platform.escrow.dto.EswRechargeSearchDto;
import com.hengxin.platform.escrow.dto.EswRechargreInfoDto;
import com.hengxin.platform.escrow.entity.EswRechargeOrderPo;
import com.hengxin.platform.escrow.enums.EEbcUserType;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.service.EswRechargeService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class EswRechargeController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswRechargeController.class);

	@Autowired
	private transient SecurityContext securityContext;

	@Autowired
	private EswRechargeService rechargeService;

	/*
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_RECHARGE })
	@RequestMapping(value = "esw/recharge", method = RequestMethod.POST)
	public CommandRequest getRechargeRequest(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@OnValid @RequestBody EswChargeDto chargeDto) {
		String userId = securityContext.getCurrentUserId();
		return rechargeService.recharge(userId, chargeDto);
	}
    */
	
	//@RequiresPermissions(value = { Permissions.ESW_ACCT_RECHARGE })
	@RequestMapping(value = UrlConstant.RECHARGE_OPEN_IE_TO_BANK_URL, method = RequestMethod.GET)
	public String getRechargeRequest(HttpServletRequest request,
			HttpServletResponse response, Model model, 
			@RequestParam(value = "userId", required = true) String userId, 
			@RequestParam(value = "ebcBankId", required = true) String ebcBankId, 
			@RequestParam(value = "amount", required = true) BigDecimal amount, 
			@RequestParam(value = "productDesc", required = false) String productDesc,
			@RequestParam(value = "userType", required = true) EEbcUserType userType) {
		EswChargeDto reqDto = new EswChargeDto();
		reqDto.setAmount(amount);
		reqDto.setEbcBankId(ebcBankId);
		reqDto.setProductDesc(productDesc);
		reqDto.setUserType(userType);
		if(StringUtils.isBlank(userId)){
			userId = securityContext.getCurrentUserId();
		}
		EswRechargeRequestDto rechargeDto = rechargeService.recharge(userId, reqDto);
		model.addAttribute("rechargeDto", rechargeDto);
		model.addAttribute("rechargeUrl", CommonBusinessUtil.getEbcRechargePostUrl());// 充值跳转地址
		return "escrow/recharge_to_ie";
	}
	
	/**
	 * 获取Ebc银行列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param userType : 银行账户类型：个人/企业
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "esw/getbanklist", method = RequestMethod.GET)
	public List<RechargeBank> getEbcRechargeBankList(
			HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam EEbcUserType userType) {
		return rechargeService.getRechargeBankList(userType);
	}

	/**
	 * 跳转到充值查询页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
	@RequiresPermissions(value = { Permissions.ESW_ACCT_RECHARGE })
	@RequestMapping(value = "esw/recharge/my/list", method = RequestMethod.GET)
	public String rechargeMyList(HttpServletRequest request, Model model) {
		Boolean isPlatformUser = securityContext.isPlatformUser();
		model.addAttribute("isPlatformUser", isPlatformUser);
		model.addAttribute("eOrderStatusEnum", getCashPoolItems());
		return "escrow/recharge_list";
	}

	@RequiresPermissions(value = { Permissions.ESW_ACCT_RECHARGE })
	@RequestMapping(value = UrlConstant.ESCROW_RECHARGE_LIST_URL, method = RequestMethod.GET)
	public String rechargeList(HttpServletRequest request, Model model) {
		Boolean isPlatformUser = securityContext.isPlatformUser();
		model.addAttribute("isPlatformUser", isPlatformUser);
		model.addAttribute("isMenuEnter", true);
		model.addAttribute("eOrderStatusEnum", getCashPoolItems());
		return "escrow/recharge_list";
	}

	private List<EnumOption> getCashPoolItems() {
		List<EnumOption> options = new ArrayList<EnumOption>();
		options.add(new EnumOption(EOrderStatusEnum.ALL.name(),
				EOrderStatusEnum.ALL.getText()));
		options.add(new EnumOption(EOrderStatusEnum.PROCESS.name(),
				EOrderStatusEnum.PROCESS.getText()));
		options.add(new EnumOption(EOrderStatusEnum.SUCCESS.name(),
				EOrderStatusEnum.SUCCESS.getText()));
		options.add(new EnumOption(EOrderStatusEnum.FAILED.name(),
				EOrderStatusEnum.FAILED.getText()));
		options.add(new EnumOption(EOrderStatusEnum.ABANDON.name(),
				EOrderStatusEnum.ABANDON.getText()));
		return options;
	}

	/**
	 * 充值信息查询
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "esw/recharge/getRechargeTransList", method = RequestMethod.POST)
	public DataTablesResponseDto<EswRechargreInfoDto> getTransferOrderDtoList(
			HttpServletRequest request, Model model,
			@RequestBody EswRechargeSearchDto searchDto) {
		LOGGER.debug("充值记录查询...");
		String userId = securityContext.getCurrentUserId();
		boolean isPlatformUser = securityContext.isPlatformUser();
		Page<EswRechargeOrderPo> pageData = rechargeService
				.getEbcRechargeTransList(userId, searchDto, isPlatformUser);
		DataTablesResponseDto<EswRechargreInfoDto> responseDtoList = new DataTablesResponseDto<EswRechargreInfoDto>();
		List<EswRechargreInfoDto> itemList = new ArrayList<EswRechargreInfoDto>();
		responseDtoList.setEcho(searchDto.getEcho());
		for (EswRechargeOrderPo order : pageData) {
			EswRechargreInfoDto dto = ConverterService.convert(order,
					EswRechargreInfoDto.class);
			if (null != dto.getTransTm()) {
				String transTm = DateUtils.formatDate(
						DateUtils.getDate(dto.getTransTm(), "HHmmss"),
						"HH:mm:ss");
				dto.setTransTm(transTm);
			}
			if (null != dto.getTransDt()) {
				String transDt = DateUtils.formatDate(
						DateUtils.getDate(dto.getTransDt(), "yyyyMMdd"),
						"yyyy-MM-dd");
				dto.setTransDt(transDt);
			}
			itemList.add(dto);
		}
		responseDtoList.setData(itemList);
		responseDtoList.setTotalDisplayRecords(pageData.getTotalElements());
		responseDtoList.setTotalRecords(pageData.getTotalElements());
		return responseDtoList;
	}

	/**
	 * 充值指令作废
	 * 
	 * @param request
	 * @param model
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "esw/recharge/abolish", method = RequestMethod.POST)
	public ResultDto abolishRechargeOrder(HttpServletRequest request,
			Model model, @RequestBody String orderId) {
		rechargeService.abolishRechargeOrder(orderId);
		return ResultDtoFactory
				.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
	}
}
