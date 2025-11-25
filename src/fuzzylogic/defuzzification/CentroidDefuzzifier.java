package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;
import java.util.Map;
import java.util.Objects;

public class CentroidDefuzzifier implements Defuzzifier {
    private final LinguisticVariable outputVariable;
    private final int numberOfSamples;

    public CentroidDefuzzifier(LinguisticVariable outputVariable) {
        this(outputVariable, 1001);
    }
    public CentroidDefuzzifier(LinguisticVariable outputVariable, int numberOfSamples) {
        Objects.requireNonNull(outputVariable, "Output LinguisticVariable cannot be null.");
        if (numberOfSamples < 3) {
            throw new IllegalArgumentException("Number of samples must be at least 3.");
        }
        this.outputVariable = outputVariable;
        this.numberOfSamples = numberOfSamples;
    }

    @Override
    public double defuzzify(Map<FuzzySet, Double> outputSetActivations) {
        if (outputSetActivations == null || outputSetActivations.isEmpty()) {
            return (outputVariable.getDomainStart() + outputVariable.getDomainEnd()) / 2.0;
        }

        double domainStart = outputVariable.getDomainStart();
        double domainEnd = outputVariable.getDomainEnd();
        double step = (domainEnd - domainStart) / (numberOfSamples - 1);

        // These variables correspond to the Centroid formula: ∫ x*μ(x)dx / ∫ μ(x)dx
        // In the discrete form used here it becomes: Σ(x * μ(x)) / Σ(μ(x))

        double numerator_weightedSum = 0.0;
        double denominator_areaSum = 0.0;
        for (int i = 0; i < numberOfSamples; i++) {
            double x_currentPoint = domainStart + i * step;

            double mu_finalShapeMembership = getFinalShapeMembership(outputSetActivations, x_currentPoint);
            numerator_weightedSum += x_currentPoint * mu_finalShapeMembership;
            denominator_areaSum += mu_finalShapeMembership;
        }

        if (denominator_areaSum < 1e-9) {
            return (outputVariable.getDomainStart() + outputVariable.getDomainEnd()) / 2.0;
        }
        return numerator_weightedSum / denominator_areaSum;
    }

    private double getFinalShapeMembership(Map<FuzzySet, Double> outputSetActivations, double x) {
        double maxMembershipAcrossSets = 0.0;

        for (Map.Entry<FuzzySet, Double> activatedSet : outputSetActivations.entrySet()) {
            FuzzySet outputSet = activatedSet.getKey();

            double activationStrength = activatedSet.getValue();
            double originalMembership = outputSet.getMembership(x);
            // Step 1: Clip the original membership function using the activation strength (Implication using min).
            // This is combining the "rule strength" with the "output membership function"
            double clippedMembership = Math.min(activationStrength, originalMembership);
            // Step 2: Aggregate the result with other sets using max (Union).
            // This builds the final output shape by taking the maximum envelope of all clipped shapes.
            if (clippedMembership > maxMembershipAcrossSets) {
                maxMembershipAcrossSets = clippedMembership;
            }
        }
        return maxMembershipAcrossSets;
    }
}