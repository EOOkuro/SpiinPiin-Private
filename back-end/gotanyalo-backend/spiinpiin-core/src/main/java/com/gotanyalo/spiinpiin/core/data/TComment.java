package com.gotanyalo.spiinpiin.core.data;

import java.io.Serializable;

public class TComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5116206439968913742L;
	
	private Integer articleId;
	
	private Integer commentId;
	
	private String comment;

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}