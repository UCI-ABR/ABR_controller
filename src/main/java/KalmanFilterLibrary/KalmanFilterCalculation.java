package KalmanFilterLibrary;

/**
 * Created by Joseph on 7/15/2017.
 */

public class KalmanFilterCalculation extends KalmanFilterEquations{
    //Creates & Initializes Input Matrix for Kalman Filter using
    //StartStateMAt for Xprev & Pmat (from Settings) for Pprev
    //Xcurr is initialized to zero (to be set by sensor readings)
    public static double[][] initializeInputMatrix(double[] StartStateMat) {
        double[][] InputMat = new double[StartStateMat.length + 2][StartStateMat.length];

        for (int row=0; row<StartStateMat.length; row++) {
            InputMat[0][row] = StartStateMat[row];
            InputMat[1][row] = 0;
        }

        for (int row=0; row<StartStateMat.length; row++) {
            for (int col=0; col<StartStateMat.length; col++) {
                InputMat[row + 2][col] = Settings.Pmat[row][col];
            }
        }

        return InputMat;
    }

    //Calculate New Position and Process Covariance Matrices from Previous;
    //Assign New Values to 'prev' Matrices + Set SMcurr Nx1 to zero
    public static double[][] KalmanFilter(double[][] InputMat) {
        /*
            InputMatrix Layout:
            {{SMprev 1x4},
             {SMcurr 1x4},
             {Pprev 4x4}}
        */
        //Initialize SM & P
        double[] SMprev = new double[InputMat[0].length];                            //Previous Measured KF State Matrix
        double[] SMcurr = new double[InputMat[0].length];                            //Current Measured KF State Matrix
        double[][] Pprev = new double[InputMat[0].length][InputMat[0].length];       //Previous Process Covariance Matrix

        //Make SM & P to the Previous State + Get Current SM
        for(int row=0; row<SMprev.length; row++) {
            SMprev[row] = InputMat[0][row];
            SMcurr[row] = InputMat[1][row];
        }

        for(int row=0; row<Pprev.length; row++) {
            for (int col=0; col<Pprev[row].length; col++) {
                Pprev[row][col] = InputMat[row + 2][col];
            }
        }

        //Calculate Predicted State
        double[] Xkp = Xpre(SMprev);            //Predicted State Matrix
        double[][] Pkp = Ppre(Pprev);          //Predicted KF Process Covariance Matrix

        //Calculate Kalman Gain
        double[][] Gain = KG(Pkp);             //Current Kalman Gain

        //Calculate Measurement Input
        double[] Yk = Y(SMcurr);                //Measured State Matrix

        //Update Predicted States w/ New Measurements
        double[] Xk = Xupdated(Xkp, Gain, Yk);         //Current KF State Matrix
        double[][] Pk = Pupdated(Pkp);                 //Current KF Process Covariance Matrix

        //Current Becomes Previous
        for (int row=0; row<SMprev.length; row++) {
            InputMat[0][row] = Xk[row];
            InputMat[1][row] = 0;             //Reset SMcurr
        }

        for (int row=0; row<Pprev.length; row++) {
            for (int col=0; col<Pprev[row].length; col++) {
                InputMat[row + 2][col] = Pk[row][col];
            }
        }
        //Output of Updated State & Process Covariance Matrix;
        return InputMat;
    }

    //Input SMcurr into 2nd Row of InputMat
    public static double[][] setCurrStateMatrix(double[][] InputMat, double[] StateMat) {
        for (int row=0; row<StateMat.length; row++) {
            InputMat[1][row] = StateMat[row];
        }

        return InputMat;
    }

    //Get SMcurr from 2nd Row of InputMat
    public static double[] getCurrStateMatrix(double[][] InputMat) {
        double[] SMcurr = new double[InputMat[0].length];

        for(int row=0; row<InputMat[0].length; row++) {
            SMcurr[row] = InputMat[1][row];
        }

        return SMcurr;
    }
}
