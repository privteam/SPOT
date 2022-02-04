package utilities;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.CustomQuadraticElement;
import edu.jhu.isi.grothsahai.entities.QuarticElement;
import edu.jhu.isi.grothsahai.entities.Vector;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractPointElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.ImmutableQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

public class VectorPPP {
	/**
	 * Fait le preprocessing d'un vecteur, 
	 * cela permet de calculer la fonction de pairing de ce vecteur et d'un autre plus rapidement
	 */
	private Pairing pairing;
	private Vector V;//Le vecteur à préparer
	private List<PairingPreProcessing[]> pppV;//Contient le preprocessing de chaque élément du vecteur

	public VectorPPP(Vector v, CommonReferenceString crs) {
		this.V = v;
		this.pairing = crs.getPairing();
		pppV = ppp(V);
	}

	public List<PairingPreProcessing[]> ppp(Vector v) {
		/**
		 * Effectue le préprocessing de chaque élément du vecteur
		 */
		List<PairingPreProcessing[]> pppv = new ArrayList<PairingPreProcessing[]>();
		for (int i = 0; i < v.getLength(); i++) {

			PairingPreProcessing[] list = {
					pairing.getPairingPreProcessingFromElement(((QuadraticElement) v.get(i)).getX()),
					pairing.getPairingPreProcessingFromElement(((QuadraticElement) v.get(i)).getY()) };
			pppv.add(list);
		}
		return pppv;
	}

	public Element pairing(Vector v) {
		/**
		 * Effectue le pairing du vecteur préparé avec celui donné en entrée
		 */
		QuadraticElement element;

		Element result = new QuarticElement(new QuadraticField(new SecureRandom(), pairing.getGT()),
				pairing.getGT().newZeroElement(), pairing.getGT().newZeroElement(), pairing.getGT().newZeroElement(),
				pairing.getGT().newZeroElement());
		for (int i = 0; i < v.getLength(); i++) {
			element = (QuadraticElement) v.get(i);

			if (v.get(i).getClass().equals(ImmutableQuadraticElement.class)) {
				v.set(i, new CustomQuadraticElement((QuadraticElement) v.get(i), pairing));
			}

			result = result.add(new QuarticElement<>(new QuadraticField(new SecureRandom(), pairing.getGT()),
					pppV.get(i)[0].pairing(element.getX()), pppV.get(i)[0].pairing(element.getY()),
					pppV.get(i)[1].pairing(element.getX()), pppV.get(i)[1].pairing(element.getY())));
		}

		return result.getImmutable();
	}

	public Element pairingreverse(Vector v) {
		/**
		 * Effectue pairing(v,this) au lieu de pairing(this,v)
		 */
		QuadraticElement element;

		Element result = new QuarticElement(new QuadraticField(new SecureRandom(), pairing.getGT()),
				pairing.getGT().newZeroElement(), pairing.getGT().newZeroElement(), pairing.getGT().newZeroElement(),
				pairing.getGT().newZeroElement());
		for (int i = 0; i < v.getLength(); i++) {
			element = (QuadraticElement) v.get(i);

			if (v.get(i).getClass().equals(ImmutableQuadraticElement.class)) {
				v.set(i, new CustomQuadraticElement((QuadraticElement) v.get(i), pairing));
			}

			result = result.add(new QuarticElement<>(new QuadraticField(new SecureRandom(), pairing.getGT()),
					pppV.get(i)[0].pairing(element.getX()), pppV.get(i)[0].pairing(element.getY()),
					pppV.get(i)[1].pairing(element.getX()), pppV.get(i)[1].pairing(element.getY())));
		}

		Element result_inv = new QuarticElement(new QuadraticField(new SecureRandom(), pairing.getGT()),
				pairing.getGT().newZeroElement(), ((QuarticElement) result).getY(), ((QuarticElement) result).getX(),
				pairing.getGT().newZeroElement());

		return result_inv.getImmutable();
	}

	public void print() {
		System.out.println(V);
	}
}
