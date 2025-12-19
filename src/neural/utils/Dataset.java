package neural.utils;

import java.util.Random;

public class Dataset {

    public static double[][] normalize(double[][] data, NormalizationType type) {
        switch (type) {
            case Z_SCORE:
                return zScoreNormalize(data);
            case MIN_MAX:
            default:
                return minMaxNormalize(data);
        }
    }

    public static double[][] normalize(double[][] data, String typeStr) {
        return normalize(data, NormalizationType.fromString(typeStr));
    }

    public static double[][] normalize(double[][] data) {
        return normalize(data, NormalizationType.MIN_MAX);
    }

    private static double[][] minMaxNormalize(double[][] data) {
        int nSamples = data.length;
        if (nSamples == 0) return new double[0][0];
        int nFeatures = data[0].length;
        double[][] normalized = new double[nSamples][nFeatures];

        for (int j = 0; j < nFeatures; j++) {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (int i = 0; i < nSamples; i++) {
                if (data[i][j] < min) min = data[i][j];
                if (data[i][j] > max) max = data[i][j];
            }
            double range = max - min;
            if (range == 0) range = 1;
            for (int i = 0; i < nSamples; i++) {
                normalized[i][j] = (data[i][j] - min) / range;
            }
        }

        return normalized;
    }

    private static double[][] zScoreNormalize(double[][] data) {
        int nSamples = data.length;
        if (nSamples == 0) return new double[0][0];
        int nFeatures = data[0].length;
        double[][] normalized = new double[nSamples][nFeatures];

        for (int j = 0; j < nFeatures; j++) {
            double sum = 0;
            for (int i = 0; i < nSamples; i++) sum += data[i][j];
            double mean = sum / nSamples;

            double varianceSum = 0;
            for (int i = 0; i < nSamples; i++)
                varianceSum += Math.pow(data[i][j] - mean, 2);
            double std = Math.sqrt(varianceSum / nSamples);
            if (std == 0) std = 1; // avoid division by zero

            for (int i = 0; i < nSamples; i++)
                normalized[i][j] = (data[i][j] - mean) / std;
        }

        return normalized;
    }

    //      return is an array containing two elements: {trainData, testData}
    public static double[][][] trainTestSplit(double[][] data, double trainFraction, long seed) {
        int nSamples = data.length;
        int nTrain = (int) (nSamples * trainFraction);

        double[][] shuffled = new double[nSamples][];
        for (int i = 0; i < nSamples; i++) {
            shuffled[i] = data[i].clone();
        }

        // Shuffle dataset
        Random rand = new Random(seed);
        for (int i = nSamples - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            double[] temp = shuffled[i];
            shuffled[i] = shuffled[j];
            shuffled[j] = temp;
        }

        // Split
        double[][] trainData = new double[nTrain][];
        double[][] testData = new double[nSamples - nTrain][];

        System.arraycopy(shuffled, 0, trainData, 0, nTrain);
        System.arraycopy(shuffled, nTrain, testData, 0, nSamples - nTrain);

        return new double[][][]{trainData, testData};
    }

    public static double[][][] trainTestSplit(double[][] data) {
        return trainTestSplit(data, 0.8, System.currentTimeMillis());
    }
}
