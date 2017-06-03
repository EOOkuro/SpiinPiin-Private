package com.gotanyalo.spiinpiin.core.service;

import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gotanyalo.spiinpiin.core.data.DBOperationType;
import com.gotanyalo.spiinpiin.core.data.MemberModule;
import com.gotanyalo.spiinpiin.core.data.MemberType;
import com.gotanyalo.spiinpiin.core.data.TSession;
import com.gotanyalo.spiinpiin.core.exceptions.ContactNotFoundException;
import com.gotanyalo.spiinpiin.core.exceptions.DBOperationException;
import com.gotanyalo.spiinpiin.core.exceptions.InvalidParameterException;
import com.gotanyalo.spiinpiin.core.exceptions.MemberExistException;
import com.gotanyalo.spiinpiin.core.exceptions.MemberNotFoundException;
import com.gotanyalo.spiinpiin.core.exceptions.SpiinPiinBaseException;
import com.gotanyalo.spiinpiin.core.model.Contact;
import com.gotanyalo.spiinpiin.core.model.Member;

public class SpiinPiinManagementUser extends SpiinPiinManagementAlerts implements ISpiinPiinManagementUser {

	private static final transient Logger logger =  LoggerFactory.getLogger(
			SpiinPiinManagementUser.class.getClass().getName());
	
	@Override
	public void addContact(TSession session, final Contact obj) throws SpiinPiinBaseException {
		logger.debug(
				"addContact :: Parameters (Contact (obj) = [null != " + 
						Boolean.toString(obj != null) + "])");
		
		this.execute(session, null, "addContact", null,
				new IActionExcecutor() {

					@SuppressWarnings("unchecked")
					@Override
					public Boolean run(String userName, String userId, String role)
							throws InvalidParameterException, DBOperationException, ContactNotFoundException {
						obj.setMemberId(userId);
						obj.setHomePhone(strVal(obj.getHomePhone()));
						obj.setMobile(strVal(obj.getMobile()));
						obj.setName(strVal(obj.getName()));
						obj.setPhysicalLocation(strVal(obj.getPhysicalLocation()));
						obj.setPostalAddress(strVal(obj.getPostalAddress()));
						obj.setState(strVal(obj.getState()));
						obj.setZip(strVal(obj.getZip()));
						
						dbOperation(obj, DBOperationType.Add, "Contact");						
						
						return true;
					}
				}, MemberModule.CLIENT);
		
	}

	private Contact getContact(int id, String mid) 
			throws ContactNotFoundException{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT obj FROM Contact obj ");
		sb.append("WHERE obj.id=:id AND obj.memberId=:mid");
		TypedQuery<Contact> qry = 
				this.entityManager.createQuery(sb.toString(), Contact.class);
		qry.setParameter("id", id);
		qry.setParameter("mid", mid);
		
		try {
			return qry.getSingleResult();
		} catch (NoResultException e) {
			throw new ContactNotFoundException(
					"Contact with id = {" + id + 
					"}, for member = {" + mid + "}, not found!");
		}
	}
	
	@Override
	public void removeContact(TSession session, final int id) throws SpiinPiinBaseException {
		logger.debug(
				"removeMemberContact :: Parameters (id = {" + id + "})");
		
		this.execute(session, null, "removeMemberContact", null,
				new IActionExcecutor() {

					@SuppressWarnings("unchecked")
					@Override
					public Boolean run(String userName, String userId, String role)
							throws InvalidParameterException, DBOperationException, ContactNotFoundException {

						Contact obj = getContact(id, userId);
						
						dbOperation(obj, DBOperationType.Delete, "Contact");						
						
						return true;
					}
				}, MemberModule.CLIENT);
		
	}

	@Override
	public void setPhoto(TSession session, final byte[] photo) throws SpiinPiinBaseException {
		logger.debug(
				"setPhoto :: Parameters (byte[] (photo) = [null != " + 
						Boolean.toString(photo != null) + 
						"] )");

		if (photo == null){
			throw new InvalidParameterException("byte[] (photo) is null!");
		}
		
		this.execute(session, null, "setPhoto", null,
				new IActionExcecutor() {

					@SuppressWarnings("unchecked")
					@Override
					public Boolean run(String userName, String userId, String role)
							throws InvalidParameterException, DBOperationException, MemberNotFoundException {

						Member obj = getMember(userId);
						obj.setPhoto(photo);
						
						dbOperation(obj, DBOperationType.Update, "SetPhoto");						
						
						return true;
					}
				}, MemberModule.CLIENT);
		
	}

	@Override
	public void updateMember(TSession session, final Member mbr) 
			throws SpiinPiinBaseException {
		logger.debug(
				"updateMember :: Parameters (Member (mbr) = [null != " + 
						Boolean.toString(mbr != null) + 
						"] )");

		if (mbr == null){
			throw new InvalidParameterException("Member (mbr) is null!");
		}
		
		this.execute(session, null, "updateMember", null,
				new IActionExcecutor() {

					@SuppressWarnings("unchecked")
					@Override
					public Boolean run(String userName, String userId, String role)
							throws InvalidParameterException, DBOperationException, MemberNotFoundException {

						Member obj = getMember(userId);
						if (isValid(mbr.getDisplayName())){
							obj.setDisplayName(strVal(mbr.getDisplayName()));
						}
						
						if (isValid(mbr.getFirstName())){
							obj.setFirstName(strVal(mbr.getFirstName()));
						}
						
						if (isValid(obj.getLastName())){
							obj.setLastName(strVal(mbr.getLastName()));
						}
						
						if (isValid(obj.getMiddleName())){
							obj.setMiddleName(strVal(mbr.getMiddleName()));
						}
						
						obj.setDob(mbr.getDob());
						obj.setGender(mbr.getGender());
						
						dbOperation(obj, DBOperationType.Update, "Member");						
						
						return true;
					}
				}, MemberModule.CLIENT);		
	}

	@Override
	public void updateContact(TSession session, final Contact obj) throws SpiinPiinBaseException {
		logger.debug(
				"updateContact :: Parameters (Contact (obj) = [null != " + 
						Boolean.toString(obj != null) + 
						"] )");

		if (obj == null){
			throw new InvalidParameterException("Contact (obj) is null!");
		}
		
		this.execute(session, null, "updateContact", null,
				new IActionExcecutor() {

					@SuppressWarnings("unchecked")
					@Override
					public Boolean run(String userName, String userId, String role)
							throws InvalidParameterException, DBOperationException, ContactNotFoundException {

						Contact sobj = getContact(obj.getId(), userId);
						if (isValid(obj.getHomePhone())){
							sobj.setHomePhone(strVal(obj.getHomePhone()));
						}
						
						if (isValid(obj.getMobile())){
							sobj.setMobile(strVal(obj.getMobile()));
						}
						
						if (isValid(obj.getName())){
							sobj.setName(strVal(obj.getName()));
						}
						
						if (isValid(obj.getPhysicalLocation())){
							sobj.setPhysicalLocation(strVal(obj.getPhysicalLocation()));
						}
						
						if (isValid(obj.getPostalAddress())){
							sobj.setPostalAddress(strVal(obj.getPostalAddress()));
						}
						
						if (isValid(obj.getState())){
							sobj.setState(strVal(strVal(obj.getState())));
						}
						
						if (isValid(obj.getZip())){
							sobj.setZip(strVal(obj.getZip()));
						}
						
						sobj.setCountry(obj.getCountry());
						
						dbOperation(sobj, DBOperationType.Update, "Contact");						
						
						return true;
					}
				}, MemberModule.CLIENT);		
	}

	protected Member getUserByFederatedData(String fid)
			throws InvalidParameterException, MemberNotFoundException {
		logger.info("getUserByFederatedData :: Parameters (fid = {" + fid + "})");
		
		if (!this.isValid(fid)) {
			throw new InvalidParameterException(
					"federated UID is either null or empty.");
		}
				
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT obj FROM Member obj ");
		sb.append("WHERE obj.fuid=:fid");
		
		TypedQuery<Member> query = this.entityManager.createQuery(sb.toString(), Member.class);
		query.setParameter("fid", fid);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			throw new MemberNotFoundException(
					"Member with fid = {" + fid + "} not found", e);		
		}
	}
	
	@Override
	public void registerMember(Member mbr) throws SpiinPiinBaseException {
		logger.debug(
				"registerMember :: Parameters (Member (mbr) = [null != " + 
						Boolean.toString(mbr != null) + 
						"])");

		if (mbr == null){
			throw new InvalidParameterException("Member (mbr) is null!");
		}
		
		if (mbr.getDob() == null){
			throw new InvalidParameterException("Date of birth is missing!");
		}
		
		if (!this.isValid(mbr.getFemail())){
			throw new InvalidParameterException("Email is missing!");
		}
		
		if (!this.isValid(mbr.getProvider())){
			throw new InvalidParameterException("Provider is missing!");
		}
		
		if (!this.isValid(mbr.getFuid())){
			throw new InvalidParameterException("Federated-ID is missing!");
		}
		
		try {
			getUserByFederatedData(mbr.getFuid());
			// Raise an exception.
			throw new MemberExistException(
					"A member with fid = {" + mbr.getFuid() + "}, already exists!");
		} catch (MemberNotFoundException e) {
			// DO NOTHING
		}
		
		if (isValid(mbr.getFirstName())){
			mbr.setFirstName(strVal(mbr.getFirstName()));
		}
		
		if (isValid(mbr.getMiddleName())){
			mbr.setMiddleName(strVal(mbr.getMiddleName()));
		}
		
		if (isValid(mbr.getLastName())){
			mbr.setLastName(strVal(mbr.getLastName()));
		}
		
		if (!isValid(mbr.getDisplayName())){
			StringBuilder sb = new StringBuilder();
			boolean isset = false;
			if (isValid(mbr.getFirstName())){
				sb.append(mbr.getFirstName());
				isset = true;
			}
			
			if (isValid(mbr.getMiddleName())){
				if (isset){
					sb.append(" ");
				}
				
				sb.append(mbr.getMiddleName());
				isset = true;
			}
			
			if (isValid(mbr.getLastName())){
				if (isset){
					sb.append(" ");
				}
				
				sb.append(mbr.getLastName());
			}
			
			mbr.setDisplayName(sb.toString());
		} else {
			mbr.setDisplayName(strVal(mbr.getDisplayName()));
		}
									
		if (isValid(mbr.getFemail())){
			mbr.setFemail(strVal(mbr.getFemail()));
		}
		
		//mbr.setDob(dob);
		//mbr.setFemail(femail);
		//mbr.setFirstName(firstName);
		//mbr.setFuid(fuid);
		//mbr.setGender(gender);
		mbr.setId(UUID.randomUUID().toString());
		
		//mbr.setProvider(provider);
		//mbr.setPhoto(photo);
									
		dbOperation(mbr, DBOperationType.Add, "Member");						
		//return true;
		
	}

}
