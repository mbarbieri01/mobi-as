package com.cesaco.mobias.util.jms;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;



public class TextMessageListener implements MessageListener {

	private MessageProducer producer;

	

	private Session session;
	private int client_id;
	private Random random;
	private MyMessageListener mml;
	private Monitor monitor;

	public TextMessageListener(MyMessageListener mml) {
		super();

		this.mml = mml;



	}

	@Override
	public void onMessage(Message message) {
		try {

			TextMessage textMessage = (TextMessage) message;

			mml.setMessage(textMessage.toString());

			System.out.println("ho ricevuto il messaggio: "+textMessage.getText());


		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		monitor.allDone();
	}
	
	

}
