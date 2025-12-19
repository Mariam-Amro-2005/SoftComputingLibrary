package neural.factories;

import neural.core.ActivationFunction;
import neural.activations.*;

public class ActivationFactory {

    public static ActivationFunction create(String name) {
        return switch (name.toLowerCase()) {
            case "relu" -> new ReLU();
            case "sigmoid" -> new Sigmoid();
            case "tanh" -> new Tanh();
            case "linear" -> new Linear();
            default -> throw new IllegalArgumentException(
                    "Unknown activation: " + name
            );
        };
    }
}

