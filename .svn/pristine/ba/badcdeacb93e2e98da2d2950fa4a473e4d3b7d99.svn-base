package com.cesaco.mobias.util.jms;

import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;

public interface MyMessageListener {
	public String listen(QueueSession session, Queue consumerQueue, String selector);
	public void setMessage(String msg);
}
