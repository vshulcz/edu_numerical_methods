package task_5;

import java.util.Arrays;
import java.util.Random;

public class task_utils {
    public static double[][] generateRandomMatrix(int n) {
        Random random = new Random();
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextDouble() * 10;
            }
        }
        return matrix;
    }

    public static double[][] generateRandomDiagonalMatrix(int n) {
        Random random = new Random();
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i][i] = random.nextDouble() * 10;
        }
        return matrix;
    }

    public static double[][] multiplyMatrix(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    public static double[] matrixVectorMultiply(double[][] matrix, double[] vector) {
        double[] result = new double[vector.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    public static double[][] invertMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] inverse = new double[n][n];
        double[][] identity = new double[n][n];
        for (int i = 0; i < n; i++) {
            identity[i][i] = 1;
        }

        double[][] augmented = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented[i][j] = matrix[i][j];
                augmented[i][j + n] = identity[i][j];
            }
        }

        for (int col = 0; col < n; col++) {
            double div = augmented[col][col];
            for (int j = 0; j < 2 * n; j++) {
                augmented[col][j] /= div;
            }

            for (int row = 0; row < n; row++) {
                if (row != col) {
                    double factor = augmented[row][col];
                    for (int j = 0; j < 2 * n; j++) {
                        augmented[row][j] -= factor * augmented[col][j];
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = augmented[i][j + n];
            }
        }
        return inverse;
    }

    public static double[][] transposeMatrix(double[][] matrix, int n) {
        double[][] transposed = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static double dotProduct(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

    public static double[] solveLinearSystem(double[][] A, double[] b) {
        int n = A.length;
        double[][] Ab = new double[n][n + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Ab[i][j] = A[i][j];
            }
            Ab[i][n] = b[i];
        }

        for (int i = 0; i < n; i++) {
            int max = i;
            for (int row = i + 1; row < n; row++) {
                if (Math.abs(Ab[row][i]) > Math.abs(Ab[max][i])) {
                    max = row;
                }
            }

            double[] temp = Ab[i];
            Ab[i] = Ab[max];
            Ab[max] = temp;

            for (int row = i + 1; row < n; row++) {
                double factor = Ab[row][i] / Ab[i][i];
                for (int col = i; col <= n; col++) {
                    Ab[row][col] -= factor * Ab[i][col];
                }
            }
        }

        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += Ab[i][j] * solution[j];
            }
            solution[i] = (Ab[i][n] - sum) / Ab[i][i];
        }

        return solution;
    }

    public static double[][] copyMatrix(double[][] original) {
        int n = original.length;
        double[][] copy = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, n);
        }
        return copy;
    }
}