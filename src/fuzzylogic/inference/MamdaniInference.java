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
    public InferenceResult infer(
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs,
            RuleBase ruleBase) {

        Map<FuzzySet, Double> outputMap = new HashMap<>();

        for (Rule rule : ruleBase.getEnabledRules()) {

            double ruleStrength = evaluateAntecedents(rule, fuzzifiedInputs);
            ruleStrength *= rule.getWeight();

            if (ruleStrength <= 0.0)
                continue;

            for (Consequent cons : rule.getConsequents()) {

                if (!(cons instanceof MamdaniConsequent mc))
                    throw new RuntimeException("MamdaniInference requires MamdaniConsequent.");

                FuzzySet outputSet = mc.getFuzzySet();

                double implied = implication.apply(ruleStrength, 1.0);

                double prev = outputMap.getOrDefault(outputSet, 0.0);
                double combined = aggregation.apply(prev, implied);

                outputMap.put(outputSet, combined);
            }
        }

        return new InferenceResult(outputMap);
    }

    private double evaluateAntecedents(
            Rule rule,
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs) {

        List<Antecedent> ants = rule.getAntecedents();
        List<LogicalOperator> ops = rule.getOperators();

        if (ants.isEmpty())
            return 0.0;

        double strength = membershipOf(ants.get(0), fuzzifiedInputs);

        for (int i = 1; i < ants.size(); i++) {
            double next = membershipOf(ants.get(i), fuzzifiedInputs);
            LogicalOperator op = ops.get(i - 1);

            strength = (op == LogicalOperator.AND)
                    ? andOperator.apply(strength, next)
                    : orOperator.apply(strength, next);
        }

        return strength;
    }

    private double membershipOf(
            Antecedent a,
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs) {

        LinguisticVariable lv = a.getVariable();
        FuzzySet fs = lv.getFuzzySetByName(a.getFuzzySetLabel());

        Map<FuzzySet, Double> map = fuzzifiedInputs.get(lv);

        return map.getOrDefault(fs, 0.0);
    }
}
