/**
 * 
 */
package com.gotanyalo.spiinpiin.core.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author otkoth
 *
 */
@XmlRootElement
public class Img implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1986207225890309380L;
	
	private String img;
	
	public Img(String img){
		this.img = img;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	

}
