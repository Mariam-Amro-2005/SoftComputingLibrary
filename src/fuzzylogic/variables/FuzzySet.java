package fuzzylogic.variables;

import fuzzylogic.membership.MembershipFunction;

public class FuzzySet {
    private final String label;
    private final MembershipFunction mf;

    public FuzzySet(String label, MembershipFunction mf) {
        if (label == null || mf == null)
            throw new IllegalArgumentException("Label and MembershipFunction cannot be null.");
        this.label = label;
        this.mf = mf;
    }

    public String getLabel() {
        return label;
    }

    public MembershipFunction getMembershipFunction() {
        return mf;
    }

    public double getMembership(double x) {
        return mf.compute(x);
    }

    // Creates a deep copy of the fuzzy set (MF must be cloneable or immutable)
    public FuzzySet copy() {
        return new FuzzySet(this.label, this.mf.copy());
    }

    @Override
    public String toString() {
        return "FuzzySet{" + "label='" + label + '\'' + ", mf=" + mf + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuzzySet)) return false;
        FuzzySet that = (FuzzySet) o;
        return label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }
}
