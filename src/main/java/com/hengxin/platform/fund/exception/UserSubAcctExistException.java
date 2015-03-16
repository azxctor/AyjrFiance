package com.hengxin.platform.fund.exception;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;

public class UserSubAcctExistException extends BizServiceException {

	private static final long serialVersionUID = 1L;

	public UserSubAcctExistException(){
        super(EErrorCode.USER_SUB_ACCT_EXIST);
    }
    
    public UserSubAcctExistException(String message){
        super(EErrorCode.USER_SUB_ACCT_EXIST, message);
    }
    
    public UserSubAcctExistException(String message, Throwable throwable){
        super(EErrorCode.USER_SUB_ACCT_EXIST, message, throwable);
    }
}
