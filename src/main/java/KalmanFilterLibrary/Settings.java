package KalmanFilterLibrary;

/**
 * Created by Joseph on 7/20/2017.
 */

public class Settings extends MatrixOperations{
    private double delT = .010;                    //Sampling Time
    private double delX = 0.5, delY = 0.5;        //Observation Errors in Position
    public double  delVx = 0.2, delVy = 0.2;       //Observation Errors in Velocity
    private double delPosX = 0.5, delPosY = 0.5;  //Process Errors in Process Covariance Matrix
    private double delVelX = 0.1, delVelY = 0.1;        //Process Errors in Process Covariance Matrix

    //Kalman Filter Parameters
    double[][] A = {{1,0,delT,0}, {0,1,0,delT},{0,0,1,0},{0,0,0,1}};
    double[][] B = {{0.5 * Math.pow(delT,2),0},{0,0.5 * Math.pow(delT,2)},{delT,0},{0,delT}};
    double[][] R = {{Math.pow(delX, 2),0,0,0}, {0, Math.pow(delY,2),0,0},{0,0, Math.pow(delVx,2),0},{0,0,0, Math.pow(delVy,2)}};

    final double[] mu = {0,0};                        //Noise Sensor Covariance Matrix
    final double[] w = {0,0,0,0}, Z = {0,0,0,0};      //Noise Parameters
    final double[][] Q = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

    final double[][] Imat = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};    //Identity Matrix
    final double[][] H = Imat;                //Formatting Matrix (Same as Identity Matrix)
    final double[][] F = Imat;                //Formatting Matrix (Same as Identity Matrix)
    public double[][] Pmat =                          //Initial Process Covariance Matrix
            {{Math.pow(delPosX,2),delPosX * delPosY,delPosX * delVelX,delPosX * delVelY},
                    {delPosX * delPosY, Math.pow(delPosY,2),delPosY * delVelX,delPosY * delVelY },
                    {delPosX * delVelX,delPosY * delVelX, Math.pow(delVelX,2),delVelX * delVelY},
                    {delPosX * delVelY,delPosY * delVelY,delVelX * delVelY, Math.pow(delVelY,2)}};
}
