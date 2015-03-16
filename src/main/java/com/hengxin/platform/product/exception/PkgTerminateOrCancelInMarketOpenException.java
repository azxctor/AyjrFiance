package com.hengxin.platform.product.exception;

import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;

public class PkgTerminateOrCancelInMarketOpenException extends BizServiceException{
    private static final long serialVersionUID = 1L;

    public PkgTerminateOrCancelInMarketOpenException(){
        super(EErrorCode.PROD_PKG_OP_NOT_IN_CLOSED_TIME);
    }
    public PkgTerminateOrCancelInMarketOpenException(String message){
        super(EErrorCode.PROD_PKG_OP_NOT_IN_CLOSED_TIME, message);
    }
    
    public PkgTerminateOrCancelInMarketOpenException(String message, Throwable throwable){
        super(EErrorCode.PROD_PKG_OP_NOT_IN_CLOSED_TIME, message, throwable);
    }
}
