package com.hengxin.platform.escrow.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.hengxin.platform.ebc.dto.EbcRechargeResponse;

@WebService
public interface EbcRechargeWebservice {

	@WebMethod
	public void doRechargeResult(
			@WebParam(name = "response") EbcRechargeResponse response);
}
