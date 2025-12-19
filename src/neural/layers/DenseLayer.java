package neural.layers;

import neural.core.Layer;
import neural.core.ActivationFunction;
import neural.core.Optimizer;
import neural.initializers.Initializer;
import neural.utils.Matrix;

public class DenseLayer implements Layer {

    private final ActivationFunction activation;

    private double[][] weights;
    private double[][] bias;

    // Cached values for backpropagation
    private double[][] inputCache;
    private double[][] zCache;

    // Gradients
    private double[][] gradWeights;
    private double[][] gradBias;

    public DenseLayer(int inputSize,
                      int outputSize,
                      ActivationFunction activation,
                      Initializer initializer) {

        this.activation = activation;
        this.weights = initializer.init(inputSize, outputSize);
        this.bias = new double[1][outputSize];
    }

    @Override
    public double[][] forward(double[][] input) {
        this.inputCache = input;

        double[][] z = Matrix.add(Matrix.dot(input, weights), bias);
        this.zCache = z;

        return activation.activate(z);
    }

    @Override
    public double[][] backward(double[][] gradOutput) {

        // dL/dZ = dL/dA ⊙ activation'(Z)
        double[][] gradZ = Matrix.hadamard(
                gradOutput,
                activation.derivative(zCache)
        );

        // dL/dW = Xᵀ · dZ
        gradWeights = Matrix.dot(
                Matrix.transpose(inputCache),
                gradZ
        );

        // dL/dB = sum rows of dZ
        gradBias = Matrix.sumRows(gradZ);

        // dL/dX = dZ · Wᵀ
        return Matrix.dot(
                gradZ,
                Matrix.transpose(weights)
        );
    }

    @Override
    public void updateParameters(Optimizer optimizer) {
        optimizer.update(weights, gradWeights);
        optimizer.update(bias, gradBias);
    }
}
