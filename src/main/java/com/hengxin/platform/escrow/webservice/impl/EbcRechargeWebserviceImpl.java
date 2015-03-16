package com.hengxin.platform.escrow.webservice.impl;

import java.util.Arrays;
import java.util.Date;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.ebc.dto.EbcRechargeResponse;
import com.hengxin.platform.escrow.dto.EswRechargeOrderDto;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.service.EswRechargeOrderService;
import com.hengxin.platform.escrow.service.EswRechargeRequiresNewService;
import com.hengxin.platform.escrow.utils.ErrorUtils;
import com.hengxin.platform.escrow.webservice.EbcRechargeWebservice;

@Service
@WebService(endpointInterface = "com.hengxin.platform.escrow.webservice.EbcRechargeWebservice")
public class EbcRechargeWebserviceImpl implements EbcRechargeWebservice {

	@Autowired
	private EswRechargeOrderService eswRechargeOrderService;
	@Autowired
	private EswRechargeRequiresNewService rechargeRequiresNewService;

	@Override
	public void doRechargeResult(EbcRechargeResponse response) {
		EswRechargeOrderDto orderDto = eswRechargeOrderService
				.findEswRechargeOrderPoByOrderId(response.getDsOrderId());
		if (Arrays.asList(EOrderStatusEnum.SUCCESS).contains(orderDto.getStatus())){
			return;
		}
		orderDto.setRetCode(response.getReturnCode());
		orderDto.setRetMsg(ErrorUtils.getEswErroMsg(response.getReturnCode()));// 返回信息
		orderDto.setRespTs(new Date()); // 反馈时间
		orderDto.setVouchNo(response.getSysSn()); // 充值凭证号
		orderDto.setTrxNo(response.getDsOrderId()); // 反馈交易编号、系统交易参考号
		orderDto.setTransDt(response.getTransDate()); // 扣款日期
		orderDto.setTransTm(response.getTransTime()); // 扣款时间
		orderDto.setRespSignVal(response.getDstbDataSign()); // 反馈签名信息

		if (response.getReturnCode().equals("00")) {
			orderDto.setStatus(EOrderStatusEnum.SUCCESS);
		} else {
			orderDto.setStatus(EOrderStatusEnum.FAILED);
		}
		rechargeRequiresNewService.saveAsyncRespRechargeOrder(
				orderDto.getOrderId(), orderDto.getRetCode(),
				orderDto.getRetMsg(), orderDto.getVouchNo(),
				orderDto.getTrxNo(), orderDto.getTransDt(),
				orderDto.getTransTm(), orderDto.getRespSignVal(),
				orderDto.getStatus());
	}

}
