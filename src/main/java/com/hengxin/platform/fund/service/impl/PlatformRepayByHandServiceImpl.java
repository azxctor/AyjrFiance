package com.hengxin.platform.fund.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.PlatformRepayByHandService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.support.AcctUtils;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

/**
 * 
 * @author dcliu
 * 
 */
@Service
@Qualifier("platformRepayByHandService")
public class PlatformRepayByHandServiceImpl implements
		PlatformRepayByHandService {

	private final static Logger LOG = LoggerFactory
			.getLogger(PlatformRepayByHandServiceImpl.class);

	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;

	@Override
	public void repayOnPayDate(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException {
		LOG.debug("~~~~~~~~~非最后一期还款~~~~~~~~~~~~~");
		fundAcctService.transferAmt(eventId, payerList, payeeList, true, bizId,
				currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

	@Override
	public void repayOnMaturityDate(String eventId,
			List<DedicatedTransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException {
		LOG.debug("~~~~~~~~~最后一期还款~~~~~~~~~~~~~");
		fundAcctService.transferAmtFromFnr(eventId, payerList, payeeList, true,
				bizId, currOpId, workDate);
        // 添加应收应付指令// 添加应收应付指令
        List<TransferInfo> transferPayerList = AcctUtils.convertToTransferInfoList(payerList);
        recvPayOrderAddService.createRecvPayOrder(eventId, transferPayerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);
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
