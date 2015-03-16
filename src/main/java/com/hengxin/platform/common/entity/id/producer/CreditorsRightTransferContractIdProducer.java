package com.hengxin.platform.common.entity.id.producer;

import java.text.MessageFormat;

import com.hengxin.platform.common.entity.id.Context;
import com.hengxin.platform.common.entity.id.DefaultIdProducer;
import com.hengxin.platform.common.util.ApplicationContextUtil;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.product.domain.CreditorsRightTransferContract;
import com.hengxin.platform.product.repository.CreditorsRightTransferContractRepository;

public class CreditorsRightTransferContractIdProducer extends
		DefaultIdProducer<CreditorsRightTransferContract> {

	private static final String ID_FORMAT = "{0}-{1}-{2,number,00}";

	@Override
	protected Object doProduceId(Context ctx) {
		CreditorsRightTransferContract contract = getEntity(ctx);
		String buyerAcctNo = ApplicationContextUtil.getBean(AcctService.class)
				.getAcctByUserId(contract.getBuyerId()).getAcctNo();
		Long transferCount = ApplicationContextUtil.getBean(
				CreditorsRightTransferContractRepository.class)
				.getCountByCreditorsRight(contract.getCreditorsRightId()) + 1;
		return MessageFormat.format(ID_FORMAT, getEntity(ctx)
				.getCreditorsRightId(), buyerAcctNo, transferCount);
	}

	@Override
	protected void doProduceSequence(Context ctx) {
	}

}
