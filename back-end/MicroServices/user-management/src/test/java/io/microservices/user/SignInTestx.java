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
public class SignInTestx {

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
		provider = "YAHOO";
		fedid = UUID.randomUUID().toString();
		registerUser(context);
	}
	
	@AfterClass
	public static void tearDown(TestContext context){
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test	
	public void signInWithFedIdAndProvider(TestContext context){
		final Async async = context.async();
		
		context.assertNotNull("provider is there", provider);
		context.assertNotNull("fedis is not null!", fedid);
		
		vertx
			.createHttpClient()
			.get(8082, "localhost", "/users/api/signin")
			.putHeader("Content-Type", "application/json")
			.putHeader("fedid", fedid)
			.putHeader("idprovider", provider)
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
								.equalsIgnoreCase("Sign In User"), 
							"Status is right!");
					
					async.complete();
				});
			})			
			.end();						
	}
	
	@Test
	public void signInWithKeyTest(TestContext context){
		final Async async = context.async();
				
		vertx
			.createHttpClient()
			.get(8082, "localhost", "/users/api/signin")
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
								.equalsIgnoreCase("Sign In User"), 
							"Status is right!");
					
					async.complete();
				});
			})
			.end();						
	}
		
	private static void registerUser(TestContext context){
		final Async async = context.async();
		
		JsonObject fed = new JsonObject();
		fed.put(User.fedid, fedid);
		fed.put(User.fedemail, "otkoth@gmail.com");
		fed.put(User.gender, "Male");
		fed.put(User.name, "Sign In User");
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
			.write(json)
			.end();
	}
}