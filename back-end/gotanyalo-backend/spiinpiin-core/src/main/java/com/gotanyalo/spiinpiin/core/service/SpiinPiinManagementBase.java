/**
 * 
 */
package com.gotanyalo.spiinpiin.core.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceProperty;
import javax.persistence.TypedQuery;

import org.apache.commons.io.FileUtils;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gotanyalo.spiinpiin.core.data.DBOperationType;
import com.gotanyalo.spiinpiin.core.data.ImgFormat;
import com.gotanyalo.spiinpiin.core.data.MemberModule;
import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.AccessDeniedException;
import com.gotanyalo.spiinpiin.core.exceptions.DBAddOperationException;
import com.gotanyalo.spiinpiin.core.exceptions.DBDeleteOperationException;
import com.gotanyalo.spiinpiin.core.exceptions.DBOperationException;
import com.gotanyalo.spiinpiin.core.exceptions.DBUpdateOperationException;
import com.gotanyalo.spiinpiin.core.exceptions.InsufficientPrivilageException;
import com.gotanyalo.spiinpiin.core.exceptions.InvalidParameterException;
import com.gotanyalo.spiinpiin.core.exceptions.MemberNotFoundException;
import com.gotanyalo.spiinpiin.core.exceptions.MethodExecRoleNotSpecifiedException;
import com.gotanyalo.spiinpiin.core.exceptions.PropertyFileReadException;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.model.Country;
import com.gotanyalo.spiinpiin.core.model.Language;
import com.gotanyalo.spiinpiin.core.model.Member;
import com.gotanyalo.spiinpiin.core.model.Occupation;

/**
 * @author otkoth
 *
 */
public class SpiinPiinManagementBase implements ISpiinPiinManagementBase {

protected String separator = System.getProperty("file.separator");

	@PersistenceContext(unitName="spiinds", properties={
		@PersistenceProperty(name="javax.persistence.cache.storeMode", value="REFRESH")
	})
	protected EntityManager entityManager;

	private static final transient Logger logger =  LoggerFactory.getLogger(
			SpiinPiinManagementBase.class.getClass().getName());
	
	protected final String TemplateReport = String.format(
			"%s%sMcFish%sreports",
			System.getProperty("jboss.server.data.dir"),
			this.separator,
			this.separator);
	
	protected final String TempPath = String.format(
			"%s%sMcFish%s",
			System.getProperty("jboss.server.data.dir"),
			this.separator,
			this.separator);
	
	private final String ModulePath = String.format("%s%ssystem%slayers%sbase", 
			System.getProperty("jboss.modules.dir"),
			this.separator,
			this.separator,
			this.separator);
	
	private final String JDTCompiler = String.format("%s%snet%ssf%sjasperreports%smain%s", 
			this.ModulePath,
			this.separator,
			this.separator,
			this.separator,
			this.separator,
			this.separator);
	
	private final String ConfigFile = String.format(
			"%s%sMcFish%sconf.properties",
			System.getProperty("jboss.server.data.dir"),
			this.separator,
			this.separator);

	protected final String ResourceFolder = String.format(
			"%s%sMcFish%s",
			System.getProperty("jboss.server.data.dir"),
			this.separator,
			this.separator);
	
	protected boolean isValid(String val){
		return val != null && !val.trim().equals("");
	}
	
	protected <T> void dbOperation(T obj, DBOperationType type, String msgOnError) 
			throws DBOperationException {
		if (type == DBOperationType.Add){
			try {
				this.entityManager.persist(obj);
				this.entityManager.flush();
			} catch (Exception e) {
				throw new DBAddOperationException(msgOnError + " 'Add' operation failed!", e);
			}
		} else if (type == DBOperationType.Delete){
			try {			
				this.entityManager.remove(obj);
				this.entityManager.flush();
			} catch (Exception e) {
				throw new DBDeleteOperationException(msgOnError + " 'Remove' operation failed!", e);
			}
		} else if (type == DBOperationType.Update){
			try {			
				this.entityManager.merge(obj);
				this.entityManager.flush();
			} catch (Exception e) {
				throw new DBUpdateOperationException(msgOnError + " 'Update' operation failed!", e);
			}
		} else if (type == DBOperationType.Get){
			// DO NOTHING.
		} else {
			// DO NOTHING.
		}
	}
	
	/* (non-Javadoc)
	 * @see com.softdynamics.insurance.admin.service.IInsuranceManagementBase#getCustomCss(java.lang.String)
	 */
	@Override
	public byte[] getCustomCss(String domain) {
		try {
			if (this.isValid(domain)){
				return FileUtils.readFileToByteArray(
						new File(this.ResourceFolder + domain + this.separator +"custom.css"));
			} else {
				return FileUtils.readFileToByteArray(
						new File(this.ResourceFolder + "custom.css"));
			}						
		} catch (Exception e) {			
			logger.warn(e.getMessage());
		}
		
		return "".getBytes();
	}

	/* (non-Javadoc)
	 * @see com.softdynamics.insurance.admin.service.IInsuranceManagementBase#getSystemProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String getSystemProperty(String key, String domain) 
			throws SpiinPiinBaseException {
		return this.getSystemConfigValue(key, domain);
	}
	
	protected String getSystemConfigValue(String key, String domain) 
			throws InvalidParameterException, PropertyFileReadException{
		logger.debug("getSystemConfigValue :: Parameters (key = {" + key + "})");
		
		if (key == null || key.equals("")){
			throw new InvalidParameterException("key is either null or empty.");
		}
		
		String configFile = this.ConfigFile;
		if (this.isValid(domain)){
			configFile = String.format(
					"%s%sMcFish%s%s%sconf.properties",
					System.getProperty("jboss.server.data.dir"),
					this.separator,
					this.separator,
					domain,
					this.separator);
		}
		
		return getConfigValue(key, configFile);			
	}
	
	protected String getConfigValue(String key, String file) 
			throws InvalidParameterException, PropertyFileReadException{
		logger.debug(
				"getConfigValue :: Parameters (key = {" + 
						key + 
						"}, file = {" + 
						file + 
						"})");
		
		if (key == null || key.equals("")){
			throw new InvalidParameterException("key is either null or empty.");
		}
		
		if (file == null || file.equals("")){
			throw new InvalidParameterException("file is either null or empty.");
		}
		
		try {
			InputStream io = new FileInputStream(file);						
			
			Properties props = new  Properties();
			props.load(io);
			
			return (String)props.get(key);
		} catch (IOException e) {
			throw new PropertyFileReadException(e.getMessage(), e);
		}	
	}

	/* (non-Javadoc)
	 * @see com.softdynamics.insurance.admin.service.IInsuranceManagementBase#print(java.lang.String, java.lang.String, java.util.HashMap, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public byte[] print(String user, String tenantId, HashMap<String, String> obj, String reportName, String title,
			String header) throws SpiinPiinBaseException {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected ImgFormat strToImgFormat(String img){
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

	protected ImgFormat getImgFormat(byte[] val) throws IOException{
		InputStream in = new ByteArrayInputStream(val);
		ImageInputStream img = ImageIO.createImageInputStream(in);
		Iterator<ImageReader> readers =  ImageIO.getImageReaders(img);
		if (readers.hasNext()){
			ImageReader ir = readers.next();
			return this.strToImgFormat(ir.getFormatName());
		}
		
		return null;
	}
	
	protected Member getMember(String userId) 
			throws InvalidParameterException, MemberNotFoundException {
		logger.info("getMember :: Parameters (userId = {" + userId + "})");
		
		if (userId == null || userId.trim().equals("")){
			throw new InvalidParameterException("userId is either null or empty.");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT obj FROM Member obj WHERE obj.id=:id");
		
		TypedQuery<Member> qry = 
				this.entityManager.createQuery(sb.toString(), Member.class);		
		qry.setParameter("id", userId);
		
		try {
			return (Member)qry.getSingleResult();
		} catch (NoResultException e) {
			throw new MemberNotFoundException(
					"Member with userId = {" + userId + "}, not found", e);
		}
		
	}
	
	protected String strVal(String val) 
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
	
	protected <T> T execute(
			TSession session, 
			String actedon, 
			String executingAction,
			String message,
			IActionExcecutor execute,
			MemberModule... roles) 
		throws SpiinPiinBaseException {
		
		logger.info(
				"Execute :: (session = [null == " + 
				Boolean.toString(session == null) + 
				"], actedon = {" +
				actedon +
				"}, executingAction = {" + 
				executingAction + 
				"}, message = {" + 
				message + 
				"},  IActionExcecutor (execute) = [null == " + 
				Boolean.toString(execute == null) + 
				"], roles [null == " + 
				Boolean.toString(roles == null) + 
				"])");
		
		if (session == null){
			throw new AccessDeniedException("Session is null");
		}
		
		if (session.getRole() == null){
			throw new AccessDeniedException("Sesssion role is missing.");
		}
		
		if (execute == null){
			throw new InvalidParameterException("IActionExcecutor (execute) is null.");
		}
		
		if (roles == null){
			throw new MethodExecRoleNotSpecifiedException(
					executingAction + " system executing roles not specified.");
		}
		
		if (session.getRole() != MemberModule.ADMIN){
			// Verify user role is acceptable.		
			boolean isValid = false;
			for (MemberModule mr : roles) {
				if (mr == MemberModule.ALL || mr == session.getRole()){
					isValid = true;
					break;
				}
			}
			
			if (!isValid){
				throw new InsufficientPrivilageException(
						"The user {" + 
								session.getUserName() + 
								"} with role {" + 
								session.getRole() + 
								"}, doesn't have rights to access {" + 
								executingAction + 
								"}.");
			}
		}
				
		MDC.put("CallerName", session.getUserName());
		MDC.put("Caller", session.getUserId());
		MDC.put("Method", executingAction);
		MDC.put("ActedOn", message);
		logger.warn("InsuranceManagementBean - Executing -->");
		
		try {
			T result = execute.run(
					session.getUserName(),
					session.getUserId(),
					session.getRole().toString());
			
			return result;
		} finally {
			logger.warn("InsuranceManagementBean - Executing <--");
			MDC.remove("CallerName");
			MDC.remove("Caller");
			MDC.remove("Method");
			MDC.remove("Tenant");
			MDC.remove("ActedOn");
		}
	}

	@Override
	public List<Language> listLanguage() {
		TypedQuery<Language> qry = this.entityManager.createQuery(
				"SELECT obj FROM Language obj ORDER BY obj.name ASC", 
				Language.class);
		return qry.getResultList();
	}

	@Override
	public List<Country> listCountry() {
		TypedQuery<Country> qry = this.entityManager.createQuery(
				"SELECT obj FROM Country obj ORDER BY obj.description ASC", 
				Country.class);
		return qry.getResultList();
	}

	@Override
	public List<Occupation> listOccupation() {
		TypedQuery<Occupation> qry = this.entityManager.createQuery(
				"SELECT obj FROM Occupation obj ORDER BY obj.name ASC", 
				Occupation.class);
		return qry.getResultList();
	}	
}