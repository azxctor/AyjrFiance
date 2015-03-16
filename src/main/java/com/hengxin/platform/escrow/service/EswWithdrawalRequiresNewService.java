package com.hengxin.platform.escrow.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.escrow.dto.EswCashOrderDto;
import com.hengxin.platform.escrow.dto.withdrawal.WithdrawalRespDto;
import com.hengxin.platform.escrow.entity.EswCashOrderPo;
import com.hengxin.platform.escrow.repository.EswCashOrderRepository;
import com.hengxin.platform.fund.dto.biz.req.FundAcctWithDrawReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctWithdrawService;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.entity.UserPo;

@Service
public class EswWithdrawalRequiresNewService {
	
	@Autowired
	private AcctService acctService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EswCashOrderRepository eswCashOrderRepository;
    @Autowired
    private FundAcctWithdrawService fundAcctWithdrawService;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
 	public EswCashOrderDto saveNewWithdrawalOrder(EswCashOrderDto eswCashOrderDto){
		EswCashOrderPo newPo = ConverterService.convert(eswCashOrderDto, EswCashOrderPo.class);
		EswCashOrderPo retPo = eswCashOrderRepository.save(newPo);
		return ConverterService.convert(retPo, EswCashOrderDto.class);
 	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
 	public void saveWithdrawalOrderSuccess(WithdrawalRespDto respDto, String currOpId){
		// 更新提现指令数据
		EswCashOrderPo cashOrderPo = eswCashOrderRepository.findOne(respDto.getOrderId());
		cashOrderPo.setRetCode(respDto.getRetCode());
		cashOrderPo.setRetMsg(respDto.getRetMsg());
		cashOrderPo.setBal(respDto.getBalance());
		cashOrderPo.setCashId(respDto.getCashId());
		cashOrderPo.setStatus(respDto.getStatus());
		cashOrderPo.setTrxNo(respDto.getOutOrderNo());
		cashOrderPo.setLastMntOpId(currOpId);
		cashOrderPo.setLastMntTs(new Date());
		EswCashOrderPo retPo = eswCashOrderRepository.save(cashOrderPo);
		
		UserPo user = userRepository.findUserByUserId(cashOrderPo.getUserId());
		AcctPo acct = acctService.getAcctByAcctNo(cashOrderPo.getAcctNo());
		// 资金账户提现扣款
		FundAcctWithDrawReq req = new FundAcctWithDrawReq();
		req.setBankCode(null);
		req.setBizId(cashOrderPo.getOrderId());
		req.setBnkAcctName(cashOrderPo.getBankCardName());
		req.setBnkAcctNo(cashOrderPo.getBankCardNo());
		req.setCashPool(acct.getCashPool());
		req.setCurrOpid(currOpId);
		req.setEventId(IdUtil.produce());
		req.setRelBnkId(retPo.getCashId());
		req.setResvJnlNo(null);
		req.setSignedFlg(EFlagType.YES.getCode());
		req.setTrxAmt(retPo.getTrxAmt());
		req.setTrxMemo(retPo.getTrxMemo());
		req.setUserId(retPo.getUserId());
		req.setUserName(user.getName());
		req.setWorkDate(retPo.getTrxDt());
		fundAcctWithdrawService.signedAcctWithdrawAmt(req);
 	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
 	public void saveWithdrawalOrderFailed(WithdrawalRespDto respDto, String currOpId){
		// 更新提现指令数据
		EswCashOrderPo cashOrderPo = eswCashOrderRepository.findOne(respDto.getOrderId());
		cashOrderPo.setRetCode(respDto.getRetCode());
		cashOrderPo.setRetMsg(respDto.getRetMsg());
		cashOrderPo.setStatus(respDto.getStatus());
		cashOrderPo.setLastMntOpId(currOpId);
		cashOrderPo.setLastMntTs(new Date());
		eswCashOrderRepository.save(cashOrderPo);
 	}
	
}
