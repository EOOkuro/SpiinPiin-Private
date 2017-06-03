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
public class TResult<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1986207225890309380L;
	
	private String msg;
	
	private T data;
	
	private int status;
	
	public TResult(int status, T data, String msg){
		this.status = status;
		this.data = data;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}