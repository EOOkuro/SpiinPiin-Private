package com.gotanyalo.spiinpiin.core.test;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class LanguageTest {
	
	private final String baseUrl = "http://localhost:8080/spin/api/base/";
	
	private JSONParser parser;
	
	private Client client;

	/*@Deployment
	public static JavaArchive createDeployment(){
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(Country.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}*/
	
	@Before
	public void setUp() throws Exception {
		this.client = ClientBuilder.newClient();
		this.parser = new JSONParser();
	}
		
	@Test
	public void verifyEnglish() {
		try {
			WebTarget target = this.client.target(this.baseUrl + "llanguage");
			Response respose = target.request().get();
			if (respose.getStatus() != 200){
				throw new Exception("Language failed! " + respose.getStatus());
			}
			
			String rst = respose.readEntity(String.class);
						
			JSONArray obj = (JSONArray)parser.parse(rst);
			
			boolean found = false;
			
			for (int i = 0; i < obj.size(); i++) {
				if (((JSONObject)obj.get(i)).get("lciddec").toString().equalsIgnoreCase("1033")){
					found = true;
				}
			}
			
			assertTrue("English language found!", found);
		} catch (Exception e) {
			assertTrue(this.getErrorMessageStack(e), false);
		}		
	}

	@Test
	public void verifyKiswahili() {
		try {
			WebTarget target = this.client.target(this.baseUrl + "llanguage");
			Response respose = target.request().get();
			if (respose.getStatus() != 200){
				throw new Exception("Language failed! " + respose.getStatus());
			}
			
			String rst = respose.readEntity(String.class);
						
			JSONArray obj = (JSONArray)parser.parse(rst);
			
			boolean found = false;
			
			for (int i = 0; i < obj.size(); i++) {
				if (((JSONObject)obj.get(i)).get("lciddec").toString().equalsIgnoreCase("1089")){
					found = true;
				}
			}
			
			assertTrue("Swahili language found!", found);
		} catch (Exception e) {
			assertTrue(this.getErrorMessageStack(e), false);
		}		
	}
	
	private String getErrorMessageStack(Throwable e){
		if (e == null){
			return "";
		}
		
		StringWriter writer = new StringWriter();
		PrintWriter print = new PrintWriter(writer);		
		e.printStackTrace(print);
		
		return e.getMessage() + " >> " + writer.toString();
	}

}