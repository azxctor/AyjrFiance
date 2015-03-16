package com.hengxin.platform.escrow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.escrow.dto.EswRechargeOrderDto;
import com.hengxin.platform.escrow.entity.EswRechargeOrderPo;
import com.hengxin.platform.escrow.repository.EswRechargeOrderRepository;

@Service
public class EswRechargeOrderService {
	
	@Autowired
	private EswRechargeOrderRepository eswRechargeOrderRepository;

	@Transactional(readOnly=true)
	public EswRechargeOrderDto findEswRechargeOrderPoByOrderId(String dsOrderId) {
		EswRechargeOrderPo newPo = eswRechargeOrderRepository.findByOrderId(dsOrderId);
		return ConverterService.convert(newPo, EswRechargeOrderDto.class);
	}
}
