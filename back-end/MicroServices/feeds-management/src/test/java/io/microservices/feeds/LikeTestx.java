package io.microservices.feeds;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.microservices.feeds.entity.Feed;
import io.microservices.feeds.entity.Likes;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class LikeTestx {

	private Vertx vertx;
	
	private String user = "aa1fbffd-fe1d-4796-928e-bde8dccaf9a5";
	
	private String feedId;
	
	@Before
	public void setUp(TestContext context){
		this.vertx = Vertx.vertx();
		
		this.vertx.deployVerticle(
				CoreMicroservice.class.getName(), 
				context.asyncAssertSuccess());
		
		// Ensure there is atleast one feed.
		this.addFeed(context);
	}
	
	@After
	public void tearDown(TestContext context){
		this.vertx.close(context.asyncAssertSuccess());
	}
		
	@Test
	public void likeFeedNoFlag(TestContext context){
		final Async async = context.async();
				
		JsonObject feed = new JsonObject();
		feed.put(Likes.article, this.feedId);
		//feed.put(Likes.flag, true);
		feed.put(Likes.Poster, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
				
		client.post(8082, "localhost", "/feeds/api/like")
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
					
					async.complete();					
				});
			})
			.write(json)
			.end();
		
		client.close();
	}
	
	@Test
	public void likeFeed(TestContext context){
		final Async async = context.async();
				
		JsonObject feed = new JsonObject();
		feed.put(Likes.article, this.feedId);
		feed.put(Likes.flag, true);
		feed.put(Likes.Poster, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
		client.post(8082, "localhost", "/feeds/api/like")
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
					
					async.complete();					
				});
			})
			.write(json)
			.end();
		
		client.close();
	}
	
	@Test
	public void unlikeFeed(TestContext context){
		final Async async = context.async();
				
		JsonObject feed = new JsonObject();
		feed.put(Likes.article, this.feedId);
		feed.put(Likes.flag, false);
		feed.put(Likes.Poster, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
		client.post(8082, "localhost", "/feeds/api/like")
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
					
					async.complete();					
				});
			})
			.write(json)
			.end();
		
		client.close();
	}
	
	private void addFeed(TestContext context){
		final Async async = context.async();
		
		StringBuilder sb = new StringBuilder();
		sb.append("India has hit back at US President Donald Trump, ");
		sb.append("after he accused the country of receiving \"billions\" ");
		sb.append("of dollars in return for signing the Paris Agreement on Climate Change.");
		sb.append("First of all, there is absolutely no reality [in what Trump alleged],\" ");
		sb.append("India\'s Foreign Minister Sushma Swaraj told CNN");
		
		JsonObject feed = new JsonObject();
		feed.put(Feed.Article, sb.toString());
		feed.put(Feed.Summary, "India hits back at Trump in war of words over climate change");
		feed.put(Feed.Poster, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
		client.post(8082, "localhost", "/feeds/api/feed")
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
					
					this.feedId = rst.getString("data");
					
					context.assertNotNull(
							this.feedId, "Feed id {" + this.feedId + "} returned!");
					
					context.assertFalse(
							this.feedId.trim().equalsIgnoreCase("success"), "Feed id {" + this.feedId + "} is valid!");
					
					async.complete();
				});
			})
			.write(json)
			.end();
		
		client.close();
	}
}