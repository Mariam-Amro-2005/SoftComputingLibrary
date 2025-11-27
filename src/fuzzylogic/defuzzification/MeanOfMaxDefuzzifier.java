package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;
import java.util.Map;


public class MeanOfMaxDefuzzifier extends BaseMamdaniDefuzzifier {

    public MeanOfMaxDefuzzifier(LinguisticVariable outputVar) {
        super(outputVar, 1001);
    }

    public MeanOfMaxDefuzzifier(LinguisticVariable outputVar, int samples) {
        super(outputVar, samples);
    }

    @Override
    public double defuzzify(Map<FuzzySet, Double> aggregatedOutput) {
        if (aggregatedOutput == null || aggregatedOutput.isEmpty()) {
            return midpoint();
        }

        double start = outputVar.getDomainStart();
        double end   = outputVar.getDomainEnd();
        double step  = (end - start) / (samples - 1);

        // First pass: find maximum membership
        double maxu = 0.0;
        for (int i = 0; i < samples; i++) {
            double x = start + i * step;
            double u = aggregatedMembership(aggregatedOutput, x);
            if (u > maxu) maxu = u;
        }

        if (maxu == 0.0) return midpoint();

        // Second pass: mean of all x with u == maxu
        double sum = 0;
        int count = 0;
        for (int i = 0; i < samples; i++) {
            double x = start + i * step;
            double u = aggregatedMembership(aggregatedOutput, x);
            if (Math.abs(u - maxu) < 1e-9) {
                sum += x;
                count++;
            }
        }

        return sum / count;
    }

    private double midpoint() {
        return (outputVar.getDomainStart() + outputVar.getDomainEnd()) / 2.0;
    }
}
