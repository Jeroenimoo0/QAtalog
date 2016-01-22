package net.jeroenimoo0.qatalog;

import net.jeroenimoo0.qatalog.web.DataPush;
import net.jeroenimoo0.qatalog.web.ServerStatus;
import net.jeroenimoo0.qatalog.web.User;
import net.jeroenimoo0.qatalog.web.UserCreate;

import org.bson.Document;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Main class for QAtalog web server.
 * Manages initialization and lifetime of the application.
 * 
 * @author Jeroenimoo0
 */
public class QAtalog {
	public static final boolean DEBUG = true;
	
	private static MongoClient client;
	private static MongoDatabase database;
	
	private static Server server;
	
	public static MongoClient getClient() {
		return client;
	}
	
	public static MongoDatabase getDatabase() {
		return database;
	}
	
	public static Server getServer() {
		return server;
	}
	
	public static void main(String[] args) {
		if(!start()) System.exit(-1);
	}
	
	public static boolean start() {
		System.out.println("Starting up server...");
		
		try {
			client = new MongoClient("localhost");
			database = client.getDatabase("qatalog");
			
			QAtalog.getDatabase().runCommand(new Document("serverStatus", "1"));
		} catch(Exception e) {
			System.out.println("Error while starting server:");
			e.printStackTrace();
			return false;
		}
		
		try {
			Component component = new Component();
			server = component.getServers().add(Protocol.HTTP, 8080);

			VirtualHost host = component.getDefaultHost();
			host.attach("/status", ServerStatus.class);
			
			host.attach("/user/{did}", User.class);
			host.attach("/user/create/{did}", UserCreate.class);
			host.attach("/push/{uid}", DataPush.class);
			
			component.start();
		} catch (Exception e) {
			System.out.println("Error while starting server:");
			e.printStackTrace();
			return false;
		}
		
		System.out.println("Succesfully started server.");
		return true;
	}
	
	public static void stop() {
		System.out.println("Stopping server...");
		
		try {
			if(server != null) server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		server = null;
		client = null;
		database = null;
		
		System.out.println("Stopped server.");
	}
}
