package io.microservices.user;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.microservices.user.entity.User;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava.core.Vertx;

@RunWith(VertxUnitRunner.class)
public class DataAccessSignInTestx {

	DataAccess db;
	
	private Vertx vertx;
	
	@Before
	public void setUp(TestContext context) throws Exception {
		this.vertx = Vertx.vertx();
		
		this.db = new DataAccess(vertx);
		this.vertx.deployVerticle(
				CoreMicroservice.class.getName(), 
				context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) throws Exception {
		this.vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void signInValidKeyTest(TestContext context) {
		JsonObject fed = new JsonObject();
		fed.put(User.fedid, UUID.randomUUID().toString());
		fed.put(User.fedemail, "otkoth@gmail.com");
		fed.put(User.gender, "Male");
		fed.put(User.name, "Felix Otieno Okoth");
		fed.put(User.phone, "0710705528");
		fed.put(User.provider, "DATAACCESS");
				
		JsonObject rst = this.db.add(fed, "127.0.0.1");
		
		context.assertNotNull(rst, "User added, session created!");		
		context.assertTrue(
				rst.getInteger("status").intValue() == 1, 
				"Status is valid! -- " + rst.encode());		
		
		JsonObject session = rst.getJsonObject("data");		
		context.assertNotNull(session, "Data value set!");
		
		String key = session.getString("key");
		
		context.assertNotNull(key, "Key found!");
		
		// Now login
		JsonObject cred = this.db.signIn(key);
		context.assertNotNull(cred, "Session returned!");		
		context.assertTrue(
				cred.getInteger("status").intValue() == 1, 
						key + " - Status is valid! -- [" 
								+ cred.encode() + "], old [" + rst.encode() + "]");		
		
		session = rst.getJsonObject("data");		
		context.assertNotNull(session, "Data value set!");
				
		context.assertNotNull(
				key.equalsIgnoreCase(session.getString("key")), 
				"Key found is simillar to the old key!");
	}

	@Test
	public void signinInvalidKeyTest(TestContext context){
		JsonObject rst = this.db.signIn(UUID.randomUUID().toString());
		
		context.assertNotNull(rst, "Session returned!");
		context.assertTrue(
				rst.getInteger("status").intValue() == 0, 
				"Status is valid!");
		context.assertTrue(
				rst.getString("msg").equalsIgnoreCase("Invalid signin"), 
				"Invalid signin");
		
		context.assertNull(rst.getString("rst"), "Data is null!");		
	}
	
	@Test
	public void signinNullKeyTest(TestContext context){
		JsonObject rst = this.db.signIn(null);
		
		context.assertNotNull(rst, "Session returned!");
		context.assertTrue(
				rst.getInteger("status").intValue() == 0, 
				"Status is valid!");
		context.assertTrue(
				rst.getString("msg").equalsIgnoreCase("Invalid signin"), 
				"Invalid signin");
		
		context.assertNull(rst.getString("rst"), "Data is null!");		
	}
}