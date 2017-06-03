package io.microservices.user.exception;

import io.microservices.user.enums.ErrorTag;

public class DBOperationException extends UserBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8900264276583886162L;

	public DBOperationException(String message, ErrorTag tag) {
		super(message, tag);
	}

	public DBOperationException(String message, Throwable cause, ErrorTag tag) {
		super(message, cause, tag);
	}

}
