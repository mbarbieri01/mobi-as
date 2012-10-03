package com.cesaco.mobias.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Flow2 implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	
	@NotNull
	@Size(min = 1, max = 25)
	private String name;
	
	private String type;
	
	private String date;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<EntityProperty> params;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<EntityProperty> columns;
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@XmlElement(name = "params")
	public List<EntityProperty> getParams() {
		return params;
	}

	public void setParams(List<EntityProperty> params) {
		this.params = params;
	}

	public List<EntityProperty> getColumns() {
		return columns;
	}

	public void setColumns(List<EntityProperty> columns) {
		this.columns = columns;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
