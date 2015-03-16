package com.hengxin.platform.fund.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.service.FundBatchTransferService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.netting.service.RecvPayOrderAddService;

@Service
@Qualifier("fundBatchTransferService")
public class FundBatchTransferServiceImpl implements FundBatchTransferService {

	@Autowired
	private FundAcctService fundAcctService;
    @Autowired
    private RecvPayOrderAddService recvPayOrderAddService;
	
	@Override
	public void batchTransferAmt(String eventId, List<TransferInfo> payerList,
			List<TransferInfo> payeeList, boolean isAddXwbPay, String bizId, String currOpId,
			Date workDate) throws BizServiceException {
		fundAcctService.transferAmt(eventId, payerList, payeeList, 
				isAddXwbPay, bizId, currOpId, workDate);
        // 添加应收应付指令
        recvPayOrderAddService.createRecvPayOrder(eventId, payerList, payeeList, 
        		bizId, null, null, currOpId, workDate);
	}

}
