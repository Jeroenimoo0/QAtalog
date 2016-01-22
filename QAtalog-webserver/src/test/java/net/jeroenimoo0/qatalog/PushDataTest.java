package net.jeroenimoo0.qatalog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PushDataTest extends QAtalogTest {
	
	@Before
	public void before() {
		Assert.assertTrue(QAtalog.start(DATABASE));
		QAtalog.getDatabase().drop();
	}
	
	@Test
	public void test() throws Exception {
		new CreateUserTest().test();
		
		URL url = new URL("http://localhost:8080" + "/push/" + USERID);
		HttpURLConnection  connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Content-Type", "application/json");
		
	    connection.setDoOutput(true);
	    
	    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	    wr.writeBytes("{\"wins\":[{\"x\":240,\"y\":920,\"time\":420}]}");
	    wr.close();
	    
	    InputStream is = connection.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    StringBuilder response = new StringBuilder();
	    String line;
	    while((line = rd.readLine()) != null) {
	      response.append(line);
	    }
	    rd.close();
	    
	    JsonObject object = new JsonParser().parse(response.toString()).getAsJsonObject();
	    System.out.println(object);
		 Assert.assertEquals(object.getAsJsonPrimitive("status").getAsString(), "success");
	}
	
	@After
	public void after() {
		QAtalog.stop();
	}
}
