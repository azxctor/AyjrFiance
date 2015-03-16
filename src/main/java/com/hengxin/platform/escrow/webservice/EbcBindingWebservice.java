package com.hengxin.platform.escrow.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.hengxin.platform.ebc.dto.EbcBindingBankCardResponse;

@WebService
public interface EbcBindingWebservice {

	@WebMethod
	public void doBindingResult(
			@WebParam(name = "response") EbcBindingBankCardResponse response);
}
