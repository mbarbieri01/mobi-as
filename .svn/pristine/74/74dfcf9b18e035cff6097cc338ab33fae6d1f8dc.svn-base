package com.cesaco.mobias.ws;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.cesaco.mobias.controller.Controller;


@WebService()
public class MobiasWS {

	private final String AWS_ACCESS_KEY = "AKIAIB6Y562DDWPXKFVQ";
	private final String AWS_SECRET_KEY = "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";
	
	@Inject
	private Controller controller;

	@WebMethod()
	public String getRequest(int clientID, long transID) {
		return controller.getRequest(clientID, transID);
	}
	
	@WebMethod()
	public String getFileName(int clientID, long transID, int size) {
		return controller.getFilename(clientID, transID, size);
	}
	
	@WebMethod()
	public void notifyCompleted(int clientID, long transID, String status) {
		controller.notifyCompleted(clientID, transID, status);
	}
	
	@WebMethod()
	public String getUpdate() {
		//TODO
		return "Not yet implemented.";
	}
	
	@WebMethod()
	public void notifyUpdated(String status) {
		//TODO
	}
	
	@WebMethod()
	public int getClientID(String aziName) {
		return controller.getClientIDbyAzi(aziName);
	}
	
	@WebMethod()
	public String getPublicRSAexp(int clientID) {
		//return jmsc.getPublicRSAexp(clientID);
		return "";
	}
	
	@WebMethod()
	public String getPublicRSAmod(int clientID) {
		return "";
	}
	
	@WebMethod()
	public void uploadEncryptedAESkey(int clientID, long transID, byte[] eKey) {
		controller.uploadEncAESkey(clientID, transID, eKey);
	}
	
	@WebMethod()
	public String uploadData(byte[] data) {
		return "";
	}
	
	@WebMethod()
	public String uploadEncryptedDataAES(int clientID, long transID, byte[] eData) {
		return controller.uploadData(clientID, transID, eData);
	}
	
	@WebMethod()
	public byte[] getAWSAccessKey() {
		try {
			return controller.encryptAESdefault(AWS_ACCESS_KEY.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@WebMethod()
	public byte[] getAWSSecretKey() {
		try {
			return controller.encryptAESdefault(AWS_SECRET_KEY.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
