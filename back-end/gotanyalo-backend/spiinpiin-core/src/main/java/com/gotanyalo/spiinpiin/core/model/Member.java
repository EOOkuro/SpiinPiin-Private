/**
 * 
 */
package com.gotanyalo.spiinpiin.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.gotanyalo.spiinpiin.core.data.Gender;
import com.gotanyalo.spiinpiin.core.data.MemberModule;
import com.gotanyalo.spiinpiin.core.data.MemberType;

/**
 * @author otkoth
 *
 */
@XmlRootElement
@Entity
@Table(name="Member")
public class Member implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
		
	private MemberModule module;
		
	private byte[] photo;
	
	private Gender gender;
	
	private String displayName;
	
	private Date dob;
	
	private MemberType mtype;
	
	private String fuid;
	
	private String femail;
	
	private String provider;
	
	private String idob;
	
	@Transient		
	public String getIdob() {
		return idob;
	}

	public void setIdob(String idob) {
		this.idob = idob;
	}

	@Column(name="spin_memb_fid", 
			insertable=true,
			nullable=false,
			unique=true,
			updatable=true,
			length=50)
	public String getFuid() {
		return fuid;
	}

	public void setFuid(String fuid) {
		this.fuid = fuid;
	}

	@Column(name="spin_memb_femail", 
			insertable=true,
			nullable=false,
			unique=false,
			updatable=true,
			length=100)
	public String getFemail() {
		return femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}

	@Column(name="spin_memb_provider", 
			insertable=true,
			nullable=false,
			unique=false,
			updatable=true,
			length=100)
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="spin_memb_stype", 
			insertable=true,
			nullable=true,
			unique=false,
			updatable=true)
	public MemberType getMtype() {
		return mtype;
	}

	public void setMtype(MemberType mtype) {
		this.mtype = mtype;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="spin_memb_dob", 
			insertable=true,
			nullable=true,
			unique=false,
			updatable=true)
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Column(name="spin_memb_dname", 
			insertable=true,
			nullable=false,
			unique=false,
			updatable=true,
			length=50)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="spin_memb_gender", 
			insertable=true,
			nullable=true,
			unique=false,
			updatable=true)	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Lob
	@Column(name="spin_memb_photo", 
			insertable=true,
			nullable=true,
			unique=false,
			updatable=true)	
	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Id
	// @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="spin_memb_mbrid", 
			insertable=true, 
			nullable=false,
			unique=true,
			updatable=false,
			length=38)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="spin_memb_fname", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=50)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="spin_memb_mname", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=15)
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	
	@Column(name="spin_memb_lname", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=15)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="spin_memb_module", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=20)
	public MemberModule getModule() {
		return module;
	}

	public void setModule(MemberModule module) {
		this.module = module;
	}
}