package KalmanFilterLibrary;

/**
 * Created by Joseph on 7/20/2017.
 * Defines all equations used in kalman filter calculation
 */

public class KalmanFilterEquations extends Settings {
    //New State Prediction
    double[] Xpre(double[] Xp) {
        return Add2MxN(Add2MxN(MultNx1(A,Xp), MultNx1(B,mu)), w);
    }

    double[][] Ppre(double[][] Pp) {
        return Add2MxN(Mult2NxN(Mult2NxN(A,Pp), TransNxN(A)), Q);
    }

    //Measurement Input
    double[] Y(double[] Xmeasured) {
        return Add2MxN(MultNx1(F, Xmeasured), Z);
    }

    //Updated State Predictions with Measurement
    double[][] KG(double[][] P) {
        double[][] numerator = Mult2NxN(P,TransNxN(H));
        double[][] denominator = Add2MxN(Mult2NxN(Mult2NxN(H,P),TransNxN(H)),R);
        return Divide2MxN(numerator,denominator);
    }

    double[] Xupdated(double[] Xpredicted, double[][] P, double[] y) {
        return Add2MxN(Xpredicted,MultNx1(KG(P),Sub2MxN(y,MultNx1(H,Xpredicted))));
    }

    double[][] Pupdated(double[][] P) {
        return Mult2NxN(Sub2MxN(Imat,Mult2NxN(KG(P),H)),P);
    }


}
