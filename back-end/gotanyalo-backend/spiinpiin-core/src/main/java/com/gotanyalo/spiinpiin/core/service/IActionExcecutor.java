/**
 * 
 */
package com.gotanyalo.spiinpiin.core.service;

import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;

/**
 * The Interface IActionExcecutor.
 *
 * @author otkoth
 */
public interface IActionExcecutor {
		
	<T> T run(
			String userName, 
			String userId, 
			String role) 
			throws SpiinPiinBaseException;
}
