package neural.initializers;

import java.util.Random;

public class RandomUniformInitializer implements Initializer {

    private final double min;
    private final double max;
    private final Random random = new Random();

    public RandomUniformInitializer(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public double[][] init(int inputSize, int outputSize) {
        double[][] weights = new double[inputSize][outputSize];

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = min + (max - min) * random.nextDouble();
            }
        }
        return weights;
    }
}

