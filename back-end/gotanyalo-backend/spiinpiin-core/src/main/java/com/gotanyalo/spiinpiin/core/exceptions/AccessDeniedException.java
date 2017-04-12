/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class AccessDeniedException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6071091275979270760L;

	/**
	 * @param message
	 * @param tag
	 */
	public AccessDeniedException(String message) {
		super(message, ErrorTag.Access);
	}

	/**
	 * @param message
	 * @param cause
	 * @param tag
	 */
	public AccessDeniedException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Access);
	}

}
