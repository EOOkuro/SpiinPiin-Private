package io.microservices.user;

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
	
    @Override
    public void start() {
    	logger.warn("Starting User Microservice App");
    	
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
    							.getInteger("port", 8080)
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
    
    private void registerRoute(Router router, String base){
    	logger.info("registerRoute :: base {" + base + "}");
    	router
			.route(HttpMethod.POST, base + "/register")
			.consumes("application/json")
			.produces("application/json")
			.handler(context -> {
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
				String ipAddress = req
						.remoteAddress()
						.host();
				
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
						
					JsonObject session = this.db.add(obj, ipAddress);
			    		
			    	String json = session.encode();
			    		
			    	String length = Integer.toString(json.length());
			    		
			    	resp
			    		.putHeader("Content-Type", "application/json")
			    		.putHeader("Content-Length", length)
			    		.write(json)
			    		.end();
				});
			});
    	logger.info("registerRoute :: <-");
    }
    
    private void updateRoute(Router router, String base){
    	logger.info("updateRoute :: base {" + base + "}");
    	router
			.route(HttpMethod.PUT, base + "/update")
			.consumes("application/json")
			.produces("application/json")	
			.handler(context -> {
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
				
				String key = req.getHeader("key");
				
				req.bodyHandler(body -> {
					JsonObject obj = body.toJsonObject();
					
					JsonObject rst = this.db.update(key, obj);
					
					String json = rst.encode();
		    		
			    	String length = Integer.toString(json.length());
					
					resp
		    			.putHeader("Content-Type", "application/json")
		    			.putHeader("Content-Length", length)
		    			.write(json)
		    			.end();		
				});    		
			});
    	logger.info("updateRoute :: <-");
    }
    
    private void getRoute(Router router, String base){
    	logger.info("getRoute :: base {" + base + "}");
    	router.route(HttpMethod.GET, base + "/user")
			.produces("application/json")
			.handler(context -> {
				
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
				
				String key = req.getHeader("key");
				
				JsonObject user  = this.db.getUserBySessionKey(key);
		    					
				String json = user.encode();
		    		
		    	String length = Integer.toString(json.length());
		    		
		    	resp
		    		.putHeader("Content-Type", "application/json")
		    		.putHeader("Content-Length", length)
		    		.write(json)
		    		.end();    	
			});
    	logger.info("getRoute :: <-");
    }
    
    private void signInRoute(Router router, String base){
    	logger.info("signInRoute :: base {" + base + "}");
    	router.route(HttpMethod.GET, base + "/signin")
			.consumes("application/json")
			.produces("application/json")    	
			.handler(context -> {   				
				    				
				HttpServerResponse resp = context.response();
				HttpServerRequest req = context.request();
				String ipAddress = req
						.remoteAddress()
						.host();
				
				String key = req.getHeader("key");
				String fedid = req.getHeader("fedid");
				String provider = req.getHeader("idprovider");        			
				
				JsonObject session = null;
		    	if (this.isValid(key)){
		    		session = this.db.signIn(key);
		    	} else {
		    		session = this.db.signIn(fedid, provider, ipAddress);
		    	}
				
		    	String json = session.encode();
		    		
		    	String length = Integer.toString(json.length());
		    		
		    	resp
		    		.putHeader("Content-Type", "application/json")
		    		.putHeader("Content-Length", length)
		    		.write(json)
		    		.end();
			});
    	
    	logger.info("signInRoute :: <-");
    }
    
    private void removeRoute(Router router, String base){
    	logger.info("removeRoute :: base {" + base + "}");
    	router.route(HttpMethod.DELETE, base + "/remove/:id")
			.consumes("application/json")
			.produces("application/json")    	
			.handler(context -> {
				// TODO
				//String id = context.pathParam("id");
			});
    	
    	logger.info("removeRoute :: <-");
    }
    
    private void pingRoute(Router router, String base){
    	logger.info("pingRoute :: base {" + base + "}");
    	router.route(HttpMethod.GET, base)
			.handler(req -> {
				req.response().end("ok");
			});
    	
    	logger.info("pingRoute :: <-");
    }
    
    private Router getRouter(String base){
    	logger.info("getRouter :: base {" + base + "}");
    	Router router = Router.router(this.vertx);
    	
    	this.registerRoute(router, base);
    	this.updateRoute(router, base);
    	this.getRoute(router, base);
    	this.signInRoute(router, base);
    	this.removeRoute(router, base);
    	this.pingRoute(router, base);
    	
    	logger.info("getRouter :: <-");
    	
    	return router;
    }
    
    @Override
    public void stop() throws Exception {
    	logger.info("stop :: ()");
    	super.stop();
    	this.db.close();
    	logger.info("stop :: () <-");
    }
    
    private boolean isValid(String val){
		return val != null && 
				!val.trim().equals("") && 
				!val.trim().equalsIgnoreCase("null");
	}
}