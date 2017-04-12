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
@Table(name="Occupation")
public class Occupation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String name;
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="spin_occupation_id", 
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

	@Column(name="spin_occupation_name", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}