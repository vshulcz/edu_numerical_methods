package task_5;

import java.util.Random;

public class task_5_1 {
    public static void main(String[] args) {
        int n = 2000;

        double[][] Gamma = task_utils.generateRandomDiagonalMatrix(n);
        double[][] C = task_utils.generateRandomMatrix(n);
        double[][] C_inv = task_utils.invertMatrix(C);
        double[][] A = task_utils.multiplyMatrix(task_utils.multiplyMatrix(C_inv, Gamma), C);

        System.out.println("Матрица A:");
        task_utils.printMatrix(A);

        powerMethod(A, n);
    }

    static void powerMethod(double[][] A, int n) {
        double[] x = new double[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            x[i] = random.nextDouble();
        }

        double[] nextX = new double[n];
        double tolerance = 1e-6;
        double lambda = 0;
        double lambdaOld;

        do {
            lambdaOld = lambda;
            double norm = 0;
            for (int i = 0; i < n; i++) {
                nextX[i] = 0;
                for (int j = 0; j < n; j++) {
                    nextX[i] += A[i][j] * x[j];
                }
                norm += nextX[i] * nextX[i];
            }

            norm = Math.sqrt(norm);
            lambda = norm;
            for (int i = 0; i < n; i++) {
                nextX[i] /= norm;
            }
            System.arraycopy(nextX, 0, x, 0, n);
        } while (Math.abs(lambda - lambdaOld) / lambdaOld > tolerance);

        double[] eigenPair = new double[n + 1];
        eigenPair[0] = lambda;
        System.arraycopy(x, 0, eigenPair, 1, n);

        System.out.println("Наибольшее собственное число: " + eigenPair[0]);
        System.out.print("Соответствующий собственный вектор: [");
        for (int i = 1; i <= n; i++) {
            System.out.print(eigenPair[i] + (i < n ? ", " : ""));
        }
        System.out.println("]");
    }
}
