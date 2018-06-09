package KalmanFilter;

import android.util.Log;

import KalmanFilter.MoreMathLibrary.ProbabilityFunctions;
import LocationInfo.Variables;

/**
 * Created by Joseph on 7/15/2017.
 * Contains methods necessary for handling kalman filter calculation including:
 *          -manipulating velocity values of current state matrix,
 *          -setting & manipulating input matrix for calculation, and
 *          -performing the calculation steps
 *
 * InputMatrix Layout: {Previous State Matrix}
 *                     {Current State Matrix}
 *                     {Process Covariance Matrix}
 *
 * Setup Methods:
 *              -setStartSM
 * Output Methods:
 *              -kalmanFilter
 */

public class Calculation extends Equations {
    private static double[] startStateMatrix = new double[stateLength];
    private static double[] currentStateMatrix = new double[stateLength];
    private static double[] measuredStateMatrix = new double[stateLength];
    private static double[][] inputMatrix = new double[stateLength + 2][stateLength];

    private ProbabilityFunctions m_prob = new ProbabilityFunctions();

    public void setStartSM(double[] stateMatrix){
        startStateMatrix = stateMatrix;
        createInputMatrix();
    }

    private void updateSensorNoiseCovarianceMatrix() {
        m_prob.setParameter(measuredStateMatrix);
        double[] observationError = m_prob.getVariance();
        double[] observationSTD = m_prob.getStandardDeviation();

        System.arraycopy(observationError,0,varDelErrors,0,stateLength);

        Log.i("kf: X", Double.toString(observationSTD[0]));
        Log.i("kf: Y", Double.toString(observationSTD[1]));
//        Log.i("kf: Rads", Double.toString(observationSTD[2]));
        Log.i("kf: VX", Double.toString(observationSTD[2]));
        Log.i("kf: VY", Double.toString(observationSTD[3]));
//        Log.i("kf: AV", Double.toString(observationSTD[5]));
    }

    //Create input matrix for kalman filter calculation
    private void createInputMatrix() {
        for (int row=0; row<stateLength; row++) {
            inputMatrix[0][row] = startStateMatrix[row];
            inputMatrix[1][row] = 0;
        }

        for (int row=0; row<stateLength; row++) {
            System.arraycopy(Pmat[row],0,inputMatrix[row + 2],0,stateLength);
        }
    }

    //Place current SM into 2nd row of input matrix
    private void setInputMatrix() {
        System.arraycopy(measuredStateMatrix,0,inputMatrix[1],0,stateLength);
    }

    //Calculate New Position and Process Covariance Matrices from Previous;
    //Assign New Values to 'prev' Matrices + Set SMcurr Nx1 to zero
    private void performCalculation() {
        //Initialize SM & P
        double[] SMprev = new double[stateLength];                  //Previous KF State Matrix
        double[] SMmea = new double[stateLength];                   //Current Measured KF State Matrix
        double[][] Pprev = new double[stateLength][stateLength];    //Previous Process Covariance Matrix

        //Make SM & P to the Previous State + Get Current SM
        System.arraycopy(inputMatrix[0],0,SMprev,0,stateLength);
        System.arraycopy(inputMatrix[1],0,SMmea,0,stateLength);

        for(int row=0; row<stateLength; row++) {
            System.arraycopy(inputMatrix[row + 2],0,Pprev[row],0,stateLength);
        }

        //Calculate Predicted State
        double[] Xkp = Xpre(SMprev);            //Predicted State Matrix
        double[][] Pkp = Ppre(Pprev);          //Predicted KF Process Covariance Matrix

        //Calculate Kalman Gain
        double[][] Gain = KG(Pkp);             //Current Kalman Gain

        //Calculate Measurement Input
        double[] Yk = Y(SMmea);                //Measured State Matrix

        //Update Predicted States w/ New Measurements
        double[] Xk = Xupdated(Xkp,Gain,Yk);         //Current KF State Matrix
        double[][] Pk = Pupdated(Pkp);                 //Current KF Process Covariance Matrix

        //EXTRA STEP:
        // modify Xk to be w/i map bounds
        if (Xk[0] < 1) {
            Xk[0] = 1;
        } else if (Xk[0] > (Variables.map_size - 2)) {
            Xk[0] = (Variables.map_size - 2);
        }

        if (Xk[1] < 1) {
            Xk[1] = 1;
        } else if (Xk[1] > (Variables.map_size - 2)) {
            Xk[1] = (Variables.map_size - 2);
        }

        //modify compass to be [0,360]
        if (Xk[2] < 0) {
            Xk[2] = Xk[2] + (2 * Math.PI);
        } else if (Xk[2] > (2 * Math.PI)) {
            Xk[2] = Xk[2] - (2 * Math.PI);
        }

        //Output of state matrix estimation
        System.arraycopy(Xk,0,currentStateMatrix,0,stateLength);

        //Current Becomes Previous
        for (int row=0; row<stateLength; row++) {
            inputMatrix[0][row] = Xk[row];
            inputMatrix[1][row] = 0;             //Reset SMcurr
        }

        for (int row=0; row<stateLength; row++) {
            System.arraycopy(Pk[row],0,inputMatrix[row + 2],0,stateLength);
        }
    }

    public double[] kalmanFilter (double[] stateMatrix) {
        measuredStateMatrix = stateMatrix;
        updateSensorNoiseCovarianceMatrix();

        setInputMatrix();
        Log.i("kf: POS_IN","{" + measuredStateMatrix[0] + ", " + measuredStateMatrix[1] + "}");
        Log.i("kf: SPD_IN","{" + measuredStateMatrix[2] + ", " + measuredStateMatrix[3] + "}");

        performCalculation();

        Log.i("kf: POS_OUT","{" + currentStateMatrix[0] + ", " + currentStateMatrix[1] +"}");
        Log.i("kf: SPD_OUT","{" + currentStateMatrix[2] + ", " + currentStateMatrix[3] +"}");
        return currentStateMatrix;
    }
}
