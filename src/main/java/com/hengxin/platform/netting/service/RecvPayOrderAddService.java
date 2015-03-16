package com.hengxin.platform.netting.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.netting.entity.RecvPayOrderPo;
import com.hengxin.platform.netting.enums.NettingStatusEnum;
import com.hengxin.platform.netting.repository.RecvPayOrderRepository;

@Service
public class RecvPayOrderAddService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RecvPayOrderAddService.class);
	
	private static final String DEFAULT_SERV_PROV = "EBC";
	
	@Autowired
	private RecvPayOrderRepository recvPayOrderRepository;
	
	@Transactional
	public void createRecvPayOrder(
			String eventId,
			List<TransferInfo> payerList,
			List<TransferInfo> payeeList,
			String bizId,
			String pkgId,
			String seqId,
			String currOpId,
			Date workDate){
		boolean bool = this.checkBalance(payerList, payeeList);
		if(bool){
			List<RecvPayOrderPo> orderList = new ArrayList<RecvPayOrderPo>();
			this.addOrder(payerList, orderList, eventId, bizId, pkgId, seqId, currOpId, workDate, EFundPayRecvFlag.PAY);
			this.addOrder(payeeList, orderList, eventId, bizId, pkgId, seqId, currOpId, workDate, EFundPayRecvFlag.RECV);
			if(!orderList.isEmpty()){
				recvPayOrderRepository.save(orderList);
			}
			else{
				LOGGER.debug("the order list is empty");
			}
		}
		else{
			LOGGER.debug("check balance return false");
		}
	}
	
	private void addOrder(
			List<TransferInfo> fromTransferList, 
			List<RecvPayOrderPo> toOrderList,
			String eventId,
			String bizId,
			String pkgId,
			String seqId,
			String currOpId,
			Date workDate, 
			EFundPayRecvFlag payRecvFlg){
		Date currDate = new Date();
		for(TransferInfo dataInfo :fromTransferList){
			RecvPayOrderPo order = new RecvPayOrderPo();
			order.setEventId(eventId);
			order.setPayRecvFlg(payRecvFlg);
			order.setPkgId(pkgId);
			order.setRelBizId(bizId);
			order.setSeqId(seqId);
			order.setServProv(DEFAULT_SERV_PROV);
			order.setNettingStatus(NettingStatusEnum.W);
			order.setTrxAmt(dataInfo.getTrxAmt());
			order.setTrxDate(workDate);
			order.setTrxMemo(dataInfo.getTrxMemo());
			order.setUserId(dataInfo.getUserId());
			order.setUseType(dataInfo.getUseType());
			order.setCreateOpId(currOpId);
			order.setCreateTs(currDate);
			order.setLastMntOpId(null);
			order.setLastMntTs(null);
			order.setNettingTs(null);
			toOrderList.add(order);
		}
	}
	
	private boolean checkBalance(List<TransferInfo> payerList, List<TransferInfo> payeeList){
		boolean bool = true;
		if(payerList==null||payeeList==null){
			LOGGER.debug("payerList or payeeList is null");
			bool = false;
			return bool;
		}
		if(payerList.isEmpty()||payeeList.isEmpty()){
			LOGGER.debug("payerList or payeeList is empty");
			bool = false;
			return bool;
		}
		BigDecimal sumPayAmt = BigDecimal.ZERO;
		BigDecimal sumRecvAmt = BigDecimal.ZERO;
		for(TransferInfo pay:payerList){
			sumPayAmt = sumPayAmt.add(pay.getTrxAmt());
		}
		for(TransferInfo recv:payeeList){
			sumRecvAmt = sumRecvAmt.add(recv.getTrxAmt());
		}
		LOGGER.debug("sum of payerList amt is {} , sum of payeeList amt is {}", sumPayAmt, sumRecvAmt);
		if(sumPayAmt.compareTo(sumRecvAmt)!=0
				&&(sumPayAmt.compareTo(BigDecimal.ZERO)>0||sumRecvAmt.compareTo(BigDecimal.ZERO)>0)){
			throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,"收付金额不相等");
		}
		return bool;
	}
	
}
