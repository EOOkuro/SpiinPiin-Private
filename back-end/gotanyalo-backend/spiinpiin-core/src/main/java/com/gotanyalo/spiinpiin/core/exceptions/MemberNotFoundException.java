/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

/**
 * @author otkoth
 *
 */
public class MemberNotFoundException extends ObjectNotFound {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4342623897452685110L;

	/**
	 * @param message
	 */
	public MemberNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MemberNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
