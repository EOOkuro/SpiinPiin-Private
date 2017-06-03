package com.gotanyalo.spiinpiin.core.data;

import java.io.Serializable;

public class RArticle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 537255744050801110L;
	
	private String summary;
			
	private Double latitude;
	
	private Double longitude;
	
	private String image;
	
	private String userName;
	
	private String userPhoto;
	
	private String articlePhoto;
	
	private int commentCount;
	
	private int likeCount;
	
	private int dislikeCount;
	
	private boolean favourite;
	
	private String postedDate;
	
	private String postedDay;
	
	private String postedTime;
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getArticlePhoto() {
		return articlePhoto;
	}

	public void setArticlePhoto(String articlePhoto) {
		this.articlePhoto = articlePhoto;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(int dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	public String getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}

	public String getPostedDay() {
		return postedDay;
	}

	public void setPostedDay(String postedDay) {
		this.postedDay = postedDay;
	}

	public String getPostedTime() {
		return postedTime;
	}

	public void setPostedTime(String postedTime) {
		this.postedTime = postedTime;
	}
}