package fuzzylogic.client.DriverRisk;

import fuzzylogic.core.FuzzySystemBuilder;
import fuzzylogic.core.FuzzyEngine;
import fuzzylogic.fuzzification.BasicFuzzifier;
import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;
import fuzzylogic.rules.Rule;
import fuzzylogic.rules.RuleBase;
import fuzzylogic.rules.SugenoRuleParser;
import fuzzylogic.inference.SugenoInference;
import fuzzylogic.operators.tnorms.MinTNorm;
import fuzzylogic.operators.snorms.MaxSNorm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverRiskSugenoClient {

    public static void main(String[] args) {

        // ===== VARIABLES =====
        LinguisticVariable speed = DriverRiskVariables.speed();
        LinguisticVariable road = DriverRiskVariables.roadCondition();
        LinguisticVariable vis = DriverRiskVariables.visibility();
        LinguisticVariable risk = DriverRiskVariables.accidentRisk();

        List<LinguisticVariable> variables = List.of(speed, road, vis, risk);

        // ===== RULE BASE =====
        RuleBase ruleBase = new RuleBase(new SugenoRuleParser(variables));
        DriverRiskSugenoRules.addRules(ruleBase, speed, road, vis, risk);

        // ===== CORRECT SUGENO INFERENCE CONSTRUCTOR =====
        SugenoInference sugeno = new SugenoInference(
                new MinTNorm(),   // AND
                new MaxSNorm()    // OR
        );

        // ===== BUILD SYSTEM =====
        FuzzyEngine engine = new FuzzySystemBuilder()
                .setRuleBase(ruleBase)
                .setMode(FuzzyEngine.Mode.SUGENO)
                .setSugenoInference(sugeno)
                .build();

        // ===== INPUTS =====
        Map<LinguisticVariable, Double> inputs = Map.of(
                speed, 120.0,
                road, 5.0,
                vis, 250.0
        );

        System.out.println("\n===== INPUTS =====");
        inputs.forEach((v, val) ->
                System.out.println(v.getName() + " = " + val));

        // ===== FUZZIFICATION =====
        BasicFuzzifier fuzzifier = new BasicFuzzifier();
        Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzified = new HashMap<>();

        System.out.println("\n===== FUZZIFIED VALUES =====");

        for (var e : inputs.entrySet()) {
            LinguisticVariable lv = e.getKey();
            double crisp = e.getValue();
            Map<FuzzySet, Double> map = fuzzifier.fuzzify(crisp, lv);
            fuzzified.put(lv, map);

            System.out.println("\n" + lv.getName() + ":");
            map.forEach((set, μ) ->
                    System.out.printf("  %-12s -> %.4f%n", set.getLabel(), μ));
        }

        // ===== RULE FIRINGS =====
        System.out.println("\n===== RULE ACTIVATION (FIRING STRENGTHS) =====");

        int index = 1;
        for (Rule rule : ruleBase.getEnabledRules()) {
            double w = engine.getSugenoInference().computeRuleActivation(rule, fuzzified);
            System.out.printf("Rule %-2d: %-50s firing = %.4f%n",
                    index++, rule.toString(), w);
        }

        // ===== FINAL OUTPUT =====
        Map<LinguisticVariable, Double> output = engine.evaluate(inputs);

        System.out.println("\n===== FINAL SUGENO OUTPUT =====");
        System.out.println("Accident Risk (crisp) = " + output.get(risk));

        System.out.println("\nDone.");
    }
}
