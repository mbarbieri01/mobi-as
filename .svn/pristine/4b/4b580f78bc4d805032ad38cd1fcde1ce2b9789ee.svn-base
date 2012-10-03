package com.cesaco.mobias.util.jms;

/**
 * Per gestire la chiusura
 * @author Stefano
 *
 */
public class Monitor {

	boolean termina = false;

	/**
	 * Metto in attesa la chiamata
	 */
	public void attendiLaFine() {
		synchronized (this) {
			while (!termina) {
				try {
					this.wait();
				} catch (InterruptedException ie) {
				}
			}
		}
	}

	/**
	 * Chiudi l'attesa
	 */
	public void allDone() {
		synchronized (this) {
			termina = true;
			this.notify();
		}
	}

}
