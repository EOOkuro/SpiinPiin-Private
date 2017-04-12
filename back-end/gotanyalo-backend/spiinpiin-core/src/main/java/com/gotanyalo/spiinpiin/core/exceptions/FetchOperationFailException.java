package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class FetchOperationFailException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4913021631441561663L;

	public FetchOperationFailException(String message) {
		super(message, ErrorTag.Get);
	}

	public FetchOperationFailException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Get);
	}

}
