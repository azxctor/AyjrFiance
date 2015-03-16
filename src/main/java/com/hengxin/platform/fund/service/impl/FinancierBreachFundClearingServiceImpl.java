package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.FinancierBreachFundClearingService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

@Service
@Qualifier("financierBreachFundClearingService")
public class FinancierBreachFundClearingServiceImpl implements
		FinancierBreachFundClearingService {

	private final static Logger LOG = LoggerFactory
			.getLogger(FinancierBreachFundClearingServiceImpl.class);

	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;

	@Override
	public BigDecimal getUserCurrentAvlAmtIgnoreFrzAmt(String userId, boolean isAddXwb) throws BizServiceException {
		BigDecimal avlAmt = BigDecimal.ZERO;
		if(isAddXwb){
			avlAmt = fundAcctService.getUserCurrentAndXwbSubAcctAvlAmtIgnoreFrzAmt(userId);
		}else{
			avlAmt = fundAcctService.getUserSubAcctAvlBalIgnoreFrzAmt(userId, ESubAcctType.CURRENT);
		}
		return avlAmt;
	}

	@Override
	public void breachRepaymentFundClearing(String eventId,
			List<TransferInfo> payerList, List<TransferInfo> payeeList,
			boolean isAddXwbPay, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate)throws BizServiceException, AvlBalNotEnoughException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~~~~融资会员违约，清分融资会员资金指定金额~~~~~~");
		}
		fundAcctService.transferAmtIgnoreFrzAmt(eventId, payerList, payeeList,
				isAddXwbPay, bizId, currOpId, workDate);
        
        // 添加应收应付指令
		recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

	@Override
	public BigDecimal refundLoadnHonourAgtDeposit(UnReserveReq req)
			throws BizServiceException {
		return fundAcctService.unReserveAmt(req);
	}

	@Override
	public BigDecimal unFreezeLateFee(UnFreezeReq req)
			throws BizServiceException {
		return fundAcctService.unFreezeAmt(req);
	}

	@Override
	public String freezeArrearageAmt(FreezeReq req) throws BizServiceException {
		return fundAcctService.freezeAmt(req);
	}

	@Override
	public void payInvsIntrProfitToExchange(String eventId,
			List<TransferInfo> payerList, List<TransferInfo> payeeList,
			String bizId, String pkgId, String seqId, String currOpId, Date workDate) {
		fundAcctService.transferAmtIgnoreFrzAmt(eventId, payerList,
				payeeList, true, bizId, currOpId, workDate);
        // 添加应收应付指令
		recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

}
