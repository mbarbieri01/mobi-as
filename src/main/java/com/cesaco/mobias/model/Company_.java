package com.cesaco.mobias.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Company.class)
public class Company_ {
  
  public static volatile SingularAttribute<Company, Integer> id;
  public static volatile SingularAttribute<Company, Integer> clientID;
  public static volatile SingularAttribute<Company, String> name;
  public static volatile SingularAttribute<Company, String> aziDesc;
  public static volatile SingularAttribute<Company, Integer> nRequest;
}
