package neural.core;

public interface Optimizer {
    void update(double[][] weights, double[][] gradients);
}

