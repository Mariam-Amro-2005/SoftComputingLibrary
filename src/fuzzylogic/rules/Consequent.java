package fuzzylogic.rules;

import fuzzylogic.variables.FuzzySet;

public class Consequent {

    private final FuzzySet fuzzySet;

    public Consequent(FuzzySet fuzzySet) {
        this.fuzzySet = fuzzySet;
    }

    public FuzzySet getFuzzySet() {
        return fuzzySet;
    }

    @Override
    public String toString() {
        return fuzzySet.getLabel();
    }
}
