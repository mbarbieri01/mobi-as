package com.cesaco.mobias.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.cesaco.business.*;

@XmlRootElement(name = "FlowStore")
public class FlowStore {
	

	// XmlElement sets the name of the entities	
//	private ArrayList<com.cesaco.business.Flow> flowList;
//	
//	private String name;
//
//	public void setFlowList(ArrayList<com.cesaco.business.Flow> bookList) {
//		this.flowList = bookList;
//	}
//
//	@XmlElementWrapper(name = "flowList")
//	@XmlElement(name = "flow")
//	public ArrayList<com.cesaco.business.Flow> getFlowList() {
//		return flowList;
//	}
//	
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
}
