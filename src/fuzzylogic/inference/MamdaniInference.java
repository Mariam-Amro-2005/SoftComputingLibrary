package fuzzylogic.inference;

import fuzzylogic.fuzzification.Fuzzifier;
import fuzzylogic.operators.implications.Implication;
import fuzzylogic.operators.snorms.SNorm;
import fuzzylogic.operators.tnorms.TNorm;
import fuzzylogic.rules.*;
import fuzzylogic.variables.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mamdani inference engine supporting multi-antecedent rules
 * with AND/OR operators, weighted rules, and multiple consequents.
 */
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
            RuleBase ruleBase
    ) {
        Map<FuzzySet, Double> outputMap = new HashMap<>();

        for (Rule rule : ruleBase.getEnabledRules()) {

            // ---------- Evaluate Antecedent ----------
            double ruleStrength = evaluateAntecedents(rule, fuzzifiedInputs);

            // Apply rule weight
            ruleStrength *= rule.getWeight();

            if (ruleStrength == 0)
                continue;

            // ---------- Apply Implication & Aggregate ----------
            for (Consequent c : rule.getConsequents()) {
                FuzzySet fs = c.getFuzzySet();
                double implied = implication.apply(ruleStrength, 1.0);

                double prev = outputMap.getOrDefault(fs, 0.0);
                double aggregated = aggregation.apply(prev, implied);
                outputMap.put(fs, aggregated);
            }
        }

        return outputMap;
    }


    private double evaluateAntecedents(
            Rule rule,
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs
    ) {
        List<Antecedent> ants = rule.getAntecedents();
        List<LogicalOperator> ops = rule.getOperators();

        // Evaluate first antecedent
        double strength = membershipOf(ants.get(0), fuzzifiedInputs);

        // Combine with others
        for (int i = 1; i < ants.size(); i++) {

            double nextValue = membershipOf(ants.get(i), fuzzifiedInputs);
            LogicalOperator op = ops.get(i - 1);

            if (op == LogicalOperator.AND)
                strength = andOperator.apply(strength, nextValue);
            else
                strength = orOperator.apply(strength, nextValue);
        }

        return strength;
    }


    private double membershipOf(
            Antecedent a,
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs
    ) {
        LinguisticVariable lv = a.getVariable();
        String setName = a.getFuzzySetLabel();

        FuzzySet fs = lv.getFuzzySetByName(setName);
        if (fs == null)
            throw new RuntimeException("Unknown fuzzy set: " + setName);

        return fuzzifiedInputs.get(lv).get(fs);
    }
}
