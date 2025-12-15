package neural.core;

public interface Layer {
    double[][] forward(double[][] input);
    double[][] backward(double[][] gradOutput);
    void updateParameters(Optimizer optimizer);
}

