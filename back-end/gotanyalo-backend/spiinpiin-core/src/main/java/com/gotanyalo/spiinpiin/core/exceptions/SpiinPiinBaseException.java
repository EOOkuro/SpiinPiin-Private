/**
 * 
 */
package com.gotanyalo.spiinpiin.core.exceptions;

import com.gotanyalo.spiinpiin.core.data.ErrorTag;

/**
 * @author otkoth
 *
 */
public class SpiinPiinBaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7111223850430087474L;

	private ErrorTag tag;
	
	/**
	 * @param message
	 */
	public SpiinPiinBaseException(String message, ErrorTag tag) {
		super(message);
		
		this.tag = tag;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SpiinPiinBaseException(String message, Throwable cause, ErrorTag tag) {
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