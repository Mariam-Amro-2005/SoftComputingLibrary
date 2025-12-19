package neural.initializers;

import neural.core.Initializer;
import java.util.Random;

public class HeInitializer implements Initializer {

    private final Random random = new Random();

    @Override
    public double[][] init(int inputSize, int outputSize) {
        double stdDev = Math.sqrt(2.0 / inputSize);
        double[][] weights = new double[inputSize][outputSize];

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = random.nextGaussian() * stdDev;
            }
        }

        return weights;
    }
}
