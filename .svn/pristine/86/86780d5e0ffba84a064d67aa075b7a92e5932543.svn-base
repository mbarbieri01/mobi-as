package com.cesaco.mobias.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.cesaco.mobias.controller.Caller;
import com.cesaco.mobias.controller.Controller;
import com.cesaco.mobias.controller.RInfo;

@Path("/flow")
@RequestScoped
public class FlowResourceRESTService {

	@Inject
	private Controller controller;

	@GET
	@Path("/registerAzi/{aziName}")
	@Produces("text/plain")
	public String registerAzi(@PathParam("aziName") String aziName) {
		return controller.getClientIDbyAzi(aziName)+"";
//		if (aziName.equals("INCODE")) {
//			return "05";
//		} else {
//			return "01";
//		}
	}

	@GET
	@Path("/getFlowList/{clientID}")
	@Produces("text/xml")
	public String getFlowList(@PathParam("clientID") String clientID) {
		return controller.getFlowList(clientID);
	}

	@GET
	@Path("/getFlowInfo/{flowID}")
	@Produces("text/xml")
	public String getFlowInfo(@PathParam("flowID") String flowID) {

		return controller.getFlowInfo(flowID);
	}
	
	@GET
	@Path("/getOldFlowData/{clientID}/{flowID}/{daData}/{aData}")
	@Produces("text/xml")
	public String getFlowData(@PathParam("clientID") int clientID,
			@PathParam("flowID") String flowID,
			@PathParam("daData") String daData, @PathParam("aData") String aData) {

		long[] transs = controller.startFlowRequest(clientID, flowID, daData,
				aData);

		int timer = 0;
		while (!controller.hasFlowData(clientID, transs) && timer < 60) {
			try {
				Thread.sleep(1000);
				timer++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if (timer >= 60) {
			return "Timeout reached.";
		} else {
			return controller.getFlowData(clientID, flowID, transs);
		}

		

	}
	
	
	@GET
	@Path("/getFlowData/{clientID}/{flowID}/{daData}/{aData}")
	@Produces("text/plain")
	public String getAscyncFlowData(@PathParam("clientID") int clientID,
			@PathParam("flowID") String flowID,
			@PathParam("daData") String daData, @PathParam("aData") String aData) {

		long[] transs = controller.startFlowRequest(clientID, flowID, daData,
				aData);

		String reqID = controller.getNewReqID(flowID, clientID, transs);
		
//		RInfo rInfo = new RInfo();
//		rInfo.setClientID(clientID);
//		rInfo.setFlowID(flowID);
//		rInfo.setTranss(transs);
//		rInfo.setReqID(reqID);
//		
//		try {
//			// Step 1. Lookup the initial context
//			InitialContext ic = new InitialContext();
//
//			// JMS operations
//
//			// Step 2. Look up the XA Connection Factory
//			ConnectionFactory cf = (ConnectionFactory) ic.lookup("java:/ConnectionFactory");
//
//			// Step 3. Look up the Queue
//			Queue queue = (Queue) ic.lookup("java:/infoQueue");
//
//			// Step 4. Create a connection, a session and a message producer for
//			// the queue
//			Connection jmsConnection = cf.createConnection();
//			Session session = jmsConnection.createSession(false,
//					Session.AUTO_ACKNOWLEDGE);
//			MessageProducer messageProducer = session.createProducer(queue);
//
//			ObjectMessage message = session.createObjectMessage(rInfo);
//			message.setStringProperty("d_class", "FreeCaller");
//			
//			messageProducer.send(message);
//			
//		} catch (NamingException ne) {
//			System.out.println(ne.toString());
//		} catch (JMSException je) {
//			System.out.println(je.toString());
//		}
		
		return reqID;
		
	}
	
	@GET
	@Path("/getResponse/{reqID}")
	@Produces("text/xml")
	public String getResponse(@PathParam("reqID") String reqID) {
		return controller.getFlowResponse(reqID);
	}
	
	@GET
	@Path("/getPlainFlowData/{clientID}/{flowID}/{daData}/{aData}")
	@Produces("text/plain")
	public String getPlainFlowData(@PathParam("clientID") int clientID,
			@PathParam("flowID") String flowID,
			@PathParam("daData") String daData, @PathParam("aData") String aData) {

		long[] transs = controller.startFlowRequest(clientID, flowID, daData,
				aData);

		int timer = 0;
		while (!controller.hasFlowData(clientID, transs) && timer < 60) {
			try {
				Thread.sleep(1000);
				timer++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (timer >= 60) {
			System.out.println("Timeout reached.");
		}

		return controller.getPlainFlowData(clientID, flowID, transs);

	}
	
	@POST
	@Path("postFlowData/{clientID}/{flowID}/{daData}/{aData}")
	@Consumes("text/xml")
	@Produces("text/plain")
	public String postOnlyAsyncXML(String incomingXML, @PathParam("clientID") int clientID,
			@PathParam("flowID") String flowID,
			@PathParam("daData") String daData, @PathParam("aData") String aData) {
		
		long[] transs = controller.startFlowRequest(incomingXML, clientID, flowID, daData,
				aData);
		String reqID = "";
		if (transs != null) {
		reqID = controller.getNewReqID(flowID, clientID, transs);
		} else {
			reqID = "-1";
		}
		
		return reqID;
	}

	@POST
	@Path("postOldFlowData/{clientID}/{flowID}/{daData}/{aData}")
	@Consumes("text/xml")
	@Produces("text/plain")
	public String postOnlyXML(String incomingXML, @PathParam("clientID") int clientID,
			@PathParam("flowID") String flowID,
			@PathParam("daData") String daData, @PathParam("aData") String aData) {
		long[] transs = controller.startFlowRequest(incomingXML, clientID, flowID, daData,
				aData);

		int timer = 0;
		while (!controller.hasFlowData(clientID, transs) && timer < 60) {
			try {
				Thread.sleep(1000);
				timer++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if (timer >= 60) {
			return "Timeout reached.";
		} else {
			return controller.getFlowData(clientID, flowID, transs);
		}
	}
	
	@GET
	@Path("getAziList/{clientID}/{flowID}")
	@Produces("text/plain")
	public String getListAziXML(@PathParam("clientID") int clientID, @PathParam("flowID") String flowID) {
		int timer = 0;
		long[] transs = new long[1];
		transs[0] = controller.startAziListRequest(clientID, flowID);
		while (!controller.hasFlowData(clientID, transs) && timer < 60) {
			try {
				Thread.sleep(1000);
				timer++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (timer >= 60) {
			System.out.println("Timeout reached.");
		}
		return controller.getListAzi(transs[0], clientID);
	}
	
	@GET
	@Path("testExcel")
	@Produces("text/plain")
	public String testExcel() {
		try {
			FileInputStream fis = new FileInputStream("c:\\source.xls");
			return controller.openExcel("c:\\source.xls");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
	}
	

}
