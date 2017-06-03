/**
 * 
 */
package com.gotanyalo.spiinpiin.core.data;

import java.io.Serializable;

/**
 * @author otkoth
 *
 */
public class RArticleDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 745134986043572853L;
	
	private String article;
	
	private String photo;
	
	private int count;

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}