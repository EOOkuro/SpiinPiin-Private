/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class DBDeleteOperationException extends DBOperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public DBDeleteOperationException(String message) {
		super(message, ErrorTag.Delete);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DBDeleteOperationException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Delete);
	}

}
