/**
 * 
 */
package com.gotanyalo.spiinpiin.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author otkoth
 *
 */
@XmlRootElement
@Entity
@Table(name="Contact")
public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String memberId;
	
	private String postalAddress;
	
	private String state;
		
	private String zip;
	
	private String physicalLocation;
	
	private String homePhone;
	
	private String mobile;
		
	private String name;
		
	private String country;
	
	@Column(name="spin_membc_state", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=50)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name="spin_membc_country", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=4)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Column(name="spin_membc_zip", 
			insertable=true, 
			nullable=true,
			unique=false,
			updatable=true,
			length=10)
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name="spin_membc_name", 
			insertable=true, 
			nullable=true,
			unique=false,
			updatable=true,
			length=100)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="spin_membc_id", 
			insertable=false, 
			nullable=false,
			unique=true,
			updatable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="spin_membc_mid", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=false,
			length=38)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	@Column(name="spin_membc_postaladdr", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=500)
	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	@Column(name="spin_membc_phyloc", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=500)
	public String getPhysicalLocation() {
		return physicalLocation;
	}

	public void setPhysicalLocation(String physicalLocation) {
		this.physicalLocation = physicalLocation;
	}

	@Column(name="spin_membc_homephone", 
			insertable=true, 
			nullable=true,
			unique=false,
			updatable=true,
			length=20)
	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@Column(name="spin_membc_mobile", 
			insertable=true, 
			nullable=true,
			unique=false,
			updatable=true,
			length=20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}	
}