/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class DBUpdateOperationException extends DBOperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public DBUpdateOperationException(String message) {
		super(message, ErrorTag.Update);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DBUpdateOperationException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Update);
	}

}
