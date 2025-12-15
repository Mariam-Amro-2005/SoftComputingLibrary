package neural.core;

public interface LossFunction {
    double compute(double[][] predicted, double[][] actual);
    double[][] gradient(double[][] predicted, double[][] actual);
}
