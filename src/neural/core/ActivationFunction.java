package neural.core;

public interface ActivationFunction {
    double[][] activate(double[][] input);
    double[][] derivative(double[][] input);
}

