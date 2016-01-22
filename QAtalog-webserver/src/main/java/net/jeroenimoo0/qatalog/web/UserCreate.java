package net.jeroenimoo0.qatalog.web;

import net.jeroenimoo0.qatalog.QAtalog;
import net.jeroenimoo0.qatalog.QAtalogServerResource;

import org.bson.Document;
import org.restlet.resource.Post;

import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * Creates a user in the database for the given did (Device ID)
 * 
 * @author Jeroenimoo0
 */
public class UserCreate extends QAtalogServerResource {
	
	@Post("json")
	public String createUser() {
		String deviceId = getAttribute("did");
		if(deviceId == null) return createError("missing parameter 'did' (Device ID)", 0x0);
		
		MongoCollection<Document> users = QAtalog.getDatabase().getCollection("users");
		
		FindIterable<Document> count = users.find(new Document("did" , deviceId)); 
		Document doc = count.first();
		
		if(doc == null) {
			doc = new Document("did", deviceId);
			users.insertOne(doc);
		}
		
		
		JsonObject response = new JsonObject();
		response.addProperty("status", "ok");
		response.addProperty("uid", doc.get("_id").toString());
		
		return response.toString();
	}
}
