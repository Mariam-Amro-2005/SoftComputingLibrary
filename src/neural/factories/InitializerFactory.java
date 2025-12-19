package neural.factories;

import neural.core.Initializer;
import neural.initializers.*;

public class InitializerFactory {

    public static Initializer create(String name) {
        return switch (name.toLowerCase()) {
            case "xavier" -> new XavierInitializer();
            case "he" -> new HeInitializer();
            case "random" -> new RandomUniformInitializer();
            default -> throw new IllegalArgumentException(
                    "Unknown initializer: " + name
            );
        };
    }

    public static Initializer create(
            String name,
            double min,
            double max
    ) {
        return switch (name.toLowerCase()) {
            case "random" -> new RandomUniformInitializer(min, max);
            default -> throw new IllegalArgumentException(
                    "Initializer does not support parameters: " + name
            );
        };
    }
}

