package neural.client.housepricing;

import neural.model.NeuralNetwork;

public class HousePriceMain {

    public static void main(String[] args) {

        // Example dataset
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

        // Normalize features
        X = HousePricePreprocessor.normalize(X);

        // Build and train model
        NeuralNetwork model =
                HousePriceTrainer.buildModel(X[0].length);

        HousePriceTrainer.train(model, X, y, 500000, 8, 0.001);

        // Prediction example
        double[][] testHouse = {
                {140, 3, 2, 7}
        };

        testHouse = HousePricePreprocessor.normalize(testHouse);

        double[][] prediction = model.predict(testHouse);

        System.out.println(
                "Predicted house price: " + prediction[0][0]
        );
    }
}
