package com.cesaco.mobias.flow;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.cesaco.business.Bean30Remote;

@Stateful
@ApplicationScoped
public class FlowManager {

	//private Flow flowList;

	private InitialContext ctx;
	private Bean30Remote br;

	@PostConstruct
	public void setup() {

		try {
			ctx = new InitialContext();
			br = (Bean30Remote) ctx
					.lookup("java:global/mobi-as-ear/mobi-as-ejb/Bean30!com.cesaco.business.Bean30Remote");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getAssociateQuery(String response, String flowID) {

		if (br != null) {
			return br.getAssociatedQuery(flowID, response);
		} else {
			String[] s = new String[1];
			s[0] = "query di test";
			return s;
		}

	}
	
	public String[] getAssociateQuery(String flowID) {

		if (br != null) {
			return br.getAssociatedQuery(flowID);
		} else {
			String[] s = new String[1];
			s[0] = "query di test";
			return s;
		}

	}

	public String getAziQuery(String flowID) {
		if (br != null) {
			return br.getAziQuery(flowID);
		} else {
			return "empty";
		}
	}

}
