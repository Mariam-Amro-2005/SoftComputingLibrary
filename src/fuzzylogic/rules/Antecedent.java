package fuzzylogic.rules;

import fuzzylogic.variables.LinguisticVariable;

public class Antecedent {

    private final LinguisticVariable variable;
    private final String fuzzySetLabel;

    public Antecedent(LinguisticVariable variable, String fuzzySetLabel) {
        this.variable = variable;
        this.fuzzySetLabel = fuzzySetLabel;
    }

    public LinguisticVariable getVariable() {
        return variable;
    }

    public String getFuzzySetLabel() {
        return fuzzySetLabel;
    }

    @Override
    public String toString() {
        return variable.getName() + " is " + fuzzySetLabel;
    }
}
