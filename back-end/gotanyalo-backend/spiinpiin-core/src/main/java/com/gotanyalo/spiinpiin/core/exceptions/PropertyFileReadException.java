/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class PropertyFileReadException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2052268600046926865L;

	/**
	 * @param message
	 * @param tag
	 */
	public PropertyFileReadException(String message, ErrorTag tag) {
		super(message, ErrorTag.Get);
	}

	/**
	 * @param message
	 * @param cause
	 * @param tag
	 */
	public PropertyFileReadException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Get);
	}

}
