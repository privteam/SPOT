package databases;

import java.util.concurrent.ExecutorService;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;

public class DatabaseTA {
	/**
	 * Contient les données publiques du système
	 */
	private final Pairing param;
	private final Element g2;
	private final Element g1;
	private final PairingParameters pairingparameters;

	private final ExecutorService executor;

	public DatabaseTA(Pairing param, PairingParameters p, Element g1, Element g2, ExecutorService executor) {
		this.g1 = g1;
		this.g2 = g2;
		this.param = param;
		pairingparameters = p;

		this.executor = executor;
	}

	public Pairing getParam() {
		return param;
	}

	public Element getG2() {
		return g2;
	}

	public Element getG1() {
		return g1;
	}

	public PairingParameters getPairingparameters() {
		return pairingparameters;
	}
	
	public ExecutorService getExecutor() {
		return executor;	
	}

}
