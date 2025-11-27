package fuzzylogic.client.DriverRisk;

import fuzzylogic.core.FuzzySystemBuilder;
import fuzzylogic.core.FuzzyEngine;
import fuzzylogic.defuzzification.CentroidDefuzzifier;
import fuzzylogic.defuzzification.Defuzzifier;
import fuzzylogic.inference.MamdaniInference;
import fuzzylogic.operators.implications.MinImplication;
import fuzzylogic.operators.snorms.MaxSNorm;
import fuzzylogic.operators.tnorms.MinTNorm;
import fuzzylogic.rules.MamdaniRuleParser;
import fuzzylogic.rules.RuleBase;
import fuzzylogic.variables.LinguisticVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverRiskClient {

    public static void main(String[] args) {

        // ----------------------------
        // 1. Create linguistic variables
        // ----------------------------
        LinguisticVariable speed = DriverRiskVariables.speed();
        LinguisticVariable road = DriverRiskVariables.roadCondition();
        LinguisticVariable visibility = DriverRiskVariables.visibility();
        LinguisticVariable risk = DriverRiskVariables.accidentRisk();

        // ----------------------------
        // 2. Create RuleBase
        // ----------------------------
        List<LinguisticVariable> variables = List.of(speed, road, visibility, risk);
        RuleBase ruleBase = new RuleBase(new MamdaniRuleParser(variables));

        // Add the rules
        DriverRiskRules.addRules(ruleBase, speed, road, visibility, risk);

        // ----------------------------
        // 3. Create Mamdani inference and defuzzifier
        // ----------------------------
        MamdaniInference mamdaniInference = new MamdaniInference(
                new MinTNorm(),       // AND operator
                new MaxSNorm(),       // OR operator
                new MinImplication(), // Implication
                new MaxSNorm()        // Aggregation
        );

        // Single defuzzifier for all output variables
        Defuzzifier defuzzifier = new CentroidDefuzzifier(risk);

        // ----------------------------
        // 4. Build FuzzyEngine
        // ----------------------------
        FuzzyEngine engine = new FuzzySystemBuilder()
                .setRuleBase(ruleBase)
                .setMode(FuzzyEngine.Mode.MAMDANI)
                .setMamdaniInference(mamdaniInference)
                .setDefuzzifier(defuzzifier)
                .build();

        // ----------------------------
        // 5. Prepare input values
        // ----------------------------
        Map<LinguisticVariable, Double> inputs = new HashMap<>();
        inputs.put(speed, 120.0);      // e.g., km/h
        inputs.put(road, 5.0);         // e.g., wetness index 0-10
        inputs.put(visibility, 250.0); // e.g., visibility in meters

        // ----------------------------
        // 6. Evaluate
        // ----------------------------
        Map<LinguisticVariable, Double> results = engine.evaluate(inputs);

        // Print accident risk output
        System.out.println("Accident Risk Level: " + results.get(risk));
    }
}
