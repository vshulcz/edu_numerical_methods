package task_6;

import java.util.Random;

public class task_utils {
    // Вариант 10
    public static double function(double x) {
        return x * x + Math.sin(x);
        // return -4 * Math.pow(x, 8) + 12 * Math.pow(x, 7) - 3 * Math.pow(x, 6) + 0.5 * Math.pow(x, 5)
        //         - 10 * Math.pow(x, 4) + 7 * Math.pow(x, 3) - 0.25 * Math.pow(x, 2) + 9 * x - 1;
    }

    public static double[][] generateExperimentalData(int m, double corridorWidth) {
        Random random = new Random();
        double[][] data = new double[m][4];

        for (int i = 0; i < m; i++) {
            double x = 2 * random.nextDouble() - 1;
            double fExact = task_utils.function(x);
            double fError = corridorWidth * (random.nextDouble() - 0.5);

            // Генерация трех значений функции с учетом погрешности в заданном коридоре
            data[i][0] = x;
            data[i][1] = fExact + fError;
            data[i][2] = fExact + fError * 0.5;
            data[i][3] = fExact + fError * 0.25;
        }

        return data;
    }

    public static double[] evaluatePolynomial(double[] coefficients, double[] xData) {
        double[] yData = new double[xData.length];
        for (int i = 0; i < xData.length; i++) {
            double y = 0;
            for (int j = 0; j < coefficients.length; j++) {
                y += coefficients[j] * Math.pow(xData[i], j);
            }
            yData[i] = y;
        }
        return yData;
    }

    public static double calculateError(double[] actual, double[] predicted) {
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.pow(actual[i] - predicted[i], 2);
        }
        return sum;
    }
}
