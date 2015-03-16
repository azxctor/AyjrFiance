package com.hengxin.platform.common.exception;

/**
* Use {@link BizServiceException} instead
*
*/
@SuppressWarnings("serial")
@Deprecated
public class ServiceException extends RuntimeException {
	
	private final String errCode;
	
	private final String errMsg;
	
	public ServiceException(){
	    super();
	    this.errCode = "-1";
        this.errMsg = "";
	}
	
	public ServiceException(String errMsg){
		super(errMsg);
		this.errCode = "-1";
		this.errMsg = errMsg;
	}
	
	public ServiceException(String errMsg, Throwable cause) {
        super(errMsg, cause);
		this.errCode = "-1";
		this.errMsg = errMsg;
    }
	
	public ServiceException(String errCode, String errMsg) {
        super("["+errCode+"]"+errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
    }
	
	public ServiceException(String errCode, String errMsg, Throwable cause) {
        super("["+errCode+"]"+errMsg, cause);
		this.errCode = errCode;
		this.errMsg = errMsg;
    }

	public String getErrCode() {
		return errCode;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	
}
