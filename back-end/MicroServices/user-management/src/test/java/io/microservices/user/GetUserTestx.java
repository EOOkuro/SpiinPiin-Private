package io.microservices.user;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.microservices.user.data.TSession;
import io.microservices.user.entity.User;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class GetUserTestx {

	private static Vertx vertx;
	
	private static String sessionKey;
	
	private static String fedid;
	
	private static String provider;
	
	@BeforeClass
	public static void setUp(TestContext context){
		vertx = Vertx.vertx();
		
		vertx.deployVerticle(
				CoreMicroservice.class.getName(), 
				context.asyncAssertSuccess());
		
		sessionKey = null;
		provider = "GIT";
		fedid = UUID.randomUUID().toString();
		registerUser(context);
	}
	
	@AfterClass
	public static void tearDown(TestContext context){
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void getUserProvider(TestContext context){
		final Async async = context.async();
		
		vertx
			.createHttpClient()
			.get(8082, "localhost", "/users/api/user")
			.putHeader("Content-Type", "application/json")
			.putHeader("key", sessionKey)
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
								.equalsIgnoreCase("User A. Bee"), 
							"Status is right!");
					context.assertTrue(
							data
								.getString(User.provider)
								.equalsIgnoreCase(provider), 
							"Provider is right!");
					context.assertTrue(
							data
								.getString(User.fedid)
								.equalsIgnoreCase(fedid), 
							"Fed-Id is right!");
					async.complete();
				});
			})
			.setTimeout(3000)
			.end();						
	}
	
	@Test
	public void getUserInvalidSessionTest(TestContext context){
		final Async async = context.async();
				
		vertx
			.createHttpClient()
			.get(8082, "localhost", "/users/api/user")
			.putHeader("key", UUID.randomUUID().toString())
			.handler(resp -> {
				context.assertTrue(
						resp.statusCode() >= HttpResponseStatus.OK.code(), 
						"Response code is fine");
				
				resp.bodyHandler(body -> {
					JsonObject rst = body.toJsonObject();
					
					context.assertTrue(
							rst.getInteger("status").intValue() == 0, 
							"Status is right!");
					
					context.assertTrue(
							rst.getString("msg").equalsIgnoreCase("Invalid session key"), 
							"The right message returned!");
					
					JsonObject data = rst.getJsonObject("data");
					context.assertNull(data, "Data is null!");
										
					async.complete();
				});
			})
			.setTimeout(3000)
			.end();
	}
		
	private static void registerUser(TestContext context){
		final Async async = context.async();
		
		JsonObject fed = new JsonObject();
		fed.put(User.fedid, fedid);
		fed.put(User.fedemail, "otkoth@gmail.com");
		fed.put(User.gender, "Male");
		fed.put(User.name, "User A. Bee");
		fed.put(User.phone, "7710705528");
		fed.put(User.provider, provider);
		
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
					sessionKey = data.getString(TSession.key);
					
					context.assertNotNull(
							sessionKey, 
							"Session key has a valid value!");
					async.complete();					
				});
			})
			.setTimeout(3000)
			.write(json)
			.end();
	}
}