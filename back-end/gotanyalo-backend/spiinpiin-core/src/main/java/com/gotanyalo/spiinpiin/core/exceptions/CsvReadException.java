/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;


/**
 * @author otkoth
 *
 */
public class CsvReadException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4421949900331969567L;

	/**
	 * @param message
	 */
	public CsvReadException(String message) {
		super(message, ErrorTag.File);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CsvReadException(String message, Throwable cause) {
		super(message, cause, ErrorTag.File);
	}

}