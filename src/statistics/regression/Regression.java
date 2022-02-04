/**
 * 
 * Class: JavaStat.base.Distributions
 * 
 * @author Ravindra Bhadti
 * @V0.2
 */

package statistics.regression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static statistics.base.Mean.*;
import static statistics.base.StandardDeviation.*;

public strictfp class Regression {

	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double pearsonCoefficient(double X[], double Y[]){
		
		assert X.length == Y.length : "The input array X & Y should be equal in length";
		
		int n = X.length;
		
		double meanX = mean(X);
		double meanY = mean(Y);
		
		double stdX  = standardDev(X);
		double stdY  = standardDev(Y);
		
		double covXY = 0;
		
		for(int i = 0; i < n; i++){
			covXY += (X[i]-meanX)*(Y[i]-meanY);
		}
		
		return covXY/(n*stdX*stdY);
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double pearsonCoefficient(int X[], int Y[]){
		
		double xTemp[] = new double[X.length];
		double yTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			xTemp[i] = X[i];
			yTemp[i] = Y[i];
		}
		
		return pearsonCoefficient(xTemp, yTemp);
		
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double pearsonCoefficient(long X[], long Y[]){
		double xTemp[] = new double[X.length];
		double yTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			xTemp[i] = X[i];
			yTemp[i] = Y[i];
		}
		
		return pearsonCoefficient(xTemp, yTemp);
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double pearsonCoefficient(float X[], float Y[]){
		double xTemp[] = new double[X.length];
		double yTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			xTemp[i] = X[i];
			yTemp[i] = Y[i];
		}
		
		return pearsonCoefficient(xTemp, yTemp);
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return Double
	 */
	public final static <T extends Number> Double pearsonCoefficient(T X[], T Y[]){
		
		double xTemp[] = new double[X.length];
		double yTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			xTemp[i] = X[i].doubleValue();
			yTemp[i] = Y[i].doubleValue();
		}		
		return pearsonCoefficient(xTemp, yTemp);
	}
	
	/* ----------- SPEARMAN RANK'S CORRELATION COEFFICIENT ---------*/
	
	/**
	 * 
	 * @param X -
	 * @param Y -
	 * @return double - 
	 */
	public final static double spearmanRank(double X[], double Y[]){
		
		assert X.length == Y.length : "The input array X & Y should be equal in length";
		
		int n = X.length;
		
		//Make copy of the two arrays.
		double XTemp[] = Arrays.copyOf(X, X.length);
		double YTemp[] = Arrays.copyOf(Y, Y.length);
		
		//Map contains X array values and its Rank.
		Map <Double, Integer> xMap = new HashMap<Double, Integer>();
	
		//Map contains Y array values and its Rank.
		Map <Double, Integer> yMap = new HashMap<Double, Integer>();
	
		//Sorted array added into a list.
		Arrays.sort(XTemp);
		Arrays.sort(YTemp);
		ArrayList<Double> xList = new ArrayList<Double>();
		ArrayList<Double> yList = new ArrayList<Double>();
		
		for(int i = 0; i < n; i++){
			xList.add(Double.valueOf(XTemp[i]));
			yList.add(Double.valueOf(YTemp[i]));
		}
		
		XTemp = null;
		YTemp = null;
		
		//Store rank of X & Y elements.
		for(int i = 0; i < n; i++){
			//Rank is the index of the sorted element + 1.
			xMap.put(Double.valueOf(X[i]) , xList.indexOf(X[i])+1);
			yMap.put(Double.valueOf(Y[i]) , yList.indexOf(Y[i])+1);
		}
		
		//Calculate sum(d_i)^2.
		double sumDiPow2 = 0;
		for(int i = 0; i < n; i++){
			sumDiPow2 += Math.pow( xMap.get(X[i])-yMap.get(Y[i]) , 2 );
		}
		
		return (1 - (6*sumDiPow2)/(n*(n*n - 1)) );
	}
	
	/**
	 * 
	 * @param X - 
	 * @param Y -
	 * @return double - 
	 */
	public final static double spearmanRank(int X[], int Y[]){
		
		assert X.length == Y.length : "The input array X & Y should be equal in length";
		
		int n = X.length;
		
		double XTemp[] = new double[X.length];
		double YTemp[] = new double[Y.length];
		
		for(int i = 0; i < n; i++){
			XTemp[i] = X[i];
			YTemp[i] = Y[i];
		}
		return spearmanRank(XTemp, YTemp);
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double spearmanRank(long X[], long Y[]){
		
		assert X.length == Y.length : "The input array X & Y should be equal in length";
		
		int n = X.length;
		
		double XTemp[] = new double[X.length];
		double YTemp[] = new double[Y.length];
		
		for(int i = 0; i < n; i++){
			XTemp[i] = X[i];
			YTemp[i] = Y[i];
		}
		return spearmanRank(XTemp, YTemp);
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double spearmanRank(float X[], float Y[]){
		
		assert X.length == Y.length : "The input array X & Y should be equal in length";
		
		int n = X.length;
		
		double XTemp[] = new double[X.length];
		double YTemp[] = new double[Y.length];
		
		for(int i = 0; i < n; i++){
			XTemp[i] = X[i];
			YTemp[i] = Y[i];
		}
		return spearmanRank(XTemp, YTemp);
	}
	
	/**
	 * 
	 * @param X -
	 * @param Y -
	 * @return Double - 
	 */
	public final static <T extends Number> Double spearmanRank(T X[], T Y[]){
		
		double XTemp[] = new double[X.length];
		double YTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			XTemp[i] = X[i].doubleValue();
			YTemp[i] = Y[i].doubleValue();
		}
		
		return Double.valueOf(spearmanRank(XTemp, YTemp));
		
	}
	
	
	/* -------------- LEAST SQUARE REGRESSION LINE -------------- */
	
	/**
	 * 
	 * @param X - An array of X-coordinates .
	 * @param Y - An array of Y-coordinates.
	 * @return double[] - The gradient and constant parameter. 
	 */
	public final static double[] leastSquareRegression(double X[], double Y[]){
		
		double prms[] = new double[2];
		
		//Using simple method.
		double pearsonCoefficient = pearsonCoefficient(X, Y);
		
		//The gradient value.
		prms[0] = pearsonCoefficient*(standardDev(Y)/standardDev(X));
		
		//The offset value.
		prms[1] = mean(Y) - prms[0]*mean(X);
		
		return prms;
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double[] leastSquareRegression(int X[], int Y[]){
		
		double XTemp[] = new double[X.length];
		double YTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			XTemp[i] = X[i];
			YTemp[i] = Y[i];
		}
		
		return leastSquareRegression(XTemp, YTemp);
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double[] leastSquareRegression(long X[], long Y[]){
		
		double XTemp[] = new double[X.length];
		double YTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			XTemp[i] = X[i];
			YTemp[i] = Y[i];
		}
		
		return leastSquareRegression(XTemp, YTemp);
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public final static double[] leastSquareRegression(float X[], float Y[]){
		
		double XTemp[] = new double[X.length];
		double YTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			XTemp[i] = X[i];
			YTemp[i] = Y[i];
		}
		
		return leastSquareRegression(XTemp, YTemp);
	}
	
	/**
	 * 
	 * 
	 * @param X -
	 * @param Y - 
	 * @return Double[] - 
	 */
	public final static <T extends Number> Double[] leastSquareRegression(T X[], T Y[]){
		
		double XTemp[] = new double[X.length];
		double YTemp[] = new double[Y.length];
		
		for(int i = 0; i < X.length; i++){
			XTemp[i] = X[i].doubleValue();
			YTemp[i] = Y[i].doubleValue();
		}
		
		double res[] = leastSquareRegression(XTemp, YTemp);
		
		Double returnVal[] = {Double.valueOf(res[0]),Double.valueOf(res[1])};
		
		return returnVal;
	}
	
	
	
}
