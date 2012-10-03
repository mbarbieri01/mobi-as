package com.cesaco.mobias.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.cesaco.mobias.controller.Request;

@Stateful
@ApplicationScoped
public class TransManager {
	
	private Map<Long, String> statusMap;
	private long i;
	private Iterator it;
	private Set<Entry<Long, String>> s;
	
	@Inject
	private EntityManager em;
	
	@PostConstruct
	public void initializeArray() {
		//transID, filename
		statusMap = new HashMap<Long, String>();
		
		i = getHighestTransID() + 1;
	}
	
	public long addTrans(int clientID) {
		//tenere conto del clientID
		//TODO
		long transID = generateTransID();
		//empty, filename, 2 = error
		statusMap.put(transID, "empty");
		return transID;
	}

	private long generateTransID() {
		return i++;
	}

	public String getStatus(String transID) {
		s = statusMap.entrySet();
		it = s.iterator();
		Map.Entry<Long,String> m;
		String status = "transID not found";
		Long tID = Long.parseLong(transID);
		
		while(it.hasNext())
        {
            // key=value separator this by Map.Entry to get key and value
            m =(Map.Entry<Long, String>)it.next();

            // getKey is used to get key of Map
            Long key=m.getKey();
            
            if (key == tID) {
            	// getValue is used to get value of key in Map
                status =m.getValue();
                break;
            }      
        }
		return status;
	}

	public void update(int client_id, long transID, String opStatus) {
		statusMap.put(transID, opStatus);	
	}
	
	private long getHighestTransID() {
		long ht = -1;
		@SuppressWarnings("unchecked")
		final List<Long> candidates = em.createQuery(
				"select MAX(m.transID) " + "from Request m").getResultList();
		try {
		if (!candidates.isEmpty()) {
			ht = candidates.get(0);
		}
		} catch(Exception e) {
			
		}
		return ht;
	}

}

