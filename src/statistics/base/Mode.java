package statistics.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mode {
	
	/**
	 * Utile pour calculer le mode d'une liste
	 */
	private Mode(){
		
	}

	/** 
     * This method calculates the modal value of
	 * an input array. (int)
	 * 
	 * @param arr - The input array.
	 * @return int - The modal value.
	 */
	public final static int mode(int[] arr){
		double[] tmpArr = new double[arr.length];
		for(int i = 0; i < arr.length; i++){
			tmpArr[i] = (double)arr[i];
		}
		return (int)mode(tmpArr);
	}
	
	/**
	 * This method calculates the modal value of
	 * an input array. (long)
	 * 
	 * @param arr - The input array.
	 * @return long - The modal value.
	 */
	public final static long mode(long[] arr){
		double[] tmpArr = new double[arr.length];
		for(int i = 0; i < arr.length; i++){
			tmpArr[i] = (double)arr[i];
		}
		return (long)mode(tmpArr);
	}
	
	/**
	 * This method calculates the modal value of
	 * an input array. (float)
	 * 
	 * @param arr - The input array.
	 * @return float - The modal value.
	 */
	public final static float mode(float[] arr){
		double[] tmpArr = new double[arr.length];
		for(int i = 0; i < arr.length; i++){
			tmpArr[i] = (double)arr[i];
		}
		return (float)mode(tmpArr);
	}
	
	/**
	 * This method calculates the modal value of
	 * an input array. (double)
	 * 
	 * @param arr - The input array.
	 * @return double - The modal value.
	 */
	public final static double mode(double[] arr){
		//Loop through array and store values in map.
		Map<Double, Integer> modalMap = new HashMap<Double, Integer>();
		for(int i = 0; i < arr.length; i++){
			//If it isn't contained in the map.
			if(!modalMap.containsKey(arr[i])){
				modalMap.put(arr[i], 1);
			}
			//If it already exists in the map.
			else{
				modalMap.replace(arr[i], modalMap.get(arr[i])+1);
			}
		}
		//Get the count values and convert to array.
		Collection<Integer> x = modalMap.values();
		List<Double> keys = new ArrayList<Double>(modalMap.keySet());
		Object[] y = x.toArray();
		int currMaxIndex = 0;
		
		for(int i = 0; i < x.size(); i++){
			//There are 2 modal values.
			if((Integer)y[i] == (Integer)y[currMaxIndex]){
				currMaxIndex = keys.get(currMaxIndex) < keys.get(i) ? currMaxIndex : i;
			}
			
			//The count at the current index is equal.
			else if((Integer)y[i] > (Integer)y[currMaxIndex]){
				currMaxIndex=i;
			}
		}
		
		//Mode returns the lowest value number with the greatest no.
		//of occurences.
		return keys.get(currMaxIndex);
	}
	
}
