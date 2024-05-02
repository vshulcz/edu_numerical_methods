package task_4_1;

import java.util.function.DoubleUnaryOperator;

public class NewtonInterpolation {
    // Вариант - 10
    public static double function(double x) {
        return x * x + 4 * Math.sin(x) - 2;
    }

    // Вычисление разделённой разности
    public static double dividedDifference(double[] x, double[] y, int k) {
        if (k == 0) {
            return y[0];
        }
        double sum = 0;
        for (int i = 0; i <= k; i++) {
            double product = y[i];
            for (int j = 0; j <= k; j++) {
                if (j != i) {
                    product /= (x[i] - x[j]);
                }
            }
            sum += product;
        }
        return sum;
    }

    // Построение интерполяционного полинома Ньютона по равноотстоящим узлам
    public static double newtonInterpolationEqual(double[] x, double[] y, double xi) {
        double result = y[0];
        for (int i = 1; i < x.length; i++) {
            double term = 1;
            for (int j = 0; j < i; j++) {
                term *= (xi - x[j]);
            }
            result += dividedDifference(x, y, i) * term;
        }
        return result;
    }

    // Построение интерполяционного полинома Ньютона по оптимальным узлам
    public static double newtonOptimalInterpolation(double a, double b, int n, double xi) {
        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = 0.5 * ((b - a) * Math.cos((2 * i + 1) * Math.PI / (2 * (n + 1))) + (b + a));
            y[i] = function(x[i]);
        }
        return newtonInterpolationEqual(x, y, xi);
    }

    // Метод для вычисления максимального отклонения интерполяционного полинома от
    // функции
    public static double maxDeviation(DoubleUnaryOperator function, DoubleUnaryOperator interpolation, double a,
            double b, int n, int m) {
        double maxDeviation = 0;
        double step = (b - a) / m;
        for (int i = 0; i <= m; i++) {
            double xi = a + i * step;
            double deviation = Math.abs(function.applyAsDouble(xi) - interpolation.applyAsDouble(xi));
            if (deviation > maxDeviation) {
                maxDeviation = deviation;
            }
        }
        return maxDeviation;
    }

    // Метод для генерации узлов интерполяции с равным шагом
    public static double[] generateEquidistantNodes(double a, double b, int n) {
        double[] nodes = new double[n + 1];
        double h = (b - a) / n;
        for (int i = 0; i <= n; i++) {
            nodes[i] = a + i * h;
        }
        return nodes;
    }

    // Метод для генерации значений функции в узлах интерполяции
    public static double[] generateFunctionValues(double a, double b, int n) {
        double[] values = new double[n + 1];
        double[] nodes = generateEquidistantNodes(a, b, n);
        for (int i = 0; i <= n; i++) {
            values[i] = LagrangeInterpolation.function(nodes[i]);
        }
        return values;
    }

    public static void main(String[] args) {
        double a = 0;
        double b = 2 * Math.PI;
        int[] nValues = { 5, 10, 20, 50, 100, 200, 500, 1000 }; // Различные значения количества узлов интерполирования
        int m = 100; // Количество точек разбиения интервала

        System.out.println(
                "Таблица №2. Поведение интерполяционного полинома Ньютона при увеличении количества узлов интерполирования.");
        System.out.println(
                "Количество узлов (n)\tКоличество проверочных точек (m)\tМаксимальное отклонение (RNn)\tМаксимальное отклонение (RNoptn)");
        for (int n : nValues) {
            double maxDeviationLn = maxDeviation(NewtonInterpolation::function,
                    x -> newtonInterpolationEqual(generateEquidistantNodes(a, b, n),
                            generateFunctionValues(a, b, n),
                            x),
                    a, b, n, m);
            double maxDeviationLoptn = maxDeviation(NewtonInterpolation::function,
                    x -> newtonOptimalInterpolation(a, b, n, x),
                    a, b, n, m);
            System.out.printf("%d\t\t\t%d\t\t\t\t\t%.6f\t\t\t%.6f\n", n, m, maxDeviationLn,
                    maxDeviationLoptn);
        }
    }
}
