package com.hengxin.platform.fund.exception;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;

public class AcctStatusIllegalException extends BizServiceException {

	private static final long serialVersionUID = 1L;

	public AcctStatusIllegalException(){
        super(EErrorCode.ACCT_STATUS_ILLEGAL);
    }
    
    public AcctStatusIllegalException(String message){
        super(EErrorCode.ACCT_STATUS_ILLEGAL, message);
    }
    
    public AcctStatusIllegalException(String message, Throwable throwable){
        super(EErrorCode.ACCT_STATUS_ILLEGAL, message, throwable);
    }
}
