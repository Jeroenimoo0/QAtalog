package net.jeroenimoo0.qatalog.web;

import net.jeroenimoo0.qatalog.QAtalog;

import org.bson.Document;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Retrieves the server status.
 * 
 * @author Jeroenimoo0
 */
public class ServerStatus extends ServerResource {
	@Get("json")
	public String toString() {
		boolean ok = false;
		
		try {
			QAtalog.getDatabase().runCommand(new Document("serverStatus", "1"));
			ok = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "{\"status:\":\"" + (ok ? "ok" : "error") + "\"}";
	}
}
