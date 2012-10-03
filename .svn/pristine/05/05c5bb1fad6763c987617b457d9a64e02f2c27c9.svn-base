package com.cesaco.mobias.queue;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;


@Stateful
@ApplicationScoped
public class JMSSender {
	
	private final String JNDI_QUEUE_NAME = "java:/request"; 
	private final long MESSAGE_TTL = 30000;

	private ConnectionFactory cf;
	private Session session;

	private Queue queue;

	private InitialContext ic = null;
	private Connection jmsConnection = null;

	private MessageProducer messageProducer;

	@PostConstruct
	public void startConnection() {

		//System.out.println("startConnection");
		try {
			// Step 1. Lookup the initial context
			ic = new InitialContext();

			// JMS operations

			// Step 2. Look up the XA Connection Factory
			cf = (ConnectionFactory) ic.lookup("java:/ConnectionFactory");

			// Step 3. Look up the Queue
			queue = (Queue) ic.lookup(JNDI_QUEUE_NAME);

			// Step 4. Create a connection, a session and a message producer for
			// the queue
			jmsConnection = cf.createConnection();
			session = jmsConnection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			messageProducer = session.createProducer(queue);
			messageProducer.setTimeToLive(MESSAGE_TTL);
		} catch (NamingException ne) {
			System.out.println(ne.toString());
		} catch (JMSException je) {
			System.out.println(je.toString());
		}

	}
	
	public QueueBrowser getQueueBrowser() {
		try {
			return session.createBrowser(queue);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

	@PreDestroy
	public void closeConnection() {
		try {
			// Step 12. Be sure to close all resources!
			if (ic != null) {
				try {
					ic.close();
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (jmsConnection != null) {
				try {
					jmsConnection.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (NullPointerException npe) {
			System.out.println("from close: " + npe.toString());
		}
	}

	public Boolean sendRequestMessage(int clientID, long transID) {

		Boolean result = false;
		try {
			System.out.println("JMSSender, transID =  " + transID + "...");
			// Step 5. Create a Text Message
			BytesMessage message = session.createBytesMessage();
			message.setIntProperty("clientID", clientID);
			message.setLongProperty("transID", transID);
			message.setStringProperty("messageType", "request");
			message.setStringProperty("dateCreated", new Date().toString());
			//message.setJMSExpiration(MESSAGE_TTL);
			
			// Step 6. Send The Text Message
			messageProducer.send(message);

			System.out.println("--Messaggio generato.");
			result = true;
		} catch (JMSException e) {
			System.out.println("Exception occurred: " + e.toString());
		} catch (NullPointerException npe) {
			System.out.println("from send: "+npe.toString());
		}
		return result;
	}
}
