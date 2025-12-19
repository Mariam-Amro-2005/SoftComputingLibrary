package neural.client.housepricing;

import neural.initializers.XavierInitializer;
import neural.model.NeuralNetwork;
import neural.layers.DenseLayer;
import neural.activations.ReLU;
import neural.activations.Linear;
import neural.losses.MSE;
import neural.optimizers.SGD;

public class HousePriceTrainer {

    public static NeuralNetwork buildModel(int inputSize) {

        NeuralNetwork nn = new NeuralNetwork(
                new MSE(),
                new SGD(0.01)
        );

        nn.addLayer(new DenseLayer(inputSize, 8, new ReLU(), new XavierInitializer()));
        nn.addLayer(new DenseLayer(8, 1, new Linear(), new XavierInitializer()));

        return nn;
    }

    public static void train(
            NeuralNetwork model,
            double[][] X,
            double[][] y,
            int epochs,
            int batchSize,
            double lossThreshold
    ) {
        model.train(X, y, epochs, batchSize, lossThreshold);
    }
}

