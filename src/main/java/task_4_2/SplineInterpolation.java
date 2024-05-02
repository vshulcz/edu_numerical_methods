package task_4_2;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

public class SplineInterpolation {
    public static void plotSplineInterpolation(List<Double> nodes, List<Double> values, double a, double b,
            String title) {
        double[] xData = new double[nodes.size()];
        double[] yData = new double[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            xData[i] = nodes.get(i);
            yData[i] = f(nodes.get(i));
        }

        double[] xSpline = new double[100 * nodes.size()];
        double[] ySpline = new double[100 * nodes.size()];
        double step = (b - a) / (xSpline.length - 1);
        for (int i = 0; i < xSpline.length; i++) {
            xSpline[i] = a + step * i;
            ySpline[i] = cubicSpline(xSpline[i], nodes, values);
        }

        XYChart chart = QuickChart.getChart(title, "x", "y", "Original Function", xData, yData);
        chart.addSeries("Spline Interpolation", xSpline, ySpline);

        new SwingWrapper<>(chart).displayChart();
    }

    public static double f(double x) {
        return x * x + 4 * Math.sin(x) - 2;
    }

    public static List<Double> generateNodes(double a, double b, int n) {
        List<Double> nodes = new ArrayList<>();
        double step = (b - a) / (n - 1);
        for (int i = 0; i < n; i++) {
            nodes.add(a + step * i);
        }
        return nodes;
    }

    public static double linearSpline(double x, List<Double> nodes, List<Double> values) {
        int n = nodes.size();
        if (n != values.size() || n < 2) {
            throw new IllegalArgumentException("Неверное количество узлов или значений");
        }

        int i = 0;
        while (i < n - 1 && x > nodes.get(i + 1)) {
            i++;
        }

        double x0 = nodes.get(i);
        double x1 = nodes.get(i + 1);
        double y0 = values.get(i);
        double y1 = values.get(i + 1);
        double a = (y1 - y0) / (x1 - x0);
        double b = y0 - a * x0;

        return a * x + b;
    }

    public static double quadraticSpline(double x, List<Double> nodes, List<Double> values) {
        int n = nodes.size();
        if (n != values.size() || n < 3) {
            throw new IllegalArgumentException("Неверное количество узлов или значений");
        }

        int i = 0;
        while (i < n - 1 && x > nodes.get(i + 1)) {
            i++;
        }

        double x0, x1, x2, y0, y1, y2, a, b, c;

        if (i == 0) {
            x0 = nodes.get(i);
            x1 = nodes.get(i + 1);
            x2 = nodes.get(i + 2);
            y0 = values.get(i);
            y1 = values.get(i + 1);
            y2 = values.get(i + 2);

            a = ((y2 - y0) - (x2 - x0) * (y1 - y0) / (x1 - x0)) / ((x2 - x0) * (x2 - x1));
            b = (y1 - y0) / (x1 - x0) - a * (x1 + x0);
            c = y0 - a * x0 * x0 - b * x0;

        } else if (i == n - 2) {
            x0 = nodes.get(i - 1);
            x1 = nodes.get(i);
            x2 = nodes.get(i + 1);
            y0 = values.get(i - 1);
            y1 = values.get(i);
            y2 = values.get(i + 1);

            a = ((y2 - y0) - (x2 - x0) * (y2 - y1) / (x2 - x1)) / ((x2 - x0) * (x1 - x0));
            b = (y2 - y1) / (x2 - x1) - a * (x2 + x1);
            c = y2 - a * x2 * x2 - b * x2;

        } else {
            x0 = nodes.get(i - 1);
            x1 = nodes.get(i);
            x2 = nodes.get(i + 1);
            y0 = values.get(i - 1);
            y1 = values.get(i);
            y2 = values.get(i + 1);

            a = ((y2 - y0) - (x2 - x0) * (y1 - y0) / (x1 - x0)) / ((x2 - x0) * (x2 - x1));
            b = (y1 - y0) / (x1 - x0) - a * (x1 + x0);
            c = y0 - a * x0 * x0 - b * x0;
        }

        return a * x * x + b * x + c;
    }

    public static double cubicSpline(double x, List<Double> nodes, List<Double> values) {
        int n = nodes.size();
        if (n != values.size() || n < 3) {
            throw new IllegalArgumentException("Неверное количество узлов или значений");
        }

        int i = 0;
        while (i < n - 1 && x > nodes.get(i + 1)) {
            i++;
        }

        double[] h = new double[n - 1];
        double[] b = new double[n - 1];
        double[] u = new double[n - 1];
        double[] v = new double[n - 1];
        double[] z = new double[n];
    
        for (int j = 0; j < n - 1; j++) {
            h[j] = nodes.get(j + 1) - nodes.get(j);
            b[j] = (values.get(j + 1) - values.get(j)) / h[j];
        }
    
        u[1] = 2 * (h[0] + h[1]);
        v[1] = 6 * (b[1] - b[0]);
    
        for (int j = 2; j < n - 1; j++) {
            u[j] = 2 * (h[j - 1] + h[j]) - h[j - 1] * h[j - 1] / u[j - 1];
            v[j] = 6 * (b[j] - b[j - 1]) - h[j - 1] * v[j - 1] / u[j - 1];
        }
    
        z[n - 1] = 0;
        for (int j = n - 2; j > 0; j--) {
            z[j] = (v[j] - h[j] * z[j + 1]) / u[j];
        }
        z[0] = 0;
    
        i = 0;
        int jPrev = 0;
        while (i < n - 1 && x > nodes.get(i + 1)) {
            i++;
            jPrev = i - 1;
        }
    
        double h_i = nodes.get(i + 1) - nodes.get(i);
        double y_i = values.get(i);
        double y_j = values.get(jPrev);
        double z_i = z[i];
        double z_j = z[jPrev];
    
        double A = (z_i - z_j) / (6 * h_i);
        double B = z_j / 2;
        double C = (y_i - y_j) / h_i - h_i * (2 * z_j + z_i) / 6;
        double D = y_j;
    
        return A * Math.pow(x - nodes.get(i), 3) + B * Math.pow(x - nodes.get(i), 2) + C * (x - nodes.get(i)) + D;
    }

    public static double maxDeviationLinear(List<Double> nodes, List<Double> values, int k) {
        int n = nodes.size();
        double maxDeviation = 0.0;

        for (int i = 0; i < n - 1; i++) {
            double step = (nodes.get(i + 1) - nodes.get(i)) / k;
            for (int j = 0; j < k; j++) {
                double x = nodes.get(i) + step * j;
                double splineValue = linearSpline(x, nodes, values);
                double functionValue = f(x);
                double deviation = Math.abs(splineValue - functionValue);
                if (deviation > maxDeviation) {
                    maxDeviation = deviation;
                }
            }
        }

        return maxDeviation;
    }

    public static double maxDeviationOptimalLinear(List<Double> nodes, List<Double> values, int k) {
        List<Double> optimalNodes = generateOptimalNodes(nodes.get(0), nodes.get(nodes.size() - 1), k);
        int n = optimalNodes.size();
        double maxDeviation = 0.0;

        for (int i = 0; i < n - 1; i++) {
            double step = (optimalNodes.get(i + 1) - optimalNodes.get(i)) / k;
            for (int j = 0; j < k; j++) {
                double x = optimalNodes.get(i) + step * j;
                double splineValue = linearSpline(x, nodes, values);
                double functionValue = f(x);
                double deviation = Math.abs(splineValue - functionValue);
                if (deviation > maxDeviation) {
                    maxDeviation = deviation;
                }
            }
        }

        return maxDeviation;
    }

    public static double maxDeviationQuadratic(List<Double> nodes, List<Double> values, int k) {
        int n = nodes.size();
        double maxDeviation = 0.0;

        for (int i = 0; i < n - 1; i++) {
            double step = (nodes.get(i + 1) - nodes.get(i)) / k;
            for (int j = 0; j < k; j++) {
                double x = nodes.get(i) + step * j;
                double splineValue = quadraticSpline(x, nodes, values);
                double functionValue = f(x);
                double deviation = Math.abs(splineValue - functionValue);
                if (deviation > maxDeviation) {
                    maxDeviation = deviation;
                }
            }
        }

        return maxDeviation;
    }

    public static double maxDeviationOptimalQuadratic(List<Double> nodes, List<Double> values, int k) {
        List<Double> optimalNodes = generateOptimalNodes(nodes.get(0), nodes.get(nodes.size() - 1), k);
        int n = optimalNodes.size();
        double maxDeviation = 0.0;

        for (int i = 0; i < n - 1; i++) {
            double step = (optimalNodes.get(i + 1) - optimalNodes.get(i)) / k;
            for (int j = 0; j < k; j++) {
                double x = optimalNodes.get(i) + step * j;
                double splineValue = quadraticSpline(x, nodes, values);
                double functionValue = f(x);
                double deviation = Math.abs(splineValue - functionValue);
                if (deviation > maxDeviation) {
                    maxDeviation = deviation;
                }
            }
        }

        return maxDeviation;
    }

    public static double maxDeviationCubic(List<Double> nodes, List<Double> values, int k) {
        int n = nodes.size();
        double maxDeviation = 0.0;

        for (int i = 0; i < n - 1; i++) {
            double step = (nodes.get(i + 1) - nodes.get(i)) / k;
            for (int j = 0; j < k; j++) {
                double x = nodes.get(i) + step * j;
                double splineValue = cubicSpline(x, nodes, values);
                double functionValue = f(x);
                double deviation = Math.abs(splineValue - functionValue);
                if (deviation > maxDeviation) {
                    maxDeviation = deviation;
                }
            }
        }

        return maxDeviation;
    }

    public static double maxDeviationOptimalCubic(List<Double> nodes, List<Double> values, int k) {
        List<Double> optimalNodes = generateOptimalNodes(nodes.get(0), nodes.get(nodes.size() - 1), k);
        int n = optimalNodes.size();
        double maxDeviation = 0.0;

        for (int i = 0; i < n - 1; i++) {
            double step = (optimalNodes.get(i + 1) - optimalNodes.get(i)) / k;
            for (int j = 0; j < k; j++) {
                double x = optimalNodes.get(i) + step * j;
                double splineValue = cubicSpline(x, nodes, values);
                double functionValue = f(x);
                double deviation = Math.abs(splineValue - functionValue);
                if (deviation > maxDeviation) {
                    maxDeviation = deviation;
                }
            }
        }

        return maxDeviation;
    }

    public static List<Double> generateOptimalNodes(double a, double b, int n) {
        List<Double> optimalNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double x = 0.5 * ((b - a) * Math.cos((2 * i + 1) * Math.PI / (2 * (n + 1))) + (b + a));
            optimalNodes.add(x);
        }
        return optimalNodes;
    }

    public static List<Double> generateValues(double a, double b, int n) {
        List<Double> values = new ArrayList<>();
        double step = (b - a) / (n - 1);
        for (int i = 0; i < n; i++) {
            double x = a + step * i;
            values.add(f(x));
        }
        return values;
    }

    public static void main(String[] args) {
        double a = 0;
        double b = 10;
        int[] nValues = { 5, 10, 20, 50, 100, 200, 500 }; // Различные значения количества узлов интерполирования
        int k = 100; // Количество проверочных точек

        System.out.println("Таблица №1. Линейный сплайн");
        System.out.println(
                "Количество узлов (n)\tКоличество проверочных точек (k)\tМаксимальное отклонение по равностоящим узлам\tМаксимальное отклонение по оптимальным узлам");
        for (int n : nValues) {
            List<Double> nodes = generateNodes(a, b, n);
            List<Double> values = new ArrayList<>();
            for (double node : nodes) {
                values.add(f(node));
            }
            double maxDeviationLinear = maxDeviationLinear(nodes, values, k);
            double maxDeviationLinearOptimal = maxDeviationOptimalLinear(nodes, values, k);
            System.out.println(
                    n + "\t\t\t" + k + "\t\t\t\t\t" + maxDeviationLinear + "\t\t\t\t" + maxDeviationLinearOptimal);
        }

        System.out.println("\nТаблица №2. Квадратичный сплайн");
        System.out.println(
                "Количество узлов (n)\tКоличество проверочных точек (k)\tМаксимальное отклонение по равностоящим узлам\tМаксимальное отклонение по оптимальным узлам");
        for (int n : nValues) {
            List<Double> nodes = generateNodes(a, b, n);
            List<Double> values = new ArrayList<>();
            for (double node : nodes) {
                values.add(f(node));
            }
            double maxDeviationQuadratic = maxDeviationQuadratic(nodes, values, k);
            double maxDeviationQuadraticOptimal = maxDeviationOptimalQuadratic(nodes, values, k);
            System.out.println(
                    n + "\t\t\t" + k + "\t\t\t\t\t" + maxDeviationQuadratic + "\t\t\t\t"
                            + maxDeviationQuadraticOptimal);
        }

        System.out.println("\nТаблица №3. Кубический сплайн");
        System.out.println(
                "Количество узлов (n)\tКоличество проверочных точек (k)\tМаксимальное отклонение по равностоящим узлам\tМаксимальное отклонение по оптимальным узлам");
        for (int n : nValues) {
            List<Double> nodes = generateNodes(a, b, n);
            List<Double> values = new ArrayList<>();
            for (double node : nodes) {
                values.add(f(node));
            }
            double maxDeviationCubic = maxDeviationCubic(nodes, values, k);
            double maxDeviationCubicOptimal = maxDeviationOptimalCubic(nodes, values, k);
            System.out.println(
                    n + "\t\t\t" + k + "\t\t\t\t\t" + maxDeviationCubic + "\t\t\t\t" + maxDeviationCubicOptimal);
        }

        List<Double> nodesLinear = generateNodes(a, b, 100);
        List<Double> valuesLinear = new ArrayList<>();
        for (double node : nodesLinear) {
            valuesLinear.add(f(node));
        }
        plotSplineInterpolation(nodesLinear, valuesLinear, a, b, "Linear Spline Interpolation");
    }
}