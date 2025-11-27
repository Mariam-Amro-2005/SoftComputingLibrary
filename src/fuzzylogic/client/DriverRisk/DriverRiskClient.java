package fuzzylogic.client.DriverRisk;

import fuzzylogic.core.FuzzySystemBuilder;
import fuzzylogic.core.FuzzyEngine;
import fuzzylogic.defuzzification.CentroidDefuzzifier;
import fuzzylogic.defuzzification.Defuzzifier;
import fuzzylogic.fuzzification.BasicFuzzifier;
import fuzzylogic.inference.MamdaniInference;
import fuzzylogic.operators.implications.MinImplication;
import fuzzylogic.operators.snorms.MaxSNorm;
import fuzzylogic.operators.tnorms.MinTNorm;
import fuzzylogic.rules.MamdaniRuleParser;
import fuzzylogic.rules.RuleBase;
import fuzzylogic.variables.FuzzySet;
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

        BasicFuzzifier fuzzifier = new BasicFuzzifier();

        Map<LinguisticVariable, Double> inputs = new HashMap<>();
        inputs.put(speed, 120.0);
        inputs.put(road, 5.0);
        inputs.put(visibility, 250.0);

        System.out.println("\n=== RAW INPUTS ===");
        inputs.forEach((var, val) ->
                System.out.println(var.getName() + " = " + val)
        );


        System.out.println("\n=== FUZZIFIED INPUTS ===");
        Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs = new HashMap<>();

        for (var entry : inputs.entrySet()) {
            LinguisticVariable lv = entry.getKey();
            double crispValue = entry.getValue();

            Map<FuzzySet, Double> fuzzified = fuzzifier.fuzzify(crispValue, lv);
            fuzzifiedInputs.put(lv, fuzzified);

            System.out.println("\n" + lv.getName() + " (" + crispValue + "):");
            for (var f : fuzzified.entrySet()) {
                System.out.printf("  %-12s → %.4f%n", f.getKey().getLabel(), f.getValue());
            }
        }


        System.out.println("\n=== INFERRED (AGGREGATED) OUTPUT FUZZY SETS ===");

        Map<LinguisticVariable, Map<FuzzySet, Double>> inferred =
                mamdaniInference.infer(fuzzifiedInputs, ruleBase);

        for (var outVarEntry : inferred.entrySet()) {
            LinguisticVariable lv = outVarEntry.getKey();
            System.out.println("\nOutput Variable: " + lv.getName());

            for (var fuzzySetEntry : outVarEntry.getValue().entrySet()) {
                FuzzySet fs = fuzzySetEntry.getKey();
                double membership = fuzzySetEntry.getValue();
                System.out.printf("  %-12s → aggregated membership = %.4f%n",
                        fs.getLabel(), membership);
            }
        }


        double crispOutput = defuzzifier.defuzzify(inferred.get(risk));

        System.out.println("\n=== FINAL CRISP OUTPUT ===");
        System.out.println("Accident Risk = " + crispOutput);


        Map<LinguisticVariable, Double> results = engine.evaluate(inputs);
        System.out.println("\n(Engine Result Check) Accident Risk = " + results.get(risk));
    }
}
