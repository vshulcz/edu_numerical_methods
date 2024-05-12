package task_6;

import java.util.Arrays;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;

public class task_6_2 {
    public static double[] leastSquaresOrthogonalPolynomials(double[][] data, int m, int n) {
        double[] x = new double[m];
        double[] f = new double[m];
        for (int i = 0; i < m; i++) {
            x[i] = data[i][0];
            f[i] = (data[i][1] + data[i][2] + data[i][3]) / 3;
        }

        RealVector fVector = new ArrayRealVector(f);
        Polynomial[] qs = new Polynomial[n + 1];
        double[] finalCoeffs = new double[n + 1];

        // q0(x) = 1
        qs[0] = new Polynomial(1);
        double[] qValues = new double[m];
        Arrays.fill(qValues, 1.0);
        RealVector qVector = new ArrayRealVector(qValues);
        double a0 = fVector.dotProduct(qVector) / qVector.dotProduct(qVector);
        finalCoeffs[0] = a0;

        for (int k = 1; k <= n; k++) {
            qs[k] = generateOrthogonalPolynomial(qs, k, x);
            qValues = new double[m];
            for (int i = 0; i < m; i++) {
                qValues[i] = qs[k].evaluate(x[i]);
            }
            qVector = new ArrayRealVector(qValues);
            double ak = fVector.dotProduct(qVector) / qVector.dotProduct(qVector);

            double[] qCoeffs = qs[k].getCoefficients();
            for (int j = 0; j <= k; j++) {
                if (j < finalCoeffs.length) {
                    finalCoeffs[j] += ak * qCoeffs[j];
                }
            }
        }

        return finalCoeffs;
    }

    private static Polynomial generateOrthogonalPolynomial(Polynomial[] qs, int k, double[] x) {
        int m = x.length;
        Polynomial p = new Polynomial(1);
        for (int j = 0; j < k; j++) {
            p = p.multiply(new Polynomial(-x[j], 1));
        }

        for (int j = 0; j < k; j++) {
            double[] pValues = new double[m];
            double[] qjValues = new double[m];
            for (int i = 0; i < m; i++) {
                pValues[i] = p.evaluate(x[i]);
                qjValues[i] = qs[j].evaluate(x[i]);
            }
            RealVector pVector = new ArrayRealVector(pValues);
            RealVector qjVector = new ArrayRealVector(qjValues);

            double dotProduct = pVector.dotProduct(qjVector);
            double normSq = qjVector.dotProduct(qjVector);
            double factor = dotProduct / normSq;

            Polynomial correction = qs[j].multiply(new Polynomial(factor));
            p = p.subtract(correction);
        }

        return p;
    }

    public static double[] leastSquaresNormalPolynomials(double[][] data, int m, int n) {
        // Матрица Вандермонда
        double[][] E = new double[m][n + 1];
        double[] f = new double[m];
        for (int i = 0; i < m; i++) {
            double x = data[i][0];
            f[i] = (data[i][1] + data[i][2] + data[i][3]) / 3;
            for (int j = 0; j <= n; j++) {
                E[i][j] = Math.pow(x, j);
            }
        }

        // E^T * E и E^T * f
        RealMatrix matrixE = new Array2DRowRealMatrix(E);
        RealVector vectorF = new ArrayRealVector(f);
        RealMatrix ET = matrixE.transpose();
        RealMatrix ETE = ET.multiply(matrixE);
        RealVector ETF = ET.operate(vectorF);

        // Решение E^T * E * a = E^T * f
        DecompositionSolver solver = new LUDecomposition(ETE).getSolver();
        RealVector coefficients = solver.solve(ETF);

        return coefficients.toArray();
    }
}
