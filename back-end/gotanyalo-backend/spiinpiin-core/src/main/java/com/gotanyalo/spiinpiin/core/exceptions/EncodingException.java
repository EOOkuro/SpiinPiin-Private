package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class EncodingException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2586515197916499702L;

	public EncodingException(String message) {
		super(message, ErrorTag.Get);
	}

	public EncodingException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Get);
	}
}
