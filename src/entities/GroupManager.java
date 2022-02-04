package entities;

import databases.DatabaseGM;
import databases.DatabaseProxy;
import databases.DatabaseTA;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import edu.jhu.isi.grothsahai.entities.Vector;

public class GroupManager {



	private final DatabaseGM databaseGM;// Group parameters
	private final DatabaseTA database; // Public parameters
	private final Pairing param;
	private final Element g1;
	private final Element g2;
	private final Element[] y2;
	private final PairingParameters pairingparameters;
	private final Vector skg;
	private final Object[] vkg;
	private final Element alpha1;
	private final Element alpha2;
	private final Element beta1;
	private final Element beta2;
	private final Element y11;
	private final Element y12;
	private final Element d11;
	private final Element d12;
	private final Element gr1;
	private final Element hu1;
	private final Element gamma1z;
	private final Element delta1z;
	private final Element gr2;
	private final Element hu2;
	private final Element gamma2z;
	private final Element delta2z;
	private final Element[] d2;

	public GroupManager(TrustedAuthority ta) {
		

		this.database = ta.getDatabase();
		this.param = database.getParam();
		this.g1 = database.getG1();
		this.g2 = database.getG2();
		this.pairingparameters = database.getPairingparameters();

		/** Setup_proxyGr Algorithm **/

		// STEP 4

		gr1 = param.getG1().newRandomElement();
		hu1 = param.getG1().newRandomElement();

		gr2 = param.getG2().newRandomElement();
		hu2 = param.getG2().newRandomElement();

		y11 = param.getZr().newRandomElement();
		y12 = param.getZr().newRandomElement();
		d11 = param.getZr().newRandomElement();
		d12 = param.getZr().newRandomElement();

		Element g11 = gr1.duplicate().mulZn(y11);
		Element g12 = gr1.duplicate().mulZn(y12);
		Element h11 = hu1.duplicate().mulZn(d11);
		Element h12 = hu1.duplicate().mulZn(d12);

		y2 = new Element[8]; 
		d2 = new Element[8];

		for (int j = 1; j <= 7; j++) {
			y2[j] = param.getZr().newRandomElement();
			d2[j] = param.getZr().newRandomElement();
		}

		Element[] g2i = new Element[8];
		Element[] h2i = new Element[8];

		for (int j = 1; j <= 7; j++) {
			g2i[j] = gr2.duplicate().mulZn(y2[j]);
			h2i[j] = hu2.duplicate().mulZn(d2[j]);
		}

		// STEP 5

		gamma1z = param.getZr().newRandomElement();
		delta1z = param.getZr().newRandomElement();
		gamma2z = param.getZr().newRandomElement();
		delta2z = param.getZr().newRandomElement();

		// STEP 6

		Element g1z = gr1.duplicate().mulZn(gamma1z);
		Element h1z = hu1.duplicate().mulZn(delta1z);
		Element g2z = gr2.duplicate().mulZn(gamma2z);
		Element h2z = hu2.duplicate().mulZn(delta2z);

		// STEP 7

		alpha1 = param.getZr().newRandomElement();
		alpha2 = param.getZr().newRandomElement();
		beta1 = param.getZr().newRandomElement();
		beta2 = param.getZr().newRandomElement();

		// STEP 8
		Element[] pk1el = { g2z, h2z, gr2, hu2, g1.duplicate().mulZn(alpha2), g1.duplicate().mulZn(beta2) };
		Vector pk1 = new Vector(new Vector(pk1el), new Vector(g2i), new Vector(h2i));

		Element[] sk1el = { alpha2, beta2, gamma2z, delta2z };
		Vector sk1 = new Vector(pk1, new Vector(sk1el), new Vector(y2), new Vector(d2));

		// STEP 9
		Element[] pk2el = { g1z, h1z, gr1, hu1, param.getG2().newRandomElement().duplicate().mulZn(alpha1),
				param.getG2().newRandomElement().duplicate().mulZn(beta1), g11, g12, h11, h12 };
		Vector pk2 = new Vector(new Vector(pk2el));

		Element[] sk2el = { alpha1, beta1, gamma1z, delta1z, y11, y12, d11, d12 };
		Vector sk2 = new Vector(pk2, new Vector(sk2el));

		// STEP 10
		Vector pkg = new Vector(pk1, pk2);
		skg = new Vector(sk1, sk2);

		// STEP 12 & 13     Note that the generation of U, V and crs relies on Groth-Sahai proof scheme implementation 
		CommonReferenceString crs = CommonReferenceString.generate(pairingparameters);

		Object[] vkg = { pkg, crs };
		this.vkg = vkg;

		Object[] output = { vkg, skg };

		DatabaseGM databaseGM = new DatabaseGM(crs, alpha1, alpha2, beta1, beta2, y11, y12, d11, d12, gr1, hu1, gamma1z,
				delta1z, gr2, hu2, gamma2z, delta2z, d2, g1z, g2z, h1z, h2z);

		this.databaseGM = databaseGM;

	}
	
	
	
	/** Join_proxyGr Algorithm STEPs 11 and 12 **/

	public DatabaseProxy join_ProxyGr(Vector pkp) {
		
		Element gz = pkp.get(0);
		Element hz = pkp.get(1);
		Element gr = pkp.get(2);
		Element hu = pkp.get(3);
		Element g2a = pkp.get(4);
		Element g2b = pkp.get(5);
		Element gy = pkp.get(6);
		Element hd = pkp.get(7);

		
		Element zeta2 = param.getZr().newRandomElement();
		Element rho2 = param.getZr().newRandomElement();
		Element tau2 = param.getZr().newRandomElement();
		Element phi2 = param.getZr().newRandomElement();
		Element omega2 = param.getZr().newRandomElement();


		Element m21 = g2a;
		Element m22 = g2b;

		Element z2 = g2.duplicate().mulZn(zeta2);

		Element r2 = g2.duplicate()
				.mulZn(alpha1.duplicate().sub(rho2.duplicate().mul(tau2)).sub(gamma1z.duplicate().mul(zeta2)));
		r2 = r2.mul(m21.duplicate().mulZn(y11.duplicate().negate()))
				.mul(m22.duplicate().mulZn(y12.duplicate().negate()));

		Element s2 = gr1.duplicate().mulZn(rho2);
		Element t2 = g2.duplicate().mulZn(tau2);

		Element u2 = g2.duplicate()
				.mulZn(beta1.duplicate().sub(phi2.duplicate().mul(omega2)).sub(delta1z.duplicate().mul(zeta2)))
				.mul(m21.duplicate().mulZn(d11.duplicate().negate()))
				.mul(m22.duplicate().mulZn(d12.duplicate().negate()));

		Element v2 = hu1.duplicate().mulZn(phi2);

		Element w2 = g2.duplicate().mulZn(omega2);


		Element[] o2liste = { z2, r2, s2, t2, u2, v2, w2 };
		Vector o2 = new Vector(o2liste);


		Element zeta1 = param.getZr().newRandomElement();
		Element rho1 = param.getZr().newRandomElement();
		Element tau1 = param.getZr().newRandomElement();
		Element phi1 = param.getZr().newRandomElement();
		Element omega1 = param.getZr().newRandomElement();

		Element[] m1 = { null, gz, hz, gr, hu, gy, hd, s2 };

		Element z1 = g1.duplicate().mulZn(zeta1);

		Element multiplier1 = m1[1].duplicate().mulZn(y2[1].duplicate().negate());
		for (int j = 2; j <= 7; j++) {
			multiplier1.mul(m1[j].duplicate().mulZn(y2[j].duplicate().negate()));
		}

		Element r1 = g1.duplicate().mulZn(alpha2.duplicate().sub(rho1.mul(tau1)).sub(gamma2z.mul(zeta1)))
				.mul(multiplier1);

		Element s1 = gr2.duplicate().mulZn(rho1);
		Element t1 = g1.duplicate().mulZn(tau1);

		Element multiplier2 = m1[1].duplicate().mulZn(d2[1].duplicate().negate());
		for (int j = 2; j <= 7; j++) {
			multiplier2.mul(m1[j].duplicate().mulZn(d2[j].duplicate().negate()));
		}
		Element u1 = g1.mulZn(beta2.duplicate().sub(phi1.duplicate().mul(omega1)).sub(delta2z.duplicate().mul(zeta1)))
				.mul(multiplier2);

		Element v1 = hu2.duplicate().mulZn(phi1);

		Element w1 = g1.duplicate().mulZn(omega1);



		Element[] o1liste = { z1, r1, s1, t1, u1, v1, w1 };
		Vector o1 = new Vector(o1liste);


		Vector op = new Vector(o1, o2);

		DatabaseProxy dbProxy = new DatabaseProxy(rho1, rho2, tau1, tau2, zeta1, zeta2, z1, z2, op);

		return dbProxy;

	}

	public DatabaseGM getDatabaseGM() {
		return this.databaseGM;
	}

}
