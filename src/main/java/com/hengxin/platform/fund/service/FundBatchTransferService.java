package com.hengxin.platform.fund.service;

import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;

public interface FundBatchTransferService {

	/**
	 * 资金划转
	 * @param eventId
	 * @param payerList
	 * @param payeeList
	 * @param bizId
	 * @param currOpId
	 * @param workDate
	 * @throws BizServiceException
	 */
	public void batchTransferAmt(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId,
			String currOpId, Date workDate)throws BizServiceException;
	
}
