package task_4_2;

import task_4_1.LagrangeInterpolation;
import task_4_1.NewtonInterpolation;
import org.knowm.xchart.*;
import java.io.IOException;

public class SplineIntGraphs {

    public static void main(String[] args) throws IOException {
        double a = 0;
        double b = 2 * Math.PI;
        int m = 20; // Количество точек разбиения интервала
        int[] nValues = { 3, 10, 20 }; // Различные значения количества узлов интерполирования

        double[] xValues = new double[m + 1];
        double[] fValues = new double[m + 1];
        double[][] lagrangeValues = new double[nValues.length][m + 1];
        double[][] newtonValues = new double[nValues.length][m + 1];

        for (int i = 0; i <= m; i++) {
            double x = a + i * (b - a) / m;
            xValues[i] = x;
            fValues[i] = LagrangeInterpolation.function(x);
            for (int j = 0; j < nValues.length; j++) {
                lagrangeValues[j][i] = LagrangeInterpolation.lagrangeInterpolationEqual(
                        generateEquidistantNodes(a, b, nValues[j]), generateFunctionValues(a, b, nValues[j]), x);
                newtonValues[j][i] = NewtonInterpolation.newtonInterpolationEqual(
                        generateEquidistantNodes(a, b, nValues[j]),
                        generateFunctionValues(a, b, nValues[j]), x);
            }
        }

        XYChart chart1Lagrange = createChart("График №1: f(x) и интерполяционные полиномы Лагранжа", "x", "y");
        XYChart chart2Lagrange = createChart("График №2: f(x) и интерполяционные полиномы Лагранжа", "x", "y");
        XYChart chart1Newton = createChart("График №1: f(x) и интерполяционные полиномы Ньютона", "x", "y");
        XYChart chart2Newton = createChart("График №2: f(x) и интерполяционные полиномы Ньютона", "x", "y");

        addSeries(chart1Lagrange, xValues, fValues, "f(x)");
        addSeries(chart2Lagrange, xValues, fValues, "f(x)");
        addSeries(chart1Newton, xValues, fValues, "f(x)");
        addSeries(chart2Newton, xValues, fValues, "f(x)");

        for (int j = 0; j < nValues.length; j++) {
            addSeries(chart1Lagrange, xValues, lagrangeValues[j], "L" + nValues[j] + "(x)");
            addSeries(chart2Lagrange, xValues, generateOptimalLagrangeValues(a, b, nValues[j], m),
                    "Lopt" + nValues[j] + "(x)");
            addSeries(chart1Newton, xValues, newtonValues[j], "N" + nValues[j] + "(x)");
            addSeries(chart2Newton, xValues, generateOptimalNewtonValues(a, b, nValues[j], m),
                    "Nopt" + nValues[j] + "(x)");
        }

        // Сохранение графиков в файлах
        BitmapEncoder.saveBitmap(chart1Lagrange, "./lagrange_1", BitmapEncoder.BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(chart2Lagrange, "./lagrange_2", BitmapEncoder.BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(chart1Newton, "./newton_1", BitmapEncoder.BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(chart2Newton, "./newton_2", BitmapEncoder.BitmapFormat.PNG);
    }

    // Метод для создания графика с заданным названием и метками осей
    private static XYChart createChart(String title, String xAxisTitle, String yAxisTitle) {
        return new XYChartBuilder().width(800).height(600).title(title).xAxisTitle(xAxisTitle).yAxisTitle(yAxisTitle)
                .build();
    }

    // Метод для добавления данных на график
    private static void addSeries(XYChart chart, double[] xData, double[] yData, String seriesName) {
        chart.addSeries(seriesName, xData, yData);
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

    // Метод для генерации оптимальных значений интерполяционного полинома Лагранжа
    public static double[] generateOptimalLagrangeValues(double a, double b, int n, int m) {
        double[] xValues = new double[m + 1];
        double[] optimalValues = new double[m + 1];
        double step = (b - a) / m;
        for (int i = 0; i <= m; i++) {
            double x = a + i * step;
            xValues[i] = x;
            optimalValues[i] = LagrangeInterpolation.lagrangeInterpolationOptimal(a, b, n, x);
        }
        return optimalValues;
    }

    // Метод для генерации оптимальных значений интерполяционного полинома Ньютона
    public static double[] generateOptimalNewtonValues(double a, double b, int n, int m) {
        double[] xValues = new double[m + 1];
        double[] optimalValues = new double[m + 1];
        double step = (b - a) / m;
        for (int i = 0; i <= m; i++) {
            double x = a + i * step;
            xValues[i] = x;
            optimalValues[i] = NewtonInterpolation.newtonOptimalInterpolation(a, b, n, x);
        }
        return optimalValues;
    }
}
