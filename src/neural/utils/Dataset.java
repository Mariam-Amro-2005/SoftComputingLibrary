package neural.utils;

import java.util.Random;

public class Dataset {
    public static NormalizationStats computeNormalizationStats(double[][] data, NormalizationType type)
     {
        return switch (type) {
            case Z_SCORE -> computeZScoreStats(data);
            case MIN_MAX -> computeMinMaxStats(data);
        };
    }

    public static NormalizationStats computeNormalizationStats(double[][] data) {
        return computeNormalizationStats(data, NormalizationType.MIN_MAX);
    }

    public static double[][] normalize(double[][] data, NormalizationStats stats, NormalizationType type) {
        return switch (type) {
            case Z_SCORE -> applyZScore(data, stats);
            case MIN_MAX -> applyMinMax(data, stats);
        };
    }

    public static double[][] normalize(double[][] data, NormalizationStats stats) {
        return normalize(data, stats, NormalizationType.MIN_MAX);
    }

    private static NormalizationStats computeMinMaxStats(double[][] data) {
        int nFeatures = data[0].length;
        double[] min = new double[nFeatures];
        double[] max = new double[nFeatures];

        for (int j = 0; j < nFeatures; j++) {
            min[j] = Double.MAX_VALUE;
            max[j] = -Double.MAX_VALUE;

            for (double[] row : data) {
                min[j] = Math.min(min[j], row[j]);
                max[j] = Math.max(max[j], row[j]);
            }

            if (max[j] - min[j] == 0) {
                max[j] = min[j] + 1;
            }
        }

        return new NormalizationStats(min, max, null, null);
    }

    private static NormalizationStats computeZScoreStats(double[][] data) {
        int nSamples = data.length;
        int nFeatures = data[0].length;

        double[] mean = new double[nFeatures];
        double[] std = new double[nFeatures];

        for (int j = 0; j < nFeatures; j++) {
            for (double[] row : data)
                mean[j] += row[j];
            mean[j] /= nSamples;

            for (double[] row : data)
                std[j] += Math.pow(row[j] - mean[j], 2);
            std[j] = Math.sqrt(std[j] / nSamples);

            if (std[j] == 0) std[j] = 1;
        }

        return new NormalizationStats(null, null, mean, std);
    }

    private static double[][] applyMinMax(double[][] data, NormalizationStats stats) {
        int nSamples = data.length;
        int nFeatures = data[0].length;
        double[][] normalized = new double[nSamples][nFeatures];

        for (int i = 0; i < nSamples; i++) {
            for (int j = 0; j < nFeatures; j++) {
                normalized[i][j] =
                        (data[i][j] - stats.min[j]) /
                                (stats.max[j] - stats.min[j]);
            }
        }

        return normalized;
    }

    private static double[][] applyZScore(double[][] data, NormalizationStats stats) {
        int nSamples = data.length;
        int nFeatures = data[0].length;
        double[][] normalized = new double[nSamples][nFeatures];

        for (int i = 0; i < nSamples; i++) {
            for (int j = 0; j < nFeatures; j++) {
                normalized[i][j] =
                        (data[i][j] - stats.mean[j]) / stats.std[j];
            }
        }

        return normalized;
    }

    //      return is an array containing two elements: {trainData, testData}
    public static double[][][] trainTestSplit(double[][] data, double trainFraction, long seed) {
        int nSamples = data.length;
        int nTrain = (int) (nSamples * trainFraction);

        double[][] shuffled = new double[nSamples][];
        for (int i = 0; i < nSamples; i++)
            shuffled[i] = data[i].clone();

        Random rand = new Random(seed);
        for (int i = nSamples - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            double[] temp = shuffled[i];
            shuffled[i] = shuffled[j];
            shuffled[j] = temp;
        }

        double[][] train = new double[nTrain][];
        double[][] test = new double[nSamples - nTrain][];

        System.arraycopy(shuffled, 0, train, 0, nTrain);
        System.arraycopy(shuffled, nTrain, test, 0, test.length);

        return new double[][][]{train, test};
    }
}
