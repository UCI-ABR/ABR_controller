package KalmanFilterLibrary;

/**
 * Created by Joseph on 7/20/2017.
 */

public class Settings {
    static double delT = 1;                    //Sampling Time
    static double delX = 25, delY = 25;        //Observation Errors in Position
    static double  delVx = 6, delVy = 6;       //Observation Errors in Velocity
    static double delPosX = 20, delPosY = 20;  //Process Errors in Process Covariance Matrix
    static double delVelX = 5, delVelY = 5;        //Process Errors in Process Covariance Matrix

    //Kalman Filter Parameters
    static double[][] A = {{1,0,delT,0}, {0,1,0,delT},{0,0,1,0},{0,0,0,1}};
    static double[][] B = {{0.5 * Math.pow(delT,2),0},{0,0.5 * Math.pow(delT,2)},{delT,0},{0,delT}};
    static double[][] R = {{Math.pow(delX, 2),0,0,0}, {0, Math.pow(delY,2),0,0},{0,0,Math.pow(delVx,2),0},{0,0,0,Math.pow(delVy,2)}};

    static final double[] mu = {2,2};                        //Noise Sensor Covariance Matrix
    static final double[] w = {0,0,0,0}, Z = {0,0,0,0};      //Noise Parameters
    static final double[][] Q = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

    static final double[][] Imat = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};    //Identity Matrix
    static final double[][] H = Imat;                //Formatting Matrix (Same as Identity Matrix)
    static final double[][] F = Imat;                //Formatting Matrix (Same as Identity Matrix)
    public static double[][] Pmat =                          //Initial Process Covariance Matrix
            {{Math.pow(delPosX,2),delPosX * delPosY,delPosX * delVelX,delPosX * delVelY},
                    {delPosX * delPosY, Math.pow(delPosY,2),delPosY * delVelX,delPosY * delVelY },
                    {delPosX * delVelX,delPosY * delVelX,Math.pow(delVx,2),delVelX * delVelY},
                    {delPosX * delVelY,delPosY * delVelY,delVelX * delVelY,Math.pow(delVy,2)}};
}
