package net.jeroenimoo0.qatalog.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.jeroenimoo0.qatalog.QAtalog;
import net.jeroenimoo0.qatalog.QAtalogServerResource;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;

/**
 * Inserts data into the database.
 * 
 * @author Jeroenimoo0
 */
public class DataPush extends QAtalogServerResource {
	
	@Post("json")
	public String toString(Representation entity) throws IOException {
		String deviceId = getAttribute("uid");
		if(deviceId == null) return createError("missing parameter 'uid' (User ID)", 0x1);
		
		String text = entity.getText();
		
		JsonObject json = new JsonParser().parse(text).getAsJsonObject();
		
		System.out.println(json);
		
		MongoCollection<Document> users = QAtalog.getDatabase().getCollection("users");
		
		for(Entry<String, JsonElement> entry : json.entrySet()) {
			System.out.println(entry.getValue());
			if(!entry.getValue().isJsonArray()) continue;
			JsonArray array = entry.getValue().getAsJsonArray();
			System.out.println("Pushing " + entry.getKey() + ": " + entry.getValue().toString());
			
			List<Document> docs = new ArrayList<>(array.size());
			
			for(JsonElement element : array) {
				if(!element.isJsonObject()) continue;
				
				Document doc = Document.parse(element.toString());
				docs.add(doc);
			}
			
			Document arrayDoc = new Document("$each", docs);
			Document valueDoc = new Document(entry.getKey(), arrayDoc);
			Document pushDoc = new Document("$push", valueDoc);
			
			users.updateOne(new Document("_id", new ObjectId(deviceId)), pushDoc, new UpdateOptions().upsert(true));
		}
		
		JsonObject response = new JsonObject();
		response.addProperty("status", "success");
		
		return response.toString();
	}
}
