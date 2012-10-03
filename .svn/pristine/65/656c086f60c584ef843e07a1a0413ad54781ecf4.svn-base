package com.cesaco.mobias.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Entity implementation class for Entity: JAAS_Roles
 *
 */
@Entity


public class JAAS_Role implements Serializable {

	   
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	private String username;   
	
	private String roles;
	private String role_group;
	private static final long serialVersionUID = 1L;
	

	
	public JAAS_Role() {
		super();
	}   
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="id")
	private int id;
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}   
	public String getRoles() {
		return this.roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}   
	public String getRole_group() {
		return this.role_group;
	}

	public void setRole_group(String role_group) {
		this.role_group = role_group;
	}
   
}
