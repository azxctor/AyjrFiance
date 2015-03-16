package com.hengxin.platform.netting.controller;

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
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.netting.dto.RecvPayOrderDto;
import com.hengxin.platform.netting.dto.RecvPayOrderSumDto;
import com.hengxin.platform.netting.dto.RecvPaySearchDto;
import com.hengxin.platform.netting.entity.RecvPayOrderPo;
import com.hengxin.platform.netting.enums.NettingStatusEnum;
import com.hengxin.platform.netting.service.NettingOrderService;
import com.hengxin.platform.netting.service.RecvPayOrderQueryService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class NettingController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NettingController.class);

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private NettingOrderService nettingOrderService;

	@Autowired
	private RecvPayOrderQueryService recvPayOrderQueryService;

	@RequiresPermissions(value = { Permissions.FUND_ACCT_NETTING })
	@RequestMapping(value = UrlConstant.FUND_ACCT_NETTING_MGT_URL, method = RequestMethod.GET)
	public String toRecvPayOrderMgtPage(HttpServletRequest request, Model model) {
		LOGGER.debug("----");
		model.addAttribute("wordDate", DateUtils.formatDate(
				CommonBusinessUtil.getCurrentWorkDate(), "yyyy-MM-dd"));
		return "netting/netting_mgt";
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.FUND_ACCT_NETTING })
	@RequestMapping(value = "netting/getRecvPayOrderList/done")
	public DataTablesResponseDto<RecvPayOrderDto> getRecvPayOrderDataDone(
			@RequestBody RecvPaySearchDto searchDto) {
		return getRecvPayOrderData(searchDto,
				Arrays.asList(NettingStatusEnum.D));
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.FUND_ACCT_NETTING })
	@RequestMapping(value = "netting/getRecvPayOrderList/deal")
	public DataTablesResponseDto<RecvPayOrderDto> getRecvPayOrderDeal(
			@RequestBody RecvPaySearchDto searchDto) {
		return getRecvPayOrderData(searchDto,
				Arrays.asList(NettingStatusEnum.W));
	}

	private DataTablesResponseDto<RecvPayOrderDto> getRecvPayOrderData(
			RecvPaySearchDto searchDto, List<NettingStatusEnum> statusList) {
		Page<RecvPayOrderPo> pageData = recvPayOrderQueryService
				.getRecvPayOrderData(searchDto, statusList);
		DataTablesResponseDto<RecvPayOrderDto> responseDtoList = new DataTablesResponseDto<RecvPayOrderDto>();
		List<RecvPayOrderDto> itemList = new ArrayList<RecvPayOrderDto>();
		responseDtoList.setEcho(searchDto.getEcho());
		for (RecvPayOrderPo order : pageData) {
			RecvPayOrderDto dto = ConverterService.convert(order,
					RecvPayOrderDto.class);
			dto.setUserName(order.getUserPo().getName());
			dto.setAcctNo(order.getUserPo().getAccount().getAcctNo());
			ECashPool servProv = EnumHelper.translate(ECashPool.class,
					dto.getServProv());
			if (servProv != null) {
				dto.setServProv(servProv.getText());
			}
			itemList.add(dto);
		}
		responseDtoList.setData(itemList);
		responseDtoList.setTotalDisplayRecords(pageData.getTotalElements());
		responseDtoList.setTotalRecords(pageData.getTotalElements());
		return responseDtoList;
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.FUND_ACCT_NETTING })
	@RequestMapping(value = "netting/getRecvPaySumAmt", method = RequestMethod.GET)
	public RecvPayOrderSumDto getTransferOrderSumAmt(
			HttpServletRequest request, Model model, String trxDateStr) {
		return null;
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.FUND_ACCT_NETTING })
	@RequestMapping(value = "netting/doNetting/{trxDateStr}", method = RequestMethod.POST)
	public ResultDto doNetting(HttpServletRequest request, Model model,
			@PathVariable(value = "trxDateStr") String trxDateStr) {
		LOGGER.debug("资金轧差...");
		String currUserId = securityContext.getCurrentUserId();
		Date trxDate = DateUtils.getDate(trxDateStr, "yyyy-MM-dd");
		nettingOrderService.doNetting(trxDate, currUserId);
		return ResultDtoFactory.toAck("轧差成功");
	}

}
