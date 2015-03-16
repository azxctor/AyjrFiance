package com.hengxin.platform.fund.exception;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;

public class UserAcctExistException extends BizServiceException {

	private static final long serialVersionUID = 1L;

	public UserAcctExistException(){
        super(EErrorCode.USER_ACCT_EXIST);
    }
    
    public UserAcctExistException(String message){
        super(EErrorCode.USER_ACCT_EXIST, message);
    }
    
    public UserAcctExistException(String message, Throwable throwable){
        super(EErrorCode.USER_ACCT_EXIST, message, throwable);
    }
}
