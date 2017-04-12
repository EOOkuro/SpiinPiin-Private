/**
 * 
 */
package com.gotanyalo.spiinpiin.core.rest;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gotanyalo.spiinpiin.core.data.CountryCode;
import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.model.Country;
import com.gotanyalo.spiinpiin.core.model.Language;
import com.gotanyalo.spiinpiin.core.model.Occupation;
import com.gotanyalo.spiinpiin.core.service.ISpiinPiinManagementLocal;

/**
 * @author otkoth
 *
 */
@Path("/base")
@Stateless
public class BaseService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Inject
    private Logger logger;
	
	@Inject
	private ISpiinPiinManagementLocal insMgt;
	
	@GET
	@Path("/lcountry")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Country> listCountry() {
		return insMgt.listCountry();
	}
	
	@GET
	@Path("/llanguage")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Language> listLanguage() {
		return insMgt.listLanguage();
	}
	
	@GET
	@Path("/loccupation")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Occupation> listOccupation() {
		return insMgt.listOccupation();
	}
		
	private <T> T excecute(String sessionKey, IJxRsExecute run){			
		try {			
			TSession session = this.insMgt.getSession(sessionKey);
			
			if (session == null){
				logger.log(Level.SEVERE, "Session {" + sessionKey+ "} is invalid.");
				Response response = Response.status(
						Response.Status.UNAUTHORIZED).entity(
								"Session {" + sessionKey+ "} is invalid.").build();
				
				throw new WebApplicationException(response);
			}
			
			return run.run(session);
		} catch (SpiinPiinBaseException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
			
			throw new WebApplicationException(response);
		}
	}	
	
}