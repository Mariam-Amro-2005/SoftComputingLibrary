package neural.losses;

import neural.core.LossFunction;
import neural.utils.Matrix;

public class MSE implements LossFunction {

    @Override
    public double compute(double[][] predicted, double[][] actual) {
        int samples = predicted.length;
        int outputs = predicted[0].length;

        double sum = 0.0;

        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < outputs; j++) {
                double diff = predicted[i][j] - actual[i][j];
                sum += diff * diff;
            }
        }

        return sum / (samples * outputs);
    }

    @Override
    public double[][] gradient(double[][] predicted, double[][] actual) {
        int samples = predicted.length;
        int outputs = predicted[0].length;

        double[][] grad = new double[samples][outputs];

        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < outputs; j++) {
                grad[i][j] = 2.0 * (predicted[i][j] - actual[i][j])
                        / (samples * outputs);
            }
        }

        return grad;
    }
}
