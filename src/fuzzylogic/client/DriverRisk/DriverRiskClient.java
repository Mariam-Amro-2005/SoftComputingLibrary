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

        LinguisticVariable speed = DriverRiskVariables.speed();
        LinguisticVariable road = DriverRiskVariables.roadCondition();
        LinguisticVariable visibility = DriverRiskVariables.visibility();
        LinguisticVariable risk = DriverRiskVariables.accidentRisk();

        List<LinguisticVariable> variables = List.of(speed, road, visibility, risk);
        RuleBase ruleBase = new RuleBase(new MamdaniRuleParser(variables));

        DriverRiskRules.addRules(ruleBase, speed, road, visibility, risk);

        MamdaniInference mamdaniInference = new MamdaniInference(
                new MinTNorm(),
                new MaxSNorm(),
                new MinImplication(),
                new MaxSNorm()
        );

        Defuzzifier defuzzifier = new CentroidDefuzzifier(risk);


        FuzzyEngine engine = new FuzzySystemBuilder()
                .setRuleBase(ruleBase)
                .setMode(FuzzyEngine.Mode.MAMDANI)
                .setMamdaniInference(mamdaniInference)
                .setDefuzzifier(defuzzifier)
                .build();


        Map<LinguisticVariable, Double> inputs = new HashMap<>();
        inputs.put(speed, 120.0);
        inputs.put(road, 5.0);
        inputs.put(visibility, 250.0);


        Map<LinguisticVariable, Double> results = engine.evaluate(inputs);

        System.out.println("Accident Risk Level: " + results.get(risk));
    }
}
