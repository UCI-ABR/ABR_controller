package KalmanFilter;

import KalmanFilter.MoreMathLibrary.MatrixOperations;

/**
 * Created by Joseph on 8/10/2017.
 * Settings for 2D extended kalman filter; adds:
 *          -filtering for compass
 *          -method for setting velocity & angle variables
 *          -Jacobian matrix for process covariance linearization
 *          -expanded matrices for handle the additional inputs
 *
 *          State Matrix Layout: {x_position, y_position, compass_orientation, x_velocity, y_velocity, angular_velocity}
 */

public class Parameters2 extends MatrixOperations {
    static int stateLength = 6;

    private double delT = .010;                    //Sampling Time [s]

    //Calculation Speeds
    private double forwardSpeed = 1.0827;                   //[cm/s]
    private double forwardStop = (forwardSpeed / 100);      //[cm/s]
    private double turnSpeed = 0.795;                       //[rad/s]
    private double turnStop = (turnSpeed / 100);            //[rad/s]

    //Variance Errors in Process Covariance Matrix
    private double varPx = Math.pow(0.779,2);
    private double varPy = Math.pow(0.517,2);
    private double varTheta = Math.pow(0.56,2);
    private double varVx = Math.pow(0.48,2);
    private double varVy = Math.pow(0.511,2);
    private double varAv = Math.pow(0.527,2);

    //Variance Errors in Measurement Noise Covariance Matrix
    double varDelErrors[] = new double[stateLength];

    //Kalman Filter Parameters
    double[][] A =
            {{1,0,0,delT,0,0},
            {0,1,0,0,delT,0},
            {0,0,1,0,0,delT},
            {0,0,0,1,0,0},
            {0,0,0,0,1,0},
            {0,0,0,0,0,1}};
    double[][] B =
            {{0.5 * Math.pow(delT,2),0,0},
            {0,0.5 * Math.pow(delT,2),0},
            {0,0,0.5 * Math.pow(delT,2)},
            {delT,0,0},
            {0,delT,0},
            {0,0,delT}};
    double[][] R = //Noise Sensor Covariance Matrix
            {{varDelErrors[0],0,0,0,0,0},
            {0,varDelErrors[1],0,0,0,0},
            {0,0,varDelErrors[2],0,0,0},
            {0,0,0,varDelErrors[3],0,0},
            {0,0,0,0,varDelErrors[4],0},
            {0,0,0,0,0,varDelErrors[5]}};

    final double[] mu = //Control variable matrix (acceleration)
            {0,0,0};
    final double[]  //Noise Parameters
            w = {0,0,0,0,0,0},
            Z = {0,0,0,0,0,0};
    final double[][] Q =
            {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},
            {0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
    final double[][] Imat = //Identity Matrix
            {{1,0,0,0,0,0}, {0,1,0,0,0,0}, {0,0,1,0,0,0},
            {0,0,0,1,0,0}, {0,0,0,0,1,0}, {0,0,0,0,0,1}};
    final double[][] //Formatting Matrices (Same as Identity Matrix)
            H = Imat,
            F = Imat;
    double[][] Pmat = //Initial Process Covariance Matrix
            {{varPx,0,0,0,0,0},
            {0,varPy,0,0,0,0},
            {0,0,varTheta,0,0,0},
            {0,0,0,varVx,0,0},
            {0,0,0,0,varVy,0},
            {0,0,0,0,0,varAv}};

    public int getStateLength() { return stateLength; }
    public double getForwardSpeed() { return forwardSpeed; }
    public double getForwardStop() { return forwardStop; }
    public double getTurnSpeed() { return turnSpeed; }
    public double getTurnStop() { return turnStop; }
}
