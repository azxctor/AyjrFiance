package com.hengxin.platform.escrow.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.escrow.dto.EswTransferOrderDto;
import com.hengxin.platform.escrow.dto.EswTransferOrderSearchDto;
import com.hengxin.platform.escrow.dto.EswTransferOrderSumDto;
import com.hengxin.platform.escrow.entity.EswTransferOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.service.EswTransferService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class EswTransferController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswTransferController.class);

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private EswTransferService transferService;

	private static final List<EOrderStatusEnum> DEAL_STAUTS_OPTIONS = Arrays
			.asList(EOrderStatusEnum.WAITING);

	private static final List<EOrderStatusEnum> DONE_STAUTS_OPTIONS = Arrays
			.asList(EOrderStatusEnum.SUCCESS, EOrderStatusEnum.FAILED,
					EOrderStatusEnum.UNKNOW, EOrderStatusEnum.ABANDON);

	@RequiresPermissions(value = { Permissions.ESW_ACCT_TRANSFER })
	@RequestMapping(value = UrlConstant.ESCROW_ACCT_TRANSFER_MGT_URL, method = RequestMethod.GET)
	public String toTransferMgtPage(HttpServletRequest request, Model model) {
		List<EnumOption> dealStatusOptions = new ArrayList<EnumOption>();
		for (EOrderStatusEnum em : DEAL_STAUTS_OPTIONS) {
			dealStatusOptions.add(new EnumOption(em.name(), em.getText()));
		}
		List<EnumOption> doneStatusOptions = new ArrayList<EnumOption>();
		for (EOrderStatusEnum em : DONE_STAUTS_OPTIONS) {
			doneStatusOptions.add(new EnumOption(em.name(), em.getText()));
		}
		model.addAttribute("dealStatusOptions", dealStatusOptions);
		model.addAttribute("doneStatusOptions", doneStatusOptions);
		model.addAttribute("wordDate", DateUtils.formatDate(
				CommonBusinessUtil.getCurrentWorkDate(), "yyyy-MM-dd"));
		return "escrow/transfer_mgt";
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_TRANSFER })
	@RequestMapping(value = "esw/transfer/getTransferOrderList/deal", method = RequestMethod.POST)
	public DataTablesResponseDto<EswTransferOrderDto> getTransferOrderDtoListDeal(
			HttpServletRequest request, Model model,
			@RequestBody EswTransferOrderSearchDto searchDto) {
		return getTransferOrderDtoListData(searchDto, DEAL_STAUTS_OPTIONS);
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_TRANSFER })
	@RequestMapping(value = "esw/transfer/getTransferOrderList/done", method = RequestMethod.POST)
	public DataTablesResponseDto<EswTransferOrderDto> getTransferOrderDtoListDone(
			HttpServletRequest request, Model model,
			@RequestBody EswTransferOrderSearchDto searchDto) {
		return getTransferOrderDtoListData(searchDto, DONE_STAUTS_OPTIONS);
	}

	private DataTablesResponseDto<EswTransferOrderDto> getTransferOrderDtoListData(
			EswTransferOrderSearchDto searchDto,
			List<EOrderStatusEnum> statusList) {
		Page<EswTransferOrderPo> pageData = transferService
				.getTransferOrderData(searchDto, statusList);
		DataTablesResponseDto<EswTransferOrderDto> responseDtoList = new DataTablesResponseDto<EswTransferOrderDto>();
		List<EswTransferOrderDto> itemList = new ArrayList<EswTransferOrderDto>();
		responseDtoList.setEcho(searchDto.getEcho());
		for (EswTransferOrderPo order : pageData) {
			EswTransferOrderDto dto = ConverterService.convert(order,
					EswTransferOrderDto.class);
			itemList.add(dto);
		}
		responseDtoList.setData(itemList);
		responseDtoList.setTotalDisplayRecords(pageData.getTotalElements());
		responseDtoList.setTotalRecords(pageData.getTotalElements());
		return responseDtoList;
	}

	/**
	 * version 20150306:"资金划转"，添加汇总信息，当天总共转账金额.
	 * 
	 * 交易金额求和
	 * 
	 * @param request
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_TRANSFER })
	@RequestMapping(value = "esw/transfer/getTransferSumAmt", method = RequestMethod.POST)
	public EswTransferOrderSumDto getTransferOrderSumAmt(
			HttpServletRequest request, Model model, @RequestBody EswTransferOrderSearchDto searchDto) {
		return transferService.getTransferOrderSumAmt(searchDto);
	}

	/**
	 * 转账处理
	 * 
	 * @param request
	 * @param model
	 * @param trxDateStr
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_TRANSFER })
	@RequestMapping(value = "esw/transfer/doTransfer/{trxDateStr}", method = RequestMethod.POST)
	public ResultDto doTransfer(HttpServletRequest request, Model model,
			@PathVariable(value = "trxDateStr") String trxDateStr) {
		LOGGER.debug("转账处理...");
		String currUserId = securityContext.getCurrentUserId();
		Date trxDate = DateUtils.getDate(trxDateStr, "yyyy-MM-dd");
		List<EswTransferOrderDto> orderList = null;
		int countIdex = 0;
		do {
			orderList = transferService.getTransferOrderToTransfer(trxDate,
					EOrderStatusEnum.WAITING, Integer.valueOf(10000));
			for (EswTransferOrderDto order : orderList) {
				transferService.doTransfer(order, trxDate, currUserId);
			}
			if (orderList != null && !orderList.isEmpty()) {
				countIdex++;
			} else {
				countIdex = 100;
			}

		} while (countIdex < 10);
		return ResultDtoFactory.toAck("转账成功");
	}

}
