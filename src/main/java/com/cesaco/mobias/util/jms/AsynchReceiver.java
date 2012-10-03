package com.cesaco.mobias.util.jms;

import com.cesaco.mobias.controller.JMScontroller;

public interface AsynchReceiver {
	
	public String getMessage(int client_id, long transID, JMScontroller jmsController);
	
}
