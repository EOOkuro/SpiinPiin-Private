package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

public class URLConnectionException extends SpiinPiinBaseException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5367704969441371393L;
	
	public URLConnectionException(String message) {
		super(message, ErrorTag.Access);
	}
	
	public URLConnectionException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Access);
	}

}
