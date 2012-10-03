package com.cesaco.mobias.queue;

import javax.inject.Named;

@Named
public class MessageInfo {
	
	private Long transID;
	private String messageType;
	private Integer clientID;
	private String dateCreated;
	private String id;
	private Boolean removable;
	
	public Boolean getRemovable() {
		return removable;
	}

	public void setRemovable(Boolean removable) {
		this.removable = removable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public MessageInfo(Long transID, String messageType, Integer clientID, String dateCreated, String id) {
		this.transID = transID;
		this.messageType = messageType;
		this.clientID = clientID;
		this.dateCreated = dateCreated;
		this.id = id;
	}

	public Long getTransID() {
		return transID;
	}

	public void setTransID(Long transID) {
		this.transID = transID;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Integer getClientID() {
		return clientID;
	}

	public void setClientID(Integer clientID) {
		this.clientID = clientID;
	}


}
