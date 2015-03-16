package com.hengxin.platform.escrow.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.escrow.dto.EswBankCardModOrderDto;
import com.hengxin.platform.escrow.dto.card.ModCardRespDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.entity.EswBankCardModOrderPo;
import com.hengxin.platform.escrow.repository.EswAcctRepository;
import com.hengxin.platform.escrow.repository.EswBankCardModOrderRepository;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.member.repository.UserRepository;

@Service
public class EswModCardRequiresNewService {

	@Autowired
	private EswAcctRepository eswAcctRepository;
	@Autowired
	private BankAcctRepository bankAcctRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EswBankCardModOrderRepository eswBankCardModOrderRepository;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswBankCardModOrderDto saveNewModBankCardOrder(EswBankCardModOrderDto orderDto){
		EswBankCardModOrderPo newPo = ConverterService.convert(orderDto, EswBankCardModOrderPo.class);
		EswBankCardModOrderPo retPo = eswBankCardModOrderRepository.save(newPo);
		return ConverterService.convert(retPo, EswBankCardModOrderDto.class);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveModBankCardSuccess(String acctNo, String provCode,String cityCode, String bankId, String bankCode, 
			String bankName, ModCardRespDto modCardRespDto, String currOpId) {
		EswAcctPo newAcctPo = eswAcctRepository.findOne(acctNo);
		newAcctPo.setProvCode(provCode);
		newAcctPo.setCityCode(cityCode);
		newAcctPo.setBankId(bankId);
		newAcctPo.setBankCode(bankCode);
		newAcctPo.setBankName(bankName);
		newAcctPo.setLastMntOpId(currOpId);
		newAcctPo.setLastMntTs(new Date());
		eswAcctRepository.save(newAcctPo);
		EswBankCardModOrderPo orderPo = eswBankCardModOrderRepository.findOne(modCardRespDto.getOrderId());
		modCardRespDto.getRetCode();
		modCardRespDto.getRetMsg();
		modCardRespDto.getStatus();
		orderPo.setRetCode(modCardRespDto.getRetCode());
		orderPo.setRetMsg(modCardRespDto.getRetMsg());
		orderPo.setStatus(modCardRespDto.getStatus());
		orderPo.setRespTs(new Date());
		orderPo.setLastMntOpid(currOpId);
		orderPo.setLastMntTs(new Date());
		eswBankCardModOrderRepository.save(orderPo);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveModBankCardFailed(ModCardRespDto modCardRespDto, String currOpId) {
		EswBankCardModOrderPo orderPo = eswBankCardModOrderRepository.findOne(modCardRespDto.getOrderId());
		modCardRespDto.getRetCode();
		modCardRespDto.getRetMsg();
		modCardRespDto.getStatus();
		orderPo.setRetCode(modCardRespDto.getRetCode());
		orderPo.setRetMsg(modCardRespDto.getRetMsg());
		orderPo.setStatus(modCardRespDto.getStatus());
		orderPo.setRespTs(new Date());
		orderPo.setLastMntOpid(currOpId);
		orderPo.setLastMntTs(new Date());
		eswBankCardModOrderRepository.save(orderPo);
	}
}
