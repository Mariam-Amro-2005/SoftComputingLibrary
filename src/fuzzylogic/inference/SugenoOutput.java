package fuzzylogic.inference;

import fuzzylogic.variables.LinguisticVariable;
import java.util.Collections;
import java.util.Map;

/**
 * Holds the crisp outputs for a Sugeno inference engine.
 * Supports ANY number of output variables.
 */
public class SugenoOutput {

    private final Map<LinguisticVariable, Double> crispOutputs;

    public SugenoOutput(Map<LinguisticVariable, Double> crispOutputs) {
        if (crispOutputs == null || crispOutputs.isEmpty()) {
            throw new IllegalArgumentException("Sugeno output map cannot be null or empty.");
        }
        this.crispOutputs = Collections.unmodifiableMap(crispOutputs);
    }

    /** Returns all output variables and their crisp values */
    public Map<LinguisticVariable, Double> getCrispOutputs() {
        return crispOutputs;
    }

    /** Convenience method to get a single output variable's crisp value */
    public double get(LinguisticVariable variable) {
        Double v = crispOutputs.get(variable);
        if (v == null)
            throw new IllegalArgumentException(
                    "Variable not found in Sugeno output: " + variable.getName());
        return v;
    }

    @Override
    public String toString() {
        return "SugenoOutput" + crispOutputs;
    }
}
