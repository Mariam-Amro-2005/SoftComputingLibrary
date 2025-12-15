package neural.activations;

import neural.core.ActivationFunction;

public class Linear implements ActivationFunction {

    @Override
    public double[][] activate(double[][] input) {
        int rows = input.length;
        int cols = input[0].length;

        double[][] output = new double[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                output[i][j] = input[i][j];

        return output;
    }

    @Override
    public double[][] derivative(double[][] input) {
        int rows = input.length;
        int cols = input[0].length;

        double[][] grad = new double[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grad[i][j] = 1.0;

        return grad;
    }
}
