/**
 * 
 */
package io.microservices.user.entity;

import java.io.Serializable;

/**
 * @author otkoth
 *
 */
public class ASession implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String key = "_id";
		
	public static String userId = "userId";
	
	public static String ipAddress = "ipAddress";
	
	public static String timestamp = "timestamp";
	
}