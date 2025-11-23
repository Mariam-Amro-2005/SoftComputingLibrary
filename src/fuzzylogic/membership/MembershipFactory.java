package fuzzylogic.membership;

public class MembershipFactory {
    public static MembershipFunction create(String type, double... params) {
        return switch (type.toLowerCase()) {
            case "triangular" -> new TriangularMF(params[0], params[1], params[2]);
            case "trapezoidal" -> new TrapezoidalMF(params[0], params[1], params[2], params[3]);
//            case "gaussian"   -> new GaussianMF(params[0], params[1]);
            default -> throw new IllegalArgumentException("Invalid MF type");
        };
    }
}
