package com.gotanyalo.spiinpiin.core.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gotanyalo.spiinpiin.core.data.DBOperationType;
import com.gotanyalo.spiinpiin.core.data.FederatedUser;
import com.gotanyalo.spiinpiin.core.data.ImgFormat;
import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.DBOperationException;
import com.gotanyalo.spiinpiin.core.exceptions.InvalidParameterException;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.model.ASession;
import com.gotanyalo.spiinpiin.core.model.Member;

@Stateless
public class SpiinPiinManagementBean 
	extends SpiinPiinManagementIncidence
		implements ISpiinPiinManagementLocal, ISpiinPiinManagementRemote {
	
	private static final transient Logger logger =  LoggerFactory.getLogger(
			SpiinPiinManagementBean.class.getClass().getName());
	
	@Override
	public void addSession(TSession obj) throws DBOperationException {
		if (obj != null) {
			ASession old = this.getSession(obj.getUserId(), obj.getClientIp());
			if (old != null) {
				this.entityManager.remove(old);
				this.entityManager.flush();
			}

			ASession aobj = new ASession();
			aobj.setClientIp(obj.getClientIp());
			aobj.setKey(obj.getKey());
			aobj.setRole(obj.getRole());
			//aobj.setTenantId(obj.getTenantId());
			aobj.setTimestamp(new Date());
			aobj.setUserId(obj.getUserId());
			aobj.setUserName(obj.getUserName());

			this.dbOperation(aobj, DBOperationType.Add, "Session");
		}
	}

	@Override
	public TSession authMember(FederatedUser usr) throws SpiinPiinBaseException {
		logger.info("authMember :: Parameters (FederatedUser (usr) = [fusr != " + 
				Boolean.toString(usr != null) + 
				"])");

		if (usr == null){
			throw new InvalidParameterException("Federated user is null!");
		}
		
		if (!this.isValid(usr.getModuleId())) {
			throw new InvalidParameterException(
					"moduleId is either null or empty.");
		}
		
		TSession session;
		
		Member mbr = this.getUserByFederatedData(usr.getUid());			
		session = new TSession(mbr.getFemail(), mbr.getId(),
				null, false, false);
		
		return this.fillTSession(mbr, session, usr.getModuleId(), usr.getDomain());
	}

	private TSession fillTSession(Member memb, TSession session, String moduleId, String domain) 
			throws SpiinPiinBaseException{
		
		session.setFedauth(true);
		session.setIsonpremise(true);
		// role
		// session.setEmail(usr.getEmail());
		session.setDisplayName(memb.getFirstName() + " "
				+ memb.getLastName());
		//session.setId(memb.getMemberId());
		//session.setCompenyName(tnt.getDescription());
		//session.setOlicense(tnt.getOlicense());
		
		if (memb.getPhoto() != null) {
			try {
				ImgFormat imgFormat = this.getImgFormat(memb.getPhoto());
				if (imgFormat == ImgFormat.JPEG) {
					session.setPhoto("data:image/jpeg;base64,"
							+ org.apache.commons.codec.binary.Base64
									.encodeBase64String(memb.getPhoto()));
				} else if (imgFormat == ImgFormat.PNG) {
					session.setPhoto("data:image/png;base64,"
							+ org.apache.commons.codec.binary.Base64
									.encodeBase64String(memb.getPhoto()));
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		} else {
			try {
				byte[] img = FileUtils.readFileToByteArray(new File(
						this.ResourceFolder + "person_placeholder.png"));

				ImgFormat imgFormat = this.getImgFormat(img);
				if (imgFormat == ImgFormat.JPEG) {
					session.setPhoto("data:image/jpeg;base64,"
							+ org.apache.commons.codec.binary.Base64
									.encodeBase64String(img));
				} else if (imgFormat == ImgFormat.PNG) {
					session.setPhoto("data:image/png;base64,"
							+ org.apache.commons.codec.binary.Base64
									.encodeBase64String(img));
				}
			} catch (Exception e) {
				// Do nothing
				logger.warn(e.getMessage(), e);
			}
		}

		/*if (tnt.getPhoto() != null) {
			try {
				ImgFormat imgFormat = this.getImgFormat(tnt.getPhoto());
				if (imgFormat == ImgFormat.JPEG) {
					session.setSchoolLogo("data:image/jpeg;base64,"
							+ org.apache.commons.codec.binary.Base64
									.encodeBase64String(tnt.getPhoto()));
				} else if (imgFormat == ImgFormat.PNG) {
					session.setSchoolLogo("data:image/png;base64,"
							+ org.apache.commons.codec.binary.Base64
									.encodeBase64String(tnt.getPhoto()));
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}*/

		session.setCopyright("Hillcroft (k)");

		String copyright = getSystemConfigValue("copyright", domain);

		if (isValid(copyright)) {
			session.setCopyright(copyright);
		}
		
		return session;
	}

	@Override
	public TSession fillTSession(TSession session, String moduleId, String domain) 
			throws SpiinPiinBaseException {
		Member memb = getMember(session.getUserId());
		
		return this.fillTSession(memb, session, moduleId, domain);
	}
	
	@Override
	public String getCopyright(String domain) {
		logger.info("getCopyright :: Parameters (" + domain + ")");

		String copyright = null;
		try {
			copyright = getSystemConfigValue("copyright", domain);
		} catch (Exception e) {
			logger.warn("reading copyright value failed!", e);
		}

		return isValid(copyright) ? copyright : "Le'Fins Solutions (k)";
	}

	@Override
	public byte[] getFavicon(String domain) {
		try {
			if (this.isValid(domain)){
				return FileUtils.readFileToByteArray(
						new File(this.ResourceFolder + domain + this.separator + "favicon.ico"));
			} else {
				return FileUtils.readFileToByteArray(
						new File(this.ResourceFolder + "favicon.ico"));
			}			
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public boolean getFederatedFlag(String domain) {
		return true;
	}
	
	@Override
	public String getLogo(String domain) {
		try {
			String fileName = getSystemConfigValue("logo", domain);

			if (this.isValid(domain)){
				fileName = domain + this.separator + fileName;
			}
			
			byte[] img = FileUtils.readFileToByteArray(new File(
					this.ResourceFolder + fileName));

			ImgFormat imgFormat = this.getImgFormat(img);
			if (imgFormat == ImgFormat.JPEG) {
				return "data:image/jpeg;base64,"
						+ org.apache.commons.codec.binary.Base64
								.encodeBase64String(img);
			} else if (imgFormat == ImgFormat.PNG) {
				return "data:image/png;base64,"
						+ org.apache.commons.codec.binary.Base64
								.encodeBase64String(img);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public TSession getSession(String key) {
		if (!this.isValid(key)) {
			return null;
		}

		ASession obj = this.getSessionByKey(key);
		if (obj == null) {
			return null;
		}

		obj.setTimestamp(new Date());

		this.entityManager.merge(obj);
		this.entityManager.flush();

		return new TSession(obj.getKey(), obj.getUserName(), obj.getUserId(),
				obj.getRole(), obj.getClientIp());
	}

	@Override
	public ASession getSession(String userId, String clientIp) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT obj FROM ASession obj ");
		sb.append("WHERE obj.userId=:usrid AND obj.tenantId=:tenant AND obj.clientIp=:clientip");

		TypedQuery<ASession> qry = this.entityManager.createQuery(
				sb.toString(), ASession.class);
		qry.setParameter("usrid", userId);
		qry.setParameter("clientip", clientIp);

		List<ASession> rst = qry.getResultList();

		return rst == null || rst.size() == 0 ? null : rst.get(0);
	}

	private ASession getSessionByKey(String key) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT obj FROM ASession obj ");
		sb.append("WHERE obj.key=:key");

		TypedQuery<ASession> qry = this.entityManager.createQuery(
				sb.toString(), ASession.class);
		qry.setParameter("key", key);

		List<ASession> rst = qry.getResultList();

		return rst == null || rst.size() == 0 ? null : rst.get(0);
	}
		
	@Override
	public void removeSession(String key) {
		if (this.isValid(key)) {
			ASession obj = this.getSessionByKey(key);
			if (obj != null) {
				this.entityManager.remove(obj);
				this.entityManager.flush();
			}
		}
		
	}	
}