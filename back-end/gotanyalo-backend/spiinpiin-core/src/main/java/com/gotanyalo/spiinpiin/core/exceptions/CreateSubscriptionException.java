package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class CreateSubscriptionException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1347790666538596611L;

	public CreateSubscriptionException(String message) {
		super(message, ErrorTag.Add);
	}

	public CreateSubscriptionException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Add);
	}

}
