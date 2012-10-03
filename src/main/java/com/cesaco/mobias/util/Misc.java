package com.cesaco.mobias.util;

import javax.ejb.Stateless;

@Stateless
public class Misc {

	public static String IndexFormatter(int clientID, long transID) {
		return clientID+":"+transID;
	}
	
}
