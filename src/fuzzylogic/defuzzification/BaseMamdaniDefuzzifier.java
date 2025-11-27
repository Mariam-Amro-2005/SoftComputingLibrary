package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.Map;

public abstract class BaseMamdaniDefuzzifier {

    protected final int samples;

    protected BaseMamdaniDefuzzifier(int samples) {
        if (samples < 3) throw new IllegalArgumentException("Samples must be >= 3");
        this.samples = samples;
    }

    /**
     * Compute aggregated membership for a specific variable at x
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

    protected double midpoint(LinguisticVariable lv) {
        return (lv.getDomainStart() + lv.getDomainEnd()) / 2.0;
    }
}
