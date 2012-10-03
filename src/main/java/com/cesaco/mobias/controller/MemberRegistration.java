package com.cesaco.mobias.controller;

import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.cesaco.mobias.model.Member;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a
// request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class MemberRegistration {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Member> memberEventSrc;

	private Member newMember;

	@Produces
	@Named
	public Member getNewMember() {
		return newMember;
	}

	public void register() throws Exception {
		newMember.setPasskey(UUID.randomUUID());
		newMember.setPasswordHash(newMember.getPassword().hashCode());
		System.out.println("hascode: " + newMember.getPasswordHash());
		log.info("Registering " + newMember.getName());
		em.persist(newMember);
		memberEventSrc.fire(newMember);
		initNewMember();
	}
	
	public void testt() {
		System.out.println("funziona");
	}

	@PostConstruct
	public void initNewMember() {
		newMember = new Member();
	}
}
