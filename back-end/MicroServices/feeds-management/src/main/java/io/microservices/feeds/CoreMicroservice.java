package io.microservices.feeds;

import java.io.File;

import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.config.ConfigRetriever;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.Router;

public class CoreMicroservice extends AbstractVerticle {

	private DataAccess db;
	
	private Logger logger = LoggerFactory.getLogger(
			CoreMicroservice.class.getName());
	
    private void addCommentRoute(Router router, String base){
    	logger.info("addCommentRoute :: base {" + base + "}");
    	router
			.route(HttpMethod.POST, base + "/comment")
			.consumes("application/json")
			.produces("application/json")
			.handler(context -> {
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
								
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject rst = this.db.addComment(obj);
			    		
			    	String json = rst.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});
			});
    	logger.info("addCommentRoute :: <-");
    }

    private void addFeedRoute(Router router, String base){
    	logger.info("addFeedRoute :: base {" + base + "}");
    	router
			.route(HttpMethod.POST, base + "/feed")
			.consumes("application/json")
			.produces("application/json")
			.handler(context -> {
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
								
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject rst = this.db.addFeed(obj);
			    		
			    	String json = rst.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});
			});
    	logger.info("addFeedRoute :: <-");
    }
    
    private void dislikeRoute(Router router, String base){
    	logger.info("dislikeRoute :: base {" + base + "}");
    	router
			.route(HttpMethod.POST, base + "/dislike")
			.consumes("application/json")
			.produces("application/json")
			.handler(context -> {				
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
								
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
					
					JsonObject rst = this.db.dislike(obj);
			    		
			    	String json = rst.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();				
				});				
			});
    	
    	logger.info("dislikeRoute :: <-");
    }
    
    private void favouriteRoute(Router router, String base){
    	logger.info("favouriteRoute :: base {" + base + "}");
    	router
			.route(HttpMethod.POST, base + "/favourite")
			.consumes("application/json")
			.produces("application/json")
			.handler(context -> {
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
								
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject rst = this.db.favourite(obj);
			    		
			    	String json = rst.encode();
			    	
			    	String length = Integer.toString(json.length());
			    	
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});
			});
    	logger.info("favouriteRoute :: <-");
    }
    
    private Router getRouter(String base){
    	logger.info("getRouter :: base {" + base + "}");
    	Router router = Router.router(this.vertx);
    	
    	this.addFeedRoute(router, base);
    	this.addCommentRoute(router, base);
    	this.likeRoute(router, base);
    	this.dislikeRoute(router, base);
    	this.favouriteRoute(router, base);
    	this.listFeedRoute(router, base);
    	this.listCommentRoute(router, base);
    	this.removeFeedRoute(router, base);
    	this.removeCommentRoute(router, base);
    	
    	logger.info("getRouter :: <-");
    	
    	return router;
    }
    
    private boolean isValid(String val){
		return val != null && 
				!val.trim().equals("") && 
				!val.trim().equalsIgnoreCase("null");
	}
    
    private void likeRoute(Router router, String base){
    	logger.info("likeRoute :: base {" + base + "}");
    	router
			.route(HttpMethod.POST, base + "/like")
			.consumes("application/json")
			.produces("application/json")
			.handler(context -> {
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
								
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject rst = this.db.like(obj);
			    		
			    	String json = rst.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});
			});
    	logger.info("likeRoute :: <-");
    }
    
    private void listCommentRoute(Router router, String base){
    	logger.info("listCommentRoute :: base {" + base + "}");
    	router.route(HttpMethod.GET, base + "/comments")
			.produces("application/json")
			.handler(context -> {
				
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
				
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject rst = this.db.listComment(obj);
			    		
			    	String json = rst.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});	
			});
    	logger.info("listCommentRoute :: <-");
    }
    
    private void listFeedRoute(Router router, String base){
    	logger.info("listFeedRoute :: base {" + base + "}");
    	router.route(HttpMethod.GET, base + "/feeds")
			.produces("application/json")
			.handler(context -> {
				
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
				
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject rst = this.db.listFeed(obj);
			    		
			    	String json = rst.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});				    	
			});
    	logger.info("listFeedRoute :: <-");
    }
    
    private void removeCommentRoute(Router router, String base){
    	logger.info("removeCommentRoute :: base {" + base + "}");
    	router.route(HttpMethod.DELETE, base + "/rcomment")
			.consumes("application/json")
			.produces("application/json")    	
			.handler(context -> {
				
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
				
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject rst = this.db.removeComment(obj);
			    		
			    	String json = rst.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});
			});
    	
    	logger.info("removeCommentRoute :: <-");
    }
    
    private void removeFeedRoute(Router router, String base){
    	logger.info("removeFeedRoute :: base {" + base + "}");
    	router.route(HttpMethod.DELETE, base + "/rfeed")
			.consumes("application/json")
			.produces("application/json")    	
			.handler(context -> {
				
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
				
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject rst = this.db.removeFeed(obj);
			    		
			    	String json = rst.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});
			});
    	
    	logger.info("removeFeedRoute :: <-");
    }
    
    @Override
    public void start() {
    	logger.warn("Starting Feed Microservice App");
    	
    	String execPath = System.getProperty("user.dir");
    	String confPath = execPath + File.separator + "config.json";
    	
    	logger.info("User Microservice App path {" + 
    			execPath + 
    			"}, config {" + 
    			confPath + 
    			"}");
    	
    	ConfigStoreOptions fileOption = new ConfigStoreOptions()
    		.setFormat("json")
    		.setType("file")
    		.setConfig(new JsonObject().put("path",	confPath));
    	    	
    	ConfigRetrieverOptions options = new ConfigRetrieverOptions()
    			.setScanPeriod(10000)
    			.addStore(fileOption);
    	
    	ConfigRetriever confRetriver = ConfigRetriever.create(vertx, options);
    	confRetriver.getConfig(conf -> {
    		if (conf.succeeded()){
    			logger.info("Configuration loading successful");
    			JsonObject rst = conf.result();
    			if (rst != null){
    				int port = rst
    							.getInteger("port", 8081)
    							.intValue();
    				String base = rst.getString("base");
    				if (!this.isValid(base)){
    					logger.warn("Base url config value not found. Defaulting to default '/users/api'");
    					base = "/users/api";
    				}
    				
    				this.startOffServer(port, base);
    			} else {
    				logger.error("Configuration file yielded null content");
    			}
    		} else {
    			logger.error("Configuration loading failed", conf.cause());
    		}
    	});
    	
    	confRetriver.listen(change -> {
    		logger.warn("Configuration file changed!");
    	});
    }
    
    private void startOffServer(int port, String base){
    	logger.info("startOffServer :: port {" + port + "}, base {" + base + "}");
    	this.db = new DataAccess(this.vertx);
    	
    	HttpServer server = this.vertx.createHttpServer();
    	
    	server
    		.requestStream()
    		.toObservable()
    		.subscribe(this.getRouter(base)::accept);
    	
    	server
			.rxListen(port)
			.subscribe();
    	logger.info("startOffServer :: <-");
    }
}