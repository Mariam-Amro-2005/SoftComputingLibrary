package neural.client.housepricing;

import neural.builder.NeuralNetworkBuilder;
import neural.model.NeuralNetwork;
import neural.utils.Dataset;
import neural.utils.NormalizationStats;
import neural.utils.NormalizationType;
import neural.evaluation.Accuracy;

public class HousePriceMain {

    public static void main(String[] args) {

        // Example in-memory dataset
        double[][] X = {
                {120, 3, 2, 10},
                {80,  2, 1, 20},
                {200, 4, 3, 5},
                {150, 3, 2, 8},
                {60,  1, 1, 30}
        };

        double[][] y = {
                {250000},
                {180000},
                {420000},
                {300000},
                {120000}
        };

        // Normalize using TRAIN statistics
        NormalizationStats stats =
                Dataset.computeNormalizationStats(X, NormalizationType.MIN_MAX);

        X = Dataset.normalize(X, stats);

        // Build neural network
        NeuralNetwork model =
                NeuralNetworkBuilder.create()
                        .loss("mse")
                        .optimizer("sgd", 0.00000001)
                        .dense(4, 16, "relu")
                        .dense(16, 8, "relu")
                        .dense(8, 1, "linear")
                        .build();

        // Train
        model.train(
                X,
                y,
                500000,
                8,
                0.001
        );

        // Evaluate on training data
        double[][] trainPredictions = model.predict(X);
        double trainMSE = Accuracy.mse(trainPredictions, y);

        System.out.println(
                "Training MSE: " + trainMSE
        );

        // Prediction example
        double[][] testHouse = {
                {140, 3, 2, 7}
        };

        testHouse = Dataset.normalize(testHouse, stats);

        double[][] prediction = model.predict(testHouse);

        System.out.println(
                "Predicted house price: " + prediction[0][0]
        );
    }
}
