package neural.model;

import neural.core.Layer;
import neural.core.LossFunction;
import neural.core.Optimizer;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    private List<Layer> layers;
    private LossFunction loss;

    public NeuralNetwork(LossFunction loss) {
        this.layers = new ArrayList<>();
        this.loss = loss;
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public double[][] forward(double[][] input) {
        for (Layer layer : layers)
            input = layer.forward(input);
        return input;
    }

    public void backward(double[][] predicted, double[][] actual) {
        double[][] grad = loss.gradient(predicted, actual);
        for (int i = layers.size() - 1; i >= 0; i--)
            grad = layers.get(i).backward(grad);
    }

    public void update(Optimizer optimizer) {
        for (Layer layer : layers)
            layer.updateParameters(optimizer);
    }
}
