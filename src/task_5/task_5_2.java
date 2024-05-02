package task_5;

import java.util.Arrays;

// Обратный степенной метод со сдвигом работает путем итерационного решения 
// системы линейных уравнений (A−σI)y=x, где σ - сдвиг, обычно выбирается 
// близким к предполагаемому собственному значению, и x - предыдущий вектор 
// приближения. Нормализованный вектор y становится новым вектором приближения.

public class task_5_2 {
    public static void main(String[] args) {
        int n = 4;
        double[][] Gamma = task_utils.generateRandomDiagonalMatrix(n);
        double[][] C = task_utils.generateRandomMatrix(n);
        double[][] C_inv = task_utils.invertMatrix(C);
        double[][] A = task_utils.multiplyMatrix(task_utils.multiplyMatrix(C_inv, Gamma), C);

        System.out.println("Матрица A:");
        task_utils.printMatrix(A);

        double[] eigenvaluesGuess = new double[n];
        for (int i = 0; i < n; i++) {
            eigenvaluesGuess[i] = Gamma[i][i];
        }

        findEigenpairs(A, eigenvaluesGuess);
    }

    private static void findEigenpairs(double[][] A, double[] initialGuesses) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            inversePowerMethod(A, initialGuesses[i], n);
        }
    }

    private static void inversePowerMethod(double[][] A, double sigma, int n) {
        double[] x = new double[n];
        double[] y = new double[n];
        Arrays.fill(x, 1);
        double lambda = sigma;

        double tolerance = 1e-6;
        double delta = 1e-8;

        // Матрица (A - sigma*I)
        double[][] B = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = A[i][j];
            }
            B[i][i] -= sigma;
        }

        int iterations = 0;
        while (true) {
            // Cистема (A - sigma*I)y = x
            y = task_utils.solveLinearSystem(B, x);

            double norm = 0;
            for (double v : y)
                norm += v * v;
            norm = Math.sqrt(norm);
            for (int i = 0; i < n; i++)
                y[i] /= norm;

            for (int i = 0; i < n; i++) {
                if (Math.abs(y[i]) < delta) {
                    y[i] = 0;
                }
            }

            double newLambda = task_utils.dotProduct(y, task_utils.matrixVectorMultiply(A, y))
                    / task_utils.dotProduct(y, y);

            if (Math.abs((newLambda - lambda) / newLambda) < tolerance) {
                lambda = newLambda;
                break;
            }

            lambda = newLambda;
            System.arraycopy(y, 0, x, 0, n);
            iterations++;
            if (iterations > 1000)
                break;
        }
        System.out.println("Собственное число: " + lambda);
        System.out.println("Соответствующий собственный вектор: " + Arrays.toString(y));
        System.out.println();
    }

}
