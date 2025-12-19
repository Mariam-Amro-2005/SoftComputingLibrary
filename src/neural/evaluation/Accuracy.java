package neural.evaluation;

public final class Accuracy {

    private Accuracy() {}

    /* =========================
       Classification Accuracy
       ========================= */

    public static double classification(double[][] predicted,
                                        double[][] actual) {

        int samples = predicted.length;
        int correct = 0;

        for (int i = 0; i < samples; i++) {
            if (argMax(predicted[i]) == argMax(actual[i])) {
                correct++;
            }
        }
        return correct / (double) samples;
    }

    private static int argMax(double[] row) {
        int index = 0;
        double max = row[0];

        for (int i = 1; i < row.length; i++) {
            if (row[i] > max) {
                max = row[i];
                index = i;
            }
        }
        return index;
    }

    /* =========================
       Regression Evaluation
       ========================= */

    public static double mse(double[][] predicted,
                             double[][] actual) {

        int samples = predicted.length;
        int outputs = predicted[0].length;

        double sum = 0.0;

        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < outputs; j++) {
                double diff = predicted[i][j] - actual[i][j];
                sum += diff * diff;
            }
        }
        return sum / (samples * outputs);
    }
}
