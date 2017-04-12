package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class ObjectToJsonConversionException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6533019052732032674L;

	public ObjectToJsonConversionException(String message) {
		super(message, ErrorTag.NA);
	}

	public ObjectToJsonConversionException(String message, Throwable cause) {
		super(message, cause, ErrorTag.NA);
	}

}
