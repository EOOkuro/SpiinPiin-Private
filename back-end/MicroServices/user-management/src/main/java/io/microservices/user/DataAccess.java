/**
 * 
 */
package io.microservices.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import io.microservices.user.data.TSession;
import io.microservices.user.entity.ASession;
import io.microservices.user.entity.User;

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
	
	private MongoCollection<Document> sessions;
	
	private DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		
	public DataAccess(Vertx vertx){
		logger.info("DataAccess instantiation ->>");
		
		this.client = new MongoClient();
		this.userManagementDb = this.client.getDatabase(this.userdb);
		this.users = this.userManagementDb.getCollection("users");
		this.sessions = this.userManagementDb.getCollection("sessions");
		
		logger.info("DataAccess instantiation <<-");
	}
		
	public JsonObject add(JsonObject obj, String ipAddress){
		logger.info("add :: obj {" + 
				Boolean.toString(obj != null) + 
				"}, ipAdress {" + 
				ipAddress + 
				"}");
		JsonObject vresult = this.validateFederatedUser(obj);
		if (vresult.getInteger("status").intValue() > 0){			
			Document doc = new Document(obj.getMap());
			
			users.insertOne(doc);
			
			JsonObject det = new JsonObject()
					.put(TSession.key, this.genSession(
							obj.getString(User.fedid), 
							ipAddress))
					.put(TSession.name, obj.getString(User.name));
					//.put(TSession.role, );
			
			return this.genSessionResponse(1, det, "Success");			
		}
		
		logger.info("add :: <-");
		
		return vresult;		
	}
	
	private void cleanUserForUpdate(JsonObject user){
		logger.info("cleanUserForUpdate :: user {" + 
				Boolean.toString(user != null) + "}");
		if (user != null){
			if (user.containsKey(User.fedid)){
				user.remove(User.fedid);
			}
			
			if (user.containsKey(User.fedemail)){
				user.remove(User.fedemail);
			}
			
			if (user.containsKey(User.provider)){
				user.remove(User.provider);
			}
		}
		
		logger.info("cleanUserForUpdate :: <- ");
	}
	
	public JsonObject update(String key, JsonObject obj){
		logger.info("update :: key = {" 
				+ key + "}, obj {" + 
				Boolean.toString(obj != null) + "}");
		
		Document session = this.getSession(key);
		if (session == null){
			return genSessionResponse(0, null, "Invalid session key!");
		} 
		
		this.cleanUserForUpdate(obj);
		
		if (!obj.isEmpty()){
			try {
				List<Bson> fields = new ArrayList<Bson>();
				for (String ukey : obj.fieldNames()) {
					fields.add(Updates.set(ukey, obj.getValue(ukey)));
				}
				
				fields.add(Updates.currentDate("lastModified"));
				
				UpdateOptions options = new UpdateOptions()
						.upsert(false);
				
				Bson updates = Updates.combine(fields);
				
				users.updateOne(Filters.eq(
						User.fedid,
						session.getString(ASession.userId)), updates, options);
			
				logger.info("update :: < -");
				return this.createResponse(1, null, "Success");
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}			
		}
		
		return this.createResponse(0, null, "Failed");
	}
	
	public void close(){
		if (this.client != null){
			logger.info("Closing database ->>");
			this.client.close();
			logger.info("Closing database <<-");
		}
	}
	
	protected JsonObject createResponse(int status, String data, String msg){
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
	
	private JsonObject genSession(Document obj){
		logger.info("genSession :: obj {" + 
				Boolean.toString(obj != null) + "}");
		if (obj != null){
			try {
				Document usr = this.getUser(obj.getString(ASession.userId));
				
				JsonObject det = new JsonObject()
						.put(TSession.key, obj.getObjectId("_id").toString())
						.put(TSession.name, usr.getString(User.name));
						//.put(TSession.role, );
				
				return this.genSessionResponse(1, det, "Success");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}		
		
		logger.info("genSession :: <-");
		
		return this.createResponse(0, null, "Invalid signin");
	}
	
	protected String genSession(String userId, String ipAddress){
		logger.info("genSession :: userId {" + 
				userId + "}, ipAddress {" + ipAddress + "}");
		
		Document doc = new Document();		
		doc.put(ASession.ipAddress, ipAddress);
		doc.put(ASession.timestamp, this.df.format(new Date()));
		doc.put(ASession.userId, userId);
		
		this.sessions.insertOne(doc);
		FindIterable<Document> rst = this.sessions.find(doc);
		
		ObjectId id = rst.first().getObjectId("_id");
		
		logger.info("genSession :: < - {" + id.toHexString() + "}");
		
		return id.toString();
	}
	
	protected JsonObject genSessionResponse(
			int status, 
			JsonObject data, 
			String msg){
		return new JsonObject()
				.put("status", status)
				.put("data", data)
				.put("msg", msg);
				
	}
	
	protected Document getSession(String key){
		logger.info("genSession :: key {" +	key + "}");
		
		if (this.isValid(key) && ObjectId.isValid(key)){
			try {
				ObjectId id = new ObjectId(key);
				
				Document qry = new Document();			
				qry.put("_id", id);
				
				FindIterable<Document> rst = this.sessions.find(qry);
				
				Document v = rst.first();
				
				return v;			
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}			
		}
		
		logger.info("genSession :: <- {null}");
		
		return null;
	}
	
	protected Document getSession(String fedid, String ipAddress){
		logger.info("genSession :: fedid {" +
				fedid + "}, ipAddress = {" + ipAddress + "}");
		if (this.isValid(fedid) && this.isValid(ipAddress)){
			try {
				
				Document qry = new Document();			
				qry.put(ASession.ipAddress, ipAddress);
				qry.put(ASession.userId, fedid);
				
				FindIterable<Document> rst = this.sessions.find(qry);
				
				Document v = rst.first();
				
				return v;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		logger.info("genSession :: <- {null}");
		
		return null;
	}
	
	public JsonObject getUserBySessionKey(String key){
		logger.info("getUserBySessionKey :: key {" + key + "}");
		
		try {
			Document session = this.getSession(key);
			if (session != null){
				Document doc = 
						this.getUser(session.getString(ASession.userId));
				if (doc != null){
					return this.createResponseByJson(1, new JsonObject(doc), "Success");
				} else {
					return this.createResponse(0, null, "User not found!");
				}			
			} else {
				return this.createResponse(0, null, "Invalid session key");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.createResponse(0, null, "User not found!");
		}				
	}
	
	protected Document getUser(String _id) {
		logger.info("getUser :: _id {" + _id + "}");
		Document doc = new Document(User.fedid, _id);
		
		FindIterable<Document> rst = this.users.find(doc);
		
		return rst.first();
	}
	
	protected Document getUser(String _id, String provider) {
		logger.info("getUser :: _id {" + _id + "}, provider {" + provider + "}");
		
		Document doc = new Document(User.fedid, _id);
		doc.put(User.provider, provider);
		
		FindIterable<Document> rst = this.users.find(doc);
		
		return rst.first();
	}
	
	private boolean isValid(String val){
		return val != null && 
				!val.trim().equals("") && 
				!val.trim().equalsIgnoreCase("null");
	}
		
	public JsonObject signIn(String key){
		logger.info("signIn :: key {" + key + "}");
		Document obj = this.getSession(key);
		
		return this.genSession(obj);
	}
	
	public JsonObject signIn(String _id, String provider, String ipAddress) {
		logger.info("signIn :: _id {" + _id + 
				"}, provider = {" + provider + 
				"}, ipAddress = {" + ipAddress + "}");
		
		Document doc = getSession(_id, ipAddress);
		if (doc != null){
			return this.genSession(doc);
		}
		
		Document usr = this.getUser(_id, provider);
		if (usr == null){
			// The user doesnt exist, the user should be created.
			return this.createResponse(-1, null, "User doesn't exist!");
		}
		
		JsonObject det = new JsonObject()
				.put(TSession.key, this.genSession(_id, ipAddress))
				.put(TSession.name, usr.getString(User.name));
				//.put(TSession.role, );
		
		logger.info("signIn :: <-");
		
		return this.genSessionResponse(1, det, "Success");		
	}
						
	protected JsonObject validateFederatedUser(JsonObject obj){
		if (obj == null){
			return this.createResponse(0, null, "User not provided!");
		}
		
		JsonArray missingVals = new JsonArray();
		if (!this.isValid(obj.getString(User.fedid))){
			missingVals.add(User.fedid);
		}
		
		if (!this.isValid(obj.getString(User.provider))){
			missingVals.add(User.provider);
		}
		
		if (!this.isValid(obj.getString(User.fedemail))){
			missingVals.add(User.fedemail);
		}
		
		if (!this.isValid(obj.getString(User.gender))){
			missingVals.add(User.gender);
		}
		
		if (!this.isValid(obj.getString(User.name))){
			missingVals.add(User.name);
		}

		if (!this.isValid(obj.getString(User.phone))){
			missingVals.add(User.phone);
		}
		
		return missingVals.size() > 0 ? 
				this.createResponse(
						0, 
						missingVals.encode(), 
						"Missing required fields") :
					this.createResponse(1, null, ""); 
	}
}