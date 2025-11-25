package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.Map;
import java.util.Objects;

public class MeanOfMaxDefuzzifier implements Defuzzifier {

    private final LinguisticVariable outputLinguisticVariable;
    private final int numSamples;

    public MeanOfMaxDefuzzifier(LinguisticVariable outputLinguisticVariable) {
        this(outputLinguisticVariable, 1001);
    }

    public MeanOfMaxDefuzzifier(LinguisticVariable outputLinguisticVariable, int numSamples) {
        Objects.requireNonNull(outputLinguisticVariable, "Output LinguisticVariable cannot be null.");
        if (numSamples < 3) {
            throw new IllegalArgumentException("Number of samples must be at least 3.");
        }
        this.outputLinguisticVariable = outputLinguisticVariable;
        this.numSamples = numSamples;
    }

    @Override
    public double defuzzify(Map<FuzzySet, Double> aggregatedOutput) {
        if (aggregatedOutput == null || aggregatedOutput.isEmpty()) {
            return (outputLinguisticVariable.getDomainStart() + outputLinguisticVariable.getDomainEnd()) / 2.0;
        }

        double domainStart = outputLinguisticVariable.getDomainStart();
        double domainEnd = outputLinguisticVariable.getDomainEnd();
        double step = (domainEnd - domainStart) / (numSamples - 1);

        double peakMembership = -1.0;

        // Step 1: Find the maximum membership (peak)
        for (int i = 0; i < numSamples; i++) {
            double crispPoint = domainStart + i * step;
            double membershipDegree = getFinalShapeMembership(aggregatedOutput, crispPoint);
            if (membershipDegree > peakMembership) {
                peakMembership = membershipDegree;
            }
        }

        if (peakMembership <= 1e-9) {
            return (outputLinguisticVariable.getDomainStart() + outputLinguisticVariable.getDomainEnd()) / 2.0;
        }

        // Step 2: Average all points at the peak
        double sumOfPeakPoints = 0.0;
        int numPeakPoints = 0;
        for (int i = 0; i < numSamples; i++) {
            double crispPoint = domainStart + i * step;
            double membershipDegree = getFinalShapeMembership(aggregatedOutput, crispPoint);
            if (Math.abs(membershipDegree - peakMembership) < 1e-9) {
                sumOfPeakPoints += crispPoint;
                numPeakPoints++;
            }
        }

        return sumOfPeakPoints / numPeakPoints;
    }

    private double getFinalShapeMembership(Map<FuzzySet, Double> aggregatedOutput, double crispPoint) {
        double peakMembership = 0.0;

        for (Map.Entry<FuzzySet, Double> entry : aggregatedOutput.entrySet()) {
            FuzzySet fuzzySet = entry.getKey();
            double ruleStrength = entry.getValue();

            double clippedMembership = Math.min(ruleStrength, fuzzySet.getMembership(crispPoint));

            if (clippedMembership > peakMembership) {
                peakMembership = clippedMembership;
            }
        }
        return peakMembership;
    }
}
