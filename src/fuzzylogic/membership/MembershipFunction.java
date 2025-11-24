package fuzzylogic.membership;

public interface MembershipFunction {
    double compute(double x);
    String getName();
    MembershipFunction copy();
}
