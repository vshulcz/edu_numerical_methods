package task_6;

import java.util.Arrays;

public class task_6_1 {
    public static double[][] main(int m, double corridorWidth) {
        double[][] experimentalData = task_utils.generateExperimentalData(m, corridorWidth);

        Arrays.sort(experimentalData, (a, b) -> Double.compare(a[0], b[0]));

        System.out.println("Сгенерированные данные:");
        System.out.println("x\t\tf(x)\t\tf1(x)\t\tf2(x)");
        for (int i = 0; i < m; i++) {
            System.out.printf("%.4f\t\t%.4f\t\t%.4f\t\t%.4f\n", experimentalData[i][0], experimentalData[i][1],
                    experimentalData[i][2], experimentalData[i][3]);
        }

        return experimentalData;
    }
}
