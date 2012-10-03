package com.cesaco.mobias.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.cesaco.mobias.model.EntityProperty;
import com.cesaco.mobias.model.Flow2;


public class FlowManager2 {

	@Inject
	private EntityManager em;

	@Inject
	private Event<Flow2> memberEventSrc;

	private Flow2 newFlow;

	private List<EntityProperty> params;

	private List<EntityProperty> columns;

	private List<Flow2> flows;

	@Produces
	@Named
	public List<Flow2> getFlows() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flow2> criteria = cb.createQuery(Flow2.class);
		Root<Flow2> flow = criteria.from(Flow2.class);
		// Swap criteria statements if you would like to try out type-safe
		// criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
		criteria.select(flow).orderBy(cb.asc(flow.get("id")));
		flows = em.createQuery(criteria).getResultList();

		return flows;
	}

	@Produces
	@Named
	public List<EntityProperty> getParams() {
		return params;
	}

	@Produces
	@Named
	public List<EntityProperty> getColumns() {
		return columns;
	}

	@Produces
	@Named
	public Flow2 getNewFlow() {
		return newFlow;
	}

	public void register() throws Exception {

		em.persist(newFlow);
		memberEventSrc.fire(newFlow);
		initNewFlow();
	}

	@PostConstruct
	public void initNewFlow() {
		newFlow = new Flow2();
	}

	public String getAssociatedQuery(Integer flowID, Integer clientID) {
		// TODO Auto-generated method stub
		return "";
	}
	
	
}
