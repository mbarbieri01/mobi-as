package com.cesaco.mobias.controller;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "transID"))
public class Request implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	//private byte[] eKey;
	//private byte eData;
	
	@NotNull
	private int clientID;

	@NotNull
	private long transID;

	@NotNull
	private String query;
	
	private String filename;
	
	private byte[] AESpassword;
	
	@Lob
	private String data;
	
	private Boolean hasData;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
		hasData = false;
	}

	public long getTransID() {
		return transID;
	}

	public void setTransID(long transID) {
		this.transID = transID;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setKey(String pKey) {
	}

	public String getKey() {
		return null;
	}

	public void setData(String pData) {
		this.data = pData;
		hasData = true;
	}

	public String getData() {
		return data;
	}
	
	public Boolean hasData() {
		return hasData;
	}
	
	public byte[] getAESpassword() {
		return AESpassword;
	}

	public void setAESpassword(byte[] AESpassword) {
		this.AESpassword = AESpassword;
	}
	

}
