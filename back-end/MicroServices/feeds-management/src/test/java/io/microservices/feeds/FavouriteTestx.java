package io.microservices.feeds;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.microservices.feeds.entity.Favourite;
import io.microservices.feeds.entity.Feed;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class FavouriteTestx {

	private Vertx vertx;
	
	private String user = "aa1fbffd-fe1d-4796-928e-bde8dccaf9a5";
	
	private String articleId = "59367c4d0f2fd93f260ed033";
	
	private int port = 8083;
	
	//private String feedId;
	
	@Before
	public void setUp(TestContext context){
		this.vertx = Vertx.vertx();
		
		this.vertx.deployVerticle(
				CoreMicroservice.class.getName(), 
				context.asyncAssertSuccess());
		
		// Ensure there is atleast one feed.
		//this.addFeed(context);
	}
	
	@After
	public void tearDown(TestContext context){
		this.vertx.close(context.asyncAssertSuccess());
	}
		
	@Test
	public void favouriteNoFlag(TestContext context){
		final Async async = context.async();
				
		JsonObject feed = new JsonObject();
		feed.put(Favourite.article, this.articleId);
		//feed.put(Favourite.flag, true);
		feed.put(Favourite.Poster, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
				
		client.post(this.port, "localhost", "/feeds/api/favourite")
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
	public void favourite(TestContext context){
		final Async async = context.async();
				
		JsonObject feed = new JsonObject();
		feed.put(Favourite.article, this.articleId);
		feed.put(Favourite.flag, true);
		feed.put(Favourite.Poster, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
		client.post(this.port, "localhost", "/feeds/api/favourite")
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
	public void unfavourite(TestContext context){
		final Async async = context.async();
				
		JsonObject feed = new JsonObject();
		feed.put(Favourite.article, this.articleId);
		feed.put(Favourite.flag, false);
		feed.put(Favourite.Poster, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
		client.post(this.port, "localhost", "/feeds/api/favourite")
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
		client.post(this.port, "localhost", "/feeds/api/feed")
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
					
					this.articleId = rst.getString("data");
					
					context.assertNotNull(
							this.articleId, "Feed id {" + this.articleId + "} returned!");
					
					context.assertFalse(
							this.articleId.trim().equalsIgnoreCase("success"), "Feed id {" + this.articleId + "} is valid!");
					
					async.complete();
				});
			})
			.write(json)
			.end();
		
		client.close();
	}
}