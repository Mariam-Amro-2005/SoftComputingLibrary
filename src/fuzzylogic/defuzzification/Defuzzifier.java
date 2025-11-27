package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import java.util.Map;

public interface Defuzzifier {
    double defuzzify(Map<FuzzySet, Double> aggregatedOutput);
}
