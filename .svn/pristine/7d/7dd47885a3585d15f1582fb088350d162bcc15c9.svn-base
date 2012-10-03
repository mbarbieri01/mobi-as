/*
 * 
 */
package com.cesaco.mobias.cquery;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class QParams.
 */
public class QParams {
	
	/** The flow name. */
	private String flowName;
	
	/** The flow id. */
	private int flowID;
	
	/** The qparams list. */
	private Map<String, String> qparamsList;

	/** The s. */
	private Set<Entry<String, String>> s;

	/** The it. */
	private Iterator<Entry<String, String>> it;
	
	
	/**
	 * Instantiates a new q params.
	 * 
	 */
	public QParams() {
		qparamsList = new HashMap<String, String>();
	}
	
	/**
	 * Adds a new parameter.
	 *
	 * @param name the parameter name
	 * @param value the parameter value
	 */
	public void addParam(String name, String value) {
		qparamsList.put(name, value);
	}
	
	/**
	 * Gets the parameter.
	 *
	 * @param name the parameter name
	 * @return the parameter value
	 */
	public String getParam(String name) {
		s = qparamsList.entrySet();
		it = s.iterator();
		Map.Entry<String,String> m;
		String value = "parameter not found";
		
		while(it.hasNext())
        {
            // key=value separator this by Map.Entry to get key and value
            m =(Map.Entry<String, String>)it.next();

            // getKey is used to get key of Map
            String key=m.getKey();
            
            if (key.equals(name)) {
            	// getValue is used to get value of key in Map
                value =m.getValue();
                break;
            }      
        }
		return value;
	}
	
	/**
	 * Sets the flow name and id.
	 *
	 * @param flowName the flow name
	 * @param flowID the flow id
	 */
	public void setFlowNameAndID(String flowName, int flowID) {
		this.flowName = flowName;
		this.flowID = flowID;
	}
	
	/**
	 * Gets the flow name.
	 *
	 * @return the flow name
	 */
	public String getFlowName() {
		return flowName;
	}
	
	/**
	 * Gets the flow id.
	 *
	 * @return the flow id
	 */
	public int getFlowID() {
		return flowID;
	}

}
