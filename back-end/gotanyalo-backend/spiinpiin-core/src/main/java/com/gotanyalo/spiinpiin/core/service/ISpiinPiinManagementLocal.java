package com.gotanyalo.spiinpiin.core.service;

import javax.ejb.Local;

import com.gotanyalo.spiinpiin.core.model.ASession;
import com.gotanyalo.spiinpiin.core.data.FederatedUser;
import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.DBOperationException;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;

@Local
public interface ISpiinPiinManagementLocal extends ISpiinPiinManagementIncidence {
	
	void addSession(TSession obj)
			throws DBOperationException;
	
	TSession authMember(FederatedUser usr) 
			throws SpiinPiinBaseException;
	
	TSession fillTSession(TSession session, String moduleId, String domain)
			throws SpiinPiinBaseException;
	
	String getCopyright(String domain);
	
	byte[] getFavicon(String domain);
	
	boolean getFederatedFlag(String domain);
	
	String getLogo(String domain);
	
	TSession getSession(String key);
	
	ASession getSession(String userId, String clientIp);
		
	void removeSession(String key);
	
	/*TSession authMember(String userName, String password, String moduleId, String domain)
			throws SoftdynamicsBaseException;*/

}
