package com.gotanyalo.spiinpiin.core.data;

import java.io.Serializable;

public class TArticle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 537255744050801110L;
	
	private String summary;
	
	private String article;
	
	private Double latitude;
	
	private Double longitude;
	
	private String image;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}