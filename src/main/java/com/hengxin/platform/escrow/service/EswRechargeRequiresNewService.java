package com.hengxin.platform.escrow.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.escrow.dto.EswRechargeOrderDto;
import com.hengxin.platform.escrow.entity.EswRechargeOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.repository.EswRechargeOrderRepository;
import com.hengxin.platform.fund.dto.biz.req.FundAcctRechargeReq;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.service.FundAcctRechargeService;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

@Service
public class EswRechargeRequiresNewService {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(EswRechargeRequiresNewService.class);

	@Autowired
	private UserService userService;
	@Autowired
	private FundAcctRechargeService fundAcctRechargeService;
	@Autowired
	private EswRechargeOrderRepository eswRechargeOrderRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswRechargeOrderDto addNewRechargeOrder(EswRechargeOrderDto orderDto) {
		EswRechargeOrderPo newPo = ConverterService.convert(orderDto,
				EswRechargeOrderPo.class);
		EswRechargeOrderPo retPo = eswRechargeOrderRepository.save(newPo);
		return ConverterService.convert(retPo, EswRechargeOrderDto.class);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswRechargeOrderDto updateRechargeOrderReqSignVal(String orderId,
			String reqSignVal) {
		EswRechargeOrderPo newPo = eswRechargeOrderRepository.findOne(orderId);
		newPo.setReqSignVal(reqSignVal);
		EswRechargeOrderPo retPo = eswRechargeOrderRepository.save(newPo);
		return ConverterService.convert(retPo, EswRechargeOrderDto.class);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswRechargeOrderDto saveAsyncRespRechargeOrder(String orderId,
			String retCode, String retMsg, String vouchNo, String trxNo,
			String transDate, String transTime, String dstbDataSign,
			EOrderStatusEnum orderStatus) {
		EswRechargeOrderPo newPo = eswRechargeOrderRepository
				.findByOrderId(orderId);
		newPo.setRetCode(retCode);
		newPo.setRetMsg(retMsg);
		newPo.setRespTs(new Date());
		newPo.setVouchNo(vouchNo);
		newPo.setTrxNo(trxNo);
		newPo.setTransDt(transDate);
		newPo.setTransTm(transTime);
		newPo.setRespSignVal(dstbDataSign);
		newPo.setStatus(orderStatus);
		EswRechargeOrderPo retPo = eswRechargeOrderRepository.save(newPo);
		EswRechargeOrderDto retDto = ConverterService.convert(retPo,
				EswRechargeOrderDto.class);
		if (EOrderStatusEnum.SUCCESS == orderStatus) {
			doFundAcctRecharge(retDto);
		}
		return retDto;
	}

	private void doFundAcctRecharge(EswRechargeOrderDto orderDto) {
		User user = userService.getUserByUserId(orderDto.getUserId());
		FundAcctRechargeReq faReq = new FundAcctRechargeReq();
		faReq.setUserId(orderDto.getUserId());
		faReq.setTrxAmt(orderDto.getTrxAmt());
		faReq.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
		faReq.setRelBnkId(orderDto.getTrxNo());
		faReq.setTrxMemo(StringUtils.defaultIfEmpty(orderDto.getTrxMemo(),
				"会员充值"));
		faReq.setCurrOpid(orderDto.getCreateOpId());
		faReq.setEventId(IdUtil.produce());
		faReq.setUserName(user.getName());
		faReq.setSignedFlg("Y");
		faReq.setBankCode(null);
		faReq.setBankAcctName(null);
		faReq.setBankAcctNo(null);
		faReq.setCashPool(ECashPool.ESCROW_EBC.getCode());
		faReq.setBizId(orderDto.getOrderId());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("执行充值动作");
		}
		fundAcctRechargeService.rechargeAmt(faReq);
	}

}
