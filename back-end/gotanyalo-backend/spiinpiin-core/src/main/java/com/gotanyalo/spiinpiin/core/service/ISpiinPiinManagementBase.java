/**
 * 
 */
package com.gotanyalo.spiinpiin.core.service;

import java.util.HashMap;
import java.util.List;

import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.model.Country;
import com.gotanyalo.spiinpiin.core.model.Language;
import com.gotanyalo.spiinpiin.core.model.Occupation;

/**
 * @author otkoth
 *
 */
public interface ISpiinPiinManagementBase {
	
	byte[] getCustomCss(String domain);
	
	String getSystemProperty(String key, String domain)
			throws SpiinPiinBaseException;
	
	byte[] print(
			String user,
			String tenantId,
			HashMap<String, String> obj, 
			String reportName, 
			String title,
			String header)
					throws SpiinPiinBaseException;	
	
	List<Language> listLanguage();
	
	List<Country> listCountry();
	
	List<Occupation> listOccupation();	
	
}
