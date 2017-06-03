/**
 * 
 */
package io.microservices.user.exception;

import io.microservices.user.enums.ErrorTag;

/**
 * @author otkoth
 *
 */
public class InvalidParameterException extends UserBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2926379580639677498L;

	/**
	 * @param message
	 */
	public InvalidParameterException(String message) {
		super(message, ErrorTag.NA);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause, ErrorTag.NA);
	}

}