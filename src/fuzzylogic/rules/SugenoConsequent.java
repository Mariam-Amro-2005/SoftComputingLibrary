package fuzzylogic.rules;

import fuzzylogic.variables.LinguisticVariable;

public class SugenoConsequent implements Consequent {

    private final LinguisticVariable outputVar;
    private final double crispValue;

    public SugenoConsequent(LinguisticVariable outputVar, double crispValue) {
        this.outputVar = outputVar;
        this.crispValue = crispValue;
    }

    public LinguisticVariable getOutputVariable() {
        return outputVar;
    }

    public double getValue() {
        return crispValue;
    }

    @Override
    public ConsequentType getType() {
        return ConsequentType.SUGENO;
    }

    @Override
    public String toString() {
        return outputVar.getName() + " = " + crispValue;
    }
}
