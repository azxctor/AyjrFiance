package com.hengxin.platform.escrow.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.ebc.enums.EEbcErrorType;

public class ErrorUtils {

	public static String getExceptionStack(Exception ex, int length) {
		String errMsg = getExceptionStack(ex);
		if(length > 0 && StringUtils.isNotBlank(errMsg)){
			if(length < errMsg.length()){
				errMsg = errMsg.substring(0, length);
			}
		}
		return errMsg;
	}
	
	public static String getExceptionStack(Exception ex){
		String errMsg = null;
		if(ex!=null){
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PrintStream pts = new PrintStream(bos);
				ex.printStackTrace(pts);
				errMsg = bos.toString();
				bos.close();
				pts.close();
			} catch (Exception e) {
				errMsg = ex.toString();
			}
		}
		return errMsg;
	}
	
	public static String getEswErroMsg(String errorCode){
		EEbcErrorType errorType = null;
		if(StringUtils.isNotBlank(errorCode)){
			errorType = EnumHelper.translate(EEbcErrorType.class, errorCode);
		}
		return errorType==null?"":errorType.getText();
	}

}
