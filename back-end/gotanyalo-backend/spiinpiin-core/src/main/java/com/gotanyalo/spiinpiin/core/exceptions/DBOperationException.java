/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;


/**
 * @author otkoth
 *
 */
public class DBOperationException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6650852780669450701L;

	/**
	 * @param message
	 */
	public DBOperationException(String message, ErrorTag tag) {
		super(message, tag);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DBOperationException(String message, Throwable cause, ErrorTag tag) {
		super(message, cause, tag);
	}

}
