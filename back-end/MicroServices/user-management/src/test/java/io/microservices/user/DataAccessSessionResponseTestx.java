package io.microservices.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava.core.Vertx;

@RunWith(VertxUnitRunner.class)
public class DataAccessSessionResponseTestx {

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
	public void sessionResponseTest(TestContext context) {
		JsonObject det = new JsonObject();
		det.put("key", "mykey");
		JsonObject session =this.db.genSessionResponse(0, det, "Success");
		
		context.assertNotNull(session, "Session created!");
		context.assertTrue(
				session.getInteger("status").intValue() == 0, 
				"Status is valid!");
		context.assertNotNull(
				session.getJsonObject("data"), 
				"Data value set!");
		context.assertTrue(
				session
					.getJsonObject("data")
					.getString("key")
					.equalsIgnoreCase("mykey"), 
				"Data value set!");
		context.assertTrue(
				session.getString("msg")
					.equalsIgnoreCase("Success"), 
				"Message is found!");
	}
}