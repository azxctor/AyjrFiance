package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.service.FundAcctCashPoolChangeService;
import com.hengxin.platform.fund.service.atom.FundAcctService;

@Service
@Qualifier("fundAcctCashPoolChangeService")
public class FundAcctCashPoolChangeServiceImpl implements
		FundAcctCashPoolChangeService {

	@Autowired
	private AcctRepository acctRepository;

	@Autowired
	private SubAcctRepository subAcctRepository;

	@Autowired
	private FundAcctService fundAcctService;

	@Override
	public void changePoolTreasurerAcctBal(String eventId, String acctNo,
			ECashPool fromPool, ECashPool toPool, String trxMemo, String bizId,
			String currOpId, Date workDate) throws BizServiceException {

		// 更新资金账户所属资金池
		AcctPo acctPo = acctRepository.findByAcctNo(acctNo);

		if (!StringUtils.equalsIgnoreCase(acctPo.getCashPool(),
				toPool.getCode())) {
			// set new pool
			acctPo.setCashPool(toPool.getCode());
			acctPo.setLastMntOpid(currOpId);
			acctPo.setLastMntTs(new Date());
			acctRepository.save(acctPo);
		}
		if (fromPool != null && fromPool != toPool) {
			// 记录资金账户的余额调拨记录
			fundAcctService.treasurerAcctTotalBal(eventId, acctNo,
					fromPool.getCode(), toPool.getCode(), trxMemo, bizId,
					currOpId, workDate);

			List<SubAcctPo> subAccts = subAcctRepository.findByAcctNo(acctNo);

			for (SubAcctPo subAcct : subAccts) {
				subAcct.setMaxCashableAmt(BigDecimal.ZERO);
				subAcct.setLastMntOpid(currOpId);
				subAcct.setLastMntTs(new Date());
			}
			subAcctRepository.save(subAccts);
		}
	}

}
