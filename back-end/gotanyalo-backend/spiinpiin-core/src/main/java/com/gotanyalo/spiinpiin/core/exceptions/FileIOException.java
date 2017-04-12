package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class FileIOException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2303057908013124347L;

	public FileIOException(String message) {
		super(message, ErrorTag.Access);
	}

	public FileIOException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Access);
	}

}
