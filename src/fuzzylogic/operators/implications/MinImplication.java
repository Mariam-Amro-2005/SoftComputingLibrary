package fuzzylogic.operators.implications;

public class MinImplication implements Implication {
    @Override
    public double apply(double a, double b) {
        return Math.min(a, b);
    }
}
