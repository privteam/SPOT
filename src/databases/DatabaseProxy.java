package databases;

import edu.jhu.isi.grothsahai.entities.Vector;
import it.unisa.dia.gas.jpbc.Element;

public class DatabaseProxy {
	/**
	 * Contient les données d'un proxy
	 */
	private final Element rho1;
	private final Element rho2;
	private final Element tau1;
	private final Element tau2;
	private final Element zeta1;
	private final Element zeta2;
	private final Element z1;
	private final Element z2;
	private final Vector op;
	
	public DatabaseProxy(Element rho1, Element rho2, Element tau1, Element tau2, Element zeta1, Element zeta2,
			Element z1, Element z2, Vector op) {
		this.rho1 = rho1;
		this.rho2 = rho2;
		this.tau1 = tau1;
		this.tau2 = tau2;
		this.zeta1 = zeta1;
		this.zeta2 = zeta2;
		this.z1 = z1;
		this.z2 = z2;
		this.op = op;
	}

	public Element getRho1() {
		return rho1;
	}

	public Element getRho2() {
		return rho2;
	}

	public Element getTau1() {
		return tau1;
	}

	public Element getTau2() {
		return tau2;
	}

	public Element getZeta1() {
		return zeta1;
	}

	public Element getZeta2() {
		return zeta2;
	}

	public Element getZ1() {
		return z1;
	}

	public Element getZ2() {
		return z2;
	}

	public Vector getOp() {
		return op;
	}
	

}
