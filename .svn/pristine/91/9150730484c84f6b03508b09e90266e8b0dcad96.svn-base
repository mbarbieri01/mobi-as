package com.cesaco.mobias.controller;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.cesaco.mobias.util.jms.AsynchReceiver;

/**
 * Message-Driven Bean implementation class for: InfoReceiver
 * 
 */
@MessageDriven(name = "InfoReceiver", activationConfig = {

		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),

		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/fileStatus"),

		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")

//,@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "client_id = 12")
} )
public class InfoReceiver implements MessageListener {
	
	@Inject
	AsynchReceiver asynchReceiver;
	
	@Inject
	JMScontroller jmsController;

	/**
	 * Default constructor.
	 */
	public InfoReceiver() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		try {
			int client_id = message.getIntProperty("client_id");
			long transID = message.getLongProperty("transID");
			asynchReceiver.getMessage(client_id, transID, jmsController);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
