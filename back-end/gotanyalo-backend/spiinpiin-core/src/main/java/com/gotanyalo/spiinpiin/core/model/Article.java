/**
 * 
 */
package com.gotanyalo.spiinpiin.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author otkoth
 *
 */
@XmlRootElement
@Entity
@Table(name="Occupation")
public class Article implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
		
	private String userId;
	
	private String title;
	
	private String details;	
	
	private Date date;
	
	private byte[] img;
	
	private Double latitude;
	
	private Double longitude;
			
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="spin_article_id", 
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

	@Column(name="spin_article_mid", 
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

	@Column(name="spin_article_title", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="spin_article_detail", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true,
			length=4000)
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="spin_article_dob", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Lob
	@Column(name="spin_article_img",
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true)
	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	@Column(name="spin_article_lat", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true)
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name="spin_article_long", 
			insertable=true, 
			nullable=false,
			unique=false,
			updatable=true)
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}