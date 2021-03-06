package net.jeroenimoo0.qatalog.web;

import net.jeroenimoo0.qatalog.QAtalog;
import net.jeroenimoo0.qatalog.QAtalogServerResource;

import org.bson.Document;
import org.restlet.resource.Get;

import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * Retrieve the uid (User ID) for the given did (Device ID).
 * 
 * @author Jeroenimoo0
 */
public class User extends QAtalogServerResource {

	@Get("json")
	public String getUser() {
		String deviceId = getAttribute("did");
		if(deviceId == null) return createError("missing parameter 'did' (Device ID)", 0x0);
		
		MongoCollection<Document> users = QAtalog.getDatabase().getCollection("users");
		FindIterable<Document> result = users.find(new Document("did", deviceId));
		
		Document document = result.first();
		if(document == null) return createError("no user with the given did (Device ID) " + deviceId, 0x02);
		
		JsonObject response = new JsonObject();
		response.addProperty("status", "ok");
		response.addProperty("uid", document.get("_id").toString());
		
		return response.toString();
	}
}
