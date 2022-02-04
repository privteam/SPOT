package utilities;

import java.util.List;

import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import it.unisa.dia.gas.jpbc.Element;

public class ContactSigned {
	/**
	 * Contient les éléments de sortie de Psign et de S_PSign combinés
	 * Représente un contact stocké sur le téléphone d'un utilsateur
	 */
	private Element M;
	private Proof[] proofs;
	private List<Statement> statements;
	private Element PSp;
	
	public ContactSigned(Contact contact, Element PSp) {

		this.M = contact.getM();
		this.proofs = contact.getProofs();
		this.statements = contact.getStatements();
		this.PSp = PSp;
		
	}

	public Element getM() {
		return M;
	}

	public Proof[] getProofs() {
		return proofs;
	}

	public List<Statement> getStatements() {
		return statements;
	}
	public Element getPSp() {
		return PSp;
	}
}
