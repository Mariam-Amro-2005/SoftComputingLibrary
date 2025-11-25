package fuzzylogic.operators.implications;

public class ProductImplication implements Implication {
    @Override
    public double apply(double a, double b) {
        return a*b;
    }
}
