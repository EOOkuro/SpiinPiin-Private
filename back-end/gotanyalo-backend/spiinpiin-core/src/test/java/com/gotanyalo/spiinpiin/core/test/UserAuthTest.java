package com.gotanyalo.spiinpiin.core.test;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gotanyalo.spiinpiin.core.data.FederatedUser;
import com.gotanyalo.spiinpiin.core.data.Gender;
import com.gotanyalo.spiinpiin.core.data.MemberModule;
import com.gotanyalo.spiinpiin.core.data.MemberType;
import com.gotanyalo.spiinpiin.core.model.Member;

@RunWith(Arquillian.class)
public class UserAuthTest {
	
	private final String baseUrl = "http://localhost:8080/spin/api/membership/";
	
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
	public void verifyRegisterUser() {
		try {
			Member mbr = new Member();			
			mbr.setFirstName(RandomStringUtils.randomAlphabetic(15));
			mbr.setFuid(UUID.randomUUID().toString());
			mbr.setGender(Gender.MALE);
			mbr.setIdob("10-10-1979");
			mbr.setLastName(RandomStringUtils.randomAlphabetic(12));
			mbr.setMiddleName(RandomStringUtils.randomAlphabetic(5));
			mbr.setProvider("felix.com");
			mbr.setFemail(mbr.getFirstName() + "@felix.com");
			WebTarget target = this.client.target(this.baseUrl + "registreuser");
			Response respose = target.request()
					.accept("application/json")
					.post(Entity.json(mbr));
			if (respose.getStatus() != 200){
				throw new Exception("Register failed! " + respose.getStatus());
			}
			
			boolean rst = respose.readEntity(Boolean.class);		
			assertTrue("Register found!", rst);
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