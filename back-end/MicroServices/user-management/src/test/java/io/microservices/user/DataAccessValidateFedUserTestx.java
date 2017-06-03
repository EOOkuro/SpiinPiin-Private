package io.microservices.user;

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
public class DataAccessValidateFedUserTestx {

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
	public void allRequiredFiledsPresentTest(TestContext context) {
		JsonObject fed = new JsonObject();
		fed.put(User.fedid, "myfedkey");
		fed.put(User.fedemail, "otkoth@gmail.com");
		fed.put(User.gender, "Male");
		fed.put(User.name, "Felix Otieno Okoth");
		fed.put(User.phone, "0710705528");
		fed.put(User.provider, "FEDUSRTEST");
		
		JsonObject rst = this.db.validateFederatedUser(fed);
		
		context.assertNotNull(rst, "Validation created!");
		context.assertTrue(
				rst.getInteger("status").intValue() == 1, 
				"Status is valid!");
		context.assertNull(
				rst.getString("data"), 
				"Data value set!");
		context.assertTrue(
				rst.getString("msg").equalsIgnoreCase(""), 
				"Message set!");
	}

	@Test
	public void nonRequiredFiledsPresentTest(TestContext context){
		JsonObject fed = new JsonObject();
		//fed.put(User.fedid, "myfedkey");
		fed.put(User.fedemail, "otkoth@gmail.com");
		//fed.put(User.gender, "Male");
		fed.put(User.name, "Felix Otieno Okoth");
		//fed.put(User.phone, "0710705528");
		
		JsonObject rst = this.db.validateFederatedUser(fed);
		
		context.assertNotNull(rst, "Validation created!");
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
	}
}