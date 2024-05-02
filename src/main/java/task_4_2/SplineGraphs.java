package task_4_2;

import org.knowm.xchart.*;

import task_4_1.LagrangeInterpolation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplineGraphs {

    public static void main(String[] args) throws IOException {
        double a = 0;
        double b = 2 * Math.PI;
        int m = 200; // Количество точек разбиения интервала
        int[] nValues = { 3, 10, 20 }; // Различные значения количества узлов интерполирования

        double[] xValues = new double[m + 1];
        double[] fValues = new double[m + 1];
        double[][] splineValues = new double[nValues.length][m + 1];
        double[][] lagrangeValues = new double[nValues.length][m + 1];

        for (int j = 0; j < nValues.length; j++) {
            XYChart splineChart = createChart("Кубический сплайн (n = " + nValues[j] + ")", "x", "y");
            addSeries(splineChart, xValues, fValues, "Original Function");
            addSeries(splineChart, xValues, splineValues[j], "Cubic Spline Interpolation");
            saveChart(splineChart, "./spline__" + nValues[j]);
        }

        for (int i = 0; i <= m; i++) {
            double x = a + i * (b - a) / m;
            xValues[i] = x;
            fValues[i] = LagrangeInterpolation.function(x);

            for (int j = 0; j < nValues.length; j++) {
                double[] nodesArray = generateEquidistantNodes(a, b, nValues[j]);
                double[] valuesArray = generateFunctionValues(a, b, nValues[j]);
                List<Double> nodesList = new ArrayList<>();
                List<Double> valuesList = new ArrayList<>();
                for (double node : nodesArray) {
                    nodesList.add(node);
                }
                for (double value : valuesArray) {
                    valuesList.add(value);
                }
                splineValues[j][i] = SplineInterpolation.cubicSpline(x, nodesList, valuesList);
                lagrangeValues[j][i] = LagrangeInterpolation.lagrangeInterpolationEqual(
                        generateEquidistantNodes(a, b, nValues[j]), generateFunctionValues(a, b, nValues[j]), x);
            }
        }
        double[][] splineErrors = new double[nValues.length][m + 1];
        for (int j = 0; j < nValues.length; j++) {
            for (int i = 0; i <= m; i++) {
                splineErrors[j][i] = Math.abs(splineValues[j][i] - fValues[i]);
            }
        }

        double[][] lagrangeErrors = new double[nValues.length][m + 1];
        for (int j = 0; j < nValues.length; j++) {
            for (int i = 0; i <= m; i++) {
                lagrangeErrors[j][i] = Math.abs(lagrangeValues[j][i] - fValues[i]);
            }
        }

        for (int j = 0; j < nValues.length; j++) {
            XYChart splineChart = createChart("Кубический сплайн (n = " + nValues[j] + ")", "x", "Absolute Error");
            addSeries(splineChart, xValues, splineErrors[j], "Spline Error");
            saveChart(splineChart, "./spline_" + nValues[j]);

            XYChart lagrangeChart = createChart("Полином Лагранжа (n = " + nValues[j] + ")", "x", "Absolute Error");
            addSeries(lagrangeChart, xValues, lagrangeErrors[j], "Lagrange Error");
            saveChart(lagrangeChart, "./lagrange_" + nValues[j]);
        }
    }

    private static XYChart createChart(String title, String xAxisTitle, String yAxisTitle) {
        return new XYChartBuilder().width(800).height(600).title(title).xAxisTitle(xAxisTitle).yAxisTitle(yAxisTitle)
                .build();
    }

    private static void addSeries(XYChart chart, double[] xData, double[] yData, String seriesName) {
        chart.addSeries(seriesName, xData, yData);
    }

    private static void saveChart(XYChart chart, String fileName) throws IOException {
        BitmapEncoder.saveBitmap(chart, fileName, BitmapEncoder.BitmapFormat.PNG);
    }

    public static double[] generateEquidistantNodes(double a, double b, int n) {
        double[] nodes = new double[n + 1];
        double h = (b - a) / n;
        for (int i = 0; i <= n; i++) {
            nodes[i] = a + i * h;
        }
        return nodes;
    }

    public static double[] generateFunctionValues(double a, double b, int n) {
        double[] values = new double[n + 1];
        double[] nodes = generateEquidistantNodes(a, b, n);
        for (int i = 0; i <= n; i++) {
            values[i] = LagrangeInterpolation.function(nodes[i]);
        }
        return values;
    }
}
