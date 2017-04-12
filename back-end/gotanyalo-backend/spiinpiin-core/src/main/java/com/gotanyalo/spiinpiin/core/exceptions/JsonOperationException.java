package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class JsonOperationException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -614911157411768477L;

	public JsonOperationException(String message) {
		super(message, ErrorTag.Access);
	}

	public JsonOperationException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Access);
	}

}
