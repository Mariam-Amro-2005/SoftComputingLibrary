package neural.factories;

import neural.core.Optimizer;
import neural.optimizers.*;

public class OptimizerFactory {

    public static Optimizer create(String name, double learningRate) {
        return switch (name.toLowerCase()) {
            case "sgd" -> new SGD(learningRate);
            default -> throw new IllegalArgumentException(
                    "Unknown optimizer: " + name
            );
        };
    }

    public static Optimizer create(String name) {
        return switch (name.toLowerCase()) {
            case "sgd" -> new SGD();
            default -> throw new IllegalArgumentException(
                    "Unknown optimizer: " + name
            );
        };
    }
}
