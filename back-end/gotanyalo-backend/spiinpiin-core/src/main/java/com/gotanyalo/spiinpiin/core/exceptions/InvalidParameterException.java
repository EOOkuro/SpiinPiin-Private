/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class InvalidParameterException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2987485836767153870L;

	/**
	 * @param message
	 * @param tag
	 */
	public InvalidParameterException(String message) {
		super(message, ErrorTag.NA);
	}

	/**
	 * @param message
	 * @param cause
	 * @param tag
	 */
	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause, ErrorTag.NA);
	}

}