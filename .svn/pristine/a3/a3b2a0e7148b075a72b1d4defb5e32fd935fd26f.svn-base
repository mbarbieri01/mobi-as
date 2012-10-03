package com.cesaco.mobias.managedbeans;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.cesaco.mobias.queue.MessageInfo;
import com.cesaco.mobias.queue.QueueManager;

@Named("JMSMessage")
@SessionScoped
public class JMSMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	private List<MessageInfo> messages;
	


	private boolean sortAscendingClientID = true;
	private boolean sortAscendingDate = true;
	private boolean sortAscendingTransID = true;

	@Inject
	private QueueManager qManager;

	public List<MessageInfo> getMessages() {
		if (messages == null)
			messages = qManager.getMessages();
		
		return messages;
	}
	
	public List<MessageInfo> refresh() {
		messages = null;
		return getMessages();
	}

	public void setMessages(List<MessageInfo> messages) {
		this.messages = messages;
	}

	public JMSMessage() {

	}

	public String sortByDate() {

		if (sortAscendingDate) {

			// ascending order
			Collections.sort(messages, new Comparator<MessageInfo>() {

				@Override
				public int compare(MessageInfo o1, MessageInfo o2) {
					if (o1.getDateCreated() != null
							&& o2.getDateCreated() != null)
						return o1.getDateCreated().compareTo(
								o2.getDateCreated());
					else
						return 0;

				}

			});
			sortAscendingDate = false;

		} else {

			// descending order
			Collections.sort(messages, new Comparator<MessageInfo>() {

				@Override
				public int compare(MessageInfo o1, MessageInfo o2) {
					if (o1.getDateCreated() != null
							&& o2.getDateCreated() != null)
						return o2.getDateCreated().compareTo(
								o1.getDateCreated());
					else
						return 0;

				}

			});
			sortAscendingDate = true;
		}

		return null;
	}
	
	public String sortByClientID() {

		if (sortAscendingClientID) {

			// ascending order
			Collections.sort(messages, new Comparator<MessageInfo>() {

				@Override
				public int compare(MessageInfo o1, MessageInfo o2) {
					if (o1.getClientID() != null
							&& o2.getClientID() != null)
						return o1.getClientID().compareTo(
								o2.getClientID());
					else
						return 0;

				}

			});
			sortAscendingClientID = false;

		} else {

			// descending order
			Collections.sort(messages, new Comparator<MessageInfo>() {

				@Override
				public int compare(MessageInfo o1, MessageInfo o2) {
					if (o1.getClientID() != null
							&& o2.getClientID() != null)
						return o2.getClientID().compareTo(
								o1.getClientID());
					else
						return 0;

				}

			});
			sortAscendingClientID = true;
		}

		return null;
	}
	
	public String sortByTransID() {

		if (sortAscendingTransID) {

			// ascending order
			Collections.sort(messages, new Comparator<MessageInfo>() {

				@Override
				public int compare(MessageInfo o1, MessageInfo o2) {
					if (o1.getTransID() != null
							&& o2.getTransID() != null)
						return o1.getTransID().compareTo(
								o2.getTransID());
					else
						return 0;

				}

			});
			sortAscendingTransID = false;

		} else {

			// descending order
			Collections.sort(messages, new Comparator<MessageInfo>() {

				@Override
				public int compare(MessageInfo o1, MessageInfo o2) {
					if (o1.getTransID() != null
							&& o2.getTransID() != null)
						return o2.getTransID().compareTo(
								o1.getTransID());
					else
						return 0;

				}

			});
			sortAscendingTransID = true;
		}

		return null;
	}
	
	public String doDelete() {
		qManager.doDeleteMessage(messages);		
		return null;
	}

}
