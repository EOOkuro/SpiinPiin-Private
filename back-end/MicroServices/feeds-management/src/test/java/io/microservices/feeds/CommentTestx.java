package io.microservices.feeds;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.microservices.feeds.entity.Comment;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class CommentTestx {

	private Vertx vertx;
	
	private String user = "aa1fbffd-fe1d-4796-928e-bde8dccaf9a5";
	
	private String articleId = "59367c4d0f2fd93f260ed033";
	
	private int port = 8084;
		
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
	public void addComments(TestContext context){
		final Async async = context.async();
				
		JsonObject feed = new JsonObject();
		feed.put(Comment.Article, this.articleId);
		feed.put(Comment.Comment, "Trump is quite the president, I say!");
		feed.put(Comment.Poster, this.user);
		
		String json = feed.encode();
		String length = Integer.toString(json.length());
		
		HttpClient client = vertx.createHttpClient();
				
		client.post(this.port, "localhost", "/feeds/api/comment")
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
}