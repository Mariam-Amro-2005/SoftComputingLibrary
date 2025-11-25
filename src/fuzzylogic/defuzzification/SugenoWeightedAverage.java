package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;

import java.util.Map;

public class SugenoWeightedAverage implements Defuzzifier {

    @Override
    public double defuzzify(Map<FuzzySet, Double> aggregatedOutput) {

        double sumWZ = 0;
        double sumW = 0;

        for (Map.Entry<FuzzySet, Double> entry : aggregatedOutput.entrySet()) {

            double w = entry.getValue();   // firing strength
            double z;

            // pick crisp representative for Sugeno
            switch (entry.getKey().getLabel().toLowerCase()) {
                case "low"     -> z = 15;
                case "medium"  -> z = 45;
                case "high"    -> z = 70;
                case "extreme" -> z = 95;
                default -> z = 50;
            }

            sumWZ += w * z;
            sumW += w;
        }

        if (sumW == 0) return 50;

        return sumWZ / sumW;
    }
}
