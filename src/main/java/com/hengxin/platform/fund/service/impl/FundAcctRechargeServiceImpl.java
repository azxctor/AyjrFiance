package com.hengxin.platform.fund.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.dto.biz.req.FundAcctRechargeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankTrxJnlPo;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.BankTrxJnlRepository;
import com.hengxin.platform.fund.service.FundAcctRechargeService;
import com.hengxin.platform.fund.service.atom.FundAcctService;

@Service
@Qualifier("fundAcctRechargeService")
public class FundAcctRechargeServiceImpl implements FundAcctRechargeService {

	private final static Logger LOG = LoggerFactory
			.getLogger(FundAcctRechargeServiceImpl.class);

	@Autowired
	private AcctRepository acctRepository;

	@Autowired
	private BankTrxJnlRepository bankTrxJnlRepository;

	@Autowired
	private FundAcctService fundAcctService;

	@Override
	public String rechargeAmt(FundAcctRechargeReq req)
			throws BizServiceException, AvlBalNotEnoughException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("~~~会员充值~~~");
			LOG.debug("~~~会员编号:" + req.getUserId() + "~~~");
			LOG.debug("~~~会员名:" + req.getUserName() + "~~~");
			LOG.debug("~~~会员名称:" + req.getUserName() + "~~~");
			LOG.debug("~~~是否签约会员:" + req.getSignedFlg() + "~~~");
			LOG.debug("~~~充值金额:" + req.getTrxAmt() + "~~~");
			LOG.debug("~~~备 注:" + req.getTrxMemo() + "~~~");
			LOG.debug("~~~当前操作用户:" + req.getCurrOpid() + "~~~");
			LOG.debug("~~~当前工作日期:" + req.getWorkDate() + "~~~");
			LOG.debug("~~~ 关联银行流水号:" + req.getRelBnkId() + "~~~");
		}

		// 通过会员编号获取综合账户
		AcctPo acctPo = acctRepository.findByUserId(req.getUserId());
		String eventId = req.getEventId();
		String pool = req.getCashPool();
		if(StringUtils.isBlank(pool)){
			pool = acctPo.getCashPool();
		}
		ECashPool cashPool = EnumHelper.translate(ECashPool.class, pool);
		List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
		TransferInfo rechargeInfo = new TransferInfo();
		rechargeInfo.setRelZQ(false);
		rechargeInfo.setTrxAmt(req.getTrxAmt());
		rechargeInfo.setTrxMemo(req.getTrxMemo());
		rechargeInfo.setUserId(req.getUserId());
		rechargeInfo.setCashPool(cashPool);
		rechargeInfo.setUseType(EFundUseType.RECHARGE);
		payeeList.add(rechargeInfo);

		// 添加会员充值日志
		BankTrxJnlPo trxJnlPo = new BankTrxJnlPo();
		trxJnlPo.setBankCode(EnumHelper.translate(EBankType.class, req.getBankCode()));
		trxJnlPo.setBnkAcctNo(req.getBankAcctNo());
		trxJnlPo.setBnkAcctName(req.getBankAcctName());
		trxJnlPo.setCashPool(cashPool);
		trxJnlPo.setUserId(req.getUserId());
		trxJnlPo.setUserName(req.getUserName());
		trxJnlPo.setAcctNo(acctPo.getAcctNo());
		trxJnlPo.setSubAcctNo(ESubAcctNo.CURRENT.getCode());
		trxJnlPo.setSignedFlg(EnumHelper.translate(EFlagType.class, req.getSignedFlg()));
		trxJnlPo.setRechargeWithdrawFlag(ERechargeWithdrawFlag.RECHARGE);
		trxJnlPo.setTrxDt(req.getWorkDate());
		trxJnlPo.setTrxAmt(req.getTrxAmt());
		trxJnlPo.setTrxMemo(req.getTrxMemo());
		trxJnlPo.setRelBnkId(req.getRelBnkId());
		trxJnlPo.setCreateOpid(req.getCurrOpid());
		trxJnlPo.setCreateTs(new Date());
		trxJnlPo.setEventId(eventId);
		trxJnlPo.setTrxStatus(EBnkTrxStatus.NORMAL);
		trxJnlPo.setRvsFlg(EFlagType.NO);
		BankTrxJnlPo retTrxJnlPo = bankTrxJnlRepository
				.save(trxJnlPo);

		fundAcctService.transferAmt(eventId, null, payeeList, false,
				retTrxJnlPo.getJnlNo(), req.getCurrOpid(),
				req.getWorkDate());

		return retTrxJnlPo.getJnlNo();
	}

}
