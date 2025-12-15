package neural.initializers;

import java.util.Random;

public class XavierInitializer implements Initializer {

    private final Random random = new Random();

    @Override
    public double[][] init(int inputSize, int outputSize) {
        double limit = Math.sqrt(6.0 / (inputSize + outputSize));
        double[][] weights = new double[inputSize][outputSize];

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = -limit + 2 * limit * random.nextDouble();
            }
        }
        return weights;
    }
}

