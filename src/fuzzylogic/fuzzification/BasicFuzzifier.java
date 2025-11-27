package fuzzylogic.fuzzification;

import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BasicFuzzifier implements Fuzzifier {

    @Override
    public Map<FuzzySet, Double> fuzzify(double crispValue, LinguisticVariable variable) {
        if (variable == null) {
            throw new IllegalArgumentException("LinguisticVariable cannot be null");
        }

        Map<FuzzySet, Double> results = new HashMap<>();

        for (FuzzySet set : variable.getSets()) {
            if (set == null) continue; // skip null sets just in case
            double membership = set.getMembership(crispValue);
            results.put(set, membership);
        }

        if (results.isEmpty()) {
            System.err.println("Warning: No fuzzy sets found for variable " + variable.getName());
        }

        return Collections.unmodifiableMap(results); // prevent external modification
    }
}
