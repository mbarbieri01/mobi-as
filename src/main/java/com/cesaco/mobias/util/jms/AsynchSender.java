package com.cesaco.mobias.util.jms;

public interface AsynchSender {
	
	public Boolean sendMessage(String msg, String filename, int client_id, long transID);
	
}
