package fuzzylogic.inference;

import fuzzylogic.operators.snorms.SNorm;
import fuzzylogic.operators.tnorms.TNorm;
import fuzzylogic.rules.*;
import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SugenoInference {

    private final TNorm andOperator;
    private final SNorm orOperator;

    public SugenoInference(TNorm andOperator, SNorm orOperator) {
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    public Map<LinguisticVariable, Double> infer(
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs,
            RuleBase ruleBase) {

        Map<LinguisticVariable, Double> weightedSums = new HashMap<>();
        Map<LinguisticVariable, Double> weightTotals = new HashMap<>();

        for (Rule rule : ruleBase.getEnabledRules()) {

            double ruleStrength = evaluateAntecedents(rule, fuzzifiedInputs);

            // Apply rule weight
            ruleStrength *= rule.getWeight();

            if (ruleStrength <= 0) continue;

            // Iterate over all Sugeno consequents
            for (Consequent c : rule.getConsequents()) {
                if (c.getType() != ConsequentType.SUGENO)
                    throw new RuntimeException("Expected SugenoConsequent, got " + c.getClass());

                SugenoConsequent sc = (SugenoConsequent) c;
                LinguisticVariable outVar = sc.getOutputVariable();
                double value = sc.getValue();

                // Weighted sum accumulation
                weightedSums.put(outVar,
                        weightedSums.getOrDefault(outVar, 0.0) + ruleStrength * value);
                weightTotals.put(outVar,
                        weightTotals.getOrDefault(outVar, 0.0) + ruleStrength);
            }
        }

        // Compute final crisp output for each output variable
        Map<LinguisticVariable, Double> outputs = new HashMap<>();
        for (LinguisticVariable var : weightedSums.keySet()) {
            double sum = weightedSums.get(var);
            double totalWeight = weightTotals.get(var);
            outputs.put(var, totalWeight == 0 ? 0.0 : sum / totalWeight);
        }

        return outputs;
    }

    private double evaluateAntecedents(
            Rule rule,
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs) {

        List<Antecedent> ants = rule.getAntecedents();
        List<LogicalOperator> ops = rule.getOperators();

        if (ants.isEmpty()) return 0.0;

        double strength = membershipOf(ants.get(0), fuzzifiedInputs);

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
        FuzzySet fs = lv.getFuzzySetByName(a.getFuzzySetLabel());

        if (fs == null)
            throw new RuntimeException("Unknown fuzzy set: " + a.getFuzzySetLabel());

        Map<FuzzySet, Double> fuzz = fuzzifiedInputs.get(lv);
        if (fuzz == null)
            throw new RuntimeException("No fuzzified values for variable: " + lv.getName());

        return fuzz.getOrDefault(fs, 0.0);
    }
}
