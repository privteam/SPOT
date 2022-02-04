/**
 * Class: JavaStat.base.BasicStats 
 * 
 * @author Ravindra Bhadti
 * @V0.2
 */

package statistics.base;

import static statistics.base.Median.*;

public strictfp class Quartile{
	
	/**
	 * Utile pour calculer les quartiles d'une liste
	 */
	private Quartile(){
		//No constructor body.
	}

	/**
	 * This method calculates the interquartile range
	 * of a 1D data-set.
	 * 
	 * @param arr - The input data array.
	 * @return double - The IQR value
	 */
	public final static double IQR(double arr[]){
		double[] quartiles = Quartiles(arr);

		return quartiles[2]-quartiles[0];
	}
	
	/**
	 * This method calculates the lower, median and upper 
	 * quartiles of the 1D data-set.
	 * 
	 * @param arr - The input data array.
	 * @return double - [q1, q2, q3] returns an array of quartiles.
	 */
	public final static double[] Quartiles(double arr[]){
		
		double quartiles[] = new double[3];
		
		int numElements = arr.length;
		
		double lowerHalf[] = new double[(int)Math.floor(numElements/2)];
		double upperHalf[] = new double[(int)Math.floor(numElements/2)];
		
		for(int i = 0; i < Math.floor(numElements/2); i++){
			lowerHalf[i] = arr[i];
			upperHalf[i] = arr[numElements-1-i];
		}
		
		quartiles[0] = median(lowerHalf);
		quartiles[1] = median(arr);
		quartiles[2] = median(upperHalf);
		
		return quartiles;
	}
	
}

