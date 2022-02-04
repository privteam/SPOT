package entities;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import databases.DatabaseTA;
import edu.jhu.isi.grothsahai.entities.Vector;

public class Server {


	private final DatabaseTA databaseTA;
	private final Pairing param;
	private final Vector public_key_server;
	private final Vector secret_key_server;
	private final Element y1;
	private final Element y2;

	public Server(TrustedAuthority ta) {
		this.databaseTA = ta.getDatabase();
		this.param = databaseTA.getParam();
		Vector[] keys = ta.s_keygen();
		this.public_key_server = keys[0];
		this.secret_key_server = keys[1];
		this.y1 = secret_key_server.get(0);
		this.y2 = secret_key_server.get(1);

	}
	
	/** S_PSign algorithm **/

	public Element[] s_PSign_S(Element CCM) {

		Element rs = param.getZr().newRandomElement();

		Element PS = CCM.duplicate().mul(y1).mul(rs);
		PS.add(y2);

		Element PSp = CCM.duplicate().mul(rs);
		Element[] output = { PS, PSp };
		return output;
	}

	public Vector getPublic_key_server() {
		return public_key_server;
	}

}
