package tests;

import org.junit.After;
import org.junit.Test;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import entities.GroupManager;
import entities.HealthAuthority;
import entities.Proxy;
import entities.Server;
import entities.TrustedAuthority;
import entities.User;
import it.unisa.dia.gas.jpbc.Element;
import statistics.base.Mean;
import statistics.base.Median;
import statistics.base.StandardDeviation;
import utilities.Contact;
import utilities.ContactSigned;

public class PerformanceTests {
	
	private String type = "a"; // pairing type (a for A-112 bits, a' for A-128 bits
										// f for F-112 bits and f' for F-128 bits

	private int nb_execs = 100;// Number of iterations


	// Lists of computation times of each algorithm
	long[] time_set_params = new long[nb_execs];
	long[] time_s_keygen = new long[nb_execs];
	long[] time_ha_keygen = new long[nb_execs];
	long[] time_setup_proxy = new long[nb_execs];
	long[] time_join_proxy = new long[nb_execs];
	long[] time_set_userIDha = new long[nb_execs]; 
	long[] time_user_keygen = new long[nb_execs];
	long[] time_set_CCM_U = new long[nb_execs];
	long[] time_s_PSign_S = new long[nb_execs];
	long[] time_P_sign_P = new long[nb_execs];
	long[] time_Sig_VerifyHA = new long[nb_execs];
	long[] time_CCM_VerifyHA = new long[nb_execs];

	long temps_total = 0;// Initialization of the test total computation time

	long start;//
	long end;

	/** ----------------------------------------------------- */

	@Test
	public void testGlobal() {
		System.out.println("Start test");
		long start_temps_total = System.nanoTime();

		TrustedAuthority ta;
		GroupManager gm;
		Proxy proxy;
		User user;
		Server server;
		HealthAuthority ha;

		for (int i = 0; i < nb_execs; i++) {

			/**
			 * set_params
			 */
			start = System.nanoTime();
			ta = new TrustedAuthority(type);
			end = System.nanoTime();
			time_set_params[i] = (long) (end - start);

			/**
			 * S_Keygen
			 */
			
			  start = System.nanoTime(); 
			  ta.s_keygen(); 
			  end = System.nanoTime();
			  time_s_keygen[i] = (long) (end - start);
			  
			 /**
			  * HA_KeyGen
				 */
			
			  start = System.nanoTime();
			  ta.ha_keygen(); 
			  end = System.nanoTime();
			  time_ha_keygen[i] = (long) (end - start);
			  
			 /**
			  * setup_proxy
			  */
			
			  start = System.nanoTime();
			  gm = new GroupManager(ta); 
			  end = System.nanoTime(); 
			  time_setup_proxy[i] = (long) (end - start);
			  
			 /**
				 * join_proxy
				 */
			
			  start = System.nanoTime(); 
			  proxy = new Proxy(ta, gm); 
			  end = System.nanoTime(); 
			  time_join_proxy[i] = (long) (end - start);
			  
			 /**
				 * set_userIDha
				 */
			
			  CommonReferenceString crs = gm.getDatabaseGM().getCrs(); ha = new
			  HealthAuthority(ta, crs);
			  
			  start = System.nanoTime(); 
			  ha.set_userIDha(); 
			  end = System.nanoTime(); 
			  time_set_userIDha[i] = (long) (end - start);
			  
			 /**
				 * UserKeyGen
				 */
			
			  
			  start = System.nanoTime(); 
			  user = new User(ta, ha); 
			  end = System.nanoTime();
			  time_user_keygen[i] = (long) (end - start);
			  
			 /**
				 * set_CCM_U
				 */
			
			  
			  start = System.nanoTime(); 
			  Element CCM = user.set_CCM_U(); 
			  end = System.nanoTime(); 
			  time_set_CCM_U[i] = (long) (end - start);
			  
			 /**
				 * s_PSign_S
				 */
			
			  server = new Server(ta);
			  
			  start = System.nanoTime(); 
			  Element[] spsign = server.s_PSign_S(CCM); // = {PS,PS'} 
			  end = System.nanoTime(); 
			  time_s_PSign_S[i] = (long) (end - start);
			  
			 /**
				 * P_sign_P
				 */
			
			  
			  start = System.nanoTime(); 
			  Contact psign = proxy.P_sign_P(spsign[0], user.getIDu()); 
			  end = System.nanoTime(); 
			  time_P_sign_P[i] = (long) (end - start);
			  user.add_contact(new ContactSigned(psign, spsign[1])); 
			  
			  
			 /**
				 * Sig_VerifyHA
				 */
			
			  
			  start = System.nanoTime();
			  ha.Sig_VerifyHA(user.getContact_list(), gm.getDatabaseGM().getCrs());
			  end = System.nanoTime();
			  time_Sig_VerifyHA[i] = (long) (end - start);
			  
			  
			  
			 /**
				 * CCM_VerifyHA
				 */
					  start = System.nanoTime(); 
					  ha.CCM_Verify(server, psign.getM(), spsign[1], user.gettU()); 
					  end = System.nanoTime();
					  time_CCM_VerifyHA[i] = (long) (end - start);
					  
					  ta.executorShutdown();
					 
		}

		long end_temps_total = System.nanoTime();
		temps_total = end_temps_total - start_temps_total;

	}

	@After
	public void after() {

		System.out.println("Tests performed with the pairing type " + type + " with a number of iterations of : " + nb_execs);


		System.out.println("set_params" + "\nmean in ms   " + String.valueOf(Mean.mean(time_set_params) / 1000000)
				+ "\nmedian in ms   " + String.valueOf(Median.median(time_set_params) / 1000000)
				+ "\nstandard deviation in ms   " + String.valueOf(StandardDeviation.standardDev(time_set_params) / 1000000));

		
		  System.out.println("s_keygen" + "\nmean in ms  " +
		  String.valueOf(Mean.mean(time_s_keygen) / 1000000) + "\nmedian in ms   " +
		  String.valueOf(Median.median(time_s_keygen) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_s_keygen) / 1000000));
		  
		  System.out.println("ha_keygen" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_ha_keygen) / 1000000) + "\nmedian in ms   " +
		  String.valueOf(Median.median(time_ha_keygen) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_ha_keygen) / 1000000));
		  
		  System.out.println("setup_proxy" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_setup_proxy) / 1000000) + "\nmedian in ms   "
		  + String.valueOf(Median.median(time_setup_proxy) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_setup_proxy) / 1000000));
		  
		  System.out.println("join_proxy" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_join_proxy) / 1000000) + "\nmedian in ms   " +
		  String.valueOf(Median.median(time_join_proxy) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_join_proxy) / 1000000));
		  
		  System.out.println("set_userIDha" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_set_userIDha) / 1000000) + "\nmedian in ms   "
		  + String.valueOf(Median.median(time_set_userIDha) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_set_userIDha) / 1000000));
		  
		  System.out.println("user_keygen" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_user_keygen) / 1000000) + "\nmedian in ms   "
		  + String.valueOf(Median.median(time_user_keygen) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_user_keygen) / 1000000));
		  
		  System.out.println("set_CCM_U" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_set_CCM_U) / 1000000) + "\nmedian in ms   " +
		  String.valueOf(Median.median(time_set_CCM_U) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_set_CCM_U) / 1000000));
		  
		  System.out.println("s_PSign_S" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_s_PSign_S) / 1000000) + "\nmedian in ms   " +
		  String.valueOf(Median.median(time_s_PSign_S) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_s_PSign_S) / 1000000));
		  
		  System.out.println("P_sign_P" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_P_sign_P) / 1000000) + "\nmedian in ms   " +
		  String.valueOf(Median.median(time_P_sign_P) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_P_sign_P) / 1000000));
		  
		  System.out.println("Sig_VerifyHA" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_Sig_VerifyHA) / 1000000) + "\nmedian in ms   "
		  + String.valueOf(Median.median(time_Sig_VerifyHA) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_Sig_VerifyHA) / 1000000));
		  
		  System.out.println("CCM_VerifyHA" + "\nmean in ms   " +
		  String.valueOf(Mean.mean(time_CCM_VerifyHA) / 1000000) + "\nmedian in ms   "
		  + String.valueOf(Median.median(time_CCM_VerifyHA) / 1000000) +
		  "\nstandard deviation in ms   " +
		  String.valueOf(StandardDeviation.standardDev(time_CCM_VerifyHA) / 1000000));
		  
		 		
		System.out.println("\nTotal computation time : " + String.valueOf(temps_total / 1000000000) + "s");

		System.out.println("\nTest performed on : " + java.util.Calendar.getInstance().getTime());
		System.out.println("\n-------------------------------------");
		System.out.println("\n-------------------------------------");


	}

}
