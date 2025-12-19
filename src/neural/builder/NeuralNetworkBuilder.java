package neural.builder;

import neural.model.NeuralNetwork;
import neural.layers.DenseLayer;
import neural.factories.*;

public class NeuralNetworkBuilder {

    private NeuralNetwork network;

    private NeuralNetworkBuilder() {}

    public static NeuralNetworkBuilder create() {
        NeuralNetworkBuilder builder = new NeuralNetworkBuilder();
        builder.network = new NeuralNetwork();
        return builder;
    }

    public NeuralNetworkBuilder loss(String lossName) {
        if (network == null)
            throw new IllegalStateException(
                    "Loss must be set before optimizer."
            );
        network.setLoss(LossFactory.create(lossName));
        return this;
    }

    public NeuralNetworkBuilder optimizer(String optimizerName, double lr) {
        if (network == null)
            throw new IllegalStateException(
                    "Loss must be set before optimizer."
            );

        network.setOptimizer(OptimizerFactory.create(optimizerName, lr));
        return this;
    }

    public NeuralNetworkBuilder dense(
            int inputSize,
            int outputSize,
            String activationName,
            String initializerName
    ) {
        if (network == null)
            throw new IllegalStateException(
                    "Network not initialized."
            );

        network.addLayer(new DenseLayer(
                inputSize,
                outputSize,
                ActivationFactory.create(activationName),
                InitializerFactory.create(initializerName)
        ));
        return this;
    }

    public NeuralNetworkBuilder dense(
            int inputSize,
            int outputSize,
            String activationName,
            String initializerName,
            double initMin,
            double initMax
    ) {
        if (network == null)
            throw new IllegalStateException(
                    "Network not initialized."
            );

        network.addLayer(new DenseLayer(
                inputSize,
                outputSize,
                ActivationFactory.create(activationName),
                InitializerFactory.create(initializerName, initMin, initMax)
        ));
        return this;
    }

    public NeuralNetworkBuilder dense(
            int inputSize,
            int outputSize,
            String initializerName,
            double initMin,
            double initMax
    ) {
        if (network == null)
            throw new IllegalStateException(
                    "Network not initialized."
            );

        network.addLayer(new DenseLayer(
                inputSize,
                outputSize,
                InitializerFactory.create(initializerName, initMin, initMax)
        ));
        return this;
    }

    public NeuralNetworkBuilder dense(
            int inputSize,
            int outputSize,
            String activationName
    ) {
        if (network == null)
            throw new IllegalStateException(
                    "Network not initialized."
            );

        network.addLayer(new DenseLayer(
                inputSize,
                outputSize,
                ActivationFactory.create(activationName)
        ));
        return this;
    }

    public NeuralNetworkBuilder dense( int inputSize, int outputSize) {
        if (network == null)
            throw new IllegalStateException(
                    "Network not initialized."
            );

        network.addLayer(new DenseLayer(inputSize, outputSize));
        return this;
    }

    public NeuralNetwork build() {
        if (network == null)
            throw new IllegalStateException("Network not built.");

        if (network.getLoss() == null)
            throw new IllegalStateException("Loss function not set.");

        if (network.getOptimizer() == null)
            throw new IllegalStateException("Optimizer not set.");

        return network;
    }
}
