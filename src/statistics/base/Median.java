package statistics.base;

import java.util.Arrays;

public class Median {

	/**
	 * Utile pour calculer la médiane d'une liste
	 */
	private Median(){
		
	}
	
	public final static double median(int[] arr){
		Arrays.sort(arr);
		
		double tmpArray[] = new double[arr.length];
		Arrays.sort(arr);
		
		for(int i = 0; i < arr.length; i++){
			tmpArray[i] = arr[i];
		}
		
		return median(tmpArray);
	}
	
	public final static double median(int a, int ...b){
		int tmpArray[] = new int[b.length+1];
		tmpArray[0] = a;
		for(int i = 1; i <= b.length; i++){
			tmpArray[i] = b[i-1];
		}
		return median(tmpArray);
	}
	
	public final static double median(long[] arr){
		Arrays.sort(arr);
		double tmpArray[] = new double[arr.length];
		Arrays.sort(arr);
		
		for(int i = 0; i < arr.length; i++){
			tmpArray[i] = arr[i];
		}
		
		return median(tmpArray);
	}
	
	public final static double median(long a, long ...b){
		long tmpArray[] = new long[b.length+1];
		tmpArray[0] = a;
		for(int i = 1; i <= b.length; i++){
			tmpArray[i] = b[i-1];
		}
		return median(tmpArray);
	}
	
	public final static double median(float[] arr){
		double tmpArray[] = new double[arr.length];
		Arrays.sort(arr);
		for(int i = 0; i < arr.length; i++){
			tmpArray[i] = arr[i];
		}
		
		return median(tmpArray);
	}
	
	public final static double median(float a, float ...b){
		float tmpArray[] = new float[b.length+1];
		tmpArray[0] = a;
		for(int i = 1; i <= b.length; i++){
			tmpArray[i] = b[i-1];
		}
		return median(tmpArray);
	}
	
	public final static double median(double[] arr){
			
		//Sort the array.
		Arrays.sort(arr);
		
		int inputArraySize = arr.length;
		double medVal = 0;
		
		//Odd size array.
		if(arr.length%2==1){
			medVal = arr[(int)Math.floor(inputArraySize/2)];
		}
		//Even size array.
		else{
			medVal = (double)(arr[(inputArraySize/2)-1]+arr[(inputArraySize/2)])/2;
		}
		
		return medVal;
	}
	
	public final static double median(double a, double ...b){
		double tmpArray[] = new double[b.length+1];
		tmpArray[0] = a;
		for(int i = 1; i <= b.length; i++){
			tmpArray[i] = b[i-1];
		}
		return median(tmpArray);
	}
	
	
}
