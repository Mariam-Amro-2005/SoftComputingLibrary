package neural.layers;

import neural.core.Layer;
import neural.core.ActivationFunction;
import neural.core.Optimizer;
import neural.initializers.Initializer;
import neural.utils.Matrix;

public class DenseLayer implements Layer {

    private final int inputSize;
    private final int outputSize;

    private final double[][] weights;
    private final double[][] biases;

    // Gradients
    private double[][] gradWeights;
    private double[][] gradBiases;

    // Cached values
    private double[][] input;
    private double[][] z;

    private final ActivationFunction activation;

    public DenseLayer(int inputSize,
                      int outputSize,
                      ActivationFunction activation,
                      Initializer initializer) {

        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activation = activation;

        this.weights = initializer.init(inputSize, outputSize);
        this.biases = new double[1][outputSize];
    }

    @Override
    public double[][] forward(double[][] input) {
        this.input = input;
        int batchSize = input.length;

        z = new double[batchSize][outputSize];

        for (int b = 0; b < batchSize; b++) {
            for (int i = 0; i < outputSize; i++) {
                double sum = 0.0;
                for (int j = 0; j < inputSize; j++) {
                    sum += input[b][j]*weights[i][j] ;
                }
                sum += biases[0][i];
                z[b][i] = sum;
            }
        }

        return activation.activate(z);
    }

    @Override
    public double[][] backward(double[][] gradOutput) {

        int batchSize = gradOutput.length;

        gradWeights = new double[outputSize][inputSize];
        gradBiases = new double[1][outputSize];

        double[][] gradInput = new double[batchSize][inputSize];

        double[][] activationDerivative = activation.derivative(z);

        for (int b = 0; b < batchSize; b++) {
            for (int i = 0; i < outputSize; i++) {

                double delta = gradOutput[b][i] * activationDerivative[b][i];
                gradBiases[0][i] += delta;

                for (int j = 0; j < inputSize; j++) {
                    gradWeights[i][j] += delta * input[b][j];
                    gradInput[b][j] += weights[i][j] * delta;
                }
            }
        }

        for (int i = 0; i < outputSize; i++) {
            gradBiases[0][i] /= batchSize;
            for (int j = 0; j < inputSize; j++) {
                gradWeights[i][j] /= batchSize;
            }
        }

        return gradInput;
    }

    @Override
    public void updateParameters(Optimizer optimizer) {
        optimizer.update(weights, gradWeights);
        optimizer.update(biases, gradBiases);
    }
}