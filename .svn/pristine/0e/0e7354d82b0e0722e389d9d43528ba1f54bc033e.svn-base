package com.cesaco.mobias.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import com.cesaco.business.Bean30Remote;
import com.cesaco.jexcel.XlsBeanRemote;
import com.cesaco.mobias.flow.FlowManager;
import com.cesaco.mobias.model.Company;
import com.cesaco.mobias.queue.QueueManager;
import com.cesaco.mobias.security.SecurityManager;
import com.cesaco.mobias.storage.S3Manager;
import com.cesaco.mobias.util.Misc;
import com.cesaco.mobias.util.TransManager;

@Stateful
@ApplicationScoped
@Named("Controller")
@LocalBean
public class Controller {

	private final String STATUS_DONE = "done";
	// sizeData, KB
	private final int THRESHOLD = 500;
	private final char S3CHOICE = 0;
	private final char WSCHOICE = 1;
	private final String WS_FILENAME_RESPONSE = "ws";
	private final int MAX_TEMP_REQUESTLIST_SIZE = 100;

	private InitialContext ctx;

	@Inject
	private S3Manager s3Manager;
	@Inject
	private FlowManager flowManager;
	@Inject
	private TransManager transManager;
	@Inject
	private QueueManager queueManager;
	@Inject
	private SecurityManager securityManager;

	private Map<String, Request> requestList;
	private Map<String, DataObject> dataTempList;

	private Bean30Remote br;

	@Inject
	private EntityManager em;

	@PostConstruct
	public void initialize() {
		reqID = 0;
		requestList = new HashMap<String, Request>();
		reqList = new HashMap<Long, RInfo>();
		dataTempList = new HashMap<String, DataObject>();
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			// br = (Bean30Remote)
			// InitialContext.doLookup("java:global/mobi-as-ejb/Bean30!com.cesaco.business.Bean30Remote");
			br = (Bean30Remote) ctx
					.lookup("java:global/mobi-as-ear/mobi-as-ejb/Bean30!com.cesaco.business.Bean30Remote");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		flowResponses = new HashMap<String, String>();
	}

	public long[] flowRequest(String flowID, int clientID) {
		// ottengo la stringa di query associata al flusso
		String[] queries = flowManager.getAssociateQuery(flowID);
		int nqueries = queries.length;
		long[] transs = new long[nqueries];
		for (int i = 0; i < nqueries; i++) {
			// genero un nuovo numero di transazione associato alla richiesta
			transs[i] = transManager.addTrans(clientID);
			// preparo una nuova richiesta
			Request newRequest = new Request();

			// imposto i campi della nuova richiesta
			newRequest.setClientID(clientID);
			newRequest.setTransID(transs[i]);
			newRequest.setQuery(queries[i]);

			// persisto la nuova richiesta
			persistRequest(newRequest);
			incrementRequestCount(clientID);

			// invio il messaggio al server extractor interessato
			queueManager.sendRequestMessage(clientID, transs[i]);
		}

		// avviso il client che la richiesta sarà preparata sul transID
		return transs;
	}

	public long[] flowRequest(String response, String flowID, int clientID) {
		// ottengo la stringa di query associata al flusso

		String[] queries = flowManager.getAssociateQuery(response, flowID);
		if (queries != null) {
			int nqueries = queries.length;
			long[] transs = new long[nqueries];
			for (int i = 0; i < nqueries; i++) {
				// genero un nuovo numero di transazione associato alla
				// richiesta
				transs[i] = transManager.addTrans(clientID);
				// preparo una nuova richiesta
				Request newRequest = new Request();

				// imposto i campi della nuova richiesta
				newRequest.setClientID(clientID);
				newRequest.setTransID(transs[i]);
				newRequest.setQuery(queries[i]);

				// persisto la nuova richiesta
				persistRequest(newRequest);
				incrementRequestCount(clientID);

				// invio il messaggio al server extractor interessato
				queueManager.sendRequestMessage(clientID, transs[i]);
			}

			// avviso il client che la richiesta sarà preparata sul transID
			return transs;

		} else {
			return null;
		}
	}

	private void incrementRequestCount(int clientID) {

		@SuppressWarnings("unchecked")
		final List<Company> candidates = em.createQuery(
				"select m " + "from Company m " + "where m.clientID='"
						+ clientID + "'").getResultList();
		if (candidates.isEmpty()) {

		} else if (candidates.size() > 1) {

		} else {
			Company company = candidates.get(0);
			company.setNRequest(company.getNRequest() + 1);
			em.merge(company);
		}
	}

	private void persistRequest(Request request) {
		em.persist(request);
	}

	public String getRequest(int clientID, long transID) {
		// return associated query
		String result = "";

		Request request = searchRequest(clientID, transID);
		if (request != null) {
			result = request.getQuery();
		}

		// aggiungo la richiesta alla lista dei temporanei, se c'è posto
		if (requestList.size() < MAX_TEMP_REQUESTLIST_SIZE - 1) {
			requestList.put(Misc.IndexFormatter(clientID, transID), request);
		}
		return result;
	}

	public String getFilename(int clientID, long transID, int size) {
		String filename = "";
		char response;
		response = valuateSize(size);
		switch (response) {
		case S3CHOICE:
			filename = s3Manager.getFilename(clientID, transID);
			break;
		case WSCHOICE:
			filename = WS_FILENAME_RESPONSE;
			break;
		default:
			filename = WS_FILENAME_RESPONSE;
			break;
		}

		Request request = searchRequest(clientID, transID);
		request.setFilename(filename);

		// aggiorno la richiesta
		updateRequest(request);

		return filename;
	}

	private Request searchRequest(int clientID, long transID) {
		Request request = null;
		String message = "";

		// controllo se è presente nei temporanei
		if (requestList.containsKey((Misc.IndexFormatter(clientID, transID)))) {
			request = requestList.get(Misc.IndexFormatter(clientID, transID));
		} else {

			@SuppressWarnings("unchecked")
			final List<Request> candidates = em.createQuery(
					"select m " + "from Request m " + "where m.transID='"
							+ transID + "' " + "and m.clientID='" + clientID
							+ "'").getResultList();
			if (candidates.isEmpty()) {
				// nessun utente trovato
				message = "errore: Request with clientID " + clientID
						+ " and transID " + transID + " non trovata.";
				System.out.println(message);
			} else if (candidates.size() > 1) {
				// errore la lista deve contenere un solo elemento
				message = "errore: Request with clientID " + clientID
						+ " and transID " + transID + " ha troppi candidati.";
				System.out.println(message);
			} else {
				request = candidates.get(0);
			}
		}

		return request;
	}

	private char valuateSize(int size) {
		char temp;
		if (size > THRESHOLD) {
			temp = S3CHOICE;
		} else {
			temp = WSCHOICE;
		}

		return temp;
	}

	private void updateRequest(Request request) {
		em.merge(request);
	}

	public void uploadEncAESkey(int clientID, long transID, byte[] eKey) {
		byte[] AESpassword = securityManager.RSAdecrypt(eKey);
		Request request = searchRequest(clientID, transID);
		request.setAESpassword(AESpassword);
		updateRequest(request);
	}

	public String uploadData(int clientID, long transID, byte[] eData) {
		dataTempList.put(Misc.IndexFormatter(clientID, transID),
				new DataObject(eData));

		return STATUS_DONE;
	}

	public void notifyCompleted(Integer clientID, long transID, String status) {
		if (status.equals(STATUS_DONE)) {
			if (dataTempList
					.containsKey(Misc.IndexFormatter(clientID, transID))) {
				byte[] eData = dataTempList.get(
						Misc.IndexFormatter(clientID, transID)).getData();
				Request request = searchRequest(clientID, transID);
				request.setData(securityManager.getPlainData(clientID, transID,
						eData, request.getAESpassword()));
				updateRequest(request);

				// tolgo la richiesta dai temporanei
				requestList.remove(Misc.IndexFormatter(clientID, transID));
				// tolgo i dati temporanei dalla lista
				dataTempList.remove(Misc.IndexFormatter(clientID, transID));
			}
		}
	}

	public byte[] encryptAESdefault(byte[] pData) {
		return securityManager.encryptAESdefault(pData);
	}

	public String getData(long transID, int clientID) {
		// System.out.println("asking data for transID: " + transID);
		Request request = searchRequest(clientID, transID);
		String data = "";
		if (request != null) {
			// System.out.println("req: " + request.getTransID());
			// System.out.println("hasData: " + request.hasData());
			data = request.getData();
			if (data != null) {

				removeRequest(clientID, transID, request);
			}
		}
		return data;
	}

	private void removeRequest(int clientID, long transID, Request request) {
		dataTempList.remove(Misc.IndexFormatter(clientID, transID));
		em.remove(request);
	}

	private class DataObject {
		private byte[] data;

		public DataObject(byte[] data) {
			this.data = data;
		}

		public byte[] getData() {
			return data;
		}
	}

	
	public String getFlowList(String clientID) {
		// int local_clientID = parseID(clientID);

		return generateFlowListXML(clientID);
		// return "<flow><id = 0001><desc = base desc></flow>";
	}

	private String generateFlowListXML(String clientID) {
		String flowList = "";

		String s = "";
		// Pattern pattern = Pattern.compile("Bean30",

		flowList = br.getFlowList();

		return flowList;
	}

	private int parseID(String clientID) {
		return Integer.parseInt(clientID);
	}

	public String getFlowData(int clientID, String flowID, long[] transs) {
		String t = null;

		String[] qData = null;
		qData = popolate(transs, clientID);
		t = br.execute(flowID, qData);

		return t;
	}

	private Map<String, String> flowResponses;

	public void executeFlowData(int clientID, String flowID, long[] transs,
			String reqID) {
		String t = null;

		String[] qData = null;
		qData = popolate(transs, clientID);
		flowResponses.put(reqID, br.execute(flowID, qData));

	}

	public String getFlowResponse(String reqID) {
		RInfo rInfo = reqList.get(Long.parseLong(reqID));
		if (rInfo != null) {
			if (hasFlowData(rInfo.getClientID(), rInfo.getTranss())) {
				executeFlowData(rInfo.getClientID(), rInfo.getFlowID(),
						rInfo.getTranss(), reqID);

				String temp = flowResponses.get(reqID);
				flowResponses.remove(reqID);
				reqList.remove(Long.parseLong(reqID));
				return temp;
			} else {
				return "empty";
			}
		} else {
			return "removed";
		}
	}

	public String getPlainFlowData(int clientID, String flowID, long[] transs) {
		String result = "";
		String[] qData = null;
		qData = popolate(transs, clientID);
		for (int i = 0; i < qData.length; i++) {
			result += qData[i] + "\n\n\n";
		}
		return result;
	}

	public long[] startFlowRequest(int clientID, String flowID, String daData,
			String aData) {
		long[] transs = null;

		transs = flowRequest(flowID, clientID);

		return transs;
	}

	public long[] startFlowRequest(String response, int clientID,
			String flowID, String daData, String aData) {
		long[] transs = null;

		transs = flowRequest(response, flowID, clientID);

		return transs;
	}

	public Boolean hasFlowData(int clientID, long[] transs) {

		Boolean done = false;
		int size = transs.length;
		done = false;
		Request r;
		for (int i = 0; i < size; i++) {
			r = searchRequest(clientID, transs[i]);
			if (!r.hasData()) {
				done = false;
				break;
			} else {
				done = true;
			}
		}

		return done;
	}

	private String[] popolate(long[] transs, int clientID) {
		int size = transs.length;
		String[] qData = new String[size];
		for (int i = 0; i < size; i++) {
			qData[i] = getData(transs[i], clientID);
		}
		return qData;
	}

	public String postXML(String xml) {
		return br.postXML(xml);
	}

	public int getClientIDbyAzi(String aziName) {
		String message = "";
		Company company = null;
		@SuppressWarnings("unchecked")
		final List<Company> candidates = em.createQuery(
				"select m " + "from Company m " + "where m.name='" + aziName
						+ "'").getResultList();
		if (candidates.isEmpty()) {
			company = new Company();
			company.setName(aziName);
			company.setNRequest(0);
			company.setAziDesc("default");
			int max = getMaxClientID();
			company.setClientID(max + 1);
			em.persist(company);
		} else if (candidates.size() > 1) {
			// errore la lista deve contenere un solo elemento
			message = "errore: troppi candidati.";
			System.out.println(message);
		} else {
			company = candidates.get(0);
		}
		return company.getClientID();
	}

	private int getMaxClientID() {

		Integer max = (Integer) em.createQuery(
				"select max(clientID) from Company m").getSingleResult();
		return (max != null ? (int) max : -1);
	}

	public String getListAzi(long trans, int clientID) {
		String data = getData(trans, clientID);
		return getAziListXML(data);
	}

	private String[] parseResult(String result) {
		return result.split(", ");
	}

	private String[] splitRows(String result) {
		return result.split("\n");
	}

	private String getAziListXML(String qData) {
		// System.out.println("from getAziListXML \n"+qData);
		String[] rows = splitRows(qData);
		String result = "";
		for (int j = 0; j < rows.length; j++) {
			String[] splitted = parseResult(rows[j]);
			result += splitted[0] + "\n";
		}
		return result;
	}

	public long startAziListRequest(int clientID, String flowID) {
		long trans = -1;

		trans = addAziListRequest(clientID, flowID);

		return trans;
	}

	private long addAziListRequest(int clientID, String flowID) {
		String aziQuery = flowManager.getAziQuery(flowID);
		long trans = -1;

		trans = transManager.addTrans(clientID);
		// preparo una nuova richiesta
		Request newRequest = new Request();

		// imposto i campi della nuova richiesta
		newRequest.setClientID(clientID);
		newRequest.setTransID(trans);
		newRequest.setQuery(aziQuery);

		// persisto la nuova richiesta
		persistRequest(newRequest);
		incrementRequestCount(clientID);

		// invio il messaggio al server extractor interessato
		queueManager.sendRequestMessage(clientID, trans);
		return trans;

	}

	public String openExcel(String filename) {
		try {

			// br = (Bean30Remote)
			// InitialContext.doLookup("java:global/mobi-as-ejb/Bean30!com.cesaco.business.Bean30Remote");
			XlsBeanRemote xlsBean = (XlsBeanRemote) ctx
					.lookup("java:app/mobi-as-excel-ejb/XlsBean!com.cesaco.jexcel.XlsBeanRemote");
			return xlsBean.openExcel(filename);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}

	}

	public String getFlowInfo(String flowID) {
		if (br != null) {

			return br.getFlowInfo(flowID);
		} else {
			return "error";
		}
	}

	private long reqID;
	private Map<Long, RInfo> reqList;

	public String getNewReqID(String flowID, int clientID, long[] transs) {
		reqID++;
		RInfo rInfo = new RInfo();
		rInfo.setClientID(clientID);
		rInfo.setFlowID(flowID);
		rInfo.setTranss(transs);
		reqList.put(reqID, rInfo);
		return reqID + "";
	}
}
