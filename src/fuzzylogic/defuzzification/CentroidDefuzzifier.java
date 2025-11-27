package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.Map;

public class CentroidDefuzzifier extends BaseMamdaniDefuzzifier {

    public CentroidDefuzzifier(LinguisticVariable outputVar) {
        super(outputVar, 1001);
    }

    public CentroidDefuzzifier(LinguisticVariable outputVar, int samples) {
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

        double sumNum = 0;
        double sumDen = 0;

        for (int i = 0; i < samples; i++) {
            double x = start + i * step;

            double u = aggregatedMembership(aggregatedOutput, x);
            sumNum += x * u;
            sumDen += u;
        }

        return (sumDen == 0) ? midpoint() : sumNum / sumDen;
    }

    private double midpoint() {
        return (outputVar.getDomainStart() + outputVar.getDomainEnd()) / 2.0;
    }
}
