package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.HashMap;
import java.util.Map;

public class CentroidDefuzzifier extends BaseMamdaniDefuzzifier implements Defuzzifier {

    public CentroidDefuzzifier() {
        super(1001);
    }

    public CentroidDefuzzifier(int samples) {
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

            double sumNum = 0;
            double sumDen = 0;

            for (int i = 0; i < samples; i++) {
                double x = start + i * step;
                double u = aggregatedMembership(fsMap, x);
                sumNum += x * u;
                sumDen += u;
            }

            double crisp = (sumDen == 0) ? midpoint(lv) : sumNum / sumDen;
            crispOutputs.put(lv, crisp);
        }

        return crispOutputs;
    }
}
