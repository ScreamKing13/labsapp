package com.myprogs.labsapp;

public class AlgTest {
    public static void main(String[] args) {
    double[][] a = new double[][]{
            {4, 2, -1},
            {5, 3, -2},
            {3, 2, -3}
    };
    double[] b = new double[]{1, 2, 0};
    // Right answer: x1 = -1; x2 = 3; x3 = 1;
    double[] result = matrixSolve(a, b, 1.);
        for (double g:result
             ) {
            System.out.println(g);
        }
    }

    public static double[] matrixSolve(double[][] aMatrix, double[] bMatrix, double omega){
        double[] oldX;
        double[] newX = new double[]{bMatrix[0], 0, 0};
        double[] result = new double[3];

        int counter = 0;
        do {
            counter++;
            oldX = newX.clone();
            newX[0] = (bMatrix[0] - oldX[1] * aMatrix[0][1] - oldX[2] * aMatrix[0][2]) / aMatrix[0][0];
            newX[1] = (bMatrix[1] - newX[0] * aMatrix[1][0] - oldX[2] * aMatrix[1][2]) / aMatrix[1][1];
            newX[2] = (bMatrix[2] - newX[0] * aMatrix[2][0] - newX[1] * aMatrix[2][1]) / aMatrix[2][2];

            for (int j = 0; j < 3; j++){
                result[j] = oldX[j] + omega * (newX[j] - oldX[j]);
            }
        } while (Math.abs(result[0] - oldX[0]) > 0.00001);

        System.out.println(counter);
        return result;
    }
}
