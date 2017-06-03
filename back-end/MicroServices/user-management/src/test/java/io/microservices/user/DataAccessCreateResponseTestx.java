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
public class DataAccessCreateResponseTestx {

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
	public void createResponseTest(TestContext context) {
		JsonObject obj = this.db.createResponse(1, "Test Me", "Right message");
		context.assertNotNull(obj, "Response created!");
		context.assertTrue(obj.getInteger("status").intValue() == 1, "Status is valid!");
		context.assertTrue(obj.getString("data").equalsIgnoreCase("Test Me"), "Data value set!");
		context.assertTrue(obj.getString("msg").equalsIgnoreCase("Right message"), "Message is found!");
	}

	@Test
	public void createResponseTestWithNullsTest(TestContext context){
		JsonObject obj = this.db.createResponse(0, null, null);
		context.assertNotNull(obj, "Response created!");
		context.assertTrue(obj.getInteger("status") == 0, "Status is null");
		context.assertNull(obj.getInteger("data"), "Data is null!");
		context.assertNull(obj.getInteger("msg"), "Message is null!");
	}
}
