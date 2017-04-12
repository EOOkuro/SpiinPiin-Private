package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class DeleteSubscriptionException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6844221862117342587L;

	public DeleteSubscriptionException(String message) {
		super(message, ErrorTag.Delete);
	}

	public DeleteSubscriptionException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Delete);
	}

}
