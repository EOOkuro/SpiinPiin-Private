/**
 * 
 */
package com.gotanyalo.spiinpiin.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class CountryTable.
 *
 * @author otkoth
 */
@Entity
@Table(name="Country")
public class Country implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8660586152382674871L;
	
	/** The id. */
	private String code;
			
	/** The description. */
	private String description;

	/**
	 * Gets the Code.
	 *
	 * @return the Code
	 */
	@Id
	@Column(name="spin_country_code", 
			insertable=false, 
			nullable=false,
			unique=false,
			updatable=false)
	public String getCode() {
		return code;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	@Column(name="spin_country_desc", 
			insertable=true, 
			nullable=false, 
			unique=false, 
			updatable=true, 
			length=200)
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
