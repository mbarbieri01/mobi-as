package com.cesaco.mobias.queue;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.QueueBrowser;

@Stateful
@ApplicationScoped
@Named
public class QueueManager {

	private QueueBrowser qBrowser;
	
	@Inject
	private JMSSender jmsSender;
	
	@Inject
	private JMSReceiver jmsReceiver;

	public void sendRequestMessage(int clientID,long transID) {
		jmsSender.sendRequestMessage(clientID, transID);
	}

	public void initializate() {
	}
	
	public List<MessageInfo> getMessages() {
		qBrowser = jmsSender.getQueueBrowser();
		List<MessageInfo> messageList= new ArrayList<MessageInfo>();
		try {
			@SuppressWarnings("unchecked")
			Enumeration<BytesMessage> en = (Enumeration<BytesMessage>) qBrowser.getEnumeration();
			while (en.hasMoreElements()) {
				BytesMessage bMessage = en.nextElement();
				Long transID = bMessage.getLongProperty("transID");
				String messageType = bMessage.getStringProperty("messageType");
				Integer clientID = bMessage.getIntProperty("clientID");
				String dateCreated = bMessage.getStringProperty("dateCreated");
				String id = bMessage.getJMSMessageID();
				messageList.add(new MessageInfo(
						transID,
						messageType,
						clientID,
						dateCreated,
						id));
				//System.out.println("retrieved transID: "+transID);
			}	
			return messageList;
		} catch (JMSException e) {	
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void doDeleteMessage(List<MessageInfo> messages) {
		for (MessageInfo message : messages) {
			if (message.getRemovable())
				jmsReceiver.consume(message.getClientID(), message.getTransID());
		}
	}

}
