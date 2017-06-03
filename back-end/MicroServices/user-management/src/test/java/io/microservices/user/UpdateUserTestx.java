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
public class UpdateUserTestx {

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
		provider = "UPDATE";
		fedid = UUID.randomUUID().toString();
		registerUser(context);
	}
	
	@AfterClass
	public static void tearDown(TestContext context){
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void updateUser(TestContext context){
		final Async async = context.async();
		
		JsonObject fed = new JsonObject();
		fed.put(User.email, "johnjames@gmail.com");
		fed.put(User.gender, "Female");
		fed.put(User.name, "User U. James");
		fed.put("MYMY", "My new field");
		
		String json = fed.encode();
		String length = Integer.toString(json.length());
		
		vertx
			.createHttpClient()
			.put(8082, "localhost", "/users/api/update")
			.putHeader("Content-Type", "application/json")
			.putHeader("Content-Length", length)
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
					
					// Do a fetch and check everything is okay.
					vertx.createHttpClient()
						.get(8082, "localhost", "/users/api/user")
						.putHeader("Content-Type", "application/json")
						.putHeader("key", sessionKey)
						.handler(gresp -> {
							context.assertTrue(
									gresp.statusCode() >= HttpResponseStatus.OK.code(), 
									"Get Response code is fine");
							gresp.bodyHandler(gbody -> {
								JsonObject grst = gbody.toJsonObject();
								
								context.assertTrue(
										grst.getInteger("status").intValue() == 1, 
										"Get Status is right!");
								
								JsonObject data = grst.getJsonObject("data");
								context.assertNotNull(data, "Data has been returned for the get.");
								context.assertTrue(
										data
											.getString(User.name)
											.equalsIgnoreCase("User U. James"), 
										"user name is updated!");
								context.assertTrue(
										data
											.getString(User.email)
											.equalsIgnoreCase("johnjames@gmail.com"), 
										"Email is right!");
								context.assertTrue(
										data
											.getString(User.gender)
											.equalsIgnoreCase("Female"), 
										"Gender is right!");
								context.assertTrue(
										data
											.getString("MYMY")
											.equalsIgnoreCase("My new field"), 
										"New field was added!");
								
								async.complete();
							});							
						})
						.setTimeout(3000)
						.end();					
				});
			})
			.setTimeout(3000)
			.write(json)
			.end();		
	}
			
	private static void registerUser(TestContext context){
		final Async async = context.async();
		
		JsonObject fed = new JsonObject();
		fed.put(User.fedid, fedid);
		fed.put(User.fedemail, "otkoth@gmail.com");
		fed.put(User.gender, "Male");
		fed.put(User.name, "User U. Bee");
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