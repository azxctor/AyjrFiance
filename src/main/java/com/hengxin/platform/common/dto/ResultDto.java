package com.hengxin.platform.common.dto;

import java.io.Serializable;

import com.hengxin.platform.common.constant.ResultCode;

public class ResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String message;
	private Object data;

	public ResultDto() {

	}

	public ResultDto(String code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public boolean isNonBizError(){
	    return ResultCode.SESSION_TIME_OUT.equals(code)||ResultCode.COMMON_ERROR.equals(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
