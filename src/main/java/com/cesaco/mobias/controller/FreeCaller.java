package com.cesaco.mobias.controller;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Message-Driven Bean implementation class for: FreeCaller
 * 
 */
@MessageDriven(activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName="messageSelector", propertyValue="d_class = 'FreeCaller'"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/infoQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")}
	, mappedName = "java:/infoQueue")

public class FreeCaller implements MessageListener {

	private final int MAX_TIMER = 300;

	@Inject
	private Controller controller;

	/**
	 * Default constructor.
	 */
	public FreeCaller() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
//		System.out.println("got message");
//		ObjectMessage o_message = (ObjectMessage) message;
//		RInfo rInfo = null;
//		try {
//			rInfo = (RInfo) o_message.getObject();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		if (rInfo != null) {
//			int clientID = rInfo.getClientID();
//			long[] transs = rInfo.getTranss();
//			String flowID = rInfo.getFlowID();
//			String reqID = rInfo.getReqID();
//
//			int timer = 0;
//
//			while (!controller.hasFlowData(clientID, transs)
//					&& timer < MAX_TIMER) {
//				try {
//					System.out.println("waiting in FreeCaller");
//					Thread.sleep(5000);
//					timer++;
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//			System.out.println("exit.");
//			if (timer >= 60) {
//
//			} else {
//				controller.executeFlowData(clientID, flowID, transs, reqID);
//			}
//		}

	}

}
