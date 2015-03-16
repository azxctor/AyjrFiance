package com.hengxin.platform.common.exception;

/**
 * Throw this ex when user has been frozen.
 * @author Gregory
 *
 */
public class FrozenUserException extends KmfexBaseException {

	private static final long serialVersionUID = 1L;

	public FrozenUserException(){
        super();
    }
	
    public FrozenUserException(DisplayableError error, String message){
        super(error, message);
    }
}
