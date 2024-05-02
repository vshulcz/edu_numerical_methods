package task_4_2;

import java.util.ArrayList;
import java.util.List;

public class SplineInterpolationTest {

    public static void main(String[] args) {
        testLinearSpline();
        testQuadraticSpline();
        testCubicSpline();
    }

    public static void testLinearSpline() {
        System.out.println("Тестирование линейного сплайна:");
        double a = -10;
        double b = 10;
        int n = 10; // Количество узлов интерполяции
        int k = 100; // Количество проверочных точек

        List<Double> nodes = SplineInterpolation.generateNodes(a, b, n);
        List<Double> values = new ArrayList<>();
        for (double node : nodes) {
            values.add(SplineInterpolation.f(node));
        }

        double maxDeviation = SplineInterpolation.maxDeviationLinear(nodes, values, k);
        double maxDeviationOptimal = SplineInterpolation.maxDeviationOptimalLinear(nodes, values, k);

        System.out.println("Максимальное отклонение по равностоящим узлам: " + maxDeviation);
        System.out.println("Максимальное отклонение по оптимальным узлам: " + maxDeviationOptimal);
        System.out.println();
    }

    public static void testQuadraticSpline() {
        System.out.println("Тестирование квадратичного сплайна:");
        double a = 0;
        double b = 10;
        int n = 10; // Количество узлов интерполяции
        int k = 100; // Количество проверочных точек

        List<Double> nodes = SplineInterpolation.generateNodes(a, b, n);
        List<Double> values = new ArrayList<>();
        for (double node : nodes) {
            values.add(SplineInterpolation.f(node));
        }

        double maxDeviation = SplineInterpolation.maxDeviationQuadratic(nodes, values, k);
        double maxDeviationOptimal = SplineInterpolation.maxDeviationOptimalQuadratic(nodes, values, k);

        System.out.println("Максимальное отклонение по равностоящим узлам: " + maxDeviation);
        System.out.println("Максимальное отклонение по оптимальным узлам: " + maxDeviationOptimal);
        System.out.println();
    }

    public static void testCubicSpline() {
        System.out.println("Тестирование кубического сплайна:");
        double a = 0;
        double b = 10;
        int n = 10; // Количество узлов интерполяции
        int k = 100; // Количество проверочных точек

        List<Double> nodes = SplineInterpolation.generateNodes(a, b, n);
        List<Double> values = new ArrayList<>();
        for (double node : nodes) {
            values.add(SplineInterpolation.f(node));
        }

        double maxDeviation = SplineInterpolation.maxDeviationCubic(nodes, values, k);
        double maxDeviationOptimal = SplineInterpolation.maxDeviationOptimalCubic(nodes, values, k);

        System.out.println("Максимальное отклонение по равностоящим узлам: " + maxDeviation);
        System.out.println("Максимальное отклонение по оптимальным узлам: " + maxDeviationOptimal);
        System.out.println();
    }
}
