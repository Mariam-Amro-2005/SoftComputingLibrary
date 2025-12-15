package neural.losses;

import neural.core.LossFunction;

public class CrossEntropy implements LossFunction {

    private static final double EPSILON = 1e-9;

    @Override
    public double compute(double[][] predicted, double[][] actual) {
        int samples = predicted.length;
        int classes = predicted[0].length;
        double loss = 0.0;

        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < classes; j++) {
                loss -= actual[i][j] * Math.log(predicted[i][j] + EPSILON);
            }
        }
        return loss / samples;
    }

    @Override
    public double[][] gradient(double[][] predicted, double[][] actual) {
        int samples = predicted.length;
        int classes = predicted[0].length;

        double[][] grad = new double[samples][classes];

        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < classes; j++) {
                grad[i][j] = (predicted[i][j] - actual[i][j]) / samples;
            }
        }
        return grad;
    }
}
