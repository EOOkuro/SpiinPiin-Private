/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

/**
 * @author otkoth
 *
 */
public class ContactNotFoundException extends ObjectNotFound {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5332679045688857275L;

	/**
	 * @param message
	 */
	public ContactNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ContactNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
