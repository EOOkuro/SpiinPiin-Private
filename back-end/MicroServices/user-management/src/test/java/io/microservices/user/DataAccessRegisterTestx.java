package io.microservices.user;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.microservices.user.entity.User;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class DataAccessRegisterTestx {

	private Vertx vertx;
	
	@Before
	public void setUp(TestContext context){
		this.vertx = Vertx.vertx();
		
		this.vertx.deployVerticle(
				CoreMicroservice.class.getName(), 
				context.asyncAssertSuccess());
	}
	
	@After
	public void tearDown(TestContext context){
		this.vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void pingTest(TestContext context){
		final Async async = context.async();
		vertx
			.createHttpClient()
			.getNow(8082, "localhost", "/users/api/", resp -> {
				resp.handler(body -> {
					context.assertTrue(body.toString().contains("ok"));
					async.complete();
				});
			});
	}
	
	@Test
	public void registerTest(TestContext context){
		final Async async = context.async();
		
		JsonObject fed = new JsonObject();
		fed.put(User.fedid, UUID.randomUUID().toString());
		fed.put(User.fedemail, "otkoth@gmail.com");
		fed.put(User.gender, "Male");
		fed.put(User.name, "James Rege");
		fed.put(User.phone, "0710705528");
		fed.put(User.provider, "TWITTER");
		fed.put("MYPIC", "Just some fake data as image");
		
		String json = fed.encode();
		String length = Integer.toString(json.length());
		
		vertx
			.createHttpClient()			
			.post(8082, "localhost", "/users/api/register")
			.putHeader("Content-Type", "application/json")
			.putHeader("Content-Length", length)
			.handler(resp -> {
				context.assertTrue(
						resp.statusCode() >= HttpResponseStatus.OK.code(), 
						"Response code is fine");
								
				resp.bodyHandler(body -> {
					JsonObject rst = body.toJsonObject();
					
					context.assertTrue(
							rst.getInteger("status").intValue() == 1, 
							"Status is right!");
					
					JsonObject data = rst.getJsonObject("data");
					context.assertTrue(
							data
								.getString(User.name)
								.equalsIgnoreCase("James Rege"), 
							"Status is right!");
					
					async.complete();
					
				});
			})
			.write(json)
			.end();
	}

}