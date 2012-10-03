package com.cesaco.mobias.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.cesaco.mobias.model.JAAS_User;

@RequestScoped
public class UsersListProducer {

	@Inject
	   private EntityManager em;

	   private List<JAAS_User> users;
	   
	   @Produces
	   @Named
	   public List<JAAS_User> getUsers() {
	      return users;
	   }
	   
	   public void onUserListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final JAAS_User user) {
		      retrieveAllUsersOrderedByName();
		   }
	   
	   @PostConstruct
	   public void retrieveAllUsersOrderedByName() {
	      CriteriaBuilder cb = em.getCriteriaBuilder();
	      CriteriaQuery<JAAS_User> criteria = cb.createQuery(JAAS_User.class);
	      Root<JAAS_User> user = criteria.from(JAAS_User.class);
	      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
	      // feature in JPA 2.0
	      // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
	      criteria.select(user).orderBy(cb.asc(user.get("username")));
	      users = em.createQuery(criteria).getResultList();
	   }

}
