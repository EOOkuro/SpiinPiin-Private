/**
 * 
 */
package com.gotanyalo.spiinpiin.core.rest;

import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;

/**
 * @author otkoth
 *
 */
public interface IJxRsExecute1 {
	
	<T> T run() throws SpiinPiinBaseException;

}
