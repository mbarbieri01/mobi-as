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

import com.cesaco.mobias.model.Company;


@RequestScoped
public class CompanyListProducer {
	@Inject
	   private EntityManager em;

	   private List<Company> companies;

	   // @Named provides access the return value via the EL variable name "members" in the UI (e.g.,
	   // Facelets or JSP view)
	   @Produces
	   @Named
	   public List<Company> getCompanies() {
	      return companies;
	   }

	   public void onCompanyListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Company company) {
	      retrieveAllCompaniesOrderedByName();
	   }

	   @PostConstruct
	   public void retrieveAllCompaniesOrderedByName() {
	      CriteriaBuilder cb = em.getCriteriaBuilder();
	      CriteriaQuery<Company> criteria = cb.createQuery(Company.class);
	      Root<Company> company = criteria.from(Company.class);
	      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
	      // feature in JPA 2.0
	      // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
	      criteria.select(company).orderBy(cb.asc(company.get("name")));
	      companies = em.createQuery(criteria).getResultList();
	   }
}
