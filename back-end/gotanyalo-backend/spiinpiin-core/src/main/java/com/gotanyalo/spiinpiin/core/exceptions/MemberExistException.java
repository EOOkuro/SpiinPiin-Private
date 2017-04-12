/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class MemberExistException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7248013905879180166L;

	/**
	 * @param message
	 * @param tag
	 */
	public MemberExistException(String message) {
		super(message, ErrorTag.Add);
	}

	/**
	 * @param message
	 * @param cause
	 * @param tag
	 */
	public MemberExistException(String message, Throwable cause) {
		super(message, cause, ErrorTag.Add);
	}

}
