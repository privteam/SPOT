package databases;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import it.unisa.dia.gas.jpbc.Element;

public class DatabaseGM {
	/**
	 * Contient les données d'un groupe
	 */
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
	private final Element g1z;
	private final Element g2z;
	private final Element h1z;
	private final Element h2z;
	
	private final CommonReferenceString crs;


	public DatabaseGM(CommonReferenceString crs, Element alpha1, Element alpha2, Element beta1, Element beta2, Element y11, Element y12,
			Element d11, Element d12, Element gr1, Element hu1, Element gamma1z, Element delta1z, Element gr2,
			Element hu2, Element gamma2z, Element delta2z, Element[] d2, Element g1z, Element g2z, Element h1z,
			Element h2z) {
		this.alpha1 = alpha1;
		this.alpha2 = alpha2;
		this.beta1 = beta1;
		this.beta2 = beta2;
		this.y11 = y11;
		this.y12 = y12;
		this.d11 = d11;
		this.d12 = d12;
		this.gr1 = gr1;
		this.hu1 = hu1;
		this.gamma1z = gamma1z;
		this.delta1z = delta1z;
		this.gr2 = gr2;
		this.hu2 = hu2;
		this.gamma2z = gamma2z;
		this.delta2z = delta2z;
		this.d2 = d2;
		this.g1z = g1z;
		this.g2z = g2z;
		this.h1z = h1z;
		this.h2z = h2z;
		
		this.crs = crs;
	}


	public Element getAlpha1() {
		return alpha1;
	}


	public Element getAlpha2() {
		return alpha2;
	}


	public Element getBeta1() {
		return beta1;
	}


	public Element getBeta2() {
		return beta2;
	}


	public Element getY11() {
		return y11;
	}


	public Element getY12() {
		return y12;
	}


	public Element getD11() {
		return d11;
	}


	public Element getD12() {
		return d12;
	}

	public Element getGr1() {
		return gr1;
	}


	public Element getHu1() {
		return hu1;
	}


	public Element getGamma1z() {
		return gamma1z;
	}


	public Element getDelta1z() {
		return delta1z;
	}


	public Element getGr2() {
		return gr2;
	}


	public Element getHu2() {
		return hu2;
	}


	public Element getGamma2z() {
		return gamma2z;
	}


	public Element getDelta2z() {
		return delta2z;
	}


	public Element[] getD2() {
		return d2;
	}


	public Element getG1z() {
		return g1z;
	}


	public Element getG2z() {
		return g2z;
	}


	public Element getH1z() {
		return h1z;
	}


	public Element getH2z() {
		return h2z;
	}


	public CommonReferenceString getCrs() {
		return crs;
	}

}
