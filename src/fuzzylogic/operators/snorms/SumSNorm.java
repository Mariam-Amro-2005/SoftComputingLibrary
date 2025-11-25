package fuzzylogic.operators.snorms;

public class SumSNorm implements SNorm {
    @Override
    public double apply(double a, double b) {
        return Math.min(1,a+b);
    }
}
