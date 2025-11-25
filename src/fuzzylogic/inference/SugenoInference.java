package fuzzylogic.inference;

import fuzzylogic.operators.snorms.SNorm;
import fuzzylogic.operators.tnorms.TNorm;
import fuzzylogic.rules.*;
import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Zero-order Sugeno inference
public class SugenoInference implements InferenceEngine {

    private final TNorm andOperator;
    private final SNorm orOperator;

    public SugenoInference(TNorm andOperator, SNorm orOperator) {
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    @Override
    public Map<FuzzySet, Double> infer(Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs, // example: temp -> {low:0.2, medium:0.5, high:0.8}, humidity -> {low:0.7, medium:0.3, high:0.0}
            RuleBase ruleBase) { // example for ruleBase: if temp is high and humidity is low then fanSpeed is high
        Map<FuzzySet, Double> outputMap = new HashMap<>();

        for (Rule rule : ruleBase.getEnabledRules()) { // example of rule: if temp is high and humidity is low then fanSpeed is high
            double ruleStrength = evaluateAntecedents(rule, fuzzifiedInputs);

            // Apply rule weight
            ruleStrength *= rule.getWeight();

            if (ruleStrength <= 0.0)
                continue;

            for (Consequent c : rule.getConsequents()) {
                FuzzySet fs = c.getFuzzySet();


                double prev = outputMap.getOrDefault(fs, 0.0);
                outputMap.put(fs, prev + ruleStrength);
            }
        }

        return outputMap;
    }

    private double evaluateAntecedents(Rule rule, Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs) {
        List<Antecedent> ants = rule.getAntecedents();
        List<LogicalOperator> ops = rule.getOperators();

        if (ants.isEmpty())
            return 0.0;

        // First antecedent
        double strength = membershipOf(ants.get(0), fuzzifiedInputs);

        // Combine with remaining
        for (int i = 1; i < ants.size(); i++) {
            double next = membershipOf(ants.get(i), fuzzifiedInputs);
            LogicalOperator op = ops.get(i - 1);

            if (op == LogicalOperator.AND)
                strength = andOperator.apply(strength, next);
            else
                strength = orOperator.apply(strength, next);
        }

        return strength;
    }

    private double membershipOf(Antecedent a, Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs) {
        LinguisticVariable lv = a.getVariable();
        String setName = a.getFuzzySetLabel();

        FuzzySet fs = lv.getFuzzySetByName(setName);
        if (fs == null)
            throw new RuntimeException("Unknown fuzzy set: " + setName);

        Map<FuzzySet, Double> fuzz = fuzzifiedInputs.get(lv);
        if (fuzz == null)
            throw new RuntimeException("No fuzzified values for variable: " + lv.getName());

        return fuzz.getOrDefault(fs, 0.0);
    }
}
