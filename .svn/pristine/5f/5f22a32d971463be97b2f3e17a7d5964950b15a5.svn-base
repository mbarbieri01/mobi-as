package com.cesaco.mobias.util.jms;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.cesaco.mobias.controller.JMScontroller;

@Stateless
@Named("AsynchReceiver")
public class AsynchReceiverBean implements AsynchReceiver {

	private Connection connection;
	private QueueConnectionFactory cf;
	private QueueSession session;

	// Queue consumerQueue;
	private Queue queue;

	private MessageConsumer consumer;
	private MessageProducer producer;

	private InitialContext ic = null;
	private QueueConnection jmsConnection = null;

	private MessageConsumer messageConsumer;
	
	private String msg;

	private int i = 0;

	public String getMessage(int client_id, long transID, JMScontroller jmsController) {
		System.out.println("AsynchReceiver.getMessage");
		System.out.println("startConnection Receiver");
		try {
			// Step 1. Lookup the initial context
			ic = new InitialContext();

			// JMS operations

			// Step 2. Look up the XA Connection Factory
			cf = (QueueConnectionFactory) ic.lookup("java:/ConnectionFactory");

			// Step 3. Look up the Queue
			queue = (Queue) ic.lookup("java:/fileStatus");

			// Step 4. Create a connection, a session and a message producer for
			// the queue
			jmsConnection = cf.createQueueConnection();
			session = jmsConnection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);
			
			TextMessage txtmsg = null;

			System.out.println("MyMessageListener");
			QueueReceiver receiver = null;
			try {
				jmsConnection.start();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				
				receiver = session.createReceiver(queue, "client_id = ".concat(Integer.toString(client_id))
						+" AND transID = ".concat(Long.toString(transID)));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				//receiver.setMessageListener(textMessageListener);
				System.out.println(receiver.getMessageSelector());
				System.out.println("queueName: "+receiver.getQueue().getQueueName());
				Message msg = receiver.receive();
				int resp_client_id = msg.getIntProperty("client_id");
				long resp_transID = msg.getLongProperty("transID");
				txtmsg = (TextMessage) msg;
					
				jmsController.updateTransStatus(resp_client_id, resp_transID, txtmsg.getText());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
			try {
				System.out.println("from MyMessageListener: "+ txtmsg.getText());
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			try {
				msg = txtmsg.getText();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

		} catch (NamingException ne) {
			System.out.println(ne.toString());
		} catch (JMSException je) {
			System.out.println(je.toString());
		}
		
		System.out.println("MyMessageListener");
		
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
		return msg;
	}
	

}
