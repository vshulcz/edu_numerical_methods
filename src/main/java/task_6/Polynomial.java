package task_6;

public class Polynomial {
    private double[] coefficients;

    public Polynomial(double... coeffs) {
        this.coefficients = coeffs;
    }

    public double evaluate(double x) {
        double result = 0.0;
        for (int i = coefficients.length - 1; i >= 0; i--) {
            result = result * x + coefficients[i];
        }
        return result;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public static Polynomial one() {
        return new Polynomial(1);
    }

    public Polynomial multiply(Polynomial other) {
        double[] result = new double[this.coefficients.length + other.coefficients.length - 1];
        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < other.coefficients.length; j++) {
                result[i + j] += this.coefficients[i] * other.coefficients[j];
            }
        }
        return new Polynomial(result);
    }

    public Polynomial subtract(Polynomial other) {
        int length = Math.max(this.coefficients.length, other.coefficients.length);
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            double a = i < this.coefficients.length ? this.coefficients[i] : 0;
            double b = i < other.coefficients.length ? other.coefficients[i] : 0;
            result[i] = a - b;
        }
        return new Polynomial(result);
    }

    public Polynomial multiply(double scalar) {
        double[] result = new double[this.coefficients.length];
        for (int i = 0; i < this.coefficients.length; i++) {
            result[i] = this.coefficients[i] * scalar;
        }
        return new Polynomial(result);
    }
}