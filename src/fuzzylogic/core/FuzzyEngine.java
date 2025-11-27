package fuzzylogic.core;

import fuzzylogic.defuzzification.Defuzzifier;
import fuzzylogic.fuzzification.Fuzzifier;
import fuzzylogic.fuzzification.BasicFuzzifier;
import fuzzylogic.inference.MamdaniInference;
import fuzzylogic.inference.SugenoInference;
import fuzzylogic.rules.*;
import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.HashMap;
import java.util.Map;

public class FuzzyEngine {

    public enum Mode { MAMDANI, SUGENO }

    private final RuleBase ruleBase;
    private final Fuzzifier fuzzifier;
    private final Mode mode;

    // Mamdani-specific
    private final MamdaniInference mamdaniInference;
    private final Map<LinguisticVariable, Defuzzifier> defuzzifiers;

    // Sugeno-specific
    private final SugenoInference sugenoInference;

    public FuzzyEngine(RuleBase ruleBase,
                       Mode mode,
                       MamdaniInference mamdaniInference,
                       Map<LinguisticVariable, Defuzzifier> defuzzifiers,
                       SugenoInference sugenoInference) {
        this.ruleBase = ruleBase;
        this.mode = mode;
        this.fuzzifier = new BasicFuzzifier(); // default fuzzifier
        this.mamdaniInference = mamdaniInference;
        this.defuzzifiers = defuzzifiers != null ? defuzzifiers : new HashMap<>();
        this.sugenoInference = sugenoInference;
    }

    public Map<LinguisticVariable, Double> evaluate(Map<LinguisticVariable, Double> inputs) {
        // Step 1: Fuzzify all input variables
        Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs = new HashMap<>();
        for (var entry : inputs.entrySet()) {
            fuzzifiedInputs.put(entry.getKey(), fuzzifier.fuzzify(entry.getValue(), entry.getKey()));
        }

        Map<LinguisticVariable, Double> outputs = new HashMap<>();

        if (mode == Mode.MAMDANI) {
            // Step 2: Run Mamdani inference
            Map<LinguisticVariable, Map<FuzzySet, Double>> inferred =
                    mamdaniInference.infer(fuzzifiedInputs, ruleBase);

            // Step 3: Defuzzify
            for (var entry : inferred.entrySet()) {
                LinguisticVariable lv = entry.getKey();
                Defuzzifier defuzz = defuzzifiers.get(lv);
                if (defuzz == null) {
                    throw new RuntimeException("No defuzzifier set for variable: " + lv.getName());
                }
                outputs.put(lv, defuzz.defuzzify(entry.getValue()));
            }
        } else { // Sugeno
            // Step 2: Run Sugeno inference (already returns crisp outputs)
            Map<LinguisticVariable, Double> inferred = sugenoInference.infer(fuzzifiedInputs, ruleBase);

            // Step 3: Return crisp outputs directly
            outputs.putAll(inferred);
        }

        return outputs;
    }
}
