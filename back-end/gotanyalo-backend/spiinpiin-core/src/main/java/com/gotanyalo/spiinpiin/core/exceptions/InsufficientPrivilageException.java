/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class InsufficientPrivilageException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3383963094186615417L;

	/**
	 * @param message
	 * @param tag
	 */
	public InsufficientPrivilageException(String message) {
		super(message, ErrorTag.Access);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param tag
	 */
	public InsufficientPrivilageException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Access);
		// TODO Auto-generated constructor stub
	}

}
