package entities;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFCurveGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import databases.DatabaseTA;
import edu.jhu.isi.grothsahai.entities.Vector;

public class TrustedAuthority {
	

	private final DatabaseTA database;// public parameters
	private final PairingParameters pairingparameters;// pairing parameters
	private final Pairing param;
	private final Element g2;
	private final Element g1;

	private final ExecutorService executor;

	/** Set_params Algorithm **/
	
	public TrustedAuthority(String type) { 
		/**
		 * Type A 112 bits (rBits = 224; qBits = 1024) - 128 bits (rBits = 256; qBits =
		 * 1536)
		 */
		int rBits;
		int qBits;
		TypeACurveGenerator pga;

		switch (type) {
		case "a":
			rBits = 224;
			qBits = 1024;
			pga = new TypeACurveGenerator(rBits, qBits);
			pairingparameters = pga.generate();
			break;
		case "a'":
			rBits = 256;
			qBits = 1536;
			pga = new TypeACurveGenerator(rBits, qBits);
			pairingparameters = pga.generate();
			break;
		case "f":
			pairingparameters = new TypeFCurveGenerator(224).generate();
			break;
		case "f'":
			pairingparameters = new TypeFCurveGenerator(256).generate();
			break;
		default:
			System.out.println("Error type not supported");
			pairingparameters = null;
		}

		param = PairingFactory.getPairing(pairingparameters);
		g2 = param.getG2().newRandomElement();
		g1 = param.getG1().newRandomElement();


		executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());// Activate parallelization

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
//				System.out.println("Performing some shutdown cleanup...");
				executor.shutdown();
				while (true) {
					try {
//						System.out.println("Waiting for the service to terminate...");
						if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
							break;
						}
					} catch (InterruptedException e) {
					}
				}
//				System.out.println("Done cleaning");
			}
		}));

		database = new DatabaseTA(param, pairingparameters, g1, g2, executor);
	}
	
	/** HA_Keygen Algorithm **/

	public Vector ha_keygen() {

		Element secret_key_ha = param.getZr().newRandomElement();
		Element public_key_ha = g2.duplicate().mulZn(secret_key_ha);
		Element[] outputlist = { public_key_ha, secret_key_ha };

		return new Vector(outputlist);

	}
	
	
	/** S_Keygen Algorithm **/

	public Vector[] s_keygen() {

		Element y1 = param.getZr().newRandomElement();
		Element y2 = param.getZr().newRandomElement();
		Element[] y1y2 = { y1, y2 };
		Vector secret_key_server = new Vector(y1y2);
		Element Y1 = g2.duplicate().mulZn(y1);
		Element Y2 = g2.duplicate().mulZn(y2);

		Element[] Y1Y2 = { Y1, Y2 };
		Vector public_key_server = new Vector(Y1Y2);

		Vector[] outputlist = { public_key_server, secret_key_server };

		return outputlist;

	}

	public DatabaseTA getDatabase() {
		return this.database;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void executorShutdown() {
		executor.shutdown();
		while (true) {

			if (executor.isTerminated()) {
				break;
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
