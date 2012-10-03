package com.cesaco.mobias.controller;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Message-Driven Bean implementation class for: AlertHasData
 *
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
				@ActivationConfigProperty(propertyName="messageSelector", propertyValue="d_class = 'AlertHasData'"),
				@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/infoQueue"),
				@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")},
		mappedName = "java:/infoQueue")

public class AlertHasData implements MessageListener {

    /**
     * Default constructor. 
     */
    public AlertHasData() {
        // TODO Auto-generated constructor stub
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        // TODO Auto-generated method stub
        
    }

}
