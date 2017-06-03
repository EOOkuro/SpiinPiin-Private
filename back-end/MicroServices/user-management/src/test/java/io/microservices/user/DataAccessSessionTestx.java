package io.microservices.user;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava.core.Vertx;

@RunWith(VertxUnitRunner.class)
public class DataAccessSessionTestx {

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
	public void getFakeSession(TestContext context) {
		
		Document rst = this.db.getSession("fake-key");		
		context.assertNull(rst, "User added, session created!");
		
		rst = this.db.getSession(null);
		context.assertNull(rst, "User added, session created!");		
	}
}