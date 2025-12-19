package neural.optimizers;

import neural.core.Optimizer;

public class SGD implements Optimizer {

    private final double learningRate;

    public SGD() {
        this(0.5);
    }

    public SGD(double learningRate) {
        this.learningRate = learningRate;
    }

    @Override
    public void update(double[][] parameters, double[][] gradients) {
        int rows = parameters.length;
        int cols = parameters[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                parameters[i][j] -= learningRate * gradients[i][j];
            }
        }
    }
}
