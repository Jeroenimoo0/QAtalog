package net.jeroenimoo0.qatalog.web;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Status extends ServerResource {
	@Get("text")
	public String toString() {
		return "hello, world";
	}
}
