package com.hengxin.platform.common.util.resource;

/**
 * Represents an error occurred when interacting with the {@link ResourcePool}.
 * 
 * @author yeliangjin
 * 
 */
public class ResourceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceException() {
		super();
	}

	public ResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceException(String message) {
		super(message);
	}

	public ResourceException(Throwable cause) {
		super(cause);
	}
}
