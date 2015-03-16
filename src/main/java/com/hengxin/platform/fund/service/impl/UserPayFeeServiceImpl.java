package com.hengxin.platform.fund.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.biz.req.UserPayFeeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.entity.FeePayStatePo;
import com.hengxin.platform.fund.enums.EFeePeriodType;
import com.hengxin.platform.fund.enums.EFeeType;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.repository.FeePayStateRepository;
import com.hengxin.platform.fund.service.UserPayFeeService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

@Service
@Qualifier("userPayFeeService")
public class UserPayFeeServiceImpl implements UserPayFeeService {

	private final static Logger LOG = LoggerFactory
			.getLogger(UserPayFeeServiceImpl.class);

	@Autowired
	private FeePayStateRepository feePayStateRepository;

	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;

	@Override
	public boolean hasPayFeeOfSeat(String userId, Date workDate) {
		return this.hasPayFee(userId, EFeeType.SEAT, workDate);
	}

	@Override
	public boolean hasPayFee(String userId, EFeeType feeType, Date workDate) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~会员缴费~~~~");
			LOG.debug("~~~费用类型为~~~~" + feeType.getText());
			LOG.debug("~~~会员缴费~~~~");
		}

		boolean hasPay = false;
		FeePayStatePo feePayState = feePayStateRepository
				.findByUserIdAndFeeType(userId, feeType.getCode());
		if (feePayState != null) {
			Date endDate = feePayState.getEndDt();
			Long endDateLong = DateUtils.date2yyyyMMddLong(endDate);
			Long workDateLong = DateUtils.date2yyyyMMddLong(workDate);
			if (workDateLong.compareTo(endDateLong) <= 0) {
				hasPay = true;
			}
		}
		return hasPay;
	}

	@Override
	public void payFee(UserPayFeeReq req) throws BizServiceException,
			AvlBalNotEnoughException {

		FeePayStatePo feePayState = feePayStateRepository
				.findByUserIdAndFeeType(req.getUserId(), req.getFeeType()
						.getCode());
		Date currDate = new Date();
		if (feePayState == null) {
			Date endDate = calFeeEndDate(req.getWorkDate(),
					req.getPeriodType(), req.getPeriodNum());
			FeePayStatePo newFeePay = new FeePayStatePo();
			newFeePay.setCreateOpid(req.getCurrOpId());
			newFeePay.setCreateTs(currDate);
			newFeePay.setEndDt(endDate);
			newFeePay.setFeeType(req.getFeeType().getCode());
			newFeePay.setStartDt(req.getWorkDate());
			newFeePay.setUserId(req.getUserId());
			newFeePay.setEventId(req.getEventId());
			feePayStateRepository.save(newFeePay);
		} else {
			Date endDate = calFeeEndDate(req.getWorkDate(),
					req.getPeriodType(), req.getPeriodNum());
			FeePayStatePo newFeePay = ConverterService.convert(feePayState,
					FeePayStatePo.class);
			newFeePay.setEndDt(endDate);
			newFeePay.setLastMntOpid(req.getCurrOpId());
			newFeePay.setLastMntTs(currDate);
			newFeePay.setEventId(req.getEventId());
			feePayStateRepository.save(newFeePay);
		}

		String bizId = req.getBizId() + "#" + req.getFeeType().getCode();
		List<TransferInfo> payerList = new ArrayList<TransferInfo>();
		TransferInfo payerInfo = new TransferInfo();
		payerInfo.setRelZQ(false);
		payerInfo.setTrxAmt(req.getTrxAmt());
		payerInfo.setTrxMemo(req.getTrxMemo());
		payerInfo.setUserId(req.getUserId());
		payerInfo.setUseType(EFundUseType.PAYFEE);
		payerList.add(payerInfo);
		
		List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
		TransferInfo payeeInfo = new TransferInfo();
		payeeInfo.setRelZQ(false);
		payeeInfo.setTrxAmt(req.getTrxAmt());
		payeeInfo.setTrxMemo(req.getTrxMemo());
		payeeInfo.setUserId(CommonBusinessUtil.getExchangeUserId());
		payeeInfo.setUseType(EFundUseType.PAYFEE);
		payeeList.add(payeeInfo);
		fundAcctService.transferAmt(req.getEventId(), payerList, payeeList,
				false, bizId, req.getCurrOpId(), req.getWorkDate());
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(req.getEventId(), payerList, payeeList, 
        		bizId, null, null, req.getCurrOpId(), req.getWorkDate());
	}

	private Date calFeeEndDate(Date startDate, EFeePeriodType periodType,
			Integer periodNum) {
		Date endDate = startDate;
		switch (periodType) {
		case YEAR:
			endDate = DateUtils.add(startDate, Calendar.YEAR, periodNum);
			endDate = DateUtils.add(endDate, Calendar.DATE, -1);
			break;
		case MONTH:
			endDate = DateUtils.add(startDate, Calendar.MONTH, periodNum);
			endDate = DateUtils.add(endDate, Calendar.DATE, -1);
			break;
		case DAY:
			endDate = DateUtils.add(startDate, Calendar.DATE, periodNum);
			break;
		}
		return endDate;
	}

	@Override
	public FeePayStatePo getFeePayState(String userId, EFeeType feeType)throws BizServiceException {
		return feePayStateRepository.findByUserIdAndFeeType(userId, feeType.getCode());
	}

}
