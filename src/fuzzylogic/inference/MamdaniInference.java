package fuzzylogic.inference;

import fuzzylogic.operators.implications.Implication;
import fuzzylogic.operators.snorms.SNorm;
import fuzzylogic.operators.tnorms.TNorm;
import fuzzylogic.rules.*;
import fuzzylogic.variables.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MamdaniInference implements InferenceEngine {

    private final TNorm andOperator;
    private final SNorm orOperator;
    private final Implication implication;
    private final SNorm aggregation;

    public MamdaniInference(TNorm andOperator,
            SNorm orOperator,
            Implication implication,
            SNorm aggregation) {
        this.andOperator = andOperator;
        this.orOperator = orOperator;
        this.implication = implication;
        this.aggregation = aggregation;
    }

    @Override
    public Map<FuzzySet, Double> infer(
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs,
            RuleBase ruleBase) {
        Map<FuzzySet, Double> outputMap = new HashMap<>();

        for (Rule rule : ruleBase.getEnabledRules()) {

            double ruleStrength = evaluateAntecedents(rule, fuzzifiedInputs);

            // Apply rule weight
            ruleStrength *= rule.getWeight();

            if (ruleStrength <= 0.0)
                continue;

            for (Consequent c : rule.getConsequents()) {

                FuzzySet fs = c.getFuzzySet();

                double implied = implication.apply(ruleStrength, 1.0);

                double prev = outputMap.getOrDefault(fs, 0.0);
                double combined = aggregation.apply(prev, implied);

                outputMap.put(fs, combined);
            }
        }

        return outputMap; 
    }


    private double evaluateAntecedents(
            Rule rule,
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs) {
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

    private double membershipOf(
            Antecedent a,
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs) {
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
