package com.hengxin.platform.escrow.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hengxin.platform.ebc.consts.EBCConsts;
import com.hengxin.platform.ebc.dto.EbcErrorResponse;
import com.hengxin.platform.escrow.dto.CommandRequest;
import com.hengxin.platform.escrow.dto.CommandResponse;
import com.hengxin.platform.escrow.utils.EswUtils;

public class EswBaseService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswBaseService.class);

	private static Map<String, String> defaultEswParams;

	private void initParams() {
		defaultEswParams = new HashMap<String, String>();
		defaultEswParams.put("wltNo", EBCConsts.WLT_NO);
		defaultEswParams.put("ownerId", EswUtils.getEswServProv());
		defaultEswParams.put("loginType", EBCConsts.LOGIN_TYPE);
		defaultEswParams.put("accountType", EBCConsts.ACCOUNT_TYPE);
		defaultEswParams.put("merchNo", EswUtils.getEswMerChNo());
		defaultEswParams.put("currency", EBCConsts.CURRENCY);
		defaultEswParams.put("version", EBCConsts.VERSION);
	}
	
	private Map<String, String> getDefaultParams(){
		if(defaultEswParams==null||defaultEswParams.isEmpty()){
			initParams();
		}
		return defaultEswParams;
	}

	/**
	 * false - EbcErrorResponse
	 * 
	 * @param response
	 * @return
	 */
	protected boolean isSuccessRespsonse(CommandResponse response) {
		if (response instanceof EbcErrorResponse) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ebc CommandRequest common build method
	 * 
	 * @param request
	 * @return
	 */
	protected CommandRequest buildRequest(CommandRequest request) {
		for (String param : getDefaultParams().keySet()) {
			String firstLetter = param.substring(0, 1).toUpperCase();
			String setMethodName = "set" + firstLetter + param.substring(1);
			Method setMethod = null;
			try {
				setMethod = request.getClass().getDeclaredMethod(setMethodName,
						String.class);
			} catch (SecurityException e) {
				LOGGER.error("", e);
			} catch (NoSuchMethodException e) {
				LOGGER.warn("no such method :{}", setMethodName);
				continue;
			}
			try {
				setMethod.invoke(request, getDefaultParams().get(param));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return request;
	}

}
