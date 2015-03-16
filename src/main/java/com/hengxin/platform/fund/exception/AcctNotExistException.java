package com.hengxin.platform.fund.exception;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;

public class AcctNotExistException extends BizServiceException {

	private static final long serialVersionUID = 1L;

	public AcctNotExistException(){
        super(EErrorCode.ACCT_NOT_EXIST);
    }
    
    public AcctNotExistException(String message){
        super(EErrorCode.ACCT_NOT_EXIST, message);
    }
    
    public AcctNotExistException(String message, Throwable throwable){
        super(EErrorCode.ACCT_NOT_EXIST, message, throwable);
    }
}
