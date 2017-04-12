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
 * @author otkoth
 *
 */
@Entity
@Table(name="Language")
public class Language implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6454097722780146618L;
	
	private int lciddec;
	
	private String name;
	
	private String lcidhex;

	/**
	 * @return the lciddec
	 */
	@Id
	@Column(name="spin_language_lciddec", 
			insertable=true, 
			nullable=false,
			unique=true,
			updatable=true)
	public int getLciddec() {
		return lciddec;
	}

	/**
	 * @param lciddec the lciddec to set
	 */
	public void setLciddec(int lciddec) {
		this.lciddec = lciddec;
	}

	/**
	 * @return the name
	 */
	@Column(name="spin_language_name", 
			insertable=true, 
			nullable=false, 
			unique=false, 
			updatable=true, 
			length=100)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the lcidhex
	 */
	@Column(name="spin_language_lcidhex", 
			insertable=true, 
			nullable=false, 
			unique=false, 
			updatable=true, 
			length=4)
	public String getLcidhex() {
		return lcidhex;
	}

	/**
	 * @param lcidhex the lcidhex to set
	 */
	public void setLcidhex(String lcidhex) {
		this.lcidhex = lcidhex;
	}
}
