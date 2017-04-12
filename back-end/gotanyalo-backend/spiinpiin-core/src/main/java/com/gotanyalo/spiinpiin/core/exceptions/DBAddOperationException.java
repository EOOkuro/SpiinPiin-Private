/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class DBAddOperationException extends DBOperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public DBAddOperationException(String message) {
		super(message, ErrorTag.Add);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DBAddOperationException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Add);
	}

}
