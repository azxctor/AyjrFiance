package com.hengxin.platform.fund.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.ManualPaymentReq;
import com.hengxin.platform.fund.dto.biz.req.PrePayMentReq;
import com.hengxin.platform.fund.dto.biz.req.SubscribeCRReq;
import com.hengxin.platform.fund.dto.biz.req.TransferCRReq;
import com.hengxin.platform.fund.dto.biz.req.atom.PositionChangeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.FundPositionService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.atom.PositionService;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

@Service
@Qualifier("fundPositionService")
public class FundPositionServiceImpl implements FundPositionService {

	@Autowired
	private PositionService positionService;
	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;

	/**
	 * 债权转让
	 * 
	 * @throws AmtParamInvalidException
	 */
	@Override
	public String transferCR(TransferCRReq req) throws BizServiceException {
		PositionChangeReq changeReq = ConverterService.convert(req,
				PositionChangeReq.class);
		changeReq.setTrdType(EFundTrdType.BONDASSIGN);
		return positionService.positionChange(changeReq);
	}

	/**
	 * 债权转让资金划转
	 * 
	 * @param eventId
	 * @param payerList
	 * @param payeeList
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws SubAcctNotExistException
	 * @throws AcctNotExistException
	 * @throws AvlBalNotEnoughException
	 * @throws AmtRecvPayNotEqualsException
	 * @throws AmtParamInvalidException
	 * @throws PayerOrPayeeCannotEmptyException
	 */
	public void transferCRPayAmt(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId, String pkgId, String seqId,
			String currOpId, Date workDate) throws BizServiceException, AvlBalNotEnoughException {
		fundAcctService.transferAmt(eventId, payerList, payeeList, isAddXwbPay, bizId,
				currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, pkgId, seqId, currOpId, workDate);
	}

	/**
	 * 债权申购
	 * 
	 * @throws AmtParamInvalidException
	 */
	@Override
	@Deprecated
	public String subscribeCR(SubscribeCRReq req) throws BizServiceException {
		PositionChangeReq changeReq = ConverterService.convert(req,
				PositionChangeReq.class);
		changeReq.setTrdType(EFundTrdType.BONDSUBS);
		return positionService.positionChange(changeReq);
	}

	/**
	 * 提前还款
	 * 
	 * @throws AmtParamInvalidException
	 */
	@Override
	public void prepayment(PrePayMentReq req) throws BizServiceException {
		PositionChangeReq changeReq = ConverterService.convert(req,
				PositionChangeReq.class);
		changeReq.setTrdType(EFundTrdType.ALL);
		positionService.positionChange(changeReq);
	}

	/**
	 * 手工还款
	 */
	@Override
	public void manualPayment(ManualPaymentReq manualPaymentReq) {
		positionService.manualPayment(manualPaymentReq);
	}
	
	
}
