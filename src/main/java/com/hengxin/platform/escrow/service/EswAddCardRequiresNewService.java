package com.hengxin.platform.escrow.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.escrow.dto.EswBankCardAddOrderDto;
import com.hengxin.platform.escrow.dto.card.AcctCardDto;
import com.hengxin.platform.escrow.dto.card.AddCardRespDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.entity.EswBankCardAddOrderPo;
import com.hengxin.platform.escrow.repository.EswAcctRepository;
import com.hengxin.platform.escrow.repository.EswBankCardAddOrderRepository;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;

@Service
public class EswAddCardRequiresNewService {

	@Autowired
	private BankAcctService bankAcctService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EswAcctRepository eswAcctRepository;
	@Autowired
	private EswBankCardAddOrderRepository eswBankCardAddOrderRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveBindBankCardSuccess(AcctCardDto acctCardDto,
			AddCardRespDto addCardRespDto, String currOpId) {

		// 保存托管账户上银行卡信息
		EswAcctPo acctPo = eswAcctRepository.findOne(acctCardDto.getAcctNo());
		acctPo.setBankAssetsId(acctCardDto.getAssetNo());
		acctPo.setBankCardName(acctCardDto.getBankCardName());
		acctPo.setBankCardNo(acctCardDto.getBankCardNo());
		acctPo.setBankCode(acctCardDto.getBankCode());
		acctPo.setBankId(acctCardDto.getBankId());
		acctPo.setBankName(acctCardDto.getBankName());
		acctPo.setBankTypeCode(acctCardDto.getBankTypeCode());
		acctPo.setCityCode(acctCardDto.getCityCode());
		acctPo.setProvCode(acctCardDto.getProvinceCode());
		acctPo.setLastMntOpId(currOpId);
		acctPo.setLastMntTs(new Date());
		eswAcctRepository.save(acctPo);
		 
		EswBankCardAddOrderPo cardAddOrderPo = null;
		if(null != addCardRespDto){
			// 将返回信息更新至新增指令中
		    cardAddOrderPo = eswBankCardAddOrderRepository
					.findOne(addCardRespDto.getOrderId());
			cardAddOrderPo.setBankAssetsId(addCardRespDto.getBankAssetsId());
			cardAddOrderPo.setRetCode(addCardRespDto.getRetCode());
			cardAddOrderPo.setRetMsg(addCardRespDto.getRetMsg());
			cardAddOrderPo.setStatus(addCardRespDto.getStatus());
			cardAddOrderPo.setRespTs(new Date());
			cardAddOrderPo.setLastMntOpid(currOpId);
			cardAddOrderPo.setLastMntTs(new Date());
			eswBankCardAddOrderRepository.save(cardAddOrderPo);
		}

		// 更新交易系统相关的银行卡数据
		BankAcct bankAcct = new BankAcct();
		bankAcct.setBnkAcctName(acctCardDto.getBankCardName());
		bankAcct.setBnkAcctNo(acctCardDto.getBankCardNo());
		bankAcct.setBnkBrch(acctCardDto.getBankName());
		bankAcct.setBnkCardImg(null);
		EBankType bankType = EBankType.translateFromBankTypeCode(acctCardDto
				.getBankTypeCode());
		if (null != bankType)
			bankAcct.setBnkCd(bankType.getCode());
		bankAcct.setBnkName(acctCardDto.getBankName());
		bankAcct.setBnkOpenCity(acctCardDto.getCityCode());
		bankAcct.setBnkOpenProv(acctCardDto.getProvinceCode());
		bankAcct.setCreateOpid(currOpId);
		bankAcct.setCreateTs(new Date());
		bankAcct.setLastMntOpid(null);
		bankAcct.setLastMntTs(null);
		bankAcct.setOldBnkAcctNo(null);
		bankAcct.setSignedAddlAgrmnt(null);
		String userId = null;
		if( null!= cardAddOrderPo){
			bankAcct.setSignedDt(cardAddOrderPo.getTrxDate());
			bankAcct.setUserId(cardAddOrderPo.getUserId());
			userId = cardAddOrderPo.getUserId();
		}
		else{
			bankAcct.setSignedDt(CommonBusinessUtil.getCurrentWorkDate());
			bankAcct.setUserId(currOpId);
			userId = currOpId;
		}
		bankAcct.setSignedFlg(EFlagType.YES.getCode());
		bankAcct.setTerminDt(null);
		bankAcctService.createBankAcct(bankAcct);

		// 更新用户信息上的绑卡状态
		UserPo userPo = userRepository.findUserByUserId(userId);
		userPo.setBindingCardStatus(BindingCardStatusEnum.DONE);
		userRepository.save(userPo);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswBankCardAddOrderDto saveBindBankCardOrderFailed(
			AddCardRespDto addCardRespDto, String currOpId) {
		// 更新绑卡失败信息至绑卡指令
		EswBankCardAddOrderPo orderPo = eswBankCardAddOrderRepository
				.findOne(addCardRespDto.getOrderId());
		orderPo.setRetCode(addCardRespDto.getRetCode());
		orderPo.setRetMsg(addCardRespDto.getRetMsg());
		orderPo.setStatus(addCardRespDto.getStatus());
		orderPo.setBankAssetsId(null);
		orderPo.setRespTs(new Date());
		EswBankCardAddOrderPo newPo = eswBankCardAddOrderRepository
				.save(orderPo);
		// 修改用户绑卡状态为 未绑卡
		String userId = newPo.getUserId();
		UserPo userPo = userRepository.findUserByUserId(userId);
		userPo.setBindingCardStatus(BindingCardStatusEnum.UNDO);
		userPo.setLastMntOpid(currOpId);
		userPo.setLastMntTs(new Date());
		userRepository.save(userPo);
		return ConverterService.convert(newPo, EswBankCardAddOrderDto.class);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswBankCardAddOrderDto saveNewBindBankCardOrder(
			EswBankCardAddOrderDto orderDto) {
		EswBankCardAddOrderPo savePo = ConverterService.convert(orderDto,
				EswBankCardAddOrderPo.class);
		EswBankCardAddOrderPo newPo = eswBankCardAddOrderRepository
				.save(savePo);
		String userId = newPo.getUserId();
		UserPo userPo = userRepository.findUserByUserId(userId);
		userPo.setBindingCardStatus(BindingCardStatusEnum.PROCESS);
		userRepository.save(userPo);
		return ConverterService.convert(newPo, EswBankCardAddOrderDto.class);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EswBankCardAddOrderDto saveBindBankCardOrderDealing(
			AddCardRespDto addCardRespDto, String currOpId) {
		// 更新绑卡处理中信息至绑卡指令
		EswBankCardAddOrderPo orderPo = eswBankCardAddOrderRepository
				.findOne(addCardRespDto.getOrderId());
		orderPo.setRetCode(addCardRespDto.getRetCode());
		orderPo.setRetMsg(addCardRespDto.getRetMsg());
		orderPo.setStatus(addCardRespDto.getStatus());
		orderPo.setBankAssetsId(null);
		orderPo.setRespTs(new Date());
		EswBankCardAddOrderPo newPo = eswBankCardAddOrderRepository
				.save(orderPo);
		// 修改用户绑卡状态为 未绑卡
		String userId = newPo.getUserId();
		UserPo userPo = userRepository.findUserByUserId(userId);
		userPo.setBindingCardStatus(BindingCardStatusEnum.PROCESS);
		userPo.setLastMntOpid(currOpId);
		userPo.setLastMntTs(new Date());
		userRepository.save(userPo);
		return ConverterService.convert(newPo, EswBankCardAddOrderDto.class);
	}

}
