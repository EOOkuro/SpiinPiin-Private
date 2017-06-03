/**
 * 
 */
package io.microservices.user.exception;

import io.microservices.user.enums.ErrorTag;

/**
 * @author otkoth
 *
 */
public class UserBaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6322350929871985937L;
	
	private ErrorTag tag;
	
	/**
	 * @param message
	 */
	public UserBaseException(String message, ErrorTag tag) {
		super(message);
		
		this.tag = tag;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserBaseException(String message, Throwable cause, ErrorTag tag) {
		super(message, cause);
		
		this.tag = tag;
	}

	public ErrorTag getTag() {
		return tag;
	}

	public void setTag(ErrorTag tag) {
		this.tag = tag;
	}
}