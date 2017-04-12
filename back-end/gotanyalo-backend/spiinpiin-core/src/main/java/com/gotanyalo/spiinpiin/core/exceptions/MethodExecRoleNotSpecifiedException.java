/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class MethodExecRoleNotSpecifiedException extends SpiinPiinBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2502140932941697027L;

	/**
	 * @param message
	 * @param tag
	 */
	public MethodExecRoleNotSpecifiedException(String message) {
		super(message, ErrorTag.NA);
	}

	/**
	 * @param message
	 * @param cause
	 * @param tag
	 */
	public MethodExecRoleNotSpecifiedException(String message, Throwable cause) {
		super(message, cause, ErrorTag.NA);
	}

}
