package com.cesaco.mobias.util.jms;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;





@Stateless
@Named("MyMessageListener")

public class MyMessageListenerBean implements MyMessageListener {


	private String msg = "error";

	public String listen(QueueSession session, Queue consumerQueue, String selector) {
		

		System.out.println("selector: "+selector);
		final Monitor monitor = new Monitor();
		
		//TextMessageListener textMessageListener = new TextMessageListener(this);
		TextMessage txtmsg = null;

			System.out.println("MyMessageListener");
			QueueReceiver receiver = null;
			try {
				
				receiver = session.createReceiver(consumerQueue, selector);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				//receiver.setMessageListener(textMessageListener);
				System.out.println(receiver.getMessageSelector());
				System.out.println("queueName: "+receiver.getQueue().getQueueName());
				txtmsg = (TextMessage) receiver.receive();
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
		
			return msg;

	}
	
	public void setMessage(String msg) {
		this.msg = msg;
	}

	

}
