package KalmanFilterLibrary;


/**
 * Created by Joseph on 7/15/2017.
 * Contains methods necessary for handling kalman filter calculation including:
 *          -manipulating velocity values of current state matrix,
 *          -setting & manipulating input matrix for calculation, and
 *          -performing the calculation steps
 */

public class KalmanFilterCalculation extends KalmanFilterEquations{
    private float compass = 0;
    private double v_x = 0, v_y = 0;
    private static double[] startStateMatrix = new double[4];
    private static double[] currentStateMatrix = new double[4];
    private static double[][] inputMatrix = new double[startStateMatrix.length + 2][startStateMatrix.length];
    /*
            InputMatrix Layout:
            {{SMprev 1x4},
             {SMcurr 1x4},
             {Pprev 4x4}}
    */


    public void setCompass(float external_compass) {
        compass = external_compass;
    }

    //Set velocities of State Matrix
    public void setMeasuredVelocity(double x_velocity, double y_velocity) {
        currentStateMatrix[2] = x_velocity;
        currentStateMatrix[3] = y_velocity;
    }

    public double getMeasuredVelocityX() {
        //facing north or south
        if ((compass >= 330 && compass < 30) || (compass >= 150 && compass < 210)) {
            v_x = 0;
        }
        //facing east or west
        else if ((compass >= 60 && compass < 120) || (compass >= 240 && compass < 300)) {
            v_x = 0.12;
        }
        //facing north east or south east or south west or north west
        else if ((compass >= 30 && compass < 60) || (compass >= 120 && compass < 150) || (compass >= 210 && compass < 240) || (compass >= 300 && compass < 330)) {
            v_x = 0.12;
        }

        return v_x;
    }

    public double getMeasuredVelocityY() {
        //facing north or south
        if ((compass >= 330 && compass < 30) || (compass >= 150 && compass < 210)) {
            v_y = 0.12;
        }
        //facing east or west
        else if ((compass >= 60 && compass < 120) || (compass >= 240 && compass < 300)) {
            v_y = 0;
        }
        //facing north east or south east or south west or north west
        else if ((compass >= 30 && compass < 60) || (compass >= 120 && compass < 150) || (compass >= 210 && compass < 240) || (compass >= 300 && compass < 330)) {
            v_y = 0.12;
        }

        return v_y;
    }

    public void setStartSM(double[] stateMatrix){
        startStateMatrix = stateMatrix;
        createInputMatrix();
    }

    public void setCurrentSM(double[] stateMatrix) {
        currentStateMatrix = stateMatrix;
    }

    //Get current SM from 1st row of input matrix
    private double[] getCurrentSM() { return currentStateMatrix; }

    //Creates input matrix for kalman filter calculation
    private void createInputMatrix() {
        for (int row=0; row<startStateMatrix.length; row++) {
            inputMatrix[0][row] = startStateMatrix[row];
            inputMatrix[1][row] = 0;
        }

        for (int row=0; row<startStateMatrix.length; row++) {
            System.arraycopy(Pmat[row],0,inputMatrix[row + 2],0,startStateMatrix.length);
        }
    }

    //Place current SM into 2nd row of input matrix
    private void setInputMatrix() {
        System.arraycopy(currentStateMatrix,0,inputMatrix[1],0,currentStateMatrix.length);
    }

    //Calculate New Position and Process Covariance Matrices from Previous;
    //Assign New Values to 'prev' Matrices + Set SMcurr Nx1 to zero
    private void KalmanFilter() {
        //Initialize SM & P
        double[] SMprev = new double[inputMatrix[0].length];                            //Previous KF State Matrix
        double[] SMcurr = new double[inputMatrix[0].length];                             //Current Measure KF State Matrix
        double[][] Pprev = new double[inputMatrix[0].length][inputMatrix[0].length];    //Previous Process Covariance Matrix

        //Make SM & P to the Previous State + Get Current SM
        System.arraycopy(inputMatrix[0],0,SMprev,0,inputMatrix[0].length);
        System.arraycopy(currentStateMatrix,0,SMcurr,0,inputMatrix[0].length);

        for(int row=0; row<Pprev.length; row++) {
            System.arraycopy(inputMatrix[row + 2],0,Pprev[row],0,Pprev[row].length);
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
            inputMatrix[0][row] = Xk[row];
            inputMatrix[1][row] = 0;             //Reset SMcurr
        }

        for (int row=0; row<Pprev.length; row++) {
            System.arraycopy(Pk[row],0,inputMatrix[row + 2],0,Pprev[row].length);
        }
/*
        //EXTRA STEP: modify Xk to be w/i map bounds
        if (Xk[0] < 1) {
            Xk[0] = 1;
        }
        else if (Xk[0] > (map_size - 2)) {
            Xk[0] = (map_size - 2);
        }

        if (Xk[1] < 1) {
            Xk[1] = 1;
        }
        else if (Xk[1] > (map_size - 2)) {
            Xk[1] = (map_size - 2);
        }
*/
        //Output of state matrix
        System.arraycopy(Xk,0,currentStateMatrix,0,Xk.length);

    }

    public double[] getNextSM() {
        setInputMatrix();
        KalmanFilter();
        return getCurrentSM();
    }
}
