package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * 
 * @author jishen
 * 
 */
@Service
public class MessageService {

	@Autowired
	private MemberMessageService memberMessageService;

	/**
	 * 发送保证金冻结金额不足的提醒及短信
	 * 
	 * @param req
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendNotEnoughMessage(Product product, BigDecimal amt,
			EBizRole role) {
		DecimalFormat format = new DecimalFormat();
		format.applyPattern("##,##0.00");
		String messageKey = null;
		String userId = null;
		switch (role) {
		case FINANCIER:
			messageKey = "product.financier.margin.message";
			userId = product.getApplUserId();
			break;
		case PROD_SERV:
			messageKey = "product.guarantor.margin.message";
			userId = product.getWarrantId();
			break;
		default:
			return;
		}
		memberMessageService.sendMessage(EMessageType.SMS, messageKey,
				userId, product.getProductName(),
				format.format(amt.doubleValue()));
		memberMessageService.sendMessage(EMessageType.MESSAGE, messageKey,
				userId, product.getProductName(),
				format.format(amt.doubleValue()));

	}
}
