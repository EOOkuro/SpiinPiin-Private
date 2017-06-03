/**
 * 
 */
package com.gotanyalo.spiinpiin.core.data;

import java.io.Serializable;

/**
 * @author otkoth
 *
 */
public class TSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 80440019574311419L;
	
	private String userName; 
	
	private String userId;
		
	private MemberModule role;
	
	private String clientIp;
	
	private String photo;
	
	private String key;
	
	private int status;
	
	private String displayName;
	
	private String companyName;
	
	private String companyLogo;
	
	private String id;
	
	private boolean isonpremise;
	
	private String copyright;
	
	private OLicenceType olicense;
		
	private boolean fedauth;
		
	public TSession(
			String key,
			String userName,
			String userId,
			MemberModule role,
			String clientIp){
		
		this.key = key;
		this.userName = userName;
		this.userId = userId;
		this.role = role;
		this.clientIp = clientIp;
	}
	
	public TSession(
			String userName,
			String userId,
			String clientIp,
			boolean isonpremise,
			boolean libmember){
		
		this.userName = userName;
		this.userId = userId;
		this.clientIp = clientIp;
		this.isonpremise = isonpremise;		
	}
			
	public boolean isFedauth() {
		return fedauth;
	}

	public void setFedauth(boolean fedauth) {
		this.fedauth = fedauth;
	}
	
	public OLicenceType getOlicense() {
		return olicense;
	}

	public void setOlicense(OLicenceType olicense) {
		this.olicense = olicense;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public boolean isIsonpremise() {
		return isonpremise;
	}

	public void setIsonpremise(boolean isonpremise) {
		this.isonpremise = isonpremise;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
		
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public MemberModule getRole() {
		return role;
	}

	public void setRole(MemberModule role) {
		this.role = role;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}	
}