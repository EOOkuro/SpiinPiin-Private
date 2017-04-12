/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class ObjectNotFound extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4743605035207868952L;

	/**
	 * @param message
	 * @param tag
	 */
	public ObjectNotFound(String message) {
		super(message, ErrorTag.Get);
	}

	/**
	 * @param message
	 * @param cause
	 * @param tag
	 */
	public ObjectNotFound(String message, Throwable cause) {
		super(message, cause, ErrorTag.Get);
	}

}
