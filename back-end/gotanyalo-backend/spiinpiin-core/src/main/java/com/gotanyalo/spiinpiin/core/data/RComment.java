/**
 * 
 */
package com.gotanyalo.spiinpiin.core.data;

import java.io.Serializable;

/**
 * @author otkoth
 *
 */
public class RComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4137151534781484212L;
	
	private String username;
	
	private String comment;
	
	private boolean like;
	
	private String date;
	
	private String time;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}