package task_6;

import java.util.Arrays;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class task_6_3 {
    public static void main(String[] args) {
        int m = 100;
        double corridorWidth = 0.01;

        double[][] data = task_6_1.main(m, corridorWidth);

        System.out.format("%-20s%-30s%-30s%n", "Степень (n)", "SSE для МНК (норм. полин.)",
                "SSE для МНК (орт. полин.)");

        for (int n = 1; n <= 5; n++) {
            double[] coeffsNormal = task_6_2.leastSquaresNormalPolynomials(data, m, n);
            double[] coeffsOrthogonal = task_6_2.leastSquaresOrthogonalPolynomials(data, m, n);

            XYChart chart = new XYChartBuilder().width(800).height(600).title("Степень полинома " + n).xAxisTitle("X")
                    .yAxisTitle("Y").build();

            double[] xData = new double[m];
            double[] yData = new double[m];
            for (int i = 0; i < m; i++) {
                xData[i] = data[i][0];
                yData[i] = task_utils.function(xData[i]);
            }
            chart.addSeries("Данные", xData, yData).setMarker(SeriesMarkers.CIRCLE).setMarkerColor(java.awt.Color.RED);

            // System.out.println("Коэффициенты (нормальные уравнения): " +
            // Arrays.toString(coeffsNormal));
            // System.out.println("Коэффициенты (ортогональные многочлены): " +
            // Arrays.toString(coeffsOrthogonal));

            double[] yDataApproxNormal = task_utils.evaluatePolynomial(coeffsNormal, xData);
            chart.addSeries("Нормальный полином степени " + n, xData, yDataApproxNormal).setMarker(SeriesMarkers.NONE);

            double[] yDataApproxOrthogonal = task_utils.evaluatePolynomial(coeffsOrthogonal, xData);
            chart.addSeries("Ортогональный полином степени " + n, xData, yDataApproxOrthogonal)
                    .setMarker(SeriesMarkers.NONE);

            new SwingWrapper<XYChart>(chart).displayChart();

            double errorNormal = task_utils.calculateError(yData, yDataApproxNormal);
            double errorOrthogonal = task_utils.calculateError(yData, yDataApproxOrthogonal);

            System.out.format("%-20d%-30f%-30f%n", n, errorNormal, errorOrthogonal);
        }
    }
}
