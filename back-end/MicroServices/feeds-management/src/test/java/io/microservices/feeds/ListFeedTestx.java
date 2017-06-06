package io.microservices.feeds;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.microservices.feeds.entity.QueryFilter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ListFeedTestx {

	private Vertx vertx;
	
	private String user = "aa1fbffd-fe1d-4796-928e-bde8dccaf9a5";
	
	//private String articleId = "59367c4d0f2fd93f260ed033";
	
	private int port = 8085;
		
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
	public void listFeed(TestContext context){
		final Async async = context.async();
				
		JsonObject feed = new JsonObject();
		//feed.put(QueryFilter.filter, this.articleId);
		feed.put(QueryFilter.user, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
				
		client.get(this.port, "localhost", "/feeds/api/feeds")
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
					
					JsonArray vals = rst.getJsonArray("data");
					
					context.assertNotNull(vals, "Comments array is not null");
					context.assertFalse(vals.isEmpty(), "Comments have values");
					
					System.out.println("Lalala! -> " + vals.encode());
					
					async.complete();
				});
			})
			.write(json)
			.end();
		
		client.close();
	}
}