package KalmanFilterLibrary;

import static KalmanFilterLibrary.MatrixOperations.Add2Nx1;
import static KalmanFilterLibrary.MatrixOperations.Add2NxN;
import static KalmanFilterLibrary.MatrixOperations.Divide2NxN;
import static KalmanFilterLibrary.MatrixOperations.Mult2NxN;
import static KalmanFilterLibrary.MatrixOperations.MultNx1;
import static KalmanFilterLibrary.MatrixOperations.Sub2Nx1;
import static KalmanFilterLibrary.MatrixOperations.Sub2NxN;
import static KalmanFilterLibrary.MatrixOperations.TransNxN;

/**
 * Created by Joseph on 7/20/2017.
 */

public class KalmanFilterEquations extends Settings {
    //New State Prediction
    static double[] Xpre(double[] Xp) {
        return Add2Nx1(Add2Nx1(MultNx1(A,Xp), MultNx1(B,mu)), w);
    }

    static double[][] Ppre(double[][] Pp) {
        return Add2NxN(Mult2NxN(Mult2NxN(A,Pp), TransNxN(A)), Q);
    }

    //Measurement Input
    static double[] Y(double[] Xmeasured) {
        return Add2Nx1(MultNx1(F, Xmeasured), Z);
    }

    //Updated State Predictions with Measurement
    static double[][] KG(double[][] P) {
        double[][] numerator = Mult2NxN(P,TransNxN(H));
        double[][] denominator = Add2NxN(Mult2NxN(Mult2NxN(H,P),TransNxN(H)),R);
        return Divide2NxN(numerator,denominator);
    }

    static double[] Xupdated(double[] Xpredicted, double[][] P, double[] y) {
        return Add2Nx1(Xpredicted,MultNx1(KG(P),Sub2Nx1(y,MultNx1(H,Xpredicted))));
    }

    static double[][] Pupdated(double[][] P) {
        return Mult2NxN(Sub2NxN(Imat,Mult2NxN(KG(P),H)),P);
    }
}
