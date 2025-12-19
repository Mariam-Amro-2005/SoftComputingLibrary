package neural.model;

import neural.core.Layer;
import neural.core.LossFunction;
import neural.core.Optimizer;

import java.util.*;

public class NeuralNetwork {

    private final List<Layer> layers;
    private final LossFunction loss;
    private final Optimizer optimizer;

    public NeuralNetwork(LossFunction loss, Optimizer optimizer) {
        this.layers = new ArrayList<>();
        this.loss = loss;
        this.optimizer = optimizer;
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

    private void update() {
        for (Layer layer : layers)
            layer.updateParameters(optimizer);
    }

    public void train(
            double[][] X,
            double[][] y,
            int epochs,
            int batchSize,
            double lossThreshold
    ) {

        int samples = X.length;
        Random random = new Random();

        for (int epoch = 1; epoch <= epochs; epoch++) {

            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < samples; i++)
                indices.add(i);
            Collections.shuffle(indices, random);

            double epochLoss = 0.0;

            for (int start = 0; start < samples; start += batchSize) {

                int end = Math.min(start + batchSize, samples);

                double[][] Xbatch = new double[end - start][];
                double[][] ybatch = new double[end - start][];

                for (int i = start; i < end; i++) {
                    Xbatch[i - start] = X[indices.get(i)];
                    ybatch[i - start] = y[indices.get(i)];
                }

                double[][] predicted = forward(Xbatch);
                epochLoss += loss.compute(predicted, ybatch);

                backward(predicted, ybatch);
                update();
            }

            epochLoss /= (samples / (double) batchSize);

            System.out.printf(
                    "Epoch %d/%d - Loss: %.6f%n",
                    epoch, epochs, epochLoss
            );

            // ✅ Early stopping
            if (epochLoss <= lossThreshold) {
                System.out.println(
                        "Training stopped early: loss threshold reached."
                );
                break;
            }
        }
    }


    public double[][] predict(double[][] input) {
        return forward(input);
    }
}
