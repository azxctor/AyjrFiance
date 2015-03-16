package com.hengxin.platform.common.exception;

public class ShowStackTraceException extends KmfexBaseException {
	
    private static final long serialVersionUID = 1L;
    
    public ShowStackTraceException(){
        super();
    }
    
    public ShowStackTraceException(DisplayableError error){
        super(error);
    }
    
    public ShowStackTraceException(DisplayableError error, String message){
        super(error, message);
    }
    
    public ShowStackTraceException(DisplayableError error, String message, Throwable cause){
        super(error, message, cause);
    }
}
