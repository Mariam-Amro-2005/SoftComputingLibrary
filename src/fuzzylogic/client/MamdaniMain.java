package fuzzylogic.client;

import fuzzylogic.fuzzification.BasicFuzzifier;
import fuzzylogic.inference.MamdaniInference;
import fuzzylogic.operators.implications.MinImplication;
import fuzzylogic.operators.snorms.MaxSNorm;
import fuzzylogic.operators.tnorms.MinTNorm;
import fuzzylogic.rules.*;
import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;
import fuzzylogic.membership.MembershipFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MamdaniMain {

    public static void main(String[] args) {

        System.out.println("=== Driver Risk Assessment (Mamdani Inference) ===");

        // ---------------- Input Variable: Speed ----------------
        MembershipFactory mfFactory = new MembershipFactory();

        FuzzySet speedLow = new FuzzySet("Low", mfFactory.create("triangular", 0, 0, 50));
        FuzzySet speedMedium = new FuzzySet("Medium", mfFactory.create("triangular", 0, 50, 100));
        FuzzySet speedHigh = new FuzzySet("High", mfFactory.create("triangular", 50, 100, 100));

        LinguisticVariable speed = new LinguisticVariable("Speed", 0, 120, List.of(speedLow, speedMedium, speedHigh));

        // ---------------- Input Variable: Braking ----------------
        FuzzySet brakingLow = new FuzzySet("Low", mfFactory.create("triangular", 0, 20, 40));
        FuzzySet brakingMedium = new FuzzySet("Medium", mfFactory.create("triangular", 20, 60, 100));
        FuzzySet brakingHigh = new FuzzySet("High", mfFactory.create("triangular", 60, 100, 120));

        LinguisticVariable braking = new LinguisticVariable("Braking", 0, 120,
                List.of(brakingLow, brakingMedium, brakingHigh));

        // ---------------- Output Variable: Risk ----------------
        FuzzySet riskLow = new FuzzySet("Low", mfFactory.create("triangular", 0, 20, 40));
        FuzzySet riskMedium = new FuzzySet("Medium", mfFactory.create("triangular", 20, 60, 100));
        FuzzySet riskHigh = new FuzzySet("High", mfFactory.create("triangular", 60, 100, 120));

        LinguisticVariable risk = new LinguisticVariable("Risk", 0, 100, List.of(riskLow, riskMedium, riskHigh));

        // ---------------- Rule Base ----------------
        RuleBase ruleBase = new RuleBase(new MamdaniRuleParser(List.of(speed, braking, risk)));

        ruleBase.addRuleFromString("IF Speed is Low AND Braking is Low THEN Risk is Low");
        ruleBase.addRuleFromString("IF Speed is High OR Braking is High THEN Risk is High");
        ruleBase.addRuleFromString("IF Speed is Medium AND Braking is Medium THEN Risk is Medium");

        // ---------------- Fuzzify Inputs ----------------
        double inputSpeed = 30.0;
        double inputBraking = 90.0;

        BasicFuzzifier fuzzifier = new BasicFuzzifier();
        Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs = new HashMap<>();
        fuzzifiedInputs.put(speed, fuzzifier.fuzzify(inputSpeed, speed));
        fuzzifiedInputs.put(braking, fuzzifier.fuzzify(inputBraking, braking));

        // ---------------- Mamdani Inference ----------------
        MamdaniInference mamdani = new MamdaniInference(
                new MinTNorm(),
                new MaxSNorm(),
                new MinImplication(),
                new MaxSNorm());

        Map<LinguisticVariable, Map<FuzzySet, Double>> results = mamdani.infer(fuzzifiedInputs, ruleBase);

        // ---------------- Display ----------------
        System.out.println("\nFuzzified Inputs:");
        fuzzifiedInputs.forEach((var, memberships) -> {
            System.out.println("  " + var.getName() + " = " + (var == speed ? inputSpeed : inputBraking));

            memberships.forEach((fs, val) -> {
                System.out.println("    " + fs.getLabel() + " = " + val);
            });
        });

        System.out.println("\n=== Final Inferred Output per Linguistic Variable ===");

        Map<FuzzySet, Double> riskResults = results.get(risk);
        System.out.println("\nOutput Variable: " + risk.getName());
        for (FuzzySet fs : risk.getSets()) {
            double activation = riskResults.getOrDefault(fs, 0.0);
            System.out.println("  Fuzzy Set: " + fs.getLabel());
            System.out.println("    Activation = " + activation);
        }

        System.out.println("\n=== Done ===");
    }
}