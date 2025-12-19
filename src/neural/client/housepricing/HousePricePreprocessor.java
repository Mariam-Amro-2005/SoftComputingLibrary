package neural.client.housepricing;

public class HousePricePreprocessor {

    // Min-max normalization
    public static double[][] normalize(double[][] data) {
        int rows = data.length;
        int cols = data[0].length;

        double[][] normalized = new double[rows][cols];

        for (int j = 0; j < cols; j++) {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            for (int i = 0; i < rows; i++) {
                min = Math.min(min, data[i][j]);
                max = Math.max(max, data[i][j]);
            }

            double range = max - min + 1e-8;

            for (int i = 0; i < rows; i++) {
                normalized[i][j] = (data[i][j] - min) / range;
            }
        }

        return normalized;
    }
}
