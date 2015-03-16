package com.hengxin.platform.escrow.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.escrow.dto.EswBankCardAddOrderDto;
import com.hengxin.platform.escrow.dto.EswBankCardModOrderDto;
import com.hengxin.platform.escrow.entity.EswBankCardAddOrderPo;
import com.hengxin.platform.escrow.entity.EswBankCardModOrderPo;
import com.hengxin.platform.escrow.repository.EswBankCardAddOrderRepository;
import com.hengxin.platform.escrow.repository.EswBankCardModOrderRepository;

@Service
public class EswBankCardOrderService {
	
	@Autowired
	private EswBankCardAddOrderRepository eswBankCardAddOrderRepository;
	
	@Autowired
	private EswBankCardModOrderRepository eswBankCardModOrderRepository;
	
	@Transactional(readOnly=true)
	public EswBankCardAddOrderDto findEswBankCardAddOrderPoByUserId(String userId){
		EswBankCardAddOrderPo orderPo = eswBankCardAddOrderRepository.findByUserId(userId);
		return ConverterService.convert(orderPo, EswBankCardAddOrderDto.class);
	}

	@Transactional(readOnly=true)
	public EswBankCardModOrderDto findEswBankCardModOrderPoByUserId(String userId) {
		EswBankCardModOrderPo orderPo = eswBankCardModOrderRepository.findByUserId(userId);
		return ConverterService.convert(orderPo, EswBankCardModOrderDto.class);
	}
	
}
