package fuzzylogic.defuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.Map;

public interface Defuzzifier {
    Map<LinguisticVariable, Double> defuzzify(Map<LinguisticVariable, Map<FuzzySet, Double>> aggregatedOutput);
}
