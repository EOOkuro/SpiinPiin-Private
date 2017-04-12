/**
 * 
 */
package com.gotanyalo.spiinpiin.core.service;

import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.model.Contact;
import com.gotanyalo.spiinpiin.core.model.Member;

/**
 * @author otkoth
 *
 */
public interface ISpiinPiinManagementUser extends ISpiinPiinManagementAlerts {
		
	void addContact(TSession session, Contact obj)
			throws SpiinPiinBaseException;
	
	void removeContact(TSession session, int id)
			throws SpiinPiinBaseException;
	
	void setPhoto(TSession session, byte[] photo) 
			throws SpiinPiinBaseException;

	void updateMember(TSession session, Member mbr)
			throws SpiinPiinBaseException;
	
	void updateContact(TSession session, Contact obj)
			throws SpiinPiinBaseException;
	
	void registerMember(Member mbr)
			throws SpiinPiinBaseException;
}
