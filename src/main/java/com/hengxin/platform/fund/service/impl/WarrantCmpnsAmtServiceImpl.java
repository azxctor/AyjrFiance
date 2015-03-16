package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.WarrantCmpnsAmtService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

@Service
@Qualifier("warrantCmpnsAmtService")
public class WarrantCmpnsAmtServiceImpl implements WarrantCmpnsAmtService {

	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;

	@Override
	public void payCmpnsAmtOnFinalPayment(String eventId, DedicatedTransferInfo warrantPayer,
			TransferInfo fncrPayee, String bizId, String pkgId, String seqId, String currOpId, Date workDate)
			throws BizServiceException, AvlBalNotEnoughException {
		warrantPayer.setRelZQ(false);
		warrantPayer.setUseType(EFundUseType.CMPNSAMT_PAY);
		fncrPayee.setRelZQ(false);
		fncrPayee.setUseType(EFundUseType.CMPNSAMT_PAY);
		fundAcctService.transferAmtFromFnr(eventId, Arrays.asList(warrantPayer),
				Arrays.asList(fncrPayee), false, bizId, currOpId, workDate);
        // 添加应收应付指令
        TransferInfo wrtrPayerInfo = ConverterService.convert(warrantPayer, TransferInfo.class);
        recvPayOrderAddService.createRecvPayOrder(eventId, Arrays.asList(wrtrPayerInfo), Arrays.asList(fncrPayee), 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

	@Override
	public void payCmpnsAmtOnPayment(String eventId, TransferInfo warrantPayer,
			TransferInfo fncrPayee, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate)throws BizServiceException, AvlBalNotEnoughException {
		warrantPayer.setRelZQ(false);
		warrantPayer.setUseType(EFundUseType.CMPNSAMT_PAY);
		fncrPayee.setRelZQ(false);
		fncrPayee.setUseType(EFundUseType.CMPNSAMT_PAY);
		fundAcctService.transferAmt(eventId, Arrays.asList(warrantPayer),
				Arrays.asList(fncrPayee), false, bizId, currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, Arrays.asList(warrantPayer), Arrays.asList(fncrPayee), 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

	@Override
	public void repaymentCmpnsAmt(String eventId, TransferInfo fncrPayer,
			TransferInfo warrantPayee, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException {
		fncrPayer.setRelZQ(false);
		warrantPayee.setRelZQ(true);
		fundAcctService.transferAmt(eventId, Arrays.asList(fncrPayer),
				Arrays.asList(warrantPayee), true, bizId, currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, Arrays.asList(fncrPayer), Arrays.asList(warrantPayee), 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

	@Override
	public BigDecimal getUserCurrentAcctAvlBal(String userId, boolean isAddXwb)
			throws BizServiceException {
		BigDecimal avlAmt = BigDecimal.ZERO;
		if (isAddXwb) {
			avlAmt = fundAcctService.getUserCurrentAndXwbSubAcctAvlAmt(userId);
		} else {
			avlAmt = fundAcctService.getUserSubAcctAvlAmt(userId,
					ESubAcctType.CURRENT);
		}
		return avlAmt;
	}

}
