package com.hengxin.platform.escrow.service;

import org.springframework.stereotype.Service;

import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.ebc.dto.EbcIdNoCheckRequest;
import com.hengxin.platform.escrow.dto.CommandResponse;

@Service
public class EswIdNoCheckService extends EswBaseService {

	private final static String BUSINESS_TYPE = "register";
	private final static String BUSINESS_PLACE = "530100";

	public boolean idCardCheck(String idCardNo, String name) {
		EbcIdNoCheckRequest request = new EbcIdNoCheckRequest();
		super.buildRequest(request);
		request.setBusinessPlace(BUSINESS_PLACE);
		request.setBusinessType(BUSINESS_TYPE);
		String serialNo = IdUtil.produce();
		request.setDsOrderId(serialNo);
		request.setIdCard(idCardNo);
		request.setOrderSn(serialNo);
		request.setUserName(name);
		CommandResponse response = request.execute();
		return super.isSuccessRespsonse(response);
	}
}
