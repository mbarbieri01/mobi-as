package com.cesaco.mobias.rest;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.cesaco.business.Bean30Remote;
import com.cesaco.mobias.controller.Controller;
import com.cesaco.mobias.controller.JMScontroller;
import com.cesaco.mobias.model.Member;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@Path("/members")
@RequestScoped
public class MemberResourceRESTService {
	@Inject
	private EntityManager em;

	@Inject
	private JMScontroller jmsc;

	@Inject
	private Controller controller;

	@GET
	@Produces("text/xml")
	public List<Member> listAllMembers() {
		// Use @SupressWarnings to force IDE to ignore warnings about
		// "genericizing" the results of
		// this query
		@SuppressWarnings("unchecked")
		// We recommend centralizing inline queries such as this one into
		// @NamedQuery annotations on
		// the @Entity class
		// as described in the named query blueprint:
		// https://blueprints.dev.java.net/bpcatalog/ee5/persistence/namedquery.html
		final List<Member> results = em.createQuery(
				"select m from Member m order by m.name").getResultList();
		return results;
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/xml")
	public Member lookupMemberById(@PathParam("id") long id) {
		return em.find(Member.class, id);
	}

	@GET
	@Path("/getPasskey/{username}/{passwordHash}")
	@Produces("text/plain")
	public String getPasskeyByCredentials(
			@PathParam("username") String username,
			@PathParam("passwordHash") String passwordHash) {

		UUID temp_uuid = null;
		String message = "";
		@SuppressWarnings("unchecked")
		final List<Member> candidates = em.createQuery(
				"select m from Member m where m.name='" + username
						+ "' and m.passwordHash='" + passwordHash + "'")
				.getResultList();
		if (candidates.isEmpty()) {
			// nessun utente trovato
			message = "errore";
		} else if (candidates.size() > 1) {
			// errore la lista deve contenere un solo elemento
			message = "errore";
		} else {
			temp_uuid = candidates.get(0).getPasskey();
		}
		message = temp_uuid.toString();

		return message;
	}

	@GET
	@Path("/testJMS/{client_id}/{mtext}")
	@Produces("text/xml")
	public String testJMS(@PathParam("client_id") int client_id,
			@PathParam("mtext") String mtext) {
		System.out.println("Rest HASH: " + this.hashCode());

		return jmsc.addRequest(client_id, mtext);

	}

	@GET
	@Path("/getMod")
	@Produces("text/plain")
	public String getMod() {

		return jmsc.getModulus();

	}

	@GET
	@Path("/testEnc/{pText}")
	@Produces("text/plain")
	public String testEnc(@PathParam("pText") String pText) {

		return jmsc.testEnc(pText);

	}

	@GET
	@Path("/flowRequest/{flowID}/{clientID}")
	@Produces("text/plain")
	public String flowRequest(@PathParam("flowID") String flowID,
			@PathParam("clientID") int clientID) {
		return "transID: " + controller.flowRequest(flowID, clientID);
	}

	@GET
	@Path("/getData/{clientID}/{transID}")
	@Produces("text/plain")
	public String getData(@PathParam("transID") int transID,
			@PathParam("clientID") int clientID) {
		return "data: " + controller.getData(transID, clientID);
	}

	

	
	@GET
	@Path("/testJNDI/{flowID}")
	@Produces("text/plain")
	public String testJNDI(@PathParam("flowID") String flowID) {
		InitialContext ctx;
		String s = "";
		//Pattern pattern = Pattern.compile("Bean30", Pattern.CASE_INSENSITIVE);
		try {
			ctx = new InitialContext();
	
			Context sContext = (Context) ctx.lookup("java:global/mobi-as-ejb");
			NamingEnumeration<Binding> list = sContext.listBindings("");
			while (list.hasMore()) {
				String temp = list.next().getName();
				if (temp.matches("(?i).*Bean30.*")) {
					s+=temp + "\n"; 
				} else {
					s+=temp + " scartato\n";
				}
			}
			Bean30Remote br;
			
			//br = (Bean30Remote) InitialContext.doLookup("java:global/mobi-as-ejb/Bean30!com.cesaco.business.Bean30Remote");
			br = (Bean30Remote) ctx.lookup("java:global/mobi-as-ejb/Bean30!com.cesaco.business.Bean30Remote");
			s += br.getMessage() + br.getCompanyname() + br.getAddress();
			
			s+="\n\n\n";
			s += br.execute(flowID, new String[0]);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;
	}
}
