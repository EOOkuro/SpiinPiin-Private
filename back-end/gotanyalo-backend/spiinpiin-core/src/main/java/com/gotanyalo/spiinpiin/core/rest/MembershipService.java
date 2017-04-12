/**
 * 
 */
package com.gotanyalo.spiinpiin.core.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.gotanyalo.spiinpiin.core.data.FederatedUser;
import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.InvalidParameterException;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.model.ASession;
import com.gotanyalo.spiinpiin.core.model.Contact;
import com.gotanyalo.spiinpiin.core.model.Member;
import com.gotanyalo.spiinpiin.core.data.Img;
import com.gotanyalo.spiinpiin.core.data.ImgFormat;
import com.gotanyalo.spiinpiin.core.service.ISpiinPiinManagementLocal;

/**
 * @author otkoth
 *
 */
@Path("/membership")
@Stateless
public class MembershipService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Inject
    private Logger logger;
	
	@Inject
	private ISpiinPiinManagementLocal insMgt;
	
	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
	private TSession auth(
			FederatedUser usr,
			String moduleId,
			HttpServletRequest request){
		try {			
			
			usr.setDomain(this.getServerName(request));
			usr.setModuleId(moduleId);
			TSession session = this.insMgt.authMember(usr);
			
			return this.auth(session, request);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			TSession fail = new TSession(null, null, null, null, false, false);
			fail.setStatus(-5);
			
			return fail;			
		}
	}
	
	private TSession auth(
			String key,
			String moduleId,
			HttpServletRequest request){
		try {
			
			TSession session = this.insMgt.getSession(key);
			session = this.insMgt.fillTSession(session, moduleId, this.getServerName(request));
			
			return this.auth(session, request);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			TSession fail = new TSession(null, null, null, null, false, false);
			fail.setStatus(-5);
			
			return fail;			
		}
	}
		
	private TSession auth(TSession session, HttpServletRequest request){
		try {
			ASession asession = null;
			boolean isreqPresent = false;
			if (request != null){
				isreqPresent = true;
				asession = this.insMgt.getSession(
						session.getUserId(), 
						request.getRemoteAddr());
			}
			
			if (asession != null){
				session.setKey(asession.getKey());
				if (isreqPresent){
					session.setClientIp(request.getRemoteAddr());
				}
				
				session.setStatus(-8);
				
				return session;
			} else {
				session.setKey(UUID.randomUUID().toString());
				
				if (isreqPresent){
					session.setClientIp(request.getRemoteAddr());
				}
				
				this.insMgt.addSession(session);
								
				session.setStatus(-8);
				
				return session;
			}
			
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			TSession fail = new TSession(null, null, null, null, false, false);
			fail.setStatus(-5);
			
			return fail;			
		}
	}
	
	@GET
    @Path("/authwithkey")
    @Produces(MediaType.APPLICATION_JSON)
	public TSession authenticate(
			@HeaderParam("ModuleID") String moduleId,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){		
		return this.auth(sessionKey, moduleId, request);		
	}
			
	@GET
    @Path("/authfedusr/{fuid}/{email}/{isverified}/{provider}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TSession authenticateFederated(
			@PathParam("fuid") String fuid,
			@PathParam("email") String email,
			@PathParam("isverified") boolean isverified,
			@PathParam("provider") String provider,
			@HeaderParam("ModuleID") String moduleId,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){
		
		FederatedUser usr = new FederatedUser();
		usr.setUid(fuid);
		usr.setEmail(email);
		usr.setEmail_verified(isverified);
		usr.setProvider(provider);
		
		return this.auth(usr, moduleId, request);		
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
			
	private ImgFormat getImgFormat(byte[] val) throws IOException{
		InputStream in = new ByteArrayInputStream(val);
		ImageInputStream img = ImageIO.createImageInputStream(in);
		Iterator<ImageReader> readers =  ImageIO.getImageReaders(img);
		
		if (readers.hasNext()){			
			ImageReader ir = readers.next();
			
			return this.strToImgFormat(ir.getFormatName());
		}		
		
		return null;
	}
		
		
	@GET
    @Path("/glogo")
    @Produces(MediaType.APPLICATION_JSON)
	public Img getLogo(@Context HttpServletRequest request){		
		return new Img(this.insMgt.getLogo(this.getServerName(request)));	
	}
		
	private String getServerName(HttpServletRequest request){
		if (request == null ||
				isIP(request.getServerName()) ||
				request.getServerName().equalsIgnoreCase(request.getLocalAddr())){
			return null;
		}
		
		return request.getServerName();
	}
		
	private boolean isIP(String text) {
	    Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	    Matcher m = p.matcher(text);
	    return m.find();
	}
	
	private boolean isStringValid(String val){
		if (val == null || 
				val.trim().equals("") || 
				val.trim().equalsIgnoreCase("null")){
			return false;
		}
		
		return true;
	}
	
	@GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
	public boolean logout(@HeaderParam("AuthorizationKey") String sessionKey){
		try {		
			this.insMgt.removeSession(sessionKey);			
						
			return true;
			
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			
			return false;			
		}
	}
			
	@POST
    @Path("/registreuser")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean registerUser(
			final Member mbr,
			@Context HttpServletRequest request){		
		try {
			try {
				mbr.setDob(df.parse(mbr.getIdob()));
			} catch (ParseException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			
			insMgt.registerMember(mbr);
			return true;
		} catch (SpiinPiinBaseException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}		
	}
		
	@POST
    @Path("/umemberphoto/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response setUserPhoto(
			final MultipartFormDataInput io,
			@PathParam("id") final String id,
			@HeaderParam("AuthorizationKey") String sessionKey){
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public Response run(TSession session) throws SpiinPiinBaseException {
				
				try {			
					if (id == null || id.trim().equals("")){
						throw new InvalidParameterException("id is either null or empty.");
					}
								
					Map<String, List<InputPart>> formParts = io.getFormDataMap();
											
					List<InputPart> parts = formParts.get("file");
								
					byte[] imgbyte = null;
					boolean issuccess = false;
					
					ImgFormat imgFormat = null;
					
					if (parts != null && parts.size() > 0){
						for (InputPart in : parts) {					
							try {						
								InputStream istream = in.getBody(InputStream.class, null);
								imgbyte = IOUtils.toByteArray(istream);
								imgFormat = getImgFormat(imgbyte);
								issuccess = true;
							} catch (IOException e) {
								logger.log(Level.SEVERE, e.getMessage(), e);
								return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error:" + e.getMessage()).build();
							}
						}
					}	
								
					if (imgbyte != null && issuccess && imgFormat != null){
						insMgt.setPhoto(session, imgbyte);
						
						if (imgFormat == ImgFormat.JPEG){
							return Response.status(200).entity("data:image/jpeg;base64," + 
									org.apache.commons.codec.binary.Base64.encodeBase64String(imgbyte)).build();
						} else if (imgFormat == ImgFormat.PNG){
							return Response.status(200).entity("data:image/png;base64," + 
									org.apache.commons.codec.binary.Base64.encodeBase64String(imgbyte)).build();
						} else {
							return Response.status(Status.NOT_ACCEPTABLE).entity(
									"Image type {" + imgFormat + "} not supported.").build();
						}
					}
										
					return Response.status(Status.NO_CONTENT).entity("No image uploaded").build();
					
				} catch (SpiinPiinBaseException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
					throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
				}
			}
		});
	}
	
	private ImgFormat strToImgFormat(String img){
		
		if (img == null || img.trim().equals("")){
			return null;
		}
		
		String val = img.trim().toLowerCase();
		
		if (val.equals("jpeg") || val.equals("jpg")){
			return ImgFormat.JPEG;
		} else if (val.equals("png")){
			return ImgFormat.PNG;
		}
				
		return null;
	}
		
	private String strVal(String val) 
			throws InvalidParameterException{
		if (val == null || 
				val.trim().equals("") || 
				val.trim().equalsIgnoreCase("null")){
			return null;
		}
		
		try {
			return URLDecoder.decode(val, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InvalidParameterException("val {" + val + "} decodding failed.", e);
		}
	}
	
	@GET
    @Path("/ucontact")
    @Produces(MediaType.APPLICATION_JSON)
	public boolean updateContact(
			final Contact obj,
			@HeaderParam("AuthorizationKey") String sessionKey){
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public Boolean run(TSession session) throws SpiinPiinBaseException {
								
				insMgt.updateContact(session, obj);
				
				return true;
			}
		});	
	}
		
	@GET
    @Path("/umember")
    @Produces(MediaType.APPLICATION_JSON)
	public boolean updateMember(
			final Member mbr,
			@HeaderParam("AuthorizationKey") String sessionKey){
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public Boolean run(TSession session) throws SpiinPiinBaseException {
				insMgt.updateMember(session, mbr);
				
				return true;
			}
		});
	}
	
	private String validateStr(String val) 
			throws InvalidParameterException{
		
		if (val == null || val.trim().equalsIgnoreCase("null")){
			return null;
		}
		
		try {
			return URLDecoder.decode(val, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InvalidParameterException(
					"val = {" + val + "}, cannot be decoded.");
		}
	}
	
}