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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.gotanyalo.spiinpiin.core.data.MemberModule;

/**
 * @author otkoth
 *
 */
@XmlRootElement
@Entity
@Table(name="ASession")
public class ASession implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String key;
		
	private String userName;
	
	private String userId;
	
	private MemberModule role;
	
	private String clientIp;
	
	private Date timestamp;
	
	public ASession(){
		
	}
	
	@Id
	// @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="spin_session_key", 
			insertable=true, 
			nullable=false,
			unique=true,
			updatable=false,
			length=36)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name="spin_session_username", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=false,
			length=100)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name="spin_session_userid", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=false,
			length=36)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="spin_session_role", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=false)
	public MemberModule getRole() {
		return role;
	}

	public void setRole(MemberModule role) {
		this.role = role;
	}

	@Column(name="spin_session_clientip", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=false,
			length=100)
	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="spin_session_tstamp", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}	
}