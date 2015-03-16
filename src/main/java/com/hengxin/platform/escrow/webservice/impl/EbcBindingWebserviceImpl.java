package com.hengxin.platform.escrow.webservice.impl;

import java.util.List;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.ebc.dto.EbcBindingBankCardResponse;
import com.hengxin.platform.escrow.dto.card.AcctCardDto;
import com.hengxin.platform.escrow.dto.card.AddCardRespDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.entity.EswBankCardAddOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.repository.EswAcctRepository;
import com.hengxin.platform.escrow.repository.EswBankCardAddOrderRepository;
import com.hengxin.platform.escrow.service.EswAcctService;
import com.hengxin.platform.escrow.service.EswAddCardRequiresNewService;
import com.hengxin.platform.escrow.webservice.EbcBindingWebservice;
import com.hengxin.platform.member.repository.UserRepository;

@Service
@WebService(endpointInterface = "com.hengxin.platform.escrow.webservice.EbcBindingWebservice")
public class EbcBindingWebserviceImpl implements EbcBindingWebservice {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EswAcctService eswAcctService;
	@Autowired
	private EswAcctRepository eswAcctRepository;
	@Autowired
	private EswBankCardAddOrderRepository cardAddRepository;
	@Autowired
	private EswAddCardRequiresNewService addCardRequiresNewService;
	
	@Override
	public void doBindingResult(EbcBindingBankCardResponse response) {
		EswAcctPo eswAcctPo = eswAcctRepository.findByEswAcctNo(response.getMediumNo());
		String userId = eswAcctPo.getUserId();
		List<EswBankCardAddOrderPo> list = cardAddRepository
				.findByUserIdAndStatusOrderByCreateTsDesc(userId, EOrderStatusEnum.PROCESS);
		if(list.isEmpty()){
			return;
		}
		EswBankCardAddOrderPo addBankCardOrderPo = list.get(0);
		if (StringUtils.equals("00", response.getReturnCode())) {
			AddCardRespDto addCardRespDto = new AddCardRespDto();
			addCardRespDto.setOrderId(addBankCardOrderPo.getOrderId());
			addCardRespDto.setStatus(EOrderStatusEnum.SUCCESS); 	// 状态
			addCardRespDto.setRetCode(response.getReturnCode());  // 返回编码
			addCardRespDto.setRetMsg(response.getErrText()); 		// 返回信息
			addCardRespDto.setBankAssetsId(response.getAssetNo()); // 银行卡资产编号
			
			AcctCardDto cardDto = new AcctCardDto();
			cardDto.setAcctNo(eswAcctPo.getAcctNo());
			cardDto.setBankCardNo(addBankCardOrderPo.getBankCardNo()); // 银行卡号
			cardDto.setBankCardName(addBankCardOrderPo.getBankCardname()); // 持卡人姓名
			cardDto.setAssetNo(response.getAssetNo()); // 银行卡资产编号
			cardDto.setBankId(addBankCardOrderPo.getBankId()); // 总行联行号
			cardDto.setBankCode(addBankCardOrderPo.getBankCode()); // 分行联行号
			cardDto.setBankName(addBankCardOrderPo.getBankName()); // 发卡行名称
			cardDto.setBankTypeCode(addBankCardOrderPo.getBankTypeCode()); // 行别码
			cardDto.setProvinceCode(addBankCardOrderPo.getProvCode()); // 省份编码
			cardDto.setCityCode(addBankCardOrderPo.getCityCode()); // 城市编码
			addCardRequiresNewService.saveBindBankCardSuccess(cardDto, addCardRespDto, "sys");
		} 
		else {
			AddCardRespDto addCardRespDto = new AddCardRespDto();
			addCardRespDto.setOrderId(addBankCardOrderPo.getOrderId());
			addCardRespDto.setStatus(EOrderStatusEnum.FAILED); // 状态
			addCardRespDto.setRetCode(response.getReturnCode()); // 返回编码
			addCardRespDto.setRetMsg(response.getErrText()); // 返回信息
			addCardRequiresNewService.saveBindBankCardOrderFailed(addCardRespDto, "sys");
		}
	}
}
