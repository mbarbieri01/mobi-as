package com.cesaco.mobias.controller;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class Caller {

	private final int MAX_TIMER = 300;
	@Inject
	private Controller controller;
	
	public void call(int clientID, long[] transs, String flowID) {
		int timer = 0;
		while (!controller.hasFlowData(clientID, transs) && timer < MAX_TIMER) {
			try {
				Thread.sleep(1000);
				timer++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
