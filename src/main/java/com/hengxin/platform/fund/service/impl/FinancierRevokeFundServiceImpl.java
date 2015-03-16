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
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.FinancierRevokeFundService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.support.AcctUtils;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

/**
 * 
 * @author dcliu
 * 
 */
@Service
@Qualifier("financierRevokeFundService")
public class FinancierRevokeFundServiceImpl implements
		FinancierRevokeFundService {

	private final static Logger LOG = LoggerFactory
			.getLogger(FinancierRevokeFundServiceImpl.class);

	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;

	@Override
	public void compensateInvestorsAndExchangeForRevoke(String eventId,
			List<DedicatedTransferInfo> payerList,
			List<TransferInfo> payeeList, String bizId, String pkgId, String seqId, 
			String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~~~融资人撤单,将履约保证金额按比例赔偿投资人、交易所~~~~~");
		}

		fundAcctService.transferAmtFromFnr(eventId, payerList, payeeList,
				false, bizId, currOpId, workDate);
        
        // 添加应收应付指令
        List<TransferInfo> transferPayerList = AcctUtils.convertToTransferInfoList(payerList);
        recvPayOrderAddService.createRecvPayOrder(eventId, transferPayerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);

	}

	@Override
	public void refundSubsPrePayAmt(UnReserveReq req)
			throws BizServiceException {
		fundAcctService.unReserveAmt(req);
	}

	@Override
	public void refundSubsFeePrePayAmt(UnReserveReq req)
			throws BizServiceException {
		fundAcctService.unReserveAmt(req);
	}

}
