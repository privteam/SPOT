package statistics.base;

import java.util.HashMap;

public class Mean {
	
	/**
	 * Utile pour calculer la moyenne d'une liste
	 */
	private Mean(){
		
	}
	
	/**
	 * The var-args mean method, it can take 1 or
	 * more integers and return the mean.
	 * 
	 * @param a - A single integer.
	 * @param b - A variable number of integers.
	 * @return double - The mean the list a,b[0],b[1],...,b[N-1].
	 */
	public final static double mean(int a, int ...b){ 
		return ((mean(b)*b.length)+a)/(b.length+1);
	}
	
	/**
	 * The var-args mean method, it can take 1 or
	 * more integers and return the mean.
	 * 
	 * @param  arr[] -  An array of integers. 
	 * @return double - The mean the list arr[0],arr[1],...,arr[N-1].
	 */
	public final static double mean(int[] arr){
		double arrSum = 0;
		for(int i = 0; i < arr.length; i++){
			arrSum+=arr[i];
		}
		return arr.length!=0 ? (arrSum/arr.length) : 0;
	}
	
	/**
	 * The var-args mean method, it can take 1 or
	 * more longs and return the mean.
	 * 
	 * @param a - A single long.
	 * @param b - A variable number of long.
	 * @return double - The mean the list a,b[0],b[1],...,b[N-1].
	 */
	public final static double mean(long a, long ...b){
		return ((mean(b)*b.length)+a)/(b.length+1);
	}
	
	/**
	 * This method returns the mean of an array
	 * of longs.
	 * 
	 * @param  arr[] -  An array of longs. 
	 * @return double - The mean the list arr[0],arr[1],...,arr[N-1].
	 */
	public final static double mean(long[] arr){
		double arrSum = 0;
		for(int i = 0; i < arr.length; i++){
			arrSum+=arr[i];
		}
		return arr.length!=0 ? (arrSum/arr.length) : 0;
	}
	
	/**
	 * The var-args mean method, it can take 1 or
	 * more floats and return the mean.
	 * 
	 * @param a - A single float.
	 * @param b - A variable number of float.
	 * @return double - The mean the list a,b[0],b[1],...,b[N-1].
	 */
	public final static double mean(float a, float ...b){
		return ((mean(b)*b.length)+a)/(b.length+1);
	}
	
	/**
	 * This method returns the mean of an array
	 * of floats.
	 * 
	 * @param  arr[] -  An array of floats. 
	 * @return double - The mean the list arr[0],arr[1],...,arr[N-1].
	 */
	public final static double mean(float[] arr){
		double arrSum = 0;
		for(int i = 0; i < arr.length; i++){
			arrSum+=arr[i];
		}
		return arr.length!=0 ? (arrSum/arr.length) : 0;
	}
	
	/**
	 * The var-args mean method, it can take 1 or
	 * more floats and return the mean.
	 * 
	 * @param a - A single double.
	 * @param b - A variable number of double.
	 * @return double - The mean the list a,b[0],b[1],...,b[N-1].
	 */
	public final static double mean(double a, double ...b){
		return ((mean(b)*b.length)+a)/(b.length+1);
	}
	
	/**
	 * This method returns the mean of an array
	 * of doubles.
	 * 
	 * @param  arr[] -  An array of floats. 
	 * @return double - The mean the list arr[0],arr[1],...,arr[N-1].
	 */
	public final static double mean(double[] arr){
		double arrSum = 0;
		for(int i = 0; i < arr.length; i++){
			arrSum+=arr[i];
		}
		return arr.length!=0 ? (arrSum/arr.length) : 0;
	}
	
	/**
	 * This method returns the mean of multiple numbers of
	 * type T. Where T is the wrapper Number object type.
	 * 
	 * @param arr - The input array.
	 * @return - Returns the mean as type T.
	 */
	@SafeVarargs
	public final static <T extends Number> double mean(T a, T ...b){
		return ((mean(b)*b.length)+a.doubleValue())/(b.length+1);
	}
	
	/**
	 * This method returns the mean of an array of type T.
 	 * Where T is the wrapper Number object type.
	 * 
	 * @param hmap
	 * @return
	 */
	public final static <T extends Number> double mean( T[] arr){
		Double arrSum = 0.0;
		for(int i = 0; i < arr.length; i++){
			arrSum += arr[i].doubleValue();
		}
		return arr.length!=0 ? (arrSum/arr.length) : 0;
	}
	
	/**
	 * This method returns the mean value obtained
	 * from a frequency table.
	 * 
	 * |Value(s)|Frequency|
	 * |	x1	|	y1	  |
	 * |	x2	|	y2	  |
	 * |    x3	|   y3    |
	 * |________|_________|
	 * 
	 * @param hmap - A map of the table in wrapper type form.
	 * @return T - The mean value as wrapper type.
	 */
	public final static <T extends Number> T mean( HashMap<T,T> hmap){
		return null;
	}

}
