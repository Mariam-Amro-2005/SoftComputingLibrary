package fuzzylogic.rules;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

public class MamdaniConsequent implements Consequent {
    private final LinguisticVariable outputVariable;
    private final FuzzySet fuzzySet;

    public MamdaniConsequent(LinguisticVariable outputVariable, FuzzySet fuzzySet) {
        if (outputVariable == null || fuzzySet == null) {
            throw new IllegalArgumentException("Output variable and fuzzy set cannot be null.");
        }
        this.outputVariable = outputVariable;
        this.fuzzySet = fuzzySet;
    }

    public LinguisticVariable getOutputVariable() {
        return outputVariable;
    }

    public FuzzySet getFuzzySet() {
        return fuzzySet;
    }

    @Override
    public ConsequentType getType() {
        return ConsequentType.MAMDANI;
    }

    @Override
    public String toString() {
        return outputVariable.getName() + " IS " + fuzzySet.getLabel();
    }
}
