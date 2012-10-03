package com.cesaco.mobias.model;

import java.io.Serializable;
import java.lang.String;

import javax.annotation.ManagedBean;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: Company
 *
 */
@Entity
public class Company implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	protected String name;  
	
	@Id
	@GeneratedValue
	protected int id;
	
	@NotNull
	protected int clientID;
	
	@NotNull
	protected int nRequest;
	
	@NotNull
	protected String aziDesc;
	
	

	 
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public int getClientID() {
		return this.clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}   
	public int getNRequest() {
		return this.nRequest;
	}

	public void setNRequest(int nRequest) {
		this.nRequest = nRequest;
	}   
	public String getAziDesc() {
		return this.aziDesc;
	}

	public void setAziDesc(String aziDesc) {
		this.aziDesc = aziDesc;
	}
   
}
