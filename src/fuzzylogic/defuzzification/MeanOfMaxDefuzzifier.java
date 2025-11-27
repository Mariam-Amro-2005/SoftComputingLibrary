package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.HashMap;
import java.util.Map;

public class MeanOfMaxDefuzzifier extends BaseMamdaniDefuzzifier implements Defuzzifier {

    public MeanOfMaxDefuzzifier() {
        super(1001);
    }

    public MeanOfMaxDefuzzifier(int samples) {
        super(samples);
    }

    @Override
    public Map<LinguisticVariable, Double> defuzzify(Map<LinguisticVariable, Map<FuzzySet, Double>> aggregatedOutput) {
        Map<LinguisticVariable, Double> crispOutputs = new HashMap<>();

        for (var entry : aggregatedOutput.entrySet()) {
            LinguisticVariable lv = entry.getKey();
            Map<FuzzySet, Double> fsMap = entry.getValue();

            double start = lv.getDomainStart();
            double end = lv.getDomainEnd();
            double step = (end - start) / (samples - 1);

            // First pass: find max membership
            double maxu = 0;
            for (int i = 0; i < samples; i++) {
                double x = start + i * step;
                double u = aggregatedMembership(fsMap, x);
                if (u > maxu) maxu = u;
            }

            if (maxu == 0) {
                crispOutputs.put(lv, midpoint(lv));
                continue;
            }

            // Second pass: mean of all x with u == maxu
            double sum = 0;
            int count = 0;

            for (int i = 0; i < samples; i++) {
                double x = start + i * step;
                double u = aggregatedMembership(fsMap, x);
                if (Math.abs(u - maxu) < 1e-9) {
                    sum += x;
                    count++;
                }
            }

            crispOutputs.put(lv, sum / count);
        }

        return crispOutputs;
    }
}
