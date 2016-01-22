package net.jeroenimoo0.qatalog;

import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;

/**
 * Base QAtalog ServerResource. Has some utility methods.
 * 
 * @author Jeroenimoo0
 */
public class QAtalogServerResource extends ServerResource {
	public String createError(String cause, int code) {
		JsonObject response = new JsonObject();
		
		response.addProperty("status", "error");
		response.addProperty("cause", cause);
		response.addProperty("code", code);
		
		return response.toString();
	}

	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
