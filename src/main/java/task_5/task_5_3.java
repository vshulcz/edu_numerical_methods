package task_5;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class task_5_3 {
    private static final double EPSILON = 1e-8;

    public static void main(String[] args) {
        int n = 100;
        double[][] Gamma = task_utils.generateRandomDiagonalMatrix(n);
        double[][] C = task_utils.generateRandomMatrix(n);
        double[][] C_inv = task_utils.invertMatrix(C);
        double[][] A = task_utils.multiplyMatrix(task_utils.multiplyMatrix(C_inv, Gamma), C);

        System.out.println("Матрица A:");
        task_utils.printMatrix(A);

        A = toHessenbergForm(A);
        System.out.println("Матрица Хезенберга:");
        task_utils.printMatrix(A);

        RealMatrix H = new Array2DRowRealMatrix(A);
        double[] eigenvalues = qrAlgorithmWithShifts(H);

        System.out.println("Собственные значения матрицы A:");
        for (double eigenvalue : eigenvalues) {
            System.out.printf("%.12f\n", eigenvalue);
        }
    }

    public static double[][] toHessenbergForm(double[][] A) {
        int n = A.length;
        for (int k = 0; k < n - 2; k++) {
            double[] x = new double[n - k - 1];
            for (int i = k + 1; i < n; i++) {
                x[i - k - 1] = A[i][k];
            }

            double normX = 0;
            for (double value : x) {
                normX += value * value;
            }
            normX = Math.sqrt(normX);

            if (x[0] + normX != 0) {
                x[0] += normX;
            }
            double normV = 0;
            for (double value : x) {
                normV += value * value;
            }
            normV = Math.sqrt(normV);
            double[] v = new double[x.length];
            for (int i = 0; i < v.length; i++) {
                v[i] = x[i] / normV;
            }

            // Построение матрицы Хаусхолдера H = I - 2vv^T
            double[][] H = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i >= k + 1 && j >= k + 1) {
                        H[i][j] = (i == j ? 1.0 : 0.0) - 2 * v[i - k - 1] * v[j - k - 1];
                    } else {
                        H[i][j] = (i == j ? 1.0 : 0.0);
                    }
                }
            }

            A = task_utils.multiplyMatrix(H, A);
            A = task_utils.multiplyMatrix(A, H);
        }
        return A;
    }

    public static double[] qrAlgorithmWithShifts(RealMatrix A) {
        int n = A.getRowDimension();
        double[] eigenvalues = new double[n];
        RealMatrix ak = A.copy();
        double mu;

        int eigenIndex = n - 1;

        while (n > 0) {
            int k = 0;
            while (true) {
                if (n <= 1) {
                    eigenvalues[eigenIndex] = ak.getEntry(n - 1, n - 1);
                    eigenIndex--;
                    break;
                }

                mu = ak.getEntry(n - 1, n - 1);
                ak = shiftMatrix(ak, -mu, n);
                QRDecomposition qr = new QRDecomposition(ak);
                RealMatrix Q = qr.getQ();
                RealMatrix R = qr.getR();
                ak = R.multiply(Q);

                addShift(ak, mu, n);

                if (k > 0 && Math.abs(ak.getEntry(n - 1, n - 2)) < EPSILON) {
                    eigenvalues[eigenIndex] = ak.getEntry(n - 1, n - 1);
                    eigenIndex--;
                    n--;
                    break;
                }
                k++;
            }
            if (n <= 1) {
                if (n == 1) {
                    eigenvalues[eigenIndex] = ak.getEntry(0, 0);
                }
                break;
            }
        }
        return eigenvalues;
    }

    public static void qrDecomposition(double[][] A, double[][] Q, double[][] R, int n) {
        int i, j, k;
        double c, s;

        for (i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, R[i], 0, n);
        }

        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                Q[i][j] = (i == j) ? 1.0 : 0.0;
            }
        }

        for (k = 0; k < n - 1; k++) {
            for (i = k + 1; i < n; i++) {
                if (R[i][k] != 0) {
                    // Вычисление коэффициентов c (cos) и s (sin) для вращения Гивенса
                    double r = Math.sqrt(R[k][k] * R[k][k] + R[i][k] * R[i][k]);
                    c = R[k][k] / r;
                    s = -R[i][k] / r;

                    // Применение вращения Гивенса к R и Q
                    for (j = 0; j < n; j++) {
                        double temp = c * R[k][j] - s * R[i][j];
                        R[i][j] = s * R[k][j] + c * R[i][j];
                        R[k][j] = temp;

                        temp = c * Q[k][j] - s * Q[i][j];
                        Q[i][j] = s * Q[k][j] + c * Q[i][j];
                        Q[k][j] = temp;
                    }
                }
            }
        }

        task_utils.transposeMatrix(Q, n);
    }

    private static void addShift(RealMatrix matrix, double shift, int n) {
        for (int i = 0; i < n; i++) {
            matrix.addToEntry(i, i, shift);
        }
    }

    private static RealMatrix shiftMatrix(RealMatrix matrix, double shift, int n) {
        RealMatrix shiftedMatrix = matrix.copy();
        for (int i = 0; i < n; i++) {
            shiftedMatrix.addToEntry(i, i, shift);
        }
        return shiftedMatrix;
    }
}