package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;
import java.util.Map;

public abstract class BaseMamdaniDefuzzifier implements Defuzzifier {

    protected final LinguisticVariable outputVar;
    protected final int samples;

    protected BaseMamdaniDefuzzifier(LinguisticVariable outputVar, int samples) {
        if (outputVar == null) {
            throw new IllegalArgumentException("Output variable cannot be null.");
        }
        if (samples < 3) {
            throw new IllegalArgumentException("Number of samples must be >= 3.");
        }
        this.outputVar = outputVar;
        this.samples = samples;
    }

    /**
     * Computes the clipped membership of all fuzzy sets at x.
     */
    protected double aggregatedMembership(Map<FuzzySet, Double> aggregatedOutput, double x) {
        double max = 0.0;
        for (var entry : aggregatedOutput.entrySet()) {
            FuzzySet set = entry.getKey();
            double strength = entry.getValue();
            double clipped = Math.min(strength, set.getMembership(x));
            if (clipped > max) max = clipped;
        }
        return max;
    }
}
