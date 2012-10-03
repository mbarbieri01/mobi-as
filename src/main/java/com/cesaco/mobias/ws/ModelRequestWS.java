package com.cesaco.mobias.ws;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.cesaco.mobias.controller.JMScontroller;

@WebService()
public class ModelRequestWS {

	@Inject
	private JMScontroller jmsc;

	@WebMethod()
	public String sayHello(String name) {
		System.out.println("Hello: " + name);
		return "Hello " + name + "!";
	}

	@WebMethod()
	public String requestModelFile(String features, int client_id) {
		// TODO
		return jmsc.addRequest(client_id, features);
	}

	@WebMethod()
	public String getStatusAndFilename(String transID) {
		return jmsc.getStatusAndFilename(transID);
	}
}
