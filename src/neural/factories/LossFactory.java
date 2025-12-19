package neural.factories;

import neural.core.LossFunction;
import neural.losses.*;

public class LossFactory {

    public static LossFunction create(String name) {
        return switch (name.toLowerCase()) {
            case "mse" -> new MSE();
            case "cross-entropy" -> new CrossEntropy();
            default -> throw new IllegalArgumentException(
                    "Unknown loss: " + name
            );
        };
    }
}
