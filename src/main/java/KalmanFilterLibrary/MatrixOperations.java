package KalmanFilterLibrary;


/**
 * Created by Joseph on 7/13/2017.
 */

public class MatrixOperations {
    //Initialize an MxN Zero Matrix
    static double[][] ZeroMxN(int M, int N) {
        double[][] ZeroMat = new double[M][N];

        for(int row=0; row<M; row++) {
            for (int col=0; col<N; col++) {
                ZeroMat[row][col] = 0;
            }
        }
        return ZeroMat;
    }

    //Initialize an Nx1 Zero Matrix
    static double[] ZeroNx1(int N) {
        double[]ZeroMat = new double[N];

        for(int row=0; row<N; row++) {
            ZeroMat[row] = 0;
        }
        return ZeroMat;
    }

    //Transpose NxN Matrix
    static double[][] TransNxN(double[][] M1) {
        double[][] result = ZeroMxN(M1.length,M1[0].length);

        for(int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                result[row][col] = M1[col][row];
            }
        }
        return result;
    }

    //Multiply MxN by Nx1 Matrices, Get Mx1
    static double[] MultNx1(double[][] M1, double[] M2) {
        double[] result = ZeroNx1(M1.length);

        for(int row=0; row<M1.length; row++) {
            for(int col=0; col<M2.length; col++) {
                result[row] += M1[row][col] * M2[col];
            }
        }
        return result;
    }

    //Multiply 2 NxN Matrices, Get NxN Matrix
    static double[][] Mult2NxN(double[][] M1, double[][] M2) {
        double[][] result = ZeroMxN(M1.length,M1[0].length);

        M2 = TransNxN(M2);

        for(int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                for (int element=0; element<M1[row].length; element++) {
                    result[row][col] += M1[row][element] * M2[col][element];
                }
            }
        }
        return result;
    }

    //Scalar Nx1 Multiply Matrix by scalar, Get Matrix
    static double[] SMultNx1 (double scalar, double[] M1) {
        double[] result = ZeroNx1(M1.length);

        for (int row=0; row<M1.length; row++) {
                result[row] = scalar * M1[row];
        }
        return result;
    }

    //Scalar NxN Multiply Matrix by scalar, Get Matrix
    static double[][] SMultNxN (double scalar, double[][] M1) {
        double[][] result = ZeroMxN(M1.length,M1[0].length);

        for (int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                result[row][col] = scalar * M1[row][col];
            }
        }
        return result;
    }

    //Divide 2 NxN Matrices (Divide Arg1 by Arg2), Get NxN
    static double[][] Divide2NxN (double[][] M1, double[][] M2) {
        double[][] result = ZeroMxN(M1.length,M1[0].length);

        for(int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                result[row][col] = M1[row][col] / M2[row][col];
            }
        }
        return result;
    }

    //Add two Nx1 Matrices, Get Nx1
    static double[] Add2Nx1 (double[] M1, double[] M2){
        double[] result = ZeroNx1(M1.length);

        for(int row=0; row<M1.length; row++) {
            result[row] = M1[row] + M2[row];
        }
        return result;
    }

    //Add 2 NxN Matrices, Get NxN Matrix
    static double[][] Add2NxN(double[][] M1, double[][] M2) {
        double[][] result = ZeroMxN(M1.length,M1[0].length);

        for(int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                result[row][col] = M1[row][col] + M2[row][col];
            }
        }
        return result;
    }

    //Subtract 2 Nx1 Matrices, Get 2x1
    static double[] Sub2Nx1 (double[] M1, double[] M2){
        double[] result = ZeroNx1(M1.length);

        for(int row=0; row<M1.length; row++) {
            result[row] = M1[row] - M2[row];
        }
        return result;
    }

    //Subtract 2 NxN Matrices, Get NxN Matrix
    static double[][] Sub2NxN(double[][] M1, double[][] M2) {
        double[][] result = ZeroMxN(M1.length,M1[0].length);

        for(int row=0; row<M1.length; row++) {
            for(int col=0; col<M1[row].length; col++) {
                result[row][col] = M1[row][col] - M2[row][col];
            }
        }
        return result;
    }

    ////////////////////////////////////////////////
    //Concatenate 2 Matrix w/ Same # of Columns
    public static double [][] ConcatMats(double[][] M1, double[][] M2) {
        double[][] result = new double[M1.length + M2.length][];

        System.arraycopy(M1,0,result,0,M1.length);
        System.arraycopy(M2,0,result,M1.length,M2.length);

        return result;
    }
    ////////////////////////////////////////////////
    public static String PrintNx1(double[] M1) {
        String string = "";

        for(int row=0; row<M1.length; row++) {
            string += Double.toString(M1[row]) + "\n";
        }

        return string;
    }

    public static String PrintMxN(double[][] M1) {
        String string = "";

        for(int row=0; row<M1.length; row++) {
           for (int col=0; col<M1[row].length; col++) {
               string += Double.toString(M1[row][col]) + "   ";
           }

           string += "\n";
        }

        return string;
    }
}
