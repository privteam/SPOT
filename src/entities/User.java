package entities;

import java.util.ArrayList;
import java.util.List;

import databases.DatabaseTA;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import utilities.ContactSigned;
import utilities.Sha512;

public class User {
	

	private final DatabaseTA databaseTA;
	private final Element IDu;
	private final Element pku;
	private final Element sku;
	private final Pairing param;
	private final Element tU;

	private List<ContactSigned> contact_list;

	public User(TrustedAuthority ta, HealthAuthority ha) {
		this.databaseTA = ta.getDatabase();
		this.param = databaseTA.getParam();

		/** Userkeygen Algorithm **/

		sku = param.getZr().newRandomElement();
		Element[] ID_and_tU = ha.set_userIDha();
		Element ID = ID_and_tU[0];
		this.IDu = ID;
		this.pku = ID.duplicate().mulZn(sku);
		this.tU = ID_and_tU[1];

		contact_list = new ArrayList<>();

	}

	
		/** Set_CCM Algorithm **/
	
	public Element set_CCM_U() {
		Element EBIDa = param.getZr().newRandomElement();
		Element EBIDb = param.getZr().newRandomElement();

		Element mAB = EBIDa.duplicate().mulZn(EBIDb);
		String CCMAB_str = mAB.duplicate().toBigInteger().toString();
		CCMAB_str = Sha512.encryptThisString(CCMAB_str);

		Element CCMAB = param.getZr().newZeroElement();
		CCMAB.setFromBytes(CCMAB_str.getBytes());

		return CCMAB;
	}

	public Element getIDu() {
		return IDu;
	}

	public Element gettU() {
		return tU;
	}

	public void add_contact(ContactSigned contact) {
		contact_list.add(contact);
	}

	public List<ContactSigned> getContact_list() {
		return contact_list;
	}
}
