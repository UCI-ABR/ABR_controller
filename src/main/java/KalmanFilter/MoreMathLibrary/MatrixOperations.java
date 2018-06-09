package KalmanFilter.MoreMathLibrary;

/**
 * Created by Joseph on 7/13/2017.
 * Contains basic matrix operations + a print method for displaying results
 */

public class MatrixOperations {
    //Transpose NxN Matrix
    protected double[][] TransNxN(double[][] M1) {
        double[][] result = new double[M1.length][M1[0].length];

        for(int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                result[row][col] = M1[col][row];
            }
        }
        return result;
    }

    //Multiply MxN by Nx1 Matrices, Get Mx1
    protected double[] MultNx1(double[][] M1, double[] M2) {
        double[] result = new double[M1.length];

        for(int row=0; row<M1.length; row++) {
            for(int col=0; col<M2.length; col++) {
                result[row] += M1[row][col] * M2[col];
            }
        }
        return result;
    }

    //Multiply 2 NxN Matrices, Get NxN Matrix
    protected double[][] Mult2NxN(double[][] M1, double[][] M2) {
        double[][] result = new double[M1.length][M1[0].length];

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
    protected double[] SMultMxN (double scalar, double[] M1) {
        double[] result = new double[M1.length];

        for (int row=0; row<M1.length; row++) {
                result[row] = scalar * M1[row];
        }
        return result;
    }

    //Scalar NxN Multiply Matrix by scalar, Get Matrix
    protected double[][] SMultMxN (double scalar, double[][] M1) {
        double[][] result = new double[M1.length][M1[0].length];

        for (int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                result[row][col] = scalar * M1[row][col];
            }
        }
        return result;
    }

    //Divide 2 NxN Matrices (Divide Arg1 by Arg2), Get NxN
    protected double[][] Divide2MxN (double[][] M1, double[][] M2) {
        double[][] result = new double[M1.length][M1[0].length];

        for(int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                result[row][col] = M1[row][col] / M2[row][col];

                //make zero if result is not equal to any value (0/0)
                if (Double.isNaN(result[row][col])) {
                    result[row][col] = 0;
                }
            }
        }
        return result;
    }

    //Add two Nx1 Matrices, Get Nx1
    protected double[] Add2MxN (double[] M1, double[] M2){
        double[] result = new double[M1.length];

        for(int row=0; row<M1.length; row++) {
            result[row] = M1[row] + M2[row];
        }
        return result;
    }

    //Add 2 MxN Matrices, Get MxN Matrix
    protected double[][] Add2MxN(double[][] M1, double[][] M2) {
        double[][] result = new double[M1.length][M1[0].length];

        //Log.i("M1 Length", "{" + M1.length + ", " + M1[0].length + "}");
        //Log.i("M2 Length", "{" + M2.length + ", " + M2[0].length + "}");

        for(int row=0; row<M1.length; row++) {
            for (int col=0; col<M1[row].length; col++) {
                result[row][col] = M1[row][col] + M2[row][col];
            }
        }
        return result;
    }

    //Subtract 2 Nx1 Matrices, Get 2x1
    protected double[] Sub2MxN (double[] M1, double[] M2){
        double[] result = new double[M1.length];

        for(int row=0; row<M1.length; row++) {
            result[row] = M1[row] - M2[row];
        }
        return result;
    }

    //Subtract 2 NxN Matrices, Get NxN Matrix
    protected double[][] Sub2MxN(double[][] M1, double[][] M2) {
        double[][] result = new double[M1.length][M1[0].length];

        for(int row=0; row<M1.length; row++) {
            for(int col=0; col<M1[row].length; col++) {
                result[row][col] = M1[row][col] - M2[row][col];
            }
        }
        return result;
    }

    //Concatenate 2 Matrix w/ Same # of Columns
    protected double[][] ConcatMats(double[][] M1, double[][] M2) {
        double[][] result = new double[M1.length + M2.length][];

        System.arraycopy(M1,0,result,0,M1.length);
        System.arraycopy(M2,0,result,M1.length,M2.length);

        return result;
    }

    public String PrintMxN(double[] M1) {
        String string = "";

        for (double d:M1) {
            string += Double.toString(d) + "\n";
        }

        return string;
    }

    public String PrintMxN(double[][] M1) {
        String string = "";

        for (double[] row:M1) {
            for (double col:row) {
                string += Double.toString(col) + "   ";
            }

            string += "\n";
        }

        return string;
    }
}
