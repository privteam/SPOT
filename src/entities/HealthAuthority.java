package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import databases.DatabaseTA;
import edu.jhu.isi.grothsahai.api.impl.VerifierImpl;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Vector;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import utilities.ContactSigned;

public class HealthAuthority {


	private final DatabaseTA databaseTA;// public parameters
	private final Element public_key_ha;
	private final Element g2;
	private final Pairing param;
	private VerifierImpl verifier;
	private final ExecutorService executor;

	public HealthAuthority(TrustedAuthority ta, CommonReferenceString crs) {

		this.databaseTA = ta.getDatabase();
		this.g2 = databaseTA.getG2();
		this.param = databaseTA.getParam();
		Vector keys = ta.ha_keygen(); 
		this.public_key_ha = keys.get(0);
		this.verifier = new VerifierImpl(crs, ta.getExecutor());
		



		this.executor = ta.getExecutor();

	}
	
	/** Set_UserID Algorithm **/

	public Element[] set_userIDha() {
		Element tu = param.getZr().newRandomElement();
		Element hu = g2.duplicate().mulZn(tu);
		Element[] output = { hu, tu };
		return output;
	}
	
	
	/** Verification of a contact list **/

	public boolean Sig_VerifyHA(List<ContactSigned> contacts, CommonReferenceString crs) {
		for (int i = 0; i < contacts.size(); i++) {
			List<Statement> equations = contacts.get(i).getStatements();
			Proof[] proofs = contacts.get(i).getProofs();
			if (!Sig_Verify(equations, proofs, crs)) {
				return false;
			}
		}
		return true;
	}

	/** Sig_Verify Algorithm : verification of a single proof/contact message with parallelization**/

	public boolean Sig_Verify(List<Statement> equations, Proof[] proofs, CommonReferenceString crs) {
	
		try {

			int len = equations.size();
			List<Statement> statement;
			List<Future<Boolean>> ftr_bools = new ArrayList<Future<Boolean>>();

			for (int i = 0; i < len; i++) {
				statement = new ArrayList<Statement>();
				statement.add(equations.get(i));

				
				ftr_bools.add(executor.submit(new Verif(verifier, statement, proofs[i])));
			}

			for (int i = 0; i < len; i++) {
				if (ftr_bools.get(i).get()) {
				} else {
					System.out.println("This is false");
					return false;
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** CCM_Verify algorithm applied on a contact list **/
	
	public boolean CCM_VerifyHA(Server server, User user) {
		Element tA = user.gettU();
		List<ContactSigned> contacts = user.getContact_list();
		for (int i=0; i<user.getContact_list().size();i++) {
			
			Element M = contacts.get(i).getM();
			Element PSp = contacts.get(i).getPSp();
			
			boolean bool = CCM_Verify(server,M,PSp,tA);
			if (!bool) {
				return false;
			}
		}
		return true;
	}
	
	/** CCM_Verify algorithm applied on a single contact message  **/
	public boolean CCM_Verify(Server server, Element M, Element PSp, Element tA) {
		try {
			Element lhs = M.duplicate();

			Element Y1 = server.getPublic_key_server().get(0).getImmutable();
			Element Y2 = server.getPublic_key_server().get(1).getImmutable();
			
			Element rhs = Y1.duplicate();
			rhs.mulZn(tA);
			rhs.mulZn(PSp);
			
			rhs.add(Y2.duplicate().mulZn(tA));

			return lhs.isEqual(rhs);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Element getPublic_key_ha() {
		return public_key_ha;
	}

	// Function used to verify one equation
	private class Verif implements Callable<Boolean> {
		private VerifierImpl verifier;
		private List<Statement> statement;
		private Proof proof;

		public Verif(VerifierImpl verifier, List<Statement> statement, Proof proof) {
			this.verifier = verifier;
			this.statement = statement;
			this.proof = proof;
		}

		@Override
		public Boolean call() throws Exception {
			return verifier.verify(statement, proof);
		}

	}





}
