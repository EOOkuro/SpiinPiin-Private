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
public class DataAccessSignIn2Testx {

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
	public void signInTest(TestContext context) {
		String uid = UUID.randomUUID().toString();
		
		JsonObject fed = new JsonObject();
		fed.put(User.fedid, uid);
		fed.put(User.fedemail, "otkoth@gmail.com");
		fed.put(User.gender, "Male");
		fed.put(User.name, "Felix Otieno Okoth");
		fed.put(User.phone, "0710705528");
		fed.put(User.provider, "GOOGLE");
		
		// Test that user auth returns -1 for user not found.
		JsonObject urst = this.db.signIn(uid, "GOOGLE", "127.0.0.1"); 
		context.assertNotNull(urst, "A session with the right message is returned!");		
		context.assertTrue(
				urst.getInteger("status").intValue() == -1, 
				"Status is valid! -- " + urst.encode());
		context.assertTrue(
				urst.getString("msg").equalsIgnoreCase("User doesn't exist!"),
				"The right user message is sent!");
		
		// Now register the user in the system.
		
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
		JsonObject cred = this.db.signIn(uid, "GOOGLE", "127.0.0.1");
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
		
		// Now log in from a differnt IP.
		cred = this.db.signIn(uid, "GOOGLE", "127.0.1.1");
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
}