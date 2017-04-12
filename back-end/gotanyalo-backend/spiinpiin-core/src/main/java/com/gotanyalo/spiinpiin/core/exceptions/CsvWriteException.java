/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;


/**
 * @author otkoth
 *
 */
public class CsvWriteException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public CsvWriteException(String message) {
		super(message, ErrorTag.File);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CsvWriteException(String message, Throwable cause) {
		super(message, cause, ErrorTag.File);
	}

}