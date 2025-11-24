package fuzzylogic.membership;

public class MembershipFactory {

    public static MembershipFunction create(String type, double... params) {

        return switch (type.toLowerCase()) {
            case "triangular" -> {
                if (params.length != 3)
                    throw new IllegalArgumentException("Triangular MF requires 3 parameters");
                yield new TriangularMF(params[0], params[1], params[2]);
            }

            case "trapezoidal" -> {
                if (params.length != 4)
                    throw new IllegalArgumentException("Trapezoidal MF requires 4 parameters");
                yield new TrapezoidalMF(params[0], params[1], params[2], params[3]);
            }

            default -> throw new IllegalArgumentException("Invalid MF type: " + type);
        };
    }
}
