package com.hengxin.platform.escrow.service;

import java.math.BigDecimal;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.ebc.dto.EbcAccountBalanceQueryRequest;
import com.hengxin.platform.ebc.dto.EbcAccountBalanceQueryResponse;
import com.hengxin.platform.ebc.dto.EbcErrorResponse;
import com.hengxin.platform.escrow.dto.CommandResponse;
import com.hengxin.platform.escrow.dto.AcctBalnceInfoDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.repository.EswAcctRepository;

@Service
public class EswAcctBalanceService extends EswBaseService {
	
	@Autowired
	private EswAcctRepository eswAcctRepository;
	
	public AcctBalnceInfoDto getEswAcctBalanceInfo(String acctNo){
		AcctBalnceInfoDto balDto = new AcctBalnceInfoDto();
		balDto.setAcctNo(acctNo);
		EbcAccountBalanceQueryRequest request = new EbcAccountBalanceQueryRequest();
		super.buildRequest(request);
		EswAcctPo eswAcctPo = eswAcctRepository.findOne(acctNo);
		request.setUserNo(eswAcctPo.getEswUserNo());
		request.setMediumNo(eswAcctPo.getEswAcctNo());
		request.setCardNo(eswAcctPo.getEswSubAcctNo());
		request.setOrderSn(IdUtil.produce());
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			EbcAccountBalanceQueryResponse response = (EbcAccountBalanceQueryResponse) rawResponse;
			balDto.setBalance(NumberUtils.createBigDecimal(response.getBalance()));
			balDto.setBankId(response.getBankId());
			balDto.setBankName(response.getBankName());
			balDto.setCurr(response.getCurrency());
			balDto.setEswAcctNo(response.getMediumNo());
			balDto.setEswSubAcctNo(response.getCardNo());
			balDto.setIsReal(response.getIsReal());
			balDto.setRetCode(response.getReturnCode());
			balDto.setRetMsg(response.getErrText());
			balDto.setServProv(response.getOwnerId());
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			balDto.setRetCode(response.getReturnCode());
			balDto.setRetMsg(response.getErrText());
			balDto.setBalance(BigDecimal.ZERO);
		}
		return balDto;
	}
	
}
