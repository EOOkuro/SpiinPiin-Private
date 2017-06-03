package io.microservices.user.exception;

import io.microservices.user.enums.ErrorTag;

public class DBAddOperationFailedException extends DBOperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7547092112755116389L;

	public DBAddOperationFailedException(String message) {
		super(message, ErrorTag.Add);
	}

	public DBAddOperationFailedException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Add);
	}

}
