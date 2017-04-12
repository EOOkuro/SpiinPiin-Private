/**
 * 
 */
package com.gotanyalo.spiinpiin.core.rest;

import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;

/**
 * @author otkoth
 *
 */
public interface IJxRsExecute {
	
	<T> T run(TSession session) throws SpiinPiinBaseException;

}
