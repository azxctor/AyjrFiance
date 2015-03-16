package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.dto.biz.req.TradeFeePayReq;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.FinancierSigningService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.support.AcctUtils;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

/**
 * Class Name: FinancierSigningServiceImpl
 * 
 * @author jishen
 * 
 *         融资人签约服务
 */
@Service
@Qualifier("financierSigningService")
public class FinancierSigningServiceImpl implements FinancierSigningService {

	private final static Logger LOG = LoggerFactory
			.getLogger(FinancierSigningServiceImpl.class);

	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;

	@Override
	public void payTradeFeeToExchange(String eventId, String userId,
			List<TradeFeePayReq> feeReqList, String bizId, String pkgId, String currOpId,
			Date workDate) throws BizServiceException, AvlBalNotEnoughException {
		// 付款总额
		BigDecimal totalFeeAmt = BigDecimal.ZERO;
		StringBuffer trxMemoBuffer = new StringBuffer();
		// 付款方信息
		List<TransferInfo> payerList = new ArrayList<TransferInfo>();
		for (TradeFeePayReq req : feeReqList) {
			BigDecimal trxAmt = AmtUtils.processNegativeAmt(req.getTrxAmt(),
					BigDecimal.ZERO);
			String trxMemo = req.getTrxMemo();
			TransferInfo payerInfo = new TransferInfo();
			payerInfo.setUserId(userId);
			payerInfo.setTrxAmt(trxAmt);
			payerInfo.setUseType(EFundUseType.TRADEEXPENSE);
			payerInfo.setTrxMemo(trxMemo);
			payerList.add(payerInfo);
			totalFeeAmt = totalFeeAmt.add(trxAmt);
			trxMemoBuffer.append(trxMemo).append(" ，");
		}
		// 收款方信息
		List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
		String exchangeUserId = CommonBusinessUtil.getExchangeUserId();
		TransferInfo payeeInfo = new TransferInfo();
		payeeInfo.setUserId(exchangeUserId);
		payeeInfo.setTrxAmt(totalFeeAmt);
		payeeInfo.setUseType(EFundUseType.TRADEEXPENSE);
		payeeInfo.setTrxMemo(AcctUtils.substr(trxMemoBuffer.toString(), 200));
		payeeList.add(payeeInfo);
		fundAcctService.transferAmt(eventId, payerList, payeeList, false,
				bizId, currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, pkgId, null, currOpId, workDate);
	}

	/**
	 * 全额退还委托履约保证金
	 * 
	 * @throws AmtParamInvalidException
	 * @throws FreezeReserveDtlOperTypeInvalidException
	 * @throws FreezeReserveDtlCloseException
	 * @throws SubAcctNotExistException
	 * @throws AcctNotExistException
	 */
	@Override
	public BigDecimal refundAllEntrustDeposit(UnReserveReq req)
			throws BizServiceException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			LOG.debug("~~~保证金全额解冻~~");
			LOG.debug("~~~解冻流水号为~~" + req.getReserveJnlNo());
			LOG.debug("~~~会员编号为~~" + req.getUserId());
			LOG.debug("~~~当前工作日期~~"
					+ DateUtils.formatDate(req.getWorkDate(),
							DictConsts.WORK_DATE_FORMAT));
			LOG.debug("~~~当前操作员编号为~~" + req.getCurrOpId());
		}
		return fundAcctService.unReserveAmt(req);
	}

	@Override
	public String signingTransferAmt(String eventId,
			List<DedicatedTransferInfo> payerList, TransferInfo payeeInfo,
			String bizId, String pkgId, String currOpId, Date workDate)
			throws BizServiceException, AvlBalNotEnoughException {
		String jnlNo =  fundAcctService.transferAmtForFinancing(eventId, payerList,
				payeeInfo, bizId, currOpId, workDate);
        // 添加应收应付指令
		List<TransferInfo> transferPayerList = AcctUtils.convertToTransferInfoList(payerList);
        recvPayOrderAddService.createRecvPayOrder(eventId, transferPayerList, Arrays.asList(payeeInfo), 
        		bizId, pkgId, null, currOpId, workDate);
        return jnlNo;
	}

	@Override
	public void subsFeeTransferAmt(String eventId,
			List<DedicatedTransferInfo> payerList, TransferInfo payeeInfo,
			String bizId, String pkgId, String currOpId, Date workDate)
			throws BizServiceException, AvlBalNotEnoughException {
		fundAcctService.transferAmtFromFnr(eventId, payerList,
				Arrays.asList(payeeInfo), false, bizId, currOpId, workDate);
		List<TransferInfo> transferPayerList = AcctUtils.convertToTransferInfoList(payerList);
        recvPayOrderAddService.createRecvPayOrder(eventId, transferPayerList, Arrays.asList(payeeInfo), 
        		bizId, pkgId, null, currOpId, workDate);
	}

}
