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
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
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

import com.gotanyalo.spiinpiin.core.data.FederatedUser;
import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.InvalidParameterException;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.model.ASession;
import com.gotanyalo.spiinpiin.core.model.Contact;
import com.gotanyalo.spiinpiin.core.model.Member;
import com.gotanyalo.spiinpiin.core.data.Img;
import com.gotanyalo.spiinpiin.core.data.ImgFormat;
import com.gotanyalo.spiinpiin.core.data.TMember;
import com.gotanyalo.spiinpiin.core.data.TResult;
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
		
	private TResult<TSession> auth(
			FederatedUser usr,
			String moduleId,
			HttpServletRequest request){
		try {			
			
			usr.setDomain(this.getServerName(request));
			usr.setModuleId(moduleId);
			TSession session = this.insMgt.authMember(usr);
			
			return new TResult<TSession>(8, this.auth(session, request), null);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			TSession fail = new TSession(null, null, null, false, false);
			fail.setStatus(-5);
			
			return new TResult<TSession>(8, fail, "Authentication failed!");			
		}
	}
	
	private TResult<TSession> auth(
			String key,
			String moduleId,
			HttpServletRequest request){
		try {
			
			TSession session = this.insMgt.getSession(key);
			session = this.insMgt.fillTSession(session, moduleId, this.getServerName(request));
			
			return new TResult<TSession>(8, this.auth(session, request), null);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			TSession fail = new TSession(null, null, null, false, false);
			fail.setStatus(-5);
			
			return new TResult<TSession>(-5, fail, "Authentication failed!");			
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
			TSession fail = new TSession(null, null, null, false, false);
			fail.setStatus(-5);
			
			return fail;			
		}
	}
	
	@GET
    @Path("/authwithkey")
    @Produces(MediaType.APPLICATION_JSON)
	public TResult<TSession> authenticate(
			@HeaderParam("ModuleID") String moduleId,
			@HeaderParam("AuthorizationKey") String sessionKey,
			@Context HttpServletRequest request){		
		return this.auth(sessionKey, moduleId, request);		
	}
			
	@GET
    @Path("/authfedusr/{fuid}/{email}/{isverified}/{provider}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TResult<TSession> authenticateFederated(
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
	public TResult<String> getLogo(@Context HttpServletRequest request){		
		return new TResult<String>(
				1, 
				this.insMgt.getLogo(this.getServerName(request)), 
				null);
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
	public TResult<Boolean> logout(@HeaderParam("AuthorizationKey") String sessionKey){
		try {		
			this.insMgt.removeSession(sessionKey);			
						
			return new TResult<Boolean>(1, true, null);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			
			return new TResult<Boolean>(-1, false, e.getMessage());			
		}
	}
			
	@POST
    @Path("/registeruser")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TResult<Boolean> registerUser(
			final TMember obj,
			@Context HttpServletRequest request){		
		try {
			Member mbr = new Member();
			// TODO Add email to the mix.
			mbr.setDisplayName(strVal(obj.getName()));
			mbr.setFemail(strVal(obj.getFemail()));
			mbr.setFuid(strVal(obj.getFuid()));
			if (isStringValid(obj.getPhoto())){
				mbr.setPhoto(obj.getPhoto().getBytes("UTF-8"));
			}
			
			String[] names = crunchName(mbr.getDisplayName());
			if (names.length > 0){
				mbr.setFirstName(names[0]);
			}
			
			if (names.length > 2){
				mbr.setLastName(names[2]);
				mbr.setMiddleName(names[1]);
			} else if (names.length > 1){
				mbr.setLastName(names[1]);
			}
						
			mbr.setProvider(strVal(obj.getProvider()));
			
			//try {
				//mbr.setDob(df.parse(mbr.getIdob()));
			//} catch (ParseException e) {
				//logger.log(Level.SEVERE, e.getMessage(), e);
			//}
			
			insMgt.registerMember(mbr);
			
			// Now do contact.
			Contact cont = new Contact();
			cont.setCountry(strVal(obj.getCountryCode()));
			//cont.setMemberId(memberId);
			cont.setMobile(strVal(obj.getPhone()));
			cont.setName(mbr.getDisplayName());
			
			// TODO Add Contact logic to the mix.
			//insMgt.addContact(session, obj);
			
			return new TResult<Boolean>(1, true, null);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return new TResult<Boolean>(-1, false, e.getMessage());
		}		
	}
	
	private String[] crunchName(String name){
		if (this.isStringValid(name)){
			String[] vals = name.split(" ");
			if (vals != null && vals.length > 0){
				String[] sanitized = new String[vals.length];
				int i = -1;
				for (String v : vals) {
					if (this.isStringValid(v)){						
						i++;
						sanitized[i] = v;						
					}
				}
				
				if (i >= 0){
					String[] rst = new String[i + 1];
					int j = 0;
					for (String s : rst) {
						rst[j] = s;
						if (j == i){
							break;
						}
						
						j++;
					}
					
					return rst;
				}
			}
		}
		
		return null;
	}
		
	@POST
    @Path("/umemberphoto")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public TResult<Boolean> setUserPhoto(
			final Img img,
			@HeaderParam("AuthorizationKey") String sessionKey){
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public TResult<Boolean> run(TSession session) 
					throws SpiinPiinBaseException {
				
				try {		
							
					if (img != null && isStringValid(img.getImg())){
						byte[] imgbyte = img.getImg().getBytes("UTF-8");
						insMgt.setPhoto(session, imgbyte);
						
						return new TResult<Boolean>(1, true, null);
					}
					
					return new TResult<Boolean>(-1, false, "No image provided");
					
				} catch (Exception e) {
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
	public TResult<Boolean> updateContact(
			final Contact obj,
			@HeaderParam("AuthorizationKey") String sessionKey){
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public TResult<Boolean> run(TSession session) throws SpiinPiinBaseException {
								
				insMgt.updateContact(session, obj);
				
				return new TResult<Boolean>(1, true, null);
			}
		});
	}
	
	@POST
    @Path("/umember")
    @Produces(MediaType.APPLICATION_JSON)
	public TResult<Boolean> updateMember(
			final TMember mbr,
			@HeaderParam("AuthorizationKey") String sessionKey){
		return this.excecute(sessionKey, new IJxRsExecute() {
			
			@SuppressWarnings("unchecked")
			@Override
			public TResult<Boolean> run(TSession session) throws SpiinPiinBaseException {
				Member obj = new Member();
				//obj.setDisplayName(displayName);
				
				insMgt.updateMember(session, obj);
				
				return new TResult<Boolean>(1, true, null);
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