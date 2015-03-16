package com.hengxin.platform.escrow.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.escrow.dto.EswTransferOrderDto;
import com.hengxin.platform.escrow.dto.tsfr.TransferRespDto;
import com.hengxin.platform.escrow.entity.EswTransferOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.repository.EswTransferOrderRepository;

@Service
public class EswTransferRequiresNewService {
	
	@Autowired
	private EswTransferOrderRepository tsfrRepo;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswTransferOrderDto updateTransferOrderStatusProcess(String orderId, String currOpId){
		EswTransferOrderPo newPo= tsfrRepo.findOne(orderId);
		newPo.setLastMntOpId(currOpId);
		newPo.setLastMntTs(new Date());
		newPo.setStatus(EOrderStatusEnum.PROCESS);
		EswTransferOrderPo retPo = tsfrRepo.save(newPo);
		return ConverterService.convert(retPo, EswTransferOrderDto.class);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswTransferOrderDto updateTransferOrderResp(TransferRespDto respDto, String currOpId){
		EswTransferOrderPo newPo = tsfrRepo.findOne(respDto.getOrderId());
		newPo.setRetCode(respDto.getRetCode());
		newPo.setRetMsg(respDto.getRetMsg());
		newPo.setRespTs(new Date());
		newPo.setLastMntOpId(currOpId);
		newPo.setLastMntTs(new Date());
		newPo.setStatus(respDto.getStatus());
		EswTransferOrderPo retPo = tsfrRepo.save(newPo);
		return ConverterService.convert(retPo, EswTransferOrderDto.class);
	}
	
	@Transactional(readOnly = true)
	public EswTransferOrderDto getTransferOrderDto(String orderId){
		EswTransferOrderPo orderPo = tsfrRepo.findOne(orderId);
		return ConverterService.convert(orderPo, EswTransferOrderDto.class);
	}
	
}
