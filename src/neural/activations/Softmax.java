package neural.activations;

import neural.core.ActivationFunction;

public class Softmax implements ActivationFunction {

    @Override
    public double[][] activate(double[][] input) {
        int rows = input.length;
        int cols = input[0].length;

        double[][] output = new double[rows][cols];

        for (int i = 0; i < rows; i++) {

            // 1. Find max value for numerical stability
            double max = input[i][0];
            for (int j = 1; j < cols; j++) {
                if (input[i][j] > max)
                    max = input[i][j];
            }

            // 2. Compute exponentials and sum
            double sum = 0.0;
            for (int j = 0; j < cols; j++) {
                output[i][j] = Math.exp(input[i][j] - max);
                sum += output[i][j];
            }

            // 3. Normalize
            for (int j = 0; j < cols; j++) {
                output[i][j] /= sum;
            }
        }

        return output;
    }

    @Override
    public double[][] derivative(double[][] input) {
        /*
         * IMPORTANT:
         * When Softmax is used with Cross-Entropy loss,
         * the gradient simplifies to (y_hat - y).
         * Therefore, this derivative is not used.
         */
        int rows = input.length;
        int cols = input[0].length;

        double[][] grad = new double[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grad[i][j] = 1.0;

        return grad;
    }
}
