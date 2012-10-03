package com.cesaco.mobias.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.cesaco.mobias.model.JAAS_User;

@Stateful
@Model
public class UserRegistration {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<JAAS_User> userEventSrc;

	private JAAS_User newUser;

	@Produces
	@Named
	public JAAS_User getNewUser() {
		return newUser;
	}

	public void register() throws Exception {
		newUser.setPassword(getMD5base64(newUser.getPassword()));
		em.persist(newUser);
		userEventSrc.fire(newUser);
		initNewUser();
	}

	@PostConstruct
	public void initNewUser() {
		newUser = new JAAS_User();
	}

	public static String getMD5base64(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			byte[] enc = md.digest();
			String md5Sum = new sun.misc.BASE64Encoder().encode(enc);
			return md5Sum;
		} catch (NoSuchAlgorithmException nsae) {
			System.out.println(nsae.getMessage());
			return null;
		}
	}
}
