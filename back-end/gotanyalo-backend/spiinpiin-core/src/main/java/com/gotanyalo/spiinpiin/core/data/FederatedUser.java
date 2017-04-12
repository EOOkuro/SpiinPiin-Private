/**
 * 
 */
package com.gotanyalo.spiinpiin.core.data;

import java.io.Serializable;

/**
 * @author otkoth
 *
 */
public class FederatedUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private String uid;
	
	private String provider;
	
	private String email;
	
	private boolean email_verified;
	
	private String domain;
	
	private String moduleId;
		
	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmail_verified() {
		return email_verified;
	}

	public void setEmail_verified(boolean email_verified) {
		this.email_verified = email_verified;
	}
}