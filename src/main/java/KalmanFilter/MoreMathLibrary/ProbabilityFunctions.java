package KalmanFilter.MoreMathLibrary;

import KalmanFilter.Equations;

/**
 * Created by Joseph on 8/9/2017.
 *      Contains methods for calculating variance & standard deviation
 */

public class ProbabilityFunctions {
    private static Equations m_pmts = new Equations();
    private static int length = m_pmts.getStateLength();

    private static int number_of_values = 0;
    private static double[] parameter = new double[length];
    private static double[] total = new double[length];
    private static double[] deviation_squared_sum = new double[length];
    private static double[] average = new double[length];
    private static double[] deviation = new double[length];
    private static double[] variance = new double[length];
    private double[] standardDeviation = new double[length];

    public void setParameter(double[] variable) {
        System.arraycopy(variable,0,parameter,0,length);
        ++number_of_values;
        calculateDeviationSquaredSum();
    }

    private void calculateDeviationSquaredSum() {
        for (int i=0; i<length; i++) {
            total[i] = total[i] + parameter[i];
            average[i] = total[i] / number_of_values;
            deviation[i] = average[i] - parameter[i];
            deviation_squared_sum[i] += Math.pow(deviation[i],2);

            //Log.i("avg " + Integer.toString(i), Double.toString(average[i]));
        }
    }

    public double[] getVariance() {
        for (int i=0; i<length; i++) {
            variance[i] = (deviation_squared_sum[i] / number_of_values);

            //Log.i("var " + Integer.toString(i), Double.toString(variance[i]));
        }

        return variance;
    }

    public double[] getStandardDeviation() {
        for (int i=0; i<parameter.length; i++) {
            standardDeviation[i] = Math.sqrt(variance[i]);
        }
        return standardDeviation; }
}
