package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class FileUploadException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6501720645152099812L;

	public FileUploadException(String message) {
		super(message, ErrorTag.File);
	}

	public FileUploadException(String message, Throwable cause) {
		super(message, cause, ErrorTag.File);
	}

}
