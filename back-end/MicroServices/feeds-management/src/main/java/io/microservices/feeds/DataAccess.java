/**
 * 
 */
package io.microservices.feeds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import io.microservices.feeds.entity.Comment;
import io.microservices.feeds.entity.Dislike;
import io.microservices.feeds.entity.Favourite;
import io.microservices.feeds.entity.Feed;
import io.microservices.feeds.entity.Likes;
import io.microservices.feeds.entity.QueryFilter;
import io.microservices.feeds.entity.User;
import io.microservices.feeds.entity.Views;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.Vertx;

/**
 * @author otkoth
 *
 */
public class DataAccess {

	private Logger logger = LoggerFactory.getLogger(DataAccess.class.getName());
	
	private MongoClient client;
	
	private final String userdb = "user_management"; 
	
	private MongoDatabase userManagementDb;
	
	private MongoCollection<Document> users;
	
	private final String feeddb = "feed_management"; 
	
	private MongoDatabase feedManagementDb;
	
	private MongoCollection<Document> feeds;
	
	private MongoCollection<Document> comments;
	
	private MongoCollection<Document> likes;
	
	private MongoCollection<Document> dislikes;
	
	private MongoCollection<Document> favoutites;
	
	private MongoCollection<Document> views;
	
	private DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private DateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
	
	private DateFormat dayformat = new SimpleDateFormat("E");
	
	private DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
		
	public DataAccess(Vertx vertx){
		logger.info("DataAccess instantiation ->>");
		
		this.client = new MongoClient();
		this.feedManagementDb = this.client.getDatabase(this.feeddb);
		this.feeds = this.feedManagementDb.getCollection("feeds");
		this.comments = this.feedManagementDb.getCollection("comments");
		this.likes = this.feedManagementDb.getCollection("likes");
		this.dislikes = this.feedManagementDb.getCollection("dislikes");
		this.favoutites = this.feedManagementDb.getCollection("favourites");
		this.views = this.feedManagementDb.getCollection("views");
		
		this.userManagementDb = this.client.getDatabase(this.userdb);
		this.users = this.userManagementDb.getCollection("users");
		
		logger.info("DataAccess instantiation <<-");
	}
	
	public JsonObject addComment(JsonObject obj){
		logger.info("addComment :: obj {" + Boolean.toString(obj != null) +	"}");
		
		JsonObject vresult = this.validateComment(obj);
		if (vresult.getInteger("status").intValue() > 0){			
			Document doc = new Document(obj.getMap());
			doc.append(Comment.Posted, df.format(new Date()));
			
			this.comments.insertOne(doc);
			
			this.viewed(
					obj.getString(Comment.Article), 
					obj.getString(Comment.Poster));
			
			return this.createResponse(1, null, "Success");			
		}
		
		logger.info("addComment :: <-");
		
		return vresult;
	}
	
	public JsonObject addFeed(JsonObject obj){
		logger.info("addFeed :: obj {" + Boolean.toString(obj != null) +	"}");
		
		JsonObject vresult = this.validateFeed(obj);
		if (vresult.getInteger("status").intValue() > 0){			
			Document doc = new Document(obj.getMap());
			//doc.append(Feed.Poster, user);
			String posted = df.format(new Date());
			doc.append(Feed.Posted, posted);
			
			this.feeds.insertOne(doc);
			
			Document rst = this.feeds.find(Filters.and(Filters.eq(
					Feed.Poster, obj.getString(Feed.Poster)),
					Filters.eq(Feed.Posted, posted))).first();
			
			return this.createResponse(
					1, 
					rst.getObjectId("_id").toHexString(), 
					"Success");
		}
		
		logger.info("addFeed :: <-");
		
		return vresult;
	}
	
	public void close(){
		if (this.client != null){
			logger.info("Closing database ->>");
			this.client.close();
			logger.info("Closing database <<-");
		}
	}
	
	private long countComments(String id){
		return this.comments.count(Filters.eq(Comment.Article, id));
	}
	
	private long countDislikes(String id){
		return this.dislikes.count(Filters.eq(Dislike.article, id));
	}
	
	private long countLikes(String id){
		return this.likes.count(Filters.eq(Likes.article, id));
	}
	
	protected JsonObject createResponse(int status, String data, String msg){
		return new JsonObject()
				.put("status", status)
				.put("data", data)
				.put("msg", msg);
	}
	
	protected JsonObject createResponseByJson(int status, JsonArray data, String msg){
		return new JsonObject()
				.put("status", status)
				.put("data", data)
				.put("msg", msg);
	}
	
	protected JsonObject createResponseByJson(int status, JsonObject data, String msg){
		return new JsonObject()
				.put("status", status)
				.put("data", data)
				.put("msg", msg);
	}
	
	public JsonObject dislike(JsonObject obj){
		logger.info("dislike :: obj {" + Boolean.toString(obj != null) +	"}");
		JsonObject vresult = this.validateDislike(obj);
		try {			
			if (vresult.getInteger("status").intValue() > 0){
				Boolean flag = obj.getBoolean(Dislike.flag);
				if (flag == null){
					flag = true;
				}
				
				// Ensure that if the user already liked, then delete that like.
				String articleId = obj.getString(Dislike.article);
				String poster = obj.getString(Dislike.Poster);
				this.removeDislike(articleId, poster);
				
				if (flag.booleanValue()){
					// Ensure that like is removed, as it will be contradictory.
					this.removeLike(articleId, poster);
					
					// Like
					Document doc = new Document()
							.append(Dislike.article, articleId)
							.append(Dislike.Poster, poster)
							.append(Dislike.Posted, this.df.format(new Date()));
					
					this.dislikes.insertOne(doc);
					
					this.viewed(
							obj.getString(Dislike.article), 
							obj.getString(Dislike.Poster));
				}
							
				return this.createResponse(1, null, "Success");			
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
				
		logger.info("dislike :: <-");
		
		return vresult;
	}
	
	public JsonObject favourite(JsonObject obj){
		logger.info("favourite :: obj {" + Boolean.toString(obj != null) +	"}");
		
		JsonObject vresult = this.validateFavourite(obj);
		if (vresult.getInteger("status").intValue() > 0){
			Boolean flag = obj.getBoolean(Favourite.flag);
			if (flag == null){
				flag = true;
			}
			
			// Ensure that if the user already liked, then delete that like.
			String articleId = obj.getString(Favourite.article);
			String poster = obj.getString(Favourite.Poster);
			this.removeFavourite(articleId, poster);
			
			if (flag.booleanValue()){		
				// Ensure that dislike is removed, as it will be contradictory.
				this.removeDislike(articleId, poster);
				
				// Like
				Document doc = new Document()
						.append(Favourite.article, articleId)
						.append(Favourite.Poster, poster)
						.append(Favourite.Posted, this.df.format(new Date()));
				
				this.favoutites.insertOne(doc);
				
				this.viewed(
						obj.getString(Favourite.article), 
						obj.getString(Favourite.Poster));
			}
			
			return this.createResponse(1, null, "Success");			
		}
		
		logger.info("favourite :: <-");
		
		return vresult;
	}
	
	private FindIterable<Document> findComments(ObjectId articleId){
		return this.comments.find(Filters.eq(
				Comment.Article, articleId.toHexString()));
	}
	
	protected Document getUser(String _id) {
		logger.info("getUser :: _id {" + _id + "}");
		Document doc = new Document(User.fedid, _id);
		
		FindIterable<Document> rst = this.users.find(doc);
		
		return rst.first();
	}
	
	private boolean isFavourite(String user, String id){
		FindIterable<Document> rst = 
				this.favoutites.find(Filters.and(
						Filters.eq(Favourite.article, id), 
						Filters.eq(Favourite.Poster, user)));
		return (rst != null && rst.first() != null);
	}
	
	private boolean isValid(String val){
		return val != null && 
				!val.trim().equals("") && 
				!val.trim().equalsIgnoreCase("null");
	}

	private boolean isValid(Double val){
		return  val != null;
	}
	
	public JsonObject like(JsonObject obj){
		logger.info("like :: obj {" + Boolean.toString(obj != null) + "}");
		
		JsonObject vresult = this.validateLike(obj);
		if (vresult.getInteger("status").intValue() > 0){
			Boolean flag = obj.getBoolean(Likes.flag);
			if (flag == null){
				flag = true;
			}
			
			// Ensure that if the user already liked, then delete that like.
			String articleId = obj.getString(Likes.article);
			String poster = obj.getString(Likes.Poster);
			this.removeLike(articleId, poster);
			
			if (flag.booleanValue()){		
				// Ensure that dislike is removed, as it will be contradictory.
				this.removeDislike(articleId, poster);
				
				// Like
				Document doc = new Document()
						.append(Likes.article, articleId)
						.append(Likes.Poster, poster)
						.append(Likes.Posted, df.format(new Date()));
				
				this.likes.insertOne(doc);	
				
				this.viewed(
						obj.getString(Likes.article), 
						obj.getString(Likes.Poster));
			}
						
			return this.createResponse(1, null, "Success");			
		}
		
		logger.info("addComment :: <-");
		
		return vresult;
	}
	
	public JsonObject listComment(JsonObject obj){
		logger.info("listComment :: obj {" + Boolean.toString(obj != null) + "}");
				
		JsonObject vresult = this.validateQueryCommentFilter(obj);
		if (vresult.getInteger("status").intValue() > 0){			
			FindIterable<Document> rst = this.feeds.find(
					Filters.eq(Comment.Article, obj.getString(QueryFilter.id)));
			
			String user = obj.getString(QueryFilter.user);
						
			JsonArray r = new JsonArray();
			rst.forEach(new Block<Document>() {

				@Override
				public void apply(Document d) {
					String docId = d.getObjectId("_id").toHexString();
					JsonObject rval =new JsonObject(d.toJson());
					rval.put("likes", countLikes(docId));
					rval.put("dislikes", countDislikes(docId));
					rval.put("comments", countComments(docId));
					rval.put("isfav", isFavourite(user, docId));
					
					// Add the user.
					Document usr = getUser(d.getString(Feed.Poster));
					if (usr != null){
						rval.put(User.name, usr.getString(User.name));
						rval.put(User.photo, usr.getString(User.photo));
					}
					
					// Work on dates.
					try {
						Date posted = df.parse(d.getString(Feed.Posted));
						rval.put("date", dateformat.format(posted));
						rval.put("day", dayformat.format(posted));
						rval.put("time", timeformat.format(posted));
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
									
					r.add(rval);
				}
			});
			
			/*rst.forEach((Document d) -> {
				String docId = d.getObjectId("_id").toHexString();
				JsonObject rval =new JsonObject(d.toJson());
				rval.put("likes", this.countLikes(docId));
				rval.put("dislikes", this.countDislikes(docId));
				rval.put("comments", this.countComments(docId));
				rval.put("isfav", this.isFavourite(user, docId));
				
				// Add the user.
				Document usr = this.getUser(d.getString(Feed.Poster));
				if (usr != null){
					rval.put(User.name, usr.getString(User.name));
					rval.put(User.photo, usr.getString(User.photo));
				}
				
				// Work on dates.
				try {
					Date posted = this.df.parse(d.getString(Feed.Posted));
					rval.put("date", dateformat.format(posted));
					rval.put("day", dayformat.format(posted));
					rval.put("time", timeformat.format(posted));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
								
				r.add(rval);
			});*/
			
			logger.info("listComment :: <-");
			
			return this.createResponseByJson(1, r, "Success");			
		}
		
		logger.info("listComment :: <-");
		
		return vresult;
	}
	
	public JsonObject listFeed(JsonObject obj){
		logger.info("listFeed :: obj {" + Boolean.toString(obj != null) + "}");
				
		JsonObject vresult = this.validateQueryFilter(obj);
		if (vresult.getInteger("status").intValue() > 0){
			JsonObject filterQry = obj.getJsonObject(QueryFilter.filter); 
			
			Bson qry = null;
			if (filterQry != null){
				List<Bson> filters = new ArrayList<Bson>();
				
				filterQry.forEach(o -> {
					filters.add(Filters.eq(o.getKey(), o.getValue()));
				});
				
				qry = Filters.and(filters);
			}			
			
			FindIterable<Document> rst = null;
			
			JsonArray fieldQry = obj.getJsonArray(QueryFilter.fields);
			if (fieldQry != null && !fieldQry.isEmpty()){
				List<String> fields = new ArrayList<>();
				fieldQry.forEach(o -> {
					fields.add((String)o);
				});
				
				fields.add(Feed.Posted);
				fields.add(Feed.Poster);
				
				if (qry == null){
					rst = this.feeds.find().projection(Projections.include(fields));
				} else {
					rst = this.feeds.find(qry).projection(Projections.include(fields));
				}				
			} else {
				if (qry == null){
					rst = this.feeds.find();
				} else {
					rst = this.feeds.find(qry);
				}				
			}
			
			String user = obj.getString(QueryFilter.user);
						
			JsonArray r = new JsonArray();
			rst.forEach(new Block<Document>() {

				@Override
				public void apply(Document d) {
					String docId = d.getObjectId("_id").toHexString();
					JsonObject rval =new JsonObject(d.toJson());
					rval.put("likes", countLikes(docId));
					rval.put("dislikes", countDislikes(docId));
					rval.put("comments", countComments(docId));
					rval.put("isfav", isFavourite(user, docId));
					
					// Add the user.
					Document usr = getUser(d.getString(Feed.Poster));
					if (usr != null){
						rval.put(User.name, usr.getString(User.name));
						rval.put(User.photo, usr.getString(User.photo));
					}
					
					// Work on dates.
					try {
						Date posted = df.parse(d.getString(Feed.Posted));
						rval.put("date", dateformat.format(posted));
						rval.put("day", dayformat.format(posted));
						rval.put("time", timeformat.format(posted));
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
									
					r.add(rval);
				}
			});
			
			logger.info("listFeed :: <-");
			
			return this.createResponseByJson(1, r, "Success");			
		}
		
		logger.info("listFeed :: <-");
		
		return vresult;
	}
	
	public JsonObject removeComment(JsonObject obj){
		logger.info("removeComment :: obj {" + Boolean.toString(obj != null) +	"}");
		
		JsonObject vresult = this.validateCommentForRemoval(obj);
		if (vresult.getInteger("status").intValue() > 0){			
			Bson qry = Filters.and(
					Filters.eq(Comment.id, new ObjectId(obj.getString(Comment.id))),
					Filters.eq(Comment.Poster, obj.getString(Comment.Poster)));
			
			Document doc = this.feeds.findOneAndDelete(qry);
			if (doc != null){
				String id = obj.getString(Comment.id);
				this.removeLike(id);
				this.removeDislike(id);
				this.removeViews(id);
				this.removeFavourite(id);
			
				FindIterable<Document> comments = 
						this.findComments(doc.getObjectId("_id"));
				if (comments != null){
					comments.forEach(new Block<Document>() {

						@Override
						public void apply(Document d) {
							removeComments(d);
						}
					});
					/*comments.forEach((Document d) -> {
						this.removeComments(d);
					});*/
				}				
			}
			
			return this.createResponse(1, null, "Success");			
		}
		
		logger.info("removeComment :: <-");
		
		return vresult;
	}
	
	private void removeComments(Document doc){
		if (doc != null){
			FindIterable<Document> comments = 
					this.findComments(doc.getObjectId("_id"));
			if (comments != null){
				comments.forEach(new Block<Document>() {

					@Override
					public void apply(Document d) {
						removeComments(d);
						
					}
				});
				/*comments.forEach((Document d) -> {
					this.removeComments(d);
				});*/
			}
			
			ObjectId id = doc.getObjectId("_id");			
			this.removeComments(id);
		}
	}
	
	private void removeComments(ObjectId id){
		logger.info("removeComments :: id {" + id +	"}");
		
		if (id != null && this.isValid(id.toHexString())){
			this.removeLike(id.toHexString());
			this.removeDislike(id.toHexString());
			this.removeViews(id.toHexString());
			this.removeFavourite(id.toHexString());
			this.comments.deleteOne(Filters.eq(Comment.Article, id.toHexString()));						
		}
		
		logger.info("removeComments :: <-");		
	}
	
	private void removeDislike(String id){
		logger.info("removeDislike :: id {" + id +	"}");
		
		if (this.isValid(id)){
			this.dislikes.deleteOne(Filters.eq(Dislike.article, id));						
		}
		
		logger.info("removeDislike :: <-");
	}
	
	private void removeDislike(String articleId, String poster){
		if (this.isValid(articleId) && this.isValid(poster)){
			Bson qry = Filters.and(
					Filters.eq(Dislike.article, articleId),
					Filters.eq(Dislike.Poster, poster));
			this.dislikes.findOneAndDelete(qry);
		}
	}
	
	private void removeFavourite(String id){
		logger.info("removeFavourite :: id {" + id + "}");
		
		if (this.isValid(id)){
			this.favoutites.deleteOne(Filters.eq(Favourite.article, id));						
		}
		
		logger.info("removeFavourite :: <-");
	}
	
	private void removeFavourite(String articleId, String poster){
		if (this.isValid(articleId) && this.isValid(poster)){
			Bson qry = Filters.and(
					Filters.eq(Favourite.article, articleId),
					Filters.eq(Favourite.Poster, poster));
			this.favoutites.findOneAndDelete(qry);
		}
	}
	
	public JsonObject removeFeed(JsonObject obj){
		logger.info("removeFeed :: obj {" + Boolean.toString(obj != null) +	"}");
		
		JsonObject vresult = this.validateFeedForRemoval(obj);
		if (vresult.getInteger("status").intValue() > 0){
			Bson qry = Filters.and(
					Filters.eq(Feed.id, new ObjectId(obj.getString(Feed.id))),
					Filters.eq(Feed.Poster, obj.getString(Feed.Poster)));
			
			Document doc = this.feeds.findOneAndDelete(qry);
			if (doc != null){
				String id = obj.getString(Feed.id);
				this.removeLike(id);
				this.removeDislike(id);
				this.removeViews(id);
				this.removeFavourite(id);
			
				FindIterable<Document> comments = 
						this.findComments(doc.getObjectId("_id"));
				if (comments != null){
					comments.forEach(new Block<Document>() {

						@Override
						public void apply(Document d) {
							removeComments(d);							
						}
					});
					
					/*comments.forEach((Document d) -> {
						this.removeComments(d);
					});*/
				}				
			}
			
			return this.createResponse(1, null, "Success");			
		}
		
		logger.info("removeFeed :: <-");
		
		return vresult;
	}
		
	private void removeLike(String id){
		logger.info("removeLike :: id {" + id +	"}");
		
		if (this.isValid(id)){
			this.likes.deleteOne(Filters.eq(Likes.article, id));						
		}
		
		logger.info("removeLike :: <-");
	}
	
	private void removeLike(String articleId, String poster){
		if (this.isValid(articleId) && this.isValid(poster)){
			Bson qry = Filters.and(
					Filters.eq(Likes.article, articleId),
					Filters.eq(Likes.Poster, poster));
			this.likes.findOneAndDelete(qry);
		}		
	}
	
	private void removeViews(String id){
		logger.info("removeViews :: id {" + id + "}");
		
		if (this.isValid(id)){
			this.views.deleteOne(Filters.eq(Views.article, id));						
		}
		
		logger.info("removeViews :: <-");
	}
	
	private void resetViewed(String articleId, String poster){
		logger.info("viewed :: articleId {" + 
				articleId + "}, poster = {" + poster + "}");
		
		// Add View
		Document doc = new Document()
				.append(Views.article, articleId)
				.append(Views.Poster, poster)
				.append(Views.Posted, this.df.format(new Date()));
		
		this.views.insertOne(doc);
		
		logger.info("viewed :: <-");		
	}
	
	protected JsonObject validateComment(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Comment not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(Comment.Article))){
			missingVals.add(Comment.Article);
		}
		
		if (!this.isValid(obj.getString(Comment.Comment))){
			missingVals.add(Comment.Comment);
		}
		
		if (!this.isValid(obj.getString(Comment.Poster))){
			missingVals.add(Comment.Poster);
		}
				
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
		
	protected JsonObject validateCommentForRemoval(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Comment identity not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(Comment.id))){
			missingVals.add(Comment.id);
		}
		
		if (!this.isValid(obj.getString(Comment.Poster))){
			missingVals.add(Comment.Poster);
		}
				
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
		
	protected JsonObject validateDislike(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Dislike not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(Dislike.article))){
			missingVals.add(Dislike.article);
		}
		
		if (!this.isValid(obj.getString(Dislike.Poster))){
			missingVals.add(Dislike.Poster);
		}
				
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
	
	protected JsonObject validateFavourite(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Favourite not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(Favourite.article))){
			missingVals.add(Favourite.article);
		}
		
		if (!this.isValid(obj.getString(Favourite.Poster))){
			missingVals.add(Favourite.Poster);
		}
				
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
	
	protected JsonObject validateFeed(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Feed not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(Feed.Article))){
			missingVals.add(Feed.Article);
		}
		
		if (!this.isValid(obj.getString(Feed.Summary))){
			missingVals.add(Feed.Summary);
		}
		
		if (!this.isValid(obj.getString(Feed.Poster))){
			missingVals.add(Feed.Poster);
		}

		if (!this.isValid(obj.getDouble(Feed.Latitude))){
			missingVals.add(Feed.Latitude);
		}

		if (!this.isValid((obj.getDouble(Feed.Longitude)))){
			missingVals.add(Feed.Longitude);
		}
				
		return missingVals.size() > 0 ? 
				this.createResponse(
						0,
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
		
	protected JsonObject validateFeedForRemoval(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Feed identity not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(Feed.id))){
			missingVals.add(Feed.id);
		}
		
		if (!this.isValid(obj.getString(Feed.Poster))){
			missingVals.add(Feed.Poster);
		}
				
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
	
	protected JsonObject validateLike(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Like not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(Likes.article))){
			missingVals.add(Likes.article);
		}
		
		if (!this.isValid(obj.getString(Likes.Poster))){
			missingVals.add(Likes.Poster);
		}
				
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
	
	protected JsonObject validateQueryCommentFilter(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Query filter not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(QueryFilter.id))){
			missingVals.add(QueryFilter.id);
		}
		
		if (!this.isValid(obj.getString(QueryFilter.user))){
			missingVals.add(QueryFilter.user);
		}
		
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
	
	protected JsonObject validateQueryFilter(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "Query filter not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		//if (!this.isValid(obj.getString(QueryFilter.filter))){
			//missingVals.add(QueryFilter.filter);
		//}
		
		if (!this.isValid(obj.getString(QueryFilter.user))){
			missingVals.add(QueryFilter.user);
		}
		
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
	
	private void viewed(String articleId, String poster){
		if (this.isValid(articleId) && this.isValid(poster)){
			Bson qry = Filters.and(
					Filters.eq(Views.article, articleId),
					Filters.eq(Views.Poster, poster));
			this.views.findOneAndDelete(qry);
			
			this.resetViewed(articleId, poster);
		}
	}
}