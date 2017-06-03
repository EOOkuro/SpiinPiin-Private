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
public class DataAccessAddTestx {

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
	public void addTest(TestContext context) {
		JsonObject fed = new JsonObject();
		fed.put(User.fedid, UUID.randomUUID().toString());
		fed.put(User.fedemail, "otkoth@gmail.com");
		fed.put(User.gender, "Male");
		fed.put(User.name, "Felix Otieno Okoth");
		fed.put(User.phone, "0710705528");
		fed.put(User.provider, "ADDTEST");
				
		JsonObject rst = this.db.add(fed, "127.0.0.1");
		
		context.assertNotNull(rst, "User added, session created!");
		context.assertTrue(
				rst.getInteger("status").intValue() == 1, 
				"Status is valid! -- " + rst.encode());		
		context.assertTrue(
				rst.getString("msg").equalsIgnoreCase("Success"), 
				"Success set!");
		
		JsonObject session = rst.getJsonObject("data");
		
		context.assertNotNull(
				session, 
				"Data value set!");
		
		context.assertNotNull(
				session.getString("key"), 
				"Key found!");
		
		context.assertTrue(
				session.getString("name").equalsIgnoreCase(fed.getString(User.name)), 
				"Valid name found!");
	}

	@Test
	public void addRequiredFieldsPresentTest(TestContext context){
		JsonObject fed = new JsonObject();
		//fed.put(User.fedid, "myfedkey");
		fed.put(User.fedemail, "otkoth@gmail.com");
		//fed.put(User.gender, "Male");
		fed.put(User.name, "Felix Otieno Okoth");
		//fed.put(User.phone, "0710705528");
		
		JsonObject rst = this.db.add(fed, "127.0.0.1");
		
		context.assertNotNull(rst, "Add user returned Validation values!");
		context.assertTrue(
				rst.getInteger("status").intValue() == 0, 
				"Status is valid!");
		context.assertTrue(
				rst.getString("msg").equalsIgnoreCase("Missing required fields"), 
				"Missing required fileds");
		
		String msg = rst.getString("data");
		
		context.assertNotNull(
				msg, 
				"Data value is null!");
		context.assertTrue(msg.contains(User.fedid), User.fedid + " found!");
		context.assertTrue(msg.contains(User.gender), User.gender + " found!");
		context.assertTrue(msg.contains(User.phone), User.phone + " found!");
		context.assertFalse(msg.contains(User.fedemail), User.fedemail + " found!");
		context.assertFalse(msg.contains(User.name), User.name + " found!");
		context.assertTrue(msg.contains(User.provider), User.provider + " found!");
	}
}