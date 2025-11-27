package fuzzylogic.inference;

import fuzzylogic.operators.tnorms.TNorm;
import fuzzylogic.operators.snorms.SNorm;
import fuzzylogic.rules.*;
import fuzzylogic.variables.*;

import java.util.*;

public class SugenoInference implements InferenceEngine {

    private final TNorm andOperator;
    private final SNorm orOperator;

    public SugenoInference(TNorm andOperator, SNorm orOperator) {
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    @Override
    public InferenceResult infer(
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs,
            RuleBase ruleBase) {

        // --- Multi-output accumulators ---
        Map<LinguisticVariable, Double> weightedSum = new HashMap<>();
        Map<LinguisticVariable, Double> weightSum = new HashMap<>();

        // Initialize accumulators for ALL output variables seen in rule consequents
        for (Rule rule : ruleBase.getEnabledRules()) {
            for (Consequent c : rule.getConsequents()) {
                if (c instanceof SugenoConsequent sc) {
                    weightedSum.putIfAbsent(sc.getOutputVariable(), 0.0);
                    weightSum.putIfAbsent(sc.getOutputVariable(), 0.0);
                }
            }
        }

        // --- Evaluate rules ---
        for (Rule rule : ruleBase.getEnabledRules()) {

            double ruleStrength = evaluateAntecedents(rule, fuzzifiedInputs);
            ruleStrength *= rule.getWeight();

            if (ruleStrength <= 0.0)
                continue;

            // For each consequent: accumulate w_i * z_i into its output variable
            for (Consequent cons : rule.getConsequents()) {

                if (!(cons instanceof SugenoConsequent sc))
                    throw new RuntimeException(
                            "SugenoInference requires SugenoConsequent, got: " + cons.getType()
                    );

                LinguisticVariable outVar = sc.getOutputVariable();
                double z = sc.getValue();

                weightedSum.put(outVar,
                        weightedSum.get(outVar) + (ruleStrength * z));

                weightSum.put(outVar,
                        weightSum.get(outVar) + ruleStrength);
            }
        }

        // --- Compute crisp outputs per variable ---
        Map<LinguisticVariable, Double> crispOutputs = new HashMap<>();

        for (LinguisticVariable outVar : weightedSum.keySet()) {

            double wSum = weightSum.get(outVar);

            double crisp = (wSum == 0)
                    ? (outVar.getDomainStart() + outVar.getDomainEnd()) / 2.0
                    : (weightedSum.get(outVar) / wSum);

            crispOutputs.put(outVar, crisp);
        }

        return new InferenceResult(crispOutputs);
    }


    //-----------------------------------------
    // Antecedents evaluation (unchanged)
    //-----------------------------------------
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

        return fuzzifiedInputs.get(lv).getOrDefault(fs, 0.0);
    }
}
