package com.cesaco.mobias.util.jms;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/*import org.hornetq.api.core.TransportConfiguration;
 import org.hornetq.api.jms.HornetQJMSClient;
 import org.hornetq.api.jms.JMSFactoryType;
 import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
 import org.hornetq.jms.client.HornetQConnectionFactory;
 */

@Stateful
@Named("AsynchSender")
@ApplicationScoped
public class AsynchSenderBean implements AsynchSender {

	private Connection connection;
	private ConnectionFactory cf;
	private Session session;

	// Queue consumerQueue;
	private Queue queue;



	private MessageConsumer consumer;
	private MessageProducer producer;



	private InitialContext ic = null;
	private Connection jmsConnection = null;

	private MessageProducer messageProducer;

	private int i = 0;

	@PostConstruct
	public void startConnection() {

		System.out.println("startConnection");
		try {
			// Step 1. Lookup the initial context
			ic = new InitialContext();

			// JMS operations

			// Step 2. Look up the XA Connection Factory
			cf = (ConnectionFactory) ic.lookup("java:/ConnectionFactory");

			// Step 3. Look up the Queue
			queue = (Queue) ic.lookup("java:/request");

			// Step 4. Create a connection, a session and a message producer for
			// the queue
			jmsConnection = cf.createConnection();
			session = jmsConnection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			messageProducer = session.createProducer(queue);
		} catch (NamingException ne) {
			System.out.println(ne.toString());
		} catch (JMSException je) {
			System.out.println(je.toString());
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

	public Boolean sendMessage(String msg, String filename, int client_id, long transID) {

		Boolean result = false;
		try {
			System.out.println("AsynchSender: " + msg + ". Number " + i);
			// Step 5. Create a Text Message
			TextMessage message = session.createTextMessage(msg);
			message.setIntProperty("clientID", client_id);
			message.setLongProperty("transID", transID);
			message.setStringProperty("filename", filename);
			message.setStringProperty("messageType", "request");
			// Step 6. Send The Text Message
			messageProducer.send(message);
			// System.out.println("Sent message: " + message.getText() + "(" +
			// message.getJMSMessageID() + ")");
			System.out.println("Messaggio generato " + i);
			i++;
			result = true;
		} catch (JMSException e) {
			System.out.println("Exception occurred: " + e.toString());
		} catch (NullPointerException npe) {
			System.out.println("from send: "+npe.toString());

		}
		return result;

	}
}
