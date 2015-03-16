package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.PrepaymentService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

@Service
@Qualifier("prepaymentService")
public class PrepaymentServiceImpl implements PrepaymentService {

	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;

	@Override
	public void prepayment(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String pkgId, String seqId,String currOpId,
			Date workDate) throws BizServiceException, AvlBalNotEnoughException {
		fundAcctService.transferAmtIgnoreFrzAmt(eventId, payerList, payeeList,
				true, bizId, currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

	@Override
	public void payInvsIntrProfitToExchange(String eventId,
			List<TransferInfo> payerList, List<TransferInfo> payeeList,
			String bizId, String pkgId, String seqId, String currOpId, Date workDate) {
		fundAcctService.transferAmtIgnoreFrzAmt(eventId, payerList, payeeList,
				true, bizId, currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

	@Override
	public BigDecimal unReserveMargin(UnReserveReq req)
			throws BizServiceException, AvlBalNotEnoughException {
		return fundAcctService.unReserveAmt(req);
	}

	@Override
	public void refundOncePaidFee(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId,
			String pkgId, String seqId, String currOpId, Date workDate)throws BizServiceException {
		fundAcctService.transferAmt(eventId, payerList, 
				payeeList, isAddXwbPay, pkgId, currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

}
