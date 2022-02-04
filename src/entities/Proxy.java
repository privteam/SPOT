package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import databases.DatabaseGM;
import databases.DatabaseProxy;
import databases.DatabaseTA;
import edu.jhu.isi.grothsahai.api.impl.ProverImpl;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import utilities.Contact;
import edu.jhu.isi.grothsahai.entities.Vector;

public class Proxy {

	private final DatabaseTA databaseTA;
	private final DatabaseGM databaseGM;
	private final DatabaseProxy databaseProxy;
	private final Pairing param;
	private final Element g2;
	private final Vector skp;
	private final Vector pkp;
	private final Element yz;
	private final Element dz;
	private final Element a;
	private final Element b;
	private final Element y;
	private final Element d;
	private final Element gr;
	private final Element hu;
	private final Element gz;
	private final Element hz;

	private Vector A1p;
	private Vector A2p;
	private Vector A3p;
	private Vector A4p;
	private Vector B1p;
	private Vector B2p;
	private Vector B3p;
	private Vector B4p;

	private Element t1p;
	private Element t2p;
	private Element t3p;
	private Element t4p;

	private final ExecutorService executor;

	public Proxy(TrustedAuthority ta, GroupManager gm) {
		this.databaseTA = ta.getDatabase();
		this.databaseGM = gm.getDatabaseGM();

		this.param = databaseTA.getParam();
		g2 = databaseTA.getG2();

		/** Join_proxyGr Algorithm STEPs 4 to 9 **/

		// STEP 4 

		gr = param.getG1().newRandomElement();
		hu = param.getG1().newRandomElement();

		y = param.getZr().newRandomElement();
		d = param.getZr().newRandomElement();

		// STEP 5

		Element gy = gr.duplicate().mulZn(y);
		Element hd = hu.duplicate().mulZn(d);

		// STEP 6

		yz = param.getZr().newRandomElement();
		dz = param.getZr().newRandomElement();

		// STEP 7

		gz = gr.duplicate().mulZn(yz);
		hz = hu.duplicate().mulZn(dz);

		// STEP 8

		a = param.getZr().newRandomElement();
		b = param.getZr().newRandomElement();

		// STEP 9

		Element[] pkpliste = { gz, hz, gr, hu, g2.duplicate().mulZn(a), g2.duplicate().mulZn(b), gy, hd };
		Element[] skpliste = { a, b, yz, dz, y, d };
		Vector skpvector = new Vector(skpliste);

		pkp = new Vector(pkpliste);
		skp = new Vector(pkp, skpvector);

		this.databaseProxy = gm.join_ProxyGr(pkp);



		Element[] A1p_list = { databaseTA.getG1().duplicate().mulZn(databaseGM.getAlpha1()) };
		Element[] A2p_list = { databaseTA.getG1().duplicate().mulZn(databaseGM.getBeta1()) };
		Element[] A3p_list = { databaseGM.getG1z(), databaseGM.getGr1() };
		Element[] A4p_list = { databaseGM.getH1z(), databaseGM.getHu1() };

		Element[] B1p_list = { databaseGM.getG2z(), databaseGM.getGr2() };
		Element[] B2p_list = { databaseGM.getH2z(), databaseGM.getHu2() };
		Element[] B3p_list = { databaseTA.getG2().duplicate().mulZn(databaseGM.getAlpha1()) };
		Element[] B4p_list = { databaseTA.getG2().duplicate().mulZn(databaseGM.getBeta1()) };

		A1p = new Vector(A1p_list);
		A2p = new Vector(A2p_list);
		A3p = new Vector(A3p_list);
		A4p = new Vector(A4p_list);

		B1p = new Vector(B1p_list);
		B2p = new Vector(B2p_list);
		B3p = new Vector(B3p_list);
		B4p = new Vector(B4p_list);

		t1p = param.pairing(databaseTA.getG1().duplicate().mulZn(databaseGM.getAlpha2()), databaseGM.getGr2());
		t2p = param.pairing(databaseTA.getG1().duplicate().mulZn(databaseGM.getBeta2()), databaseGM.getHu2());
		t3p = param.pairing(databaseGM.getGr1(), databaseTA.getG2().duplicate().mulZn(databaseGM.getAlpha1()));
		t4p = param.pairing(databaseGM.getHu1(), databaseTA.getG2().duplicate().mulZn(databaseGM.getBeta1()));

		this.executor = ta.getExecutor();
	}
	
	
	
	/** P_Sign Algorithm  with parallelization **/

	public Contact P_sign_P(Element PS, Element IDa) {


		// STEP 4

		Element M = IDa.duplicate().mulZn(PS);

		// STEP 6

		Element zeta = param.getZr().newRandomElement();
		Element rho = param.getZr().newRandomElement();
		Element tau = param.getZr().newRandomElement();
		Element phi = param.getZr().newRandomElement();
		Element omega = param.getZr().newRandomElement();

		// STEP 7

		Element z = g2.duplicate().mulZn(zeta);

		Element r = g2.duplicate()
				.mulZn(a.duplicate().sub(rho.duplicate().mul(tau)).duplicate().sub(yz.duplicate().mul(zeta)));

		r = r.duplicate().mul(M);
		r.mulZn(y.duplicate().negate());
		Element s = gr.duplicate().mulZn(rho);
		Element t = g2.duplicate().mulZn(tau);
		Element u = g2.duplicate()
				.mulZn(b.duplicate().sub(phi.duplicate().mul(omega)).duplicate().sub(dz.duplicate().mul(zeta)));
		u = u.duplicate().mul(M.duplicate().mulZn(d.duplicate().negate()));
		Element v = hu.duplicate().mulZn(phi);
		Element w = g2.duplicate().mulZn(omega);

		/**
		 * STEP 8 we do not need to return sigma_m to the user
		 **/

		// Element[] listesig = { z, r, s, t, u, v, w };
		// Vector signaturesigmam = new Vector(listesig);

		// STEP 9

		CommonReferenceString crs = databaseGM.getCrs();
		ProverImpl prover = new ProverImpl(crs, executor); 
		
		
		// STEP 10 & 11

		Element[] X1mliste = { gz, gr, s };
		Vector X1m = new Vector(X1mliste);
		Element[] X2mliste = { hz, hu, v };
		Vector X2m = new Vector(X2mliste);

		Element[] Y1mliste = { z, g2.duplicate()
				.mulZn(a.duplicate().sub(rho.duplicate().mul(tau)).duplicate().sub(yz.duplicate().mul(zeta))), t };
		Vector Y1m = new Vector(Y1mliste);
		Element[] Y2mliste = { z, g2.duplicate()
				.mulZn(b.duplicate().sub(rho.duplicate().mul(tau)).duplicate().sub(dz.duplicate().mul(zeta))), w };
		Vector Y2m = new Vector(Y2mliste);

		Element oneel = param.getZr().newOneElement();
		Element[][] onelist = { { oneel, oneel, oneel }, { oneel, oneel, oneel }, { oneel, oneel, oneel } };
		Matrix gamma1m = new Matrix(onelist);

		Vector Aim = Vector.getZeroVector(3, param.getG1());
		Vector Bim = Vector.getZeroVector(3, param.getG2());

		Witness witness1m = new Witness(X1m, Y1m);
		Witness witness2m = new Witness(X2m, Y2m);

	
		Statement statement1m = new Statement(Aim, Bim, gamma1m, param.getGT().newOneElement());
		Statement statement2m = new Statement(Aim, Bim, gamma1m, param.getGT().newOneElement());


		List<Statement> statement1m_list = new ArrayList<Statement>();
		statement1m_list.add(statement1m);
		List<Statement> statement2m_list = new ArrayList<Statement>();
		statement2m_list.add(statement2m);

		// Compute pi_1m proof in a new thread
		Callable<Proof> proof1 = new Callable<Proof>() {
			@Override
			public Proof call() throws Exception {
				return prover.proof(statement1m_list, witness1m);
			}
		};
		Future<Proof> ftr_pi1m = executor.submit(proof1);

		// Compute pi_2m proof in a new thread
		Callable<Proof> proof2 = new Callable<Proof>() {
			@Override
			public Proof call() throws Exception {
				return prover.proof(statement2m_list, witness2m);
			}
		};
		Future<Proof> ftr_pi2m = executor.submit(proof2);

		// STEPs 13-15

		Element minus_one = param.getZr().newOneElement().negate();

		Element[] Gamma1p_list = { databaseGM.getGamma2z(), minus_one };
		Element[] Gamma2p_list = { databaseGM.getDelta2z(), minus_one };
		Element[] Gamma3p_list = { databaseGM.getGamma1z(), minus_one };
		Element[] Gamma4p_list = { databaseGM.getDelta1z(), minus_one };

		Matrix Gamma1p = new Vector(Gamma1p_list).asMatrix();
		Matrix Gamma2p = new Vector(Gamma2p_list).asMatrix();
		Matrix Gamma3p = new Vector(Gamma3p_list).asMatrix();
		Gamma3p = Gamma3p.getTranspose();
		Matrix Gamma4p = new Vector(Gamma4p_list).asMatrix();
		Gamma4p = Gamma4p.getTranspose();

		

		Element g1 = databaseTA.getG1();
		Element alpha2 = databaseGM.getAlpha2();
		Element rho1 = databaseProxy.getRho1();
		Element tau1 = databaseProxy.getTau1();
		Element gamma2z = databaseGM.getGamma2z();
		Element zeta1 = databaseProxy.getZeta1();
		Element z1 = databaseProxy.getZ1();

		Element X1p_exponent2 = rho1.duplicate().mul(tau1);
		Element X1p_exponent3 = gamma2z.duplicate().mul(zeta1);
		Element X1p_exponent = alpha2.duplicate().sub(X1p_exponent2).sub(X1p_exponent3);

		Element[] X1p_list = { z1, g1.duplicate().mulZn(X1p_exponent) };

		Element beta2 = databaseGM.getBeta2();
		Element delta2z = databaseGM.getDelta2z();

		Element X2p_exponent2 = X1p_exponent2;
		Element X2p_exponent3 = delta2z.duplicate().mul(zeta1);
		Element X2p_exponent = beta2.duplicate().sub(X2p_exponent2).sub(X2p_exponent3);

		Element[] X2p_list = { z1, g1.duplicate().mulZn(X2p_exponent) };
		Element[] X3p_list = { databaseGM.getGr1() };
		Element[] X4p_list = { databaseGM.getHu1() };

		Element[] Y1p_list = { databaseGM.getGr2() };
		Element[] Y2p_list = { databaseGM.getHu2() };

		Element alpha1 = databaseGM.getAlpha1();
		Element z2 = databaseProxy.getZ2();
		Element rho2 = databaseProxy.getRho2();
		Element tau2 = databaseProxy.getTau2();
		Element gamma1z = databaseGM.getGamma1z();
		Element zeta2 = databaseProxy.getZeta2();
		Element delta1z = databaseGM.getDelta1z();
		Element beta1 = databaseGM.getBeta1();

		Element Y3p_exponent2 = rho2.duplicate().mul(tau2);
		Element Y3p_exponent3 = gamma1z.duplicate().mul(zeta2);
		Element Y3p_exponent = alpha1.duplicate().sub(Y3p_exponent2).sub(Y3p_exponent3);

		Element[] Y3p_list = { z2, g2.duplicate().mulZn(Y3p_exponent) };

		Element Y4p_exponent2 = Y3p_exponent2;
		Element Y4p_exponent3 = delta1z.duplicate().mul(zeta2);
		Element Y4p_exponent = beta1.duplicate().sub(Y4p_exponent2).sub(Y4p_exponent3);

		Element[] Y4p_list = { z2, g2.duplicate().mulZn(Y4p_exponent) };

		Vector X1p = new Vector(X1p_list);
		Vector X2p = new Vector(X2p_list);
		Vector X3p = new Vector(X3p_list);
		Vector X4p = new Vector(X4p_list);

		Vector Y1p = new Vector(Y1p_list);
		Vector Y2p = new Vector(Y2p_list);
		Vector Y3p = new Vector(Y3p_list);
		Vector Y4p = new Vector(Y4p_list);


		Witness witness1p = new Witness(X1p, Y1p);
		Witness witness2p = new Witness(X2p, Y2p);
		Witness witness3p = new Witness(X3p, Y3p);
		Witness witness4p = new Witness(X4p, Y4p);


		Statement statement1p = new Statement(A1p, B1p, Gamma1p, t1p);
		Statement statement2p = new Statement(A2p, B2p, Gamma2p, t2p);
		Statement statement3p = new Statement(A3p, B3p, Gamma3p, t3p);
		Statement statement4p = new Statement(A4p, B4p, Gamma4p, t4p);

		// Compute pi_1p proof in a new thread
		Callable<Proof> proofp1 = new Callable<Proof>() {
			@Override
			public Proof call() throws Exception {
				return prover.proof(Arrays.asList(statement1p), witness1p);
			}
		};
		Future<Proof> ftr_pi1p = executor.submit(proofp1);

		// Compute pi_2p proof in a new thread
		Callable<Proof> proofp2 = new Callable<Proof>() {
			@Override
			public Proof call() throws Exception {
				return prover.proof(Arrays.asList(statement2p), witness2p);
			}
		};
		Future<Proof> ftr_pi2p = executor.submit(proofp2);

		// Compute pi_3p proof in a new thread
		Callable<Proof> proofp3 = new Callable<Proof>() {
			@Override
			public Proof call() throws Exception {
				return prover.proof(Arrays.asList(statement3p), witness3p);
			}
		};
		Future<Proof> ftr_pi3p = executor.submit(proofp3);

		// Compute pi_4p proof in a new thread
		Callable<Proof> proofp4 = new Callable<Proof>() {
			@Override
			public Proof call() throws Exception {
				return prover.proof(Arrays.asList(statement4p), witness4p);
			}
		};
		Future<Proof> ftr_pi4p = executor.submit(proofp4);

		Proof pi1m = null;
		Proof pi2m = null;
		Proof pi1p = null;
		Proof pi2p = null;
		Proof pi3p = null;
		Proof pi4p = null;
		try {
			pi1m = ftr_pi1m.get();
			pi2m = ftr_pi2m.get();
			pi1p = ftr_pi1p.get();
			pi2p = ftr_pi2p.get();
			pi3p = ftr_pi3p.get();
			pi4p = ftr_pi4p.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		// STEP 16 

		/**
		 * Step 19, the most important of all
		 */
		List<Statement> statements = new ArrayList<Statement>();
		statements.add(statement1m);
		statements.add(statement2m);

		statements.add(statement1p);
		statements.add(statement2p);
		statements.add(statement3p);
		statements.add(statement4p);

		Proof[] pi = { pi1m, pi2m, pi1p, pi2p, pi3p, pi4p };

		// STEP 17
		return new Contact(M, pi, statements);

	}

}
